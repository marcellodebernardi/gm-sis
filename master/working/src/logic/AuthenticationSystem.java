package logic;

import entities.User;
import persistence.DatabaseRepository;

import java.util.Collections;

/**
 * @author Marcello De Bernardi, Dillon Vaghela, Muhammad Shakib Hoque
 * @version 0.1
 * @since 0.1
 */
public class AuthenticationSystem {

    private static AuthenticationSystem instance;
    private CriterionRepository persistence = DatabaseRepository.getInstance();


    private AuthenticationSystem() {

    }

    // currently only accepted userID is "team31", password "hello"
    public boolean login(String username, String password) {
        User result = persistence.getByCriteria(false, User.class,
                Collections.singletonList(new User(username, password, null, null, null)))
                .get(0);

        return (result.getUserID().equals(username) && result.getPassword().equals(password));
    }

    /**
     * Returns the singleton instance of the authentication system.
     *
     * @return authentication system instance
     */
    public static AuthenticationSystem getInstance() {
        if (instance == null) instance = new AuthenticationSystem();
        return instance;
    }
}