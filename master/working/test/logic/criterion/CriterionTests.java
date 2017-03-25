package logic.criterion;

import domain.Customer;
import domain.DiagRepBooking;
import domain.Vehicle;
import org.junit.Test;
import persistence.DatabaseRepository;

import static logic.criterion.CriterionOperator.in;
import static logic.criterion.CriterionOperator.matches;

/**
 * @author Marcello De Bernardi
 */
public class CriterionTests {
    CriterionRepository persistence = DatabaseRepository.getInstance();

    /**
     * Tests the parsing of lists of criteria into string representing SQL statements.
     */
    @Test
    public void testSubqueries() {
        System.out.println(new Criterion<>
                (DiagRepBooking.class, "vehicleRegNumber", matches, "Gooby")
                .or("vehicleRegNumber", in, new Criterion<>
                        (Vehicle.class, "manufacturer", matches, "Gauby"))
                .or("vehicleRegNumber", in, new Criterion<>
                        (Vehicle.class, "customerID", in, new Criterion<>
                                (Customer.class, "customerFirstname", matches, "Patrolone")
                                .or("customerSurname", matches, "Unione"))));
    }
}
