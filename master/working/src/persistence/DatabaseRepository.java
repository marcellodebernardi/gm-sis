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
import java.util.Collections;
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

    // todo setup connection correctly
    private final String DB_URL = "jdbc:sqlite:master/working/lib/GM-SIS.db";
    private Connection connection;
    private PreparedStatement statement;

    private final String SELECTSTRING = "SELECT * FROM ";
    private final String INSERTSTRING = "INSERT INTO ";
    private final String UPDATESTRING = "UPDATE ";
    private final String DELETESTRING = "DELETE FROM ";


    /**
     * Constructor for DatabaseRepository. Temporary.
     */
    private DatabaseRepository() {
        try {
            connection = DriverManager.getConnection(DB_URL);
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

    public <E extends Searchable> boolean addItem(Class<E> eClass, E item) {
        // handle bad input
        if (item == null) throw new NullPointerException();

        try {
            // set up transaction mode
            connection.setAutoCommit(false);

            // generate and queue statements for transaction
            List<String> statements = toINSERTTransaction(eClass, item);
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

    public <E extends Searchable> boolean updateItem(Class<E> eClass, E item) {
        // handle bad input
        if (item == null) throw new NullPointerException();

        try {
            //set up transaction mode
            connection.setAutoCommit(false);

            // generate and queue queries for transaction
            List<String> statements = toUPDATETransaction(eClass, item);
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

    /* Converts criterion to SQL SELECT query */
    <E extends Searchable> String toSELECTQuery(Criterion<E> criteria) {
        return SELECTSTRING + criteria.getCriterionClass().getSimpleName()
                + (criteria.toString().equals("") ? "" : " WHERE ")
                + criteria.toString() + ";";
    }

    /* Converts an entity into an insertion transaction */
    <E extends Searchable> List<String> toINSERTTransaction(Class<E> eClass, E item) {
        List<String> queries = new ArrayList<>();
        String columnString = "(";
        String valueString = "VALUES (";
        String delim = "";

        // getters and annotations
        List<Method> simpleGetters = new ArrayList<>();
        List<Method> complexGetters = new ArrayList<>();
        List<Annotation> simpleAnnotations = new ArrayList<>();
        List<Annotation> complexAnnotations = new ArrayList<>();

        // gets annotated getters fixme assumes reflection annotation is first
        for (Method m : eClass.getDeclaredMethods()) {
            if (Modifier.isPublic(m.getModifiers()) && m.getDeclaredAnnotations().length != 0) {
                if (m.getDeclaredAnnotations()[0].annotationType().equals(Simple.class)) {
                    simpleGetters.add(m);
                    simpleAnnotations.add(m.getDeclaredAnnotations()[0]);
                }
                else if (m.getDeclaredAnnotations()[0].annotationType().equals(Complex.class)) {
                    complexGetters.add(m);
                    complexAnnotations.add(m.getDeclaredAnnotations()[0]);
                }
            }
        }

        // add simple attributes into INSERT query
        for (int i = 0; i < simpleGetters.size(); i++) {
            try {
                if (simpleGetters.get(i).invoke(item) != null) {
                    columnString += delim + ((Simple)simpleAnnotations.get(i)).name();
                    valueString += delim + simpleGetters.get(i).invoke(item);
                    delim = ", ";
                }
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                System.err.print(e.getMessage());
            }
        }
        columnString += ") ";
        valueString += ")";
        queries.add(INSERTSTRING + columnString + valueString);

        // recursively get complex attributes
        for (int i = 0; i < complexGetters.size(); i++) {}

        return queries;
    }

    /* Converts an entity into an update transaction */
    <E extends Searchable> List<String> toUPDATETransaction(Class<E> eClass, E item) {
        // todo implement similar to INSERT
        return null;
    }

    /* Converts a criterion into a deletion transaction */
    // todo implement cascading
    <E extends Searchable> List<String> toDELETETransaction(Criterion<E> criteria) {
        List<String> query = new ArrayList<>();

        query.add(DELETESTRING + criteria.getCriterionClass().getSimpleName()
                + (criteria.toString().equals("") ? "" : " WHERE ")
                + criteria.toString() + ";");
        return query;
    }

    /* Converts a ResultSet into a List<E> of objects todo break down into helper method */
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
        }
        catch (SQLException e) {
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
        }
        catch(NoSuchMethodException e) {
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

                    if (annotation.annotationType().equals(Simple.class)) {
                        Simple metadata = (Simple)annotation;
                        String columnName = metadata.name();
                        int columnIndex = columnNames.indexOf(columnName) + 1;

                        /* This procedure is vulnerable to changes in the names of
                        enumerated types used in the software, as well as to changes
                        in the classes used in the program. If problems arise, make
                        sure all expected data types have a switch case below.
                        todo make more elegant by changing to something without switch
                         */
                        switch(constructorArgumentTypes[i].getSimpleName()) {
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
                                        + "(" + columnIndex + ", " +  columnName + ") could not be identified."
                                        + "Check DatabaseRepository.toObjects for missing switch cases.");
                                return null;
                        }
                    }
                    else if (annotation.annotationType().equals(Complex.class)) {
                        Complex metadata = (Complex)annotation;

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
                    }
                    else {
                        System.out.println("Annotation type not detected");
                    }
                }
                returnList.add(constructor.newInstance(initArgs));
            }
            return returnList;
        }
        catch (SQLException e) {
            System.err.print(e.getMessage() + " - Failed to map results to object.");
            return null;
        }
        catch(InvocationTargetException | InstantiationException | IllegalAccessException e) {
            System.err.print(e.getMessage() + " - Failed to create object instance.");
            return null;
        }
    }

    private <E extends Searchable> boolean exists(E item) {
        return false;
        // return getByCriteria(new Criterion<>(item.getClass(), item.))
    }

    /* Allows running a raw SQL query on the database. Consider minimal use */
    private ResultSet runSQL(String query) {
        try {
            return connection.prepareStatement(query).executeQuery();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}