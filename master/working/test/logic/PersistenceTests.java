package logic;

import domain.Customer;
import domain.PartOccurrence;
import domain.User;
import domain.Vehicle;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;
import org.junit.Test;
import persistence.DatabaseRepository;

import java.util.List;

import static logic.criterion.CriterionOperator.equalTo;
import static org.junit.Assert.assertNotNull;


/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class PersistenceTests {
    CriterionRepository persistence = DatabaseRepository.getInstance();

    /**
     * Tests the parsing of lists of criteria into string representing SQL statements.
     */
    @Test
    public void testParseSQLwhere() {
    }

    @Test
    public void getSingleUserFromDatabase() {
        User user = persistence.getByCriteria(new Criterion<>(User.class, "userID", equalTo, "00000").
                and("password", equalTo, "password")).get(0);
        System.out.println(user.getUserID() + " " + user.getPassword());
        assertNotNull(user);
    }

    @Test
    public void getAllCustomerFromDatabase() {
        List<Customer> customers = persistence.getByCriteria(
                new Criterion<>(Customer.class));

        for (Customer c : customers) {
            System.out.println(c.getCustomerID() + " " + c.getCustomerFirstname() + " " + c.getCustomerSurname());
            System.out.println("Vehicles: " + c.getVehicles().size());
            if (c.getVehicles().size() > 0) {
                System.out.println(c.getVehicles().get(0).getDateLastServiced());
            }
        }
        assertNotNull(customers);
    }

    @Test
    public void getAllPartOccurrences() {
        List<PartOccurrence> parts = persistence.getByCriteria(new Criterion<>(PartOccurrence.class));

        for (PartOccurrence p : parts)
            System.out.println(p.getPartOccurrenceID());
    }

    @Test
    public void getAllVehicles() {
        List<Vehicle> vehicles = persistence.getByCriteria(new Criterion<>(Vehicle.class));

        for (Vehicle v : vehicles) {
            System.out.println(v.getVehicleRegNumber() + " " + v.getCustomerID());
        }
    }
}
