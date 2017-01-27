package logic;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class AuthenticationSystem {

    private static AuthenticationSystem instance;


    private AuthenticationSystem() {

    }

    /**
     * Returns the singleton instance of the authentication system.
     *
     * @return authentication system instance
     */
    public static AuthenticationSystem getInstance() {
        if (instance != null) {
            return instance;
        }
        else {
            instance = new AuthenticationSystem();
            return instance;
        }
    }
}
