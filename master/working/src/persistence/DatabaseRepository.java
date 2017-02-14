package persistence;

import entities.Complex;
import entities.Simple;
import entities.User;
import entities.Vehicle;
import logic.Criterion;
import logic.CriterionOperator;
import logic.CriterionRepository;
import logic.Searchable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
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
            String[] statements = toINSERTTransaction(eClass, item).split("~~~");
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
            String[] statements = toUPDATETransaction(eClass, item).split("~~~");
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
            String[] statements = toDELETETransaction(criteria).split("~~~");
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
        return SELECTSTRING + criteria.getCriterionClass().getSimpleName() + " WHERE "
                + criteria.toString() + ";";
    }

    /* Converts an entity into an insertion transaction */
    <E extends Searchable> String toINSERTTransaction(Class<E> eClass, E item) {
        // todo implement
        return null;
    }

    /* Converts an entity into an update transaction */
    <E extends Searchable> String toUPDATETransaction(Class<E> eClass, E item) {
        // todo implement
        return null;
    }

    /* Converts a criterion into a deletion transaction */
    <E extends Searchable> String toDELETETransaction(Criterion<E> criteria) {
        // todo implement
        return null;
    }

    /* Converts a ResultSet into a List<E> of objects */
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
            for (int i = 0; i < columnNumber; i++)
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
                if (c.getParameterTypes().length > constructorArgumentTypes.length) {
                    constructorArgumentTypes = c.getParameterTypes();
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
            // rows of ResultSet
            while (results.next()) {
                Object[] initArgs = new Object[constructorAnnotations.length];

                // columns of ResultSet (constructor arguments)
                for (int i = 0; i < constructorAnnotations.length; i++) {
                    Annotation annotation = constructorAnnotations[i][0];

                    if (annotation.annotationType().equals(Simple.class)) {
                        Simple metadata = (Simple)annotation;
                        String columnName = metadata.name();
                        int columnIndex = columnNames.indexOf(columnName) + 1;
                        initArgs[i] = results.getObject(columnIndex); // todo might not work
                    }
                    else if (annotation.annotationType().equals(Complex.class)) {
                        Complex metadata = (Complex)annotation;
                        initArgs[i] = getByCriteria(new Criterion<>(metadata.baseType(), metadata.key(), EqualTo,
                                results.getObject(1)));
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
}