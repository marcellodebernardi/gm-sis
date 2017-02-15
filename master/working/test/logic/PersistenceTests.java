package logic;

import entities.User;
import org.junit.Test;
import static org.junit.Assert.*;

import persistence.DatabaseRepository;
import static logic.CriterionOperator.*;


/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class PersistenceTests {
    CriterionRepository persistenceLayer = DatabaseRepository.getInstance();

    @Test
    /**
     * Tests the parsing of lists of criteria into string representing SQL statements.
     */
    public void testParseSQLwhere() {
    }

    @Test
    public void getUserFromDatabase() {

        User user = persistenceLayer.getByCriteria(new Criterion<>(User.class, "userID", EqualTo, "00000").
                and("password", EqualTo, "password")).get(0);
        assertNotNull(user);
    }
}
