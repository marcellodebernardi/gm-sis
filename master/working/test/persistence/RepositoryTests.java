package persistence;

import domain.*;
import logic.criterion.Criterion;
import logic.criterion.CriterionOperator;
import logic.criterion.CriterionRepository;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class RepositoryTests {
    /**
     * Tests instantiation of all database singletons. Fails if the process takes longer
     * than 0.4
     */
    @Test
    public void testRepositoryCreationPerformance() {
        long startTime = System.nanoTime();
        CriterionRepository database = DatabaseRepository.getInstance();
        long endTime = System.nanoTime();

        try {
            assertTrue((endTime - startTime) / 1000 < 400000);
        }
        catch (AssertionError e) {
            e.printStackTrace();
            System.err.println("Database instantiation took longer than 0.4 seconds.");
        }
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
    public void testDatabaseSearching() {
        // query for users, customers, vehicles, SRCs and part abstractions
        System.out.println(DatabaseRepository.getInstance().getByCriteria(new Criterion<>(User.class)));
        System.out.println(DatabaseRepository.getInstance().getByCriteria(new Criterion<>(Customer.class)));
        System.out.println(DatabaseRepository.getInstance().getByCriteria(new Criterion<>(Vehicle.class)));
        System.out.println(DatabaseRepository.getInstance().getByCriteria(new Criterion<>(SpecialistRepairCenter.class)));
        System.out.println(DatabaseRepository.getInstance().getByCriteria(new Criterion<>(PartAbstraction.class)));
        System.out.println(DatabaseRepository.getInstance().getByCriteria(new Criterion<>(PartOccurrence.class)));
        System.out.println(DatabaseRepository.getInstance().getByCriteria(new Criterion<>(Installation.class)));
        System.out.println(DatabaseRepository.getInstance().getByCriteria(new Criterion<>(DiagRepBooking.class)));
    }

    @Test
    public void testDatabaseInsertion() {
        DatabaseRepository.getInstance().commitItem(new User(
                "55555",
                "password",
                "Dillon",
                "Vaghela",
                UserType.NORMAL));

        List<User> result = DatabaseRepository
                .getInstance()
                .getByCriteria(new Criterion<>(
                        User.class,
                        "userID",
                        CriterionOperator.equalTo,
                        "55555"));

        DatabaseRepository.getInstance().commitItem(new Vehicle(
                "TEST REG", 1, VehicleType.Car, "TestModel", "QMUL Car company",
                69.5, FuelType.diesel, "red", 100, new Date(), new Date(),
                false, null, null, null, null, null));

        List<Vehicle> vehicle = DatabaseRepository
                .getInstance()
                .getByCriteria(new Criterion<>(
                        Vehicle.class,
                        "vehicleRegNumber",
                        CriterionOperator.equalTo,
                        "TEST REG"));

        DatabaseRepository.getInstance().commitItem(new PartAbstraction("" +
                "Spork plug",
                "Spork plugs are amazing",
                10,
                10,
                null
        ));

        List<PartAbstraction> parts = DatabaseRepository
                .getInstance()
                .getByCriteria(new Criterion<>(
                        PartAbstraction.class));

        assertTrue(result.size() != 0 && vehicle.size() != 0 && parts.size() != 0);
    }

    @Test
    public void testDatabaseDeletion() {
        DatabaseRepository.getInstance().deleteItem(new Criterion<>(
                SpecialistRepairCenter.class,
                "spcID",
                CriterionOperator.equalTo,
                2));
        List<SpecialistRepairCenter> result = DatabaseRepository
                .getInstance()
                .getByCriteria(new Criterion<>(
                        SpecialistRepairCenter.class,
                        "spcID",
                        CriterionOperator.equalTo,
                        2
                ));
        assertTrue(result.size() == 0);
    }

    @Test
    public void testINSERTTransaction() {
        while (true) {
            Customer customer = DatabaseRepository.getInstance().getByCriteria(new Criterion<>(Customer.class)).get(0);
            List<String> result = ObjectRelationalMapper.getInstance().toCOMMITTransaction(customer);

            System.out.println(result);
        }
    }

    @Test
    public void testGetID() {
        int ID = DatabaseRepository.getInstance().getNextID("Customer", "customerID");
        System.out.println(ID);
    }
}
