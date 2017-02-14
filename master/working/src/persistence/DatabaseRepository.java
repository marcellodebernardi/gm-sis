package persistence;

import entities.User;
import entities.Vehicle;
import logic.Criterion;
import logic.CriterionOperator;
import logic.CriterionRepository;
import logic.Searchable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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
            // todo not necessarily a good way to handle error
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
    private <E extends Searchable> String toSELECTQuery(Criterion<E> criteria) {
        return SELECTSTRING + criteria.getCriterionClass() + " WHERE " + criteria.toString() + ";";
    }

    /* Converts an entity into an insertion transaction */
    private <E extends Searchable> String toINSERTTransaction(Class<E> eClass, E item) {
        // todo implement
        return null;
    }

    /* Converts an entity into an update transaction */
    private <E extends Searchable> String toUPDATETransaction(Class<E> eClass, E item) {
        // todo implement
        return null;
    }

    /* Converts a criterion into a deletion transaction */
    private <E extends Searchable> String toDELETETransaction(Criterion<E> criteria) {
        // todo implement
        return null;
    }

    private <E extends Searchable> List<E> toObjects(Class<E> eClass, ResultSet results) {
        List<E> returnList = new ArrayList<>();

        try {
            // column number and column names
            int columnNumber = results.getMetaData().getColumnCount();
            String[] columnNames = new String[columnNumber];
            for (int i = 0; i < columnNumber; i++) {
                columnNames[i] = results.getMetaData().getColumnName(i);
            }

            // select full constructor and get parameter list
            Constructor<?>[] constructors = eClass.getConstructors();
            Class<?>[] argumentTypes = new Class<?>[0];
            for (int i = 0; i < constructors.length; i++) {
                if (constructors[i].getParameterTypes().length > argumentTypes.length) {
                    argumentTypes = constructors[i].getParameterTypes();
                }
            }
            Constructor<E> constructor = eClass.getConstructor(argumentTypes);

            // iterate over rows
            while (results.next()) {
                Object[] initArgs = new Object[columnNumber];
                // iterate over columns and extract attribute complexity
                for (int i = 0; i < columnNumber; i++) {

                }
                returnList.add(constructor.newInstance(initArgs));
            }
            return returnList;
        }
        catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException
                | NoSuchMethodException e) {
            System.err.print(e.getMessage());
            return null;
        }
    }
}