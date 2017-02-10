package persistence;

import logic.Criterion;
import logic.CriterionRepository;

import java.sql.*;
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


    public <E extends Criterion> List<E> getByCriteria(boolean patternMatching, Class<E> eClass, List<E> criteria) {
        // handle bad input
        if (criteria == null || criteria.size() == 0) throw new NullPointerException("No criteria given.");

        // get requested objects using appropriate mapper
        try {
            statement = connection.prepareStatement(factory.getMapper(eClass).toSELECTQuery(criteria));
            return factory.getMapper(eClass).toObjects(statement.executeQuery());
        }
        catch(SQLException e) {
            // todo not necessarily a good way to handle error
            System.err.print(e.getMessage());
            return null;
        }
    }

    public <E extends Criterion> boolean addItem(Class<E> eClass, E item) {
        // handle bad input
        if (item == null) { System.out.println("nothing to add");
        throw new NullPointerException();

        }

        // todo implement so it prevents adding existing items
        try {
            System.out.println("try");
            statement = connection.prepareStatement(factory.getMapper(eClass).toINSERTQuery(item));
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            System.out.println("catch");
            System.err.print(e.getMessage());
            return false;
        }
    }

    public <E extends Criterion> boolean updateItem(Class<E> eClass, E item) {
        // handle bad input
        if (item == null) throw new NullPointerException();

        // todo implement to prevent undesired updates
        try {
            statement = connection.prepareStatement(factory.getMapper(eClass).toUPDATEQuery(item));
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    public <E extends Criterion> boolean deleteItem(Class<E> eClass, E item) {
        // handle bad input
        if (item == null) throw new NullPointerException();

        // todo implement to prevent undesired deletes
        try {
            statement = connection.prepareStatement(factory.getMapper(eClass).toDELETEQuery(item));
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    public <E extends Criterion> List<E> doSomething(Class<E> eClass, List<E> list) {
        return null;
    }
}