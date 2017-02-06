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

    public static boolean checkLogin(String username, String password) {

        System.out.println(username);
        System.out.println(password);
        if (username.equals("h") && password.equals("b"))
        {
            System.out.println("logged in");
            return true;
        }
        else
        {
            System.out.println("wrong");
            return false;

        }
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
