package logic.criterion;

import domain.Customer;
import domain.DiagRepBooking;
import domain.Vehicle;
import org.junit.Test;
import persistence.DatabaseRepository;

import static logic.criterion.CriterionOperator.In;
import static logic.criterion.CriterionOperator.Matches;

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
                (DiagRepBooking.class, "vehicleRegNumber", Matches, "Gooby")
                .or("vehicleRegNumber", In, new Criterion<>
                        (Vehicle.class, "manufacturer", Matches, "Gauby"))
                .or("vehicleRegNumber", In, new Criterion<>
                        (Vehicle.class, "customerID", In, new Criterion<>
                                (Customer.class, "customerFirstname", Matches, "Patrolone")
                                .or("customerSurname", Matches, "Unione"))));
    }
}
