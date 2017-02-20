package logic;

import domain.User;
import domain.UserType;
import persistence.DatabaseRepository;
import static logic.CriterionOperator.*;

import java.util.List;

/**
 * @author Marcello De Bernardi, Dillon Vaghela, Muhammad Shakib Hoque
 * @version 0.1
 * @since 0.1
 */
public class AuthenticationSystem {

    private static AuthenticationSystem instance;
    private CriterionRepository persistence;
    private UserType loginS;


    private AuthenticationSystem() {
        persistence = DatabaseRepository.getInstance();
    }

    // currently only accepted userID is "admin", password "password"


    public boolean login(String username, String password) {
        User user;
        List<User> results = persistence.getByCriteria(new Criterion<>(User.class, "userID", EqualTo, username)
                .and("password", EqualTo, password));

        if (results.size() != 0) {
            user = results.get(0);
            loginS = user.getUserType();
            return true;
        }
        return false;
    }


    public UserType getUserType()
    {
        return loginS;
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