package persistence;

import domain.*;
import logic.criterion.Criterion;
import persistence.cellgetter.CellGetterDispatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static logic.criterion.CriterionOperator.EqualTo;

/**
 * @author Marcello De Bernardi
 */
class ObjectRelationalMapper {
    // singleton instance
    private static ObjectRelationalMapper instance;

    // helpers
    private ForeignKeyResolver resolver;
    private DatabaseRepository persistence;
    private CellGetterInterface cellGetter;

    // reflection data
    private List<Class<? extends Searchable>> searchableList;
    private HashMap<Class<? extends Searchable>, Constructor<?>> constructorMap;
    private HashMap<Class<? extends Searchable>, List<Method>> getterListMap;
    private HashMap<Class<? extends Searchable>, Method> primaryGetters;
    private List<Class<?>> numericTypes;
    private List<Class<?>> dateTypes;

    // database structure
    private HashMap<Class<? extends Searchable>, Class<? extends Searchable>> dependencyMap;


    /**
     * Singleton constructor for ObjectRelationalMapper
     */
    private ObjectRelationalMapper() {
        resolver = ForeignKeyResolver.getInstance();
        cellGetter = new CellGetterDispatcher();

        // reflection information
        searchableList = new ArrayList<>();
        searchableList.add(Customer.class);
        searchableList.add(DiagRepBooking.class);
        searchableList.add(Installation.class);
        searchableList.add(Mechanic.class);
        searchableList.add(PartAbstraction.class);
        searchableList.add(PartOccurrence.class);
        searchableList.add(PartRepair.class);
        searchableList.add(SpecialistRepairCenter.class);
        searchableList.add(User.class);
        searchableList.add(Vehicle.class);
        searchableList.add(VehicleRepair.class);

        constructorMap = new HashMap<>();
        getterListMap = new HashMap<>();
        primaryGetters = new HashMap<>();
        for (Class<? extends Searchable> eClass : searchableList) {
            for (Constructor<?> c : eClass.getDeclaredConstructors()) {
                if (c.getDeclaredAnnotations().length != 0 && c.getDeclaredAnnotations()[0].annotationType().equals(Reflective.class)) {
                    constructorMap.put(eClass, c);
                    break;
                }
            }
            getterListMap.put(eClass, getColumnGetters(eClass));
            getterListMap.put(eClass, getTableGetters(eClass));
            primaryGetters.put(eClass, getPrimaryKeyGetter(eClass));
            getterListMap.remove(primaryGetters.get(eClass));
        }

        numericTypes = new ArrayList<>();
        numericTypes.add(Integer.class);
        numericTypes.add(int.class);
        numericTypes.add(Double.class);
        numericTypes.add(double.class);
        numericTypes.add(Float.class);
        numericTypes.add(float.class);
        numericTypes.add(Short.class);
        numericTypes.add(short.class);
        numericTypes.add(Long.class);
        numericTypes.add(long.class);
        numericTypes.add(Byte.class);
        numericTypes.add(byte.class);

        dateTypes = new ArrayList<>();
        dateTypes.add(Date.class);
        dateTypes.add(LocalDateTime.class);
        dateTypes.add(LocalDate.class);
        dateTypes.add(LocalTime.class);
        dateTypes.add(ZonedDateTime.class);

        numericTypes.addAll(dateTypes);
    }

    /**
     * Returns a reference to the singleton instance of this class.
     *
     * @return singleton instance.
     */
    static ObjectRelationalMapper getInstance() {
        if (instance == null) instance = new ObjectRelationalMapper();
        return instance;
    }

    void initialize(DatabaseRepository persistence) {
        this.persistence = persistence;
        resolver.initialize(persistence);
    }


    /**
     * Converts criterion to SQL SELECT query
     */
    <E extends Searchable> String toSELECTQuery(Criterion<E> criteria) {
        return "SELECT * FROM " + criteria.getCriterionClass().getSimpleName()
                + (criteria.toString().equals("") ? "" : " WHERE ")
                + criteria.toString() + ";";
    }

    /**
     * Converts an entity into an insertion transaction
     */
    <E extends Searchable> List<String> toINSERTTransaction(E item) {
        return resolver.resolveForeignKeys(generateStatementGraph(item, null));
    }

