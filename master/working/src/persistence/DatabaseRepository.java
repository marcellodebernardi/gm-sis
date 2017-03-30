package persistence;

import domain.Searchable;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;

import java.net.URISyntaxException;
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

    // database connection
    private String DB_URL;
    private Connection connection;
    private PreparedStatement statement;

    // helper classes
    private ObjectRelationalMapper mapper;

    /**
     * Constructor for DatabaseRepository. Temporary.
     */
    private DatabaseRepository() {
        try {
            // connect to database

            DB_URL = "jdbc:sqlite:"
                    + this.getClass().getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath().replaceAll("[/\\\\]\\w*\\.jar", "/")
                    + "lib/GM-SIS.db";
            connection = DriverManager.getConnection(DB_URL);
            mapper = ObjectRelationalMapper.getInstance();
            mapper.initialize(this);
        }
        catch (SQLException | URISyntaxException e) {
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

    public void close() {
        try {
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() {
        try {
            connection.close();
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


    public <E extends Searchable> List<E> getByCriteria(Criterion<E> criteria) {
        // handle bad input
        if (criteria == null) throw new NullPointerException("No criteria provided.");

        // get requested objects using appropriate mapper
        try {
            statement = connection.prepareStatement(mapper.toSELECTQuery(criteria).toString());
            return mapper.toObjects(criteria.getCriterionClass(), statement.executeQuery());
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
            return null;
        }
    }

    public <E extends Searchable> boolean commitItem(E item) {
        // handle bad input
        if (item == null) throw new NullPointerException("No item provided.");

        try {
            // set up transaction mode
            connection.setAutoCommit(false);

            // generate and queue statements for transaction
            List<String> statements = mapper.toCOMMITTransaction(item);
            for (String s : statements) {
                connection.prepareStatement(s).executeUpdate();
            }

            // commit transaction and wrap up
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        }
        catch (SQLException e) {
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
            List<String> statements = mapper.toDELETETransaction(criteria, this);
            for (String s : statements) {
                connection.prepareStatement(s).executeUpdate();
            }

            // commit transaction and wrap up
            connection.commit();
            connection.setAutoCommit(false);
            return true;
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    /**
     * Gets the next ID that will be used for a row in the table specified as
     * the argument.
     *
     * @param table      table to check
     * @param primaryKey column used as primary key
     * @return next ID value that will be used
     */
    int getNextID(String table, String primaryKey) {
        try {
            ResultSet result = connection
                    .prepareStatement("SELECT MAX(" + primaryKey + ") FROM " + table + ";")
                    .executeQuery();
            return result.getInt(1) + 1;
        }
        catch (SQLException e) {
            System.err.println(e.getMessage()
                    + "\nFailed to get MAX(" + primaryKey + ") from " + table + ".");
            return -1;
        }
    }

    /**
     * Allows running a raw SQL query on the database. Should be zused minimally
     * and carefully.
     */
    ResultSet runSQL(String query) {
        try {
            return connection.prepareStatement(query).executeQuery();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}