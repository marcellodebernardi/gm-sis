package persistence;

import entities.*;
import logic.Criterion;
import logic.CriterionRepository;
import logic.Searchable;
import org.joda.time.DateTime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static logic.CriterionOperator.EqualTo;


/**
 * Implementation of the CriterionRepository interface to work with the SQLite DBMS.
 *
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DatabaseRepository implements CriterionRepository {
    private static DatabaseRepository instance;

    // database connection
    private final String DB_URL = "jdbc:sqlite:master/working/lib/GM-SIS.db";
    private Connection connection;
    private PreparedStatement statement;

    // string components
    private final String SELECTSTRING = "SELECT * FROM ";
    private final String INSERTSTRING = "INSERT INTO ";
    private final String UPDATESTRING = "UPDATE ";
    private final String DELETESTRING = "DELETE FROM ";

    // reflection data
    private List<Class<?>> numericDataTypes;
    // todo get reflection data once

    /**
     * Constructor for DatabaseRepository. Temporary.
     */
    private DatabaseRepository() {
        try {
            // connect to database
            connection = DriverManager.getConnection(DB_URL);

            // perform reflection
            numericDataTypes = new ArrayList<>();
            numericDataTypes.add(Integer.class);
            numericDataTypes.add(Double.class);
            numericDataTypes.add(Float.class);
            numericDataTypes.add(Short.class);
            numericDataTypes.add(Byte.class);
            numericDataTypes.add(Date.class);
            numericDataTypes.add(DateTime.class);


        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Gets singleton instance of DatabaseRepository.
     *
     * @return new DatabaseRepository
     */
    public static DatabaseRepository getInstance() {
        if (instance == null) instance = new DatabaseRepository();
        return instance;
    }

    @Override
    protected void finalize() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


    public <E extends Searchable> List<E> getByCriteria(Criterion<E> criteria) {
        // handle bad input
        if (criteria == null) throw new NullPointerException("No criteria given.");

        // get requested objects using appropriate mapper
        try {
            statement = connection.prepareStatement(toSELECTQuery(criteria));
            return toObjects(criteria.getCriterionClass(), statement.executeQuery());
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return null;
        }
    }

    public <E extends Searchable> boolean commitItem(E item) {
        // handle bad input
        if (item == null) throw new NullPointerException();

        try {
            // set up transaction mode
            connection.setAutoCommit(false);

            // generate and queue statements for transaction
            List<String> statements = toINSERTTransaction(item);
            for (String s : statements) {
                connection.prepareStatement(s).executeUpdate();
            }

            // commit transaction and wrap up
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    public <E extends Searchable> boolean deleteItem(Criterion<E> criteria) {
        // handle bad input
        if (criteria == null) throw new NullPointerException();

        try {
            // set up transaction mode
            connection.setAutoCommit(false);

            // generate and queue queries for transaction
            List<String> statements = toDELETETransaction(criteria);
            for (String s : statements) {
                connection.prepareStatement(s).executeUpdate();
            }

            // commit transaction and wrap up
            connection.commit();
            connection.setAutoCommit(false);
            return true;
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    /**
     * Converts criterion to SQL SELECT query
     */
    <E extends Searchable> String toSELECTQuery(Criterion<E> criteria) {
        return SELECTSTRING + criteria.getCriterionClass().getSimpleName()
                + (criteria.toString().equals("") ? "" : " WHERE ")
                + criteria.toString() + ";";
    }

    /**
     * Converts an entity into an insertion transaction
     */
    <E extends Searchable> List<String> toINSERTTransaction(E item) {
        Class<? extends Searchable> eClass = item.getClass();
        List<String> queries = new ArrayList<>();
        List<Method> columnGetters = getColumnGetters(eClass);
        List<Method> tableRefGetters = getTableGetters(eClass);

        String columns = "(";
        String values = "VALUES (";
        String delim = "";

        // todo figure out if INSERT or UPDATE

        // generate query for this item
        for (Method column : columnGetters) {
            try {
                Object attribute = column.invoke(item);

                // if not null ,add to insert statement
                if (attribute != null) {
                    columns += delim + ((Column)column.getDeclaredAnnotations()[0]).name();
                    values += delim
                            + (numericDataTypes.contains(attribute.getClass()) ? "" : "'")
                            + attribute.toString()
                            + (numericDataTypes.contains(attribute.getClass()) ? "" : "'");
                    delim = ", ";
                }
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                System.err.println(e.getMessage());
            }
        }
        queries.add(INSERTSTRING + eClass.getSimpleName() + columns + ") " + values + ");");

        // todo how to handle references to parent items
        // recurse on children
        for (Method tableRef : tableRefGetters) {
            try {
                Object attribute = tableRef.invoke(item);
                TableReference tableRefAnn = (TableReference)tableRef.getDeclaredAnnotations()[0];

                // item is list
                if (attribute != null && attribute instanceof List<?>) {
                    List attributeList = ArrayList.class.cast(attribute);
                    for (int i = 0; i < attributeList.size(); i++) {
                        for (Class<? extends Searchable> type : tableRefAnn.specTypes()) {
                            queries.addAll(toINSERTTransaction(type.cast(attributeList.get(i))));
                        }
                    }
                }
                // item is individual
                else if (attribute != null) {
                    for (Class<? extends Searchable> type : tableRefAnn.specTypes()) {
                        queries.addAll(toINSERTTransaction(type.cast(attribute)));
                    }
                }
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                System.err.println(e.getMessage());
            }
        }

        resolveForeignKeys(queries);

        return queries;
    }

    /**
     * Converts a criterion into a deletion transaction
     */
    <E extends Searchable> List<String> toDELETETransaction(Criterion<E> criteria) {
        List<String> queries = new ArrayList<>();
        List<Method> complexGetters = getTableGetters(criteria.getCriterionClass());
        List<E> targets = getByCriteria(criteria);

        if (targets.size() == 0) return queries;

        // form delete transaction for specified item
        queries.add(DELETESTRING + criteria.getCriterionClass().getSimpleName()
                + (criteria.toString().equals("") ? "" : " WHERE ")
                + criteria.toString() + ";");

        for (E target : targets) {
            // issue delete transaction for complex children
            for (Method method : complexGetters) {
                TableReference annotation = (TableReference) method.getDeclaredAnnotations()[0];
                try {
                    Object attribute = method.invoke(target);
                    if (attribute == null) break;

                    // is list
                    if (attribute instanceof List<?>) {
                        List list = ArrayList.class.cast(attribute);
                        // for each list element, issue delete transaction
                        for (int i = 0; i < list.size(); i++) {
                            for (Class<? extends Searchable> type : annotation.specTypes()) {
                                queries.addAll(toDELETETransaction(
                                        new Criterion<>(type,
                                                ((Column) getPrimaryKeyGetter(list.get(i).getClass()).getDeclaredAnnotations()[0]).name(),
                                                EqualTo,
                                                getPrimaryKeyGetter(list.get(i).getClass()).invoke(list.get(i)))));
                            }
                        }
                    }
                    // is not list
                    else {
                        for (Class<? extends Searchable> type : annotation.specTypes()) {
                            queries.addAll(toDELETETransaction(
                                    new Criterion<>(type,
                                            ((Column) getPrimaryKeyGetter(attribute.getClass()).getDeclaredAnnotations()[0]).name(),
                                            EqualTo,
                                            getPrimaryKeyGetter(attribute.getClass()).invoke(attribute))));
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.err.print(e.getMessage() + "DELETE: invoke @TableReference getter.");
                    return null;
                }
            }
        }
        return queries;
    }

    /**
     * Converts a ResultSet into a List<E> of objects
     */
    private <E extends Searchable> List<E> toObjects(Class<E> eClass, ResultSet results) {
        List<E> returnList = new ArrayList<>();

        // reflection class data
        Constructor<E> constructor;
        Annotation[][] constructorAnnotations;
        Class<?>[] constructorArgumentTypes;

        // results metadata
        int columnNumber;
        List<String> columnNames;

        // obtain results metadata
        try {
            columnNumber = results.getMetaData().getColumnCount();
            columnNames = new ArrayList<>();
            for (int i = 1; i <= columnNumber; i++)
                columnNames.add(results.getMetaData().getColumnName(i));
        } catch (SQLException e) {
            System.err.print(e.getMessage() + " - Failed to obtain results metadata.");
            return null;
        }

        // obtain reflection data
        try {
            constructorArgumentTypes = new Class<?>[0];
            for (Constructor<?> c : eClass.getConstructors()) {
                if (c.getDeclaredAnnotations()[0].annotationType().equals(Reflective.class)) {
                    constructorArgumentTypes = c.getParameterTypes();
                    break;
                }
            }
            constructor = eClass.getConstructor(constructorArgumentTypes);
            constructorAnnotations = constructor.getParameterAnnotations();
        } catch (NoSuchMethodException e) {
            System.err.print(e.getMessage() + " - Failed to obtain constructor and parameters.");
            return null;
        }

        // map results to objects and add
        try {
            // for each row of resultset,
            while (results.next()) {
                Object[] initArgs = new Object[constructorAnnotations.length];

                // columns of ResultSet (constructor arguments)
                for (int i = 0; i < constructorAnnotations.length; i++) {
                    Annotation annotation = constructorAnnotations[i][0];

                    if (annotation.annotationType().equals(Column.class)) {
                        Column metadata = (Column) annotation;
                        String columnName = metadata.name();
                        int columnIndex = columnNames.indexOf(columnName) + 1;

                        /* This procedure is vulnerable to changes in the names of
                        enumerated types used in the software, as well as to changes
                        in the classes used in the program. If problems arise, make
                        sure all expected data types have a switch case below.
                        todo make more elegant by changing to something without switch
                         */
                        switch (constructorArgumentTypes[i].getSimpleName()) {
                            case "String":
                                initArgs[i] = results.getString(columnIndex);
                                break;
                            case "int":
                                initArgs[i] = results.getInt(columnIndex);
                                break;
                            case "double":
                                initArgs[i] = results.getDouble(columnIndex);
                                break;
                            case "float":
                                initArgs[i] = results.getFloat(columnIndex);
                                break;
                            case "boolean":
                                initArgs[i] = results.getBoolean(columnIndex);
                                break;
                            case "CustomerType": // todo fall-through behavior with Enum.valueOf
                                initArgs[i] = CustomerType.valueOf(results.getString(columnIndex));
                                break;
                            case "FuelType":
                                initArgs[i] = FuelType.valueOf(results.getString(columnIndex));
                                break;
                            case "UserType":
                                initArgs[i] = UserType.valueOf(results.getString(columnIndex));
                                break;
                            case "VehicleType":
                                initArgs[i] = VehicleType.valueOf(results.getString(columnIndex));
                                break;
                            case "Date":
                                initArgs[i] = new Date(results.getLong(columnIndex));
                                break;
                            case "DateTime":
                                initArgs[i] = new DateTime(results.getLong(columnIndex));
                                break;
                            default:
                                System.err.print("\nORM toObjects(): data type of constructor argument for database column "
                                        + "(" + columnIndex + ", " + columnName + ") could not be identified."
                                        + "Check DatabaseRepository.toObjects for missing switch cases.");
                                return null;
                        }
                    } else if (annotation.annotationType().equals(TableReference.class)) {
                        TableReference metadata = (TableReference) annotation;

                        // the complex attribute is a list of some form
                        if (constructorArgumentTypes[i].isAssignableFrom(List.class)) {

                            List<Object> finalResult = new ArrayList<>();

                            // fetch complex attribute data
                            for (Class<? extends Searchable> type : metadata.specTypes()) {
                                List<? extends Searchable> result = getByCriteria(new Criterion<>(type, metadata.key(),
                                        EqualTo, results.getObject(1).getClass().cast(results.getObject(1))));

                                if (result.size() > 0) {
                                    finalResult.addAll(result);
                                }
                            }
                            initArgs[i] = finalResult;
                        }
                        // the complex attribute is not a list
                        else {
                            for (Class<? extends Searchable> type : metadata.specTypes()) {
                                List<? extends Searchable> result = getByCriteria(new Criterion<>(type, metadata.key(),
                                        EqualTo, results.getObject(1).getClass().cast(results.getObject(1))));

                                // stop searching when single attribute is found
                                if (result.size() == 0) initArgs[i] = null;
                                else {
                                    initArgs[i] = result.get(0);
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("Annotation type not detected");
                    }
                }
                returnList.add(constructor.newInstance(initArgs));
            }
            return returnList;
        } catch (SQLException e) {
            System.err.print(e.getMessage() + " - Failed to map results to object.");
            return null;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            System.err.print(e.getMessage() + " - Failed to create object instance.");
            return null;
        }
    }

    /**
     * Allows running a raw SQL query on the database. Consider minimal use
     */
    private ResultSet runSQL(String query) {
        try {
            return connection.prepareStatement(query).executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Returns all reflective @Column getter methods for the class
     */
    private List<Method> getColumnGetters(Class<?> eClass) {
        List<Method> methods = new ArrayList<>(Arrays.asList(eClass.getDeclaredMethods()));

        // discard non-public, non-reflective methods and return reflective getters
        for (int i = 0; i < methods.size(); i++) {
            Method method = methods.get(i);
            if (!(Modifier.isPublic(method.getModifiers())
                    && method.getDeclaredAnnotations().length != 0
                    && method.getDeclaredAnnotations()[0].annotationType().equals(Column.class))) {
                methods.remove(method);
                i--;
            }
        }
        return methods;
    }

    /**
     * Returns all reflective @TableReference getter methods for the class
     */
    private List<Method> getTableGetters(Class<?> eClass) {
        List<Method> methods = new ArrayList<>(Arrays.asList(eClass.getDeclaredMethods()));

        // discard non-public, non-reflective methods and return @TableReference getters
        for (int i = 0; i < methods.size(); i++) {
            Method method = methods.get(i);
            if (!(Modifier.isPublic(method.getModifiers())
                    && method.getDeclaredAnnotations().length != 0
                    && method.getDeclaredAnnotations()[0].annotationType().equals(TableReference.class))) {
                methods.remove(method);
                i--;
            }
        }
        return methods;
    }

    /**
     * Returns getter for primary key
     */
    private Method getPrimaryKeyGetter(Class<?> eClass) {
        List<Method> methods = Arrays.asList(eClass.getDeclaredMethods());

        // return getter for primary key
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())
                    && method.getDeclaredAnnotations().length != 0
                    && method.getDeclaredAnnotations()[0].annotationType().equals(Column.class)
                    && ((Column) method.getDeclaredAnnotations()[0]).primary())
                return method;
        }
        System.err.println("getPrimaryKeyGetter: return NULL on " + eClass.getSimpleName());
        return null;
    }

    /** Resolves the foreign keys in a transaction */
    private void resolveForeignKeys(List<String> transaction) {

    }
}