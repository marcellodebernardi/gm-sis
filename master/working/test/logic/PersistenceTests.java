package logic;

import entities.Customer;
import entities.PartOccurrence;
import entities.User;
import logic.Criterion;
import logic.CriterionRepository;
import org.junit.Test;
import static org.junit.Assert.*;

import persistence.DatabaseRepository;

import java.util.List;

import static logic.CriterionOperator.*;


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
        User user = persistence.getByCriteria(new Criterion<>(User.class, "userID", EqualTo, "00000").
                and("password", EqualTo, "password")).get(0);
        System.out.println(user.getUserID() + " " + user.getPassword());
        assertNotNull(user);
    }

    @Test
    public void getAllCustomerFromDatabase() {
        List<Customer> customers = persistence.getByCriteria(
                new Criterion<>(Customer.class));

        for(Customer c : customers) {
            System.out.println(c.getCustomerFirstname() + " " + c.getCustomerSurname());
        }
        assertNotNull(customers);
    }

    @Test
    public void getAllPartOccurrences() {
        List<PartOccurrence> parts = persistence.getByCriteria(new Criterion<>(PartOccurrence.class));

        for (PartOccurrence p : parts)
            System.out.println(p.getPartOccurrenceID());
    }
}