    /**
     * Converts a criterion into a deletion transaction
     */
    <E extends Searchable> List<String> toDELETETransaction(Criterion<E> criteria, DatabaseRepository persistence) {
        final String DELETESTRING = "DELETE FROM ";

        List<String> queries = new ArrayList<>();
        List<Method> complexGetters = getTableGetters(criteria.getCriterionClass());
        List<E> targets = persistence.getByCriteria(criteria);

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
                                                getPrimaryKeyGetter(list.get(i).getClass()).invoke(list.get(i))), persistence));
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
                                            getPrimaryKeyGetter(attribute.getClass()).invoke(attribute)), persistence));
                        }
                    }
                }
                catch (IllegalAccessException | InvocationTargetException e) {
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
    <E extends Searchable> List<E> toObjects(Class<E> eClass, ResultSet results, DatabaseRepository persistence) {
        List<E> objects = new ArrayList<>();

        // reflection class data
        Constructor<?> constructor = constructorMap.get(eClass);
        Annotation[][] constructorAnnotations = constructor.getParameterAnnotations();
        Class<?>[] constructorArgumentTypes = constructor.getParameterTypes();
        constructor.setAccessible(true);

        // results metadata
        List<String> columnNames;
        try {
            columnNames = new ArrayList<>();
            for (int i = 1; i <= results.getMetaData().getColumnCount(); i++)
                columnNames.add(results.getMetaData().getColumnName(i));
        }
        catch (SQLException e) {
            System.err.print(e.getMessage() + " - Failed to obtain results metadata.");
            return null;
        }

        // map results to objects and add
        try {
            // rows of ResultSet
            while (results.next()) {
                Object[] initArgs = new Object[constructorAnnotations.length];

                // columns of ResultSet (constructor arguments)
                for (int i = 0; i < constructorAnnotations.length; i++) {
                    Annotation annotation = constructorAnnotations[i][0];

                    // simple attribute
                    if (annotation.annotationType().equals(Column.class)) {
                        String columnName = ((Column) annotation).name();
                        int columnIndex = columnNames.indexOf(columnName) + 1;

                        initArgs[i] = cellGetter.getObject(constructorArgumentTypes[i], results, columnIndex);
                    }
                    // complex attribute
                    else if (annotation.annotationType().equals(TableReference.class)) {
                        TableReference metadata = (TableReference) annotation;

                        // the complex attribute is a list of some form
                        if (constructorArgumentTypes[i].isAssignableFrom(List.class)) {

                            List<Object> finalResult = new ArrayList<>();

                            // fetch complex attribute data
                            for (Class<? extends Searchable> type : metadata.specTypes()) {
                                List<? extends Searchable> result = persistence.getByCriteria(new Criterion<>(type, metadata.key(),
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
                                List<? extends Searchable> result = persistence.getByCriteria(new Criterion<>(type, metadata.key(),
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
                objects.add(eClass.cast(constructor.newInstance(initArgs)));
            }
            constructor.setAccessible(false);
            return objects;
        }
        catch (SQLException e) {
            System.err.print(e.getMessage() + " - Failed to map results to object.");
            constructor.setAccessible(false);
            return null;
        }
        catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            System.err.print(e.getMessage() + " - Failed to create object instance.");
            constructor.setAccessible(false);
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


    private <E extends Searchable> List<StatementNode> generateStatementGraph(E item, StatementNode parent) {
        Class<? extends Searchable> eClass = item.getClass();
        List<StatementNode> statementGraph = new ArrayList<>();
        List<Method> columnGetters = getColumnGetters(eClass);
        List<Method> tableRefGetters = getTableGetters(eClass);

        HashMap<String, Object> columnValues = new HashMap<>();
        String primaryKey = "";

        // generate StatementNode for this item (and non-foreign key values)
        for (Method column : columnGetters) {
            Column annotation = (Column) column.getDeclaredAnnotations()[0];
            try {
                if (!annotation.primary() && !annotation.foreign()) {
                    Object attribute = column.invoke(item);
                    if (!(attribute == null || (numericTypes.contains(attribute.getClass()) && attribute.equals(-1))))
                        columnValues.put(((Column) column.getDeclaredAnnotations()[0]).name(), attribute);
                }
                else if (annotation.primary()) {
                    primaryKey = annotation.name();
                }
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                System.err.println(e.getMessage());
            }
        }

        // add this StatementNode to graph
        StatementNode sN = new StatementNode(eClass, item, primaryGetters.get(eClass), columnValues,
                primaryKey, persistence, numericTypes, dateTypes);
        if (parent != null) {
            sN.addDependency(parent);
            parent.addDependent(sN);
        }
        if (item instanceof DependencyConnectable) {
            for (DependencyConnection connection : ((DependencyConnectable) item).getDependencies()) {
                connection.transmit(sN);
            }
        }
        statementGraph.add(sN);

        // recurse on children
        for (Method tableRef : tableRefGetters) {
            TableReference tableRefAnn = (TableReference) tableRef.getDeclaredAnnotations()[0];
            try {
                Object attribute = tableRef.invoke(item);
                // item is non-empty list
                if (attribute != null && attribute instanceof List<?>) {
                    List attributeList = ArrayList.class.cast(attribute);
                    for (int i = 0; i < attributeList.size(); i++) {
                        statementGraph.addAll(generateStatementGraph(tableRefAnn.baseType().cast(attributeList.get(i)), sN));
                    }
                }
                // item is individual
                else if (attribute != null) {
                    statementGraph.addAll(generateStatementGraph(tableRefAnn.baseType().cast(attribute), sN));
                }
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                System.err.println(e.getMessage());
            }
        }
        return statementGraph;
    }
}
