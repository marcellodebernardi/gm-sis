package persistence;

import domain.Customer;
import domain.User;
import logic.Criterion;
import logic.CriterionOperator;
import logic.CriterionRepository;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

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
        final String DB_URL = "jdbc:sqlite:master/working/lib/GM-SIS.db";
        Connection connection;

        try {
            connection = DriverManager.getConnection(DB_URL);
            assertTrue(connection.isValid(0));
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testSELECTQuery() {
        System.out.println(ObjectRelationalMapper.getInstance().toSELECTQuery(new Criterion<>(User.class, "userID",
                CriterionOperator.EqualTo, "foo").and("password", CriterionOperator.EqualTo, "bar")));
    }

    @Test
    public void testDELETETransaction() {
        // hello
        while (true) {
            List<String> result = ObjectRelationalMapper.getInstance().toDELETETransaction(new Criterion<>(Customer.class),
                    DatabaseRepository.getInstance());

            for (String s : result) {
                System.out.println(s);
            }
        }
    }

    @Test
    public void testINSERTTransaction() {
        Customer customer = DatabaseRepository.getInstance().getByCriteria(new Criterion<>(Customer.class)).get(0);
        List<String> result = ObjectRelationalMapper.getInstance().toINSERTTransaction(customer);

        for (String s : result) {
            System.out.println(s);
        }
    }

    @Test
    public void testGetID() {
        int ID = DatabaseRepository.getInstance().getNextID("Customer", "customerID");
        System.out.println(ID);
    }
}
