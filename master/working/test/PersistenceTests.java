import entities.Booking;
import logic.Criterion;
import logic.CriterionRepository;
import org.junit.Test;
import persistence.DatabaseRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class PersistenceTests {

    @Test
    /**
     * Tests the parsing of lists of criteria into string representing SQL statements.
     */
    public void testParseSQLwhere() {
        CriterionRepository persistenceLayer = DatabaseRepository.getInstance();
        // stuff
    }
}
