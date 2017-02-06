package logic;

import entities.User;
import org.junit.Test;
import static org.junit.Assert.*;

import persistence.DatabaseRepository;

import java.util.Collections;


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
        User user = persistenceLayer.getByCriteria(false, User.class,
                Collections.singletonList(new User(
                        "team31",
                        "hello",
                        null,
                        null,
                        null))).get(0);
        assertNotNull(user);
    }
}
