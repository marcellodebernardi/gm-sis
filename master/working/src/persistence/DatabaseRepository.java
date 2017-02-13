package persistence;

import entities.User;
import entities.Vehicle;
import logic.Criterion;
import logic.CriterionOperator;
import logic.CriterionRepository;
import logic.Searchable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private final String PK_RETURN = "SELECT last_insert_rowid()";
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


    public <E extends Searchable> List<E> getByCriteria(Criterion<E> criteria) {
        // handle bad input
        if (criteria == null) throw new NullPointerException("No criteria given.");

        // get requested objects using appropriate mapper
        try {
            statement = connection.prepareStatement(factory.getMapper(criteria.getCriterionClass()).toSELECTQuery(criteria));
            return factory.getMapper(criteria.getCriterionClass()).toObjects(statement.executeQuery());
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
            String[] statements = factory.getMapper(eClass).toINSERTTransaction(item).split("~~~");
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
            String[] statements = factory.getMapper(eClass).toUPDATETransaction(item).split("~~~");
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

    public <E extends Searchable> boolean deleteItem(Class<E> eClass, E item) {
        // handle bad input
        if (item == null) throw new NullPointerException();

        try {
            // set up transaction mode
            connection.setAutoCommit(false);

            // generate and queue queries for transaction
            String[] statements = factory.getMapper(eClass).toDELETETransaction(item).split("~~~");
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
}