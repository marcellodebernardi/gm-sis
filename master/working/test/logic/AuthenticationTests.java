package logic;

import logic.authentication.AuthenticationSystem;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the authentication module.
 *
 * @author Marcello De Bernardi
 */
public class AuthenticationTests {

    /**
     * Checks that login succeeds with correct credentials, and fails with incorrect credentials.
     */
    @Test
    public void testLoginCorrectness() {
        assertTrue(AuthenticationSystem.getInstance().login("admin", "password")
                && !AuthenticationSystem.getInstance().login("hobo", "gooby"));
    }
}
