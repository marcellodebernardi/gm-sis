package persistence;

import logic.Criterion;
import logic.CriterionRepository;
import logic.InconsistentCriteriaException;

import java.sql.*;
import java.util.List;


/**
 * Implementation of the CriterionRepository interface to work with the SQLite DBMS.
 *
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DatabaseRepository<E extends Criterion> implements CriterionRepository<E> {
    private static DatabaseRepository instance;

    // todo setup connection correctly
    private final String DB_URL = "jdbc:sqlite:master/working/lib/GM-SIS.db";
    private Connection connection;
    private PreparedStatement statement;
    private MapperFactory factory;


    /**
     * Constructor for DatabaseRepository. Temporary.
     */
    private DatabaseRepository() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            factory = MapperFactory.getInstance();
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


    public List<E> getByCriteria(boolean patternMatching, Class<E> eClass, List<E> criteria)
            throws InconsistentCriteriaException {
        // handle bad input
        if (criteria == null || criteria.size() == 0) throw new NullPointerException("No criteria given.");
        for (Criterion crit : criteria) {
            if (!eClass.getName().equals(crit.getClass().getName())) throw new InconsistentCriteriaException();
        }

        // get requested objects using appropriate mapper
        try {
            statement = connection.prepareStatement(factory.getMapper(eClass).toSelectQuery(criteria));
            return factory.getMapper(eClass).toObjects(statement.executeQuery());
        }
        catch(SQLException e) {
            // todo not necessarily a good way to handle error
            System.err.print(e.toString());
            return null;
        }
    }

    public boolean addItem(Class<E> eClass, E item) {
        // handle bad input
        if (item == null) throw new NullPointerException();

        // todo implement so it prevents adding existing items
        try {
            statement = connection.prepareStatement(factory.getMapper(eClass).toInsertQuery(item));
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            System.err.print(e.toString());
            return false;
        }
    }

    public boolean updateItem(Class<E> eClass, E item) {
        // handle bad input
        if (item == null) throw new NullPointerException();

        // todo implement to prevent undesired updates
        try {
            statement = connection.prepareStatement(factory.getMapper(eClass).toUpdateQuery(item));
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            System.err.print(e.toString());
            return false;
        }
    }

    public boolean deleteItem(Class<E> eClass, E item) {
        // handle bad input
        if (item == null) throw new NullPointerException();

        // todo implement to prevent undesired deletes
        try {
            statement = connection.prepareStatement(factory.getMapper(eClass).toDeleteQuery(item));
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            System.err.print(e.toString());
            return false;
        }
    }
}