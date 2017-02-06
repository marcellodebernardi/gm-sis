package persistence;

import logic.CriterionRepository;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class RepositoryTests {

    /**
     * Tests overall creation of DatabaseRepository. Failure may be due to a variety
     * of reasons.
     */
    @Test
    public void testRepositoryCreation() {
        CriterionRepository database = DatabaseRepository.getInstance();
    }

    /**
     * Tests connection settings for database. If fails, then connection settings
     * are faulty and database cannot be accessed.
     */
    @Test
    public void testJDBCSettings() {
        final String DB_URL = "jdbc:sqlite:GM-SIS.db";
        Connection connection;

        try {
            connection = DriverManager.getConnection(DB_URL);
            assertTrue(connection.isValid(0));
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}
