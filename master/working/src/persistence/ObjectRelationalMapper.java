package persistence;

import domain.*;
import logic.criterion.Criterion;
import persistence.support.CellGetterDispatcher;

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
import java.util.*;

import static logic.criterion.CriterionOperator.equalTo;
import static persistence.DependencyConnection.Directionality.TRANSMITTER;

/**
 * @author Marcello De Bernardi
 */
class ObjectRelationalMapper {
    // singleton instance
    private static ObjectRelationalMapper instance;

    // helpers
    private StatementGraphResolver resolver;
    private DatabaseRepository persistence;
    private CellGetterInterface cellGetter;

    // reflection data
    private List<Class<? extends Searchable>> searchableList;
    private Map<Class<? extends Searchable>, Constructor<?>> constructorMap;
    private Map<Constructor<?>, Annotation[][]> parameterAnnotationMap;
    private Map<Constructor<?>, Class<?>[]> parameterTypeMap;
    private Map<Class<? extends Searchable>, List<Method>> reflectiveGetterListMap;
    private Map<Class<? extends Searchable>, Method> primaryGetterMap;
    private Map<Class<? extends Searchable>, List<Method>> reflectiveColumnGetters;
    private Map<Class<? extends Searchable>, List<Method>> reflectiveTableGetters;
    private Map<Class<?>, Method> dateTimestampMethodMap;
    private List<Class<?>> noQuotesTypes;
    private List<Class<?>> dateTypes;


    /**
     * Singleton constructor for ObjectRelationalMapper
     */
    private ObjectRelationalMapper() {
        resolver = StatementGraphResolver.getInstance();
        cellGetter = new CellGetterDispatcher();

        populateSearchableList();
        populateReflectiveMethodMaps();
        populateDateTypeMap();
        populateNoQuotesList();
        populateParameterAnnotationMap();
        populateParameterTypeMap();
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


    /** Converts criterion to SQL SELECT query */
    <E extends Searchable> StringBuilder toSELECTQuery(Criterion<E> criteria) {
        return new StringBuilder(150)
                .append("SELECT * FROM ")
                .append(criteria.getCriterionClass().getSimpleName())
                .append(criteria.toString().equals("") ? "" : " WHERE ")
                .append(criteria.toString())
                .append(";");
    }

    /** Converts an entity into an insertion transaction */
    <E extends Searchable> List<String> toCOMMITTransaction(E item) {
        return resolver.resolveForeignKeys(generateStatementGraph(item, null));
    }

    /** Converts a criterion into a deletion transaction */
    <E extends Searchable> List<String> toDELETETransaction(Criterion<E> criteria, DatabaseRepository persistence) {
        List<String> queries = new ArrayList<>();
        List<Method> complexGetters = getTableGetters(criteria.getCriterionClass());
        List<E> targets = persistence.getByCriteria(criteria);

        if (targets.size() == 0) return queries;

        // delete query for parent
        queries.add("DELETE FROM " + criteria.getCriterionClass().getSimpleName()
                + (criteria.toString().equals("") ? "" : " WHERE ")
                + criteria.toString() + ";");

        // delete queries for complex children
        for (E target : targets) {
            // issue delete transaction for complex children
            for (Method method : complexGetters) {
                TableReference annotation = (TableReference) method.getDeclaredAnnotations()[0];

                try {
                    Object attribute = method.invoke(target);

                    if (attribute == null)
                        break;
                    else if (attribute instanceof List<?>)
                        queries.addAll(generateListDeletionQueries(ArrayList.class.cast(attribute), annotation));
                    else
                        queries.addAll(generateDeletionQueries(attribute, annotation));
                }
                catch (IllegalAccessException | InvocationTargetException e) {
                    System.err.print(e.getMessage() + "DELETE: invoke @TableReference getter.");
                    return null;
                }
            }
        }
        return queries;
    }

    /** Converts a ResultSet into a List<E> of objects */
    <E extends Searchable> List<E> toObjects(Class<E> eClass, ResultSet results) {
        Constructor<?> constructor = constructorMap.get(eClass);        // get reflection information
        Annotation[][] constructorAnnotations = parameterAnnotationMap.get(constructor);
        Class<?>[] constructorArgumentTypes = parameterTypeMap.get(constructor);
        List<String> columnNames = getColumnNames(results);             // results metadata
        List<E> objects = new ArrayList<>();

        try {
            // rows of ResultSet
            while (results.next()) {
                Object[] initArgs = new Object[constructorAnnotations.length];

                // columns of ResultSet / constructor arguments
                for (int i = 0; i < constructorAnnotations.length; i++) {
                    Annotation annotation = constructorAnnotations[i][0];
                    Class<? extends Annotation> type = annotation.annotationType();

                    if (type.equals(Column.class))
                        initArgs[i] = getSimpleAttribute(constructorArgumentTypes[i], annotation, columnNames, results);
                    else if (type.equals(TableReference.class) && constructorArgumentTypes[i].isAssignableFrom(List.class))
                        initArgs[i] = getComplexList(annotation, results);
                    else if (type.equals(TableReference.class))
                        initArgs[i] = getComplexAttribute(annotation, results);
                    else
                        System.out.println("Annotation type not detected");
                }

                objects.add(eClass.cast(constructor.newInstance(initArgs)));
            }
            return objects;
        }
        catch (SQLException e) {
            System.err.print(e.getMessage() + " - Failed to map results to object.");
            return null;
        }
        catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            System.err.print(e.getMessage() + " - Failed to create object instance.");
            return null;
        }
    }


    /* Generates the graph of SQL statements with unresolved dependencies */
    private <E extends Searchable> List<StatementNode> generateStatementGraph(E item, StatementNode parent) {
        Class<? extends Searchable> eClass = item.getClass();
        List<StatementNode> statementGraph = new ArrayList<>();
        List<Method> columnGetters = reflectiveColumnGetters.get(eClass);
        List<Method> tableRefGetters = reflectiveTableGetters.get(eClass);

        HashMap<String, Object> columnValues = new HashMap<>();
        String primaryKey = "";

        // get Column values for the statementNode (simple attributes)
        for (Method column : columnGetters) {
            Column annotation = (Column) column.getDeclaredAnnotations()[0];
            try {
                if (annotation.primary()) {
                    primaryKey = annotation.name();
                }
                else if (!annotation.foreign()) {
                    Object attribute = column.invoke(item);

                    if (attribute != null && !(noQuotesTypes.contains(attribute.getClass()) && attribute.equals(-1)))
                        columnValues.put(((Column) column.getDeclaredAnnotations()[0]).name(), attribute);
                }
                else {
                    Object attribute = column.invoke(item);

                    if (attribute != null && !(noQuotesTypes.contains(attribute.getClass()) && attribute.equals(-1)))
                        columnValues.put(((Column) column.getDeclaredAnnotations()[0]).name(), attribute);
                }
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        // add this StatementNode to graph
        StatementNode sN = new StatementNode(eClass, item, primaryGetterMap.get(eClass), columnValues,
                primaryKey, persistence, noQuotesTypes);
        if (parent != null) {
            sN.addDependency(parent);
            parent.addDependent(sN);
        }
        if (item instanceof DependencyConnectable) {
            // System.out.println(((DependencyConnectable) item).getDependencies());

            for (DependencyConnection connection : ((DependencyConnectable) item).getDependencies()) {
                connection.transmit(sN);

                // todo this shouldn't break since it only goes one way
                if (connection.directionality() == TRANSMITTER) {
                    // System.out.println("RECURSING VIA TRANSMITTER.");
                    statementGraph.addAll(generateStatementGraph(connection.pair().getHost(), null));
                }
            }
        }
        statementGraph.add(sN);

        // recurse on TableReference children
        for (Method tableReference : tableRefGetters) {
            TableReference annotation = (TableReference) tableReference.getDeclaredAnnotations()[0];

            try {
                Object attribute = tableReference.invoke(item);

                // item is non-empty list
                if (attribute != null && attribute instanceof List<?>) {
                    List attributeList = ArrayList.class.cast(attribute);
                    for (int i = 0; i < attributeList.size(); i++) {
                        statementGraph.addAll(generateStatementGraph(annotation.baseType().cast(attributeList.get(i)), sN));
                    }
                }
                // item is individual
                else if (attribute != null) {
                    statementGraph.addAll(generateStatementGraph(annotation.baseType().cast(attribute), sN));
                }
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                System.err.println(e.getMessage());
            }
        }

        return statementGraph;
    }

    /* HELPER: generate DELETE queries for every complex child */
    private List<String> generateDeletionQueries(Object attribute, TableReference annotation)
            throws IllegalAccessException, InvocationTargetException {
        List<String> queries = new ArrayList<>();

        for (Class<? extends Searchable> type : annotation.subTypes()) {
            queries.addAll(toDELETETransaction(
                    new Criterion<>(
                            type,
                            ((Column) getPrimaryKeyGetter(attribute.getClass()).getDeclaredAnnotations()[0]).name(),
                            equalTo,
                            getPrimaryKeyGetter(attribute.getClass()).invoke(attribute)), persistence));
        }

        return queries;
    }

    /* HELPER: for every item in the list, generates a DELETE query */
    private List<String> generateListDeletionQueries(List list, TableReference annotation)
            throws IllegalAccessException, InvocationTargetException {
        List<String> queries = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            for (Class<? extends Searchable> type : annotation.subTypes()) {
                queries.addAll(toDELETETransaction(
                        new Criterion<>(type,
                                ((Column) getPrimaryKeyGetter(list.get(i).getClass()).getDeclaredAnnotations()[0]).name(),
                                equalTo,
                                getPrimaryKeyGetter(list.get(i).getClass()).invoke(list.get(i))), persistence));
            }
        }

        return queries;
    }

    /* HELPER: returns the names of the columns in a ResultSet */
    private List<String> getColumnNames(ResultSet results) {
        try {
            List<String> names = new ArrayList<>();
            for (int i = 1; i <= results.getMetaData().getColumnCount(); i++)
                names.add(results.getMetaData().getColumnName(i));
            return names;
        }
        catch (SQLException e) {
            System.err.print(e.getMessage() + " - Failed to obtain results metadata.");
            return null;
        }
    }

    /* HELPER: returns an Object for the given simple attribute */
    private Object getSimpleAttribute(Class<?> argumentType, Annotation annotation, List<String> columnNames,
                                      ResultSet results) throws SQLException {
        int columnIndex = columnNames.indexOf(((Column) annotation).name()) + 1;
        return cellGetter.getObject(argumentType, results, columnIndex);
    }

    /* HELPER: returns a list of Objects for the given complex attribute */
    private Object getComplexList(Annotation annotation, ResultSet results) throws SQLException {
        TableReference metadata = (TableReference) annotation;
        Class<? extends Searchable>[] subTypes = metadata.subTypes();
        Object primaryKey = results.getObject(1);
        String foreignKey = metadata.key();
        List<Object> finalResult = new ArrayList<>();

        // for every potential subtype, add to list
        for (Class<? extends Searchable> type : subTypes) {
            List<? extends Searchable> result
                    = persistence.getByCriteria(new Criterion<>(type, foreignKey, equalTo,
                    primaryKey.getClass().cast(primaryKey))
            );

            if (result.size() > 0) finalResult.addAll(result);
        }
        return finalResult;
    }

    /* HELPER: returns the Object for the given complex attribute */
    private Object getComplexAttribute(Annotation annotation, ResultSet results) throws SQLException {
        TableReference metadata = (TableReference) annotation;
        Class<? extends Searchable>[] subTypes = metadata.subTypes();
        String foreignKey = metadata.key();
        Object primaryKey = results.getObject(1);

        // find object from one of potential subtypes
        for (Class<? extends Searchable> type : subTypes) {
            List<? extends Searchable> result
                    = persistence.getByCriteria(new Criterion<>(type, foreignKey, equalTo,
                    primaryKey.getClass().cast(primaryKey))
            );

            if (result.size() != 0) return result.get(0);
        }
        return null;
    }

    /* HELPER: populates the list of types implementing Searchable */
    private void populateSearchableList() {
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
    }

    /* HELPER: populates the various maps of reflective methods */
    private void populateReflectiveMethodMaps() {
        constructorMap = new HashMap<>();
        reflectiveColumnGetters = new HashMap<>();
        reflectiveTableGetters = new HashMap<>();
        primaryGetterMap = new HashMap<>();

        reflectiveGetterListMap = new HashMap<>(); // todo remove?

        for (Class<? extends Searchable> eClass : searchableList) {
            for (Constructor<?> c : eClass.getDeclaredConstructors()) {
                if (c.getDeclaredAnnotations().length != 0 && c.getDeclaredAnnotations()[0].annotationType().equals(Reflective.class)) {
                    constructorMap.put(eClass, c);
                    c.setAccessible(true);
                    break;
                }
            }
            reflectiveColumnGetters.put(eClass, getColumnGetterList(eClass));
            reflectiveTableGetters.put(eClass, getTableGetters(eClass));
            primaryGetterMap.put(eClass, getPrimaryKeyGetter(eClass));

            reflectiveGetterListMap.remove(primaryGetterMap.get(eClass)); // todo remove?
        }
    }

    /* HELPER: populates the list of types to be handled as dates */
    private void populateDateTypeMap() {
        dateTypes = new ArrayList<>();
        dateTypes.add(Date.class);
        dateTypes.add(LocalDateTime.class);
        dateTypes.add(LocalDate.class);
        dateTypes.add(LocalTime.class);
        dateTypes.add(ZonedDateTime.class);
    }

    /* HELPER: populates the list of numeric types recognized by the mapper */
    private void populateNoQuotesList() {
        noQuotesTypes = new ArrayList<>();
        noQuotesTypes.add(Integer.class);
        noQuotesTypes.add(int.class);
        noQuotesTypes.add(Double.class);
        noQuotesTypes.add(double.class);
        noQuotesTypes.add(Float.class);
        noQuotesTypes.add(float.class);
        noQuotesTypes.add(Short.class);
        noQuotesTypes.add(short.class);
        noQuotesTypes.add(Long.class);
        noQuotesTypes.add(long.class);
        noQuotesTypes.add(Byte.class);
        noQuotesTypes.add(byte.class);
        noQuotesTypes.addAll(dateTypes);
    }

    /* HELPER: populates the map containing arrays of parameter annotations */
    private void populateParameterAnnotationMap() {
        parameterAnnotationMap = new HashMap<>();
        for (Constructor<?> constructor : constructorMap.values()) {
            parameterAnnotationMap.put(constructor, constructor.getParameterAnnotations());
        }
    }

    /* HELPER: populates the map containing arrays of parameter types */
    private void populateParameterTypeMap() {
        parameterTypeMap = new HashMap<>();
        for (Constructor<?> constructor : constructorMap.values()) {
            parameterTypeMap.put(constructor, constructor.getParameterTypes());
        }
    }

    /* HELPER: Returns all reflective @Column getter methods for the class */
    private List<Method> getColumnGetterList(Class<?> eClass) {
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

    /* HELPER: Returns all reflective @TableReference getter methods for the class */
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

    /* HELPER: Returns all reflective @Column(primary = true) getter methods for a class */
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
}
