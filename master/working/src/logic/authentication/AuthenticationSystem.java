package logic.authentication;

import domain.User;
import domain.UserType;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;
import persistence.DatabaseRepository;

import java.util.List;

import static logic.criterion.CriterionOperator.equalTo;
import static logic.criterion.CriterionOperator.matches;

/**
 * @author Marcello De Bernardi, Dillon Vaghela, Muhammad Shakib Hoque
 * @version 0.1
 * @since 0.1
 */
public class AuthenticationSystem {

    private static AuthenticationSystem instance;
    private CriterionRepository persistence;
    private UserType loginS;
    private String loggedInUser;


    private AuthenticationSystem() {
        persistence = DatabaseRepository.getInstance();
    }

    // currently only accepted userID is "admin", password "password"

    /**
     * Returns the singleton instance of the authentication system.
     *
     * @return authentication system instance
     */
    public static AuthenticationSystem getInstance() {
        if (instance == null) instance = new AuthenticationSystem();
        return instance;
    }

    public boolean login(String username, String password) {
        User user;
        List<User> results = persistence.getByCriteria(new Criterion<>(User.class, "userID", equalTo, username)
                .and("password", equalTo, password));

        if (results.size() != 0) {
            user = results.get(0);
            loginS = user.getUserType();
            loggedInUser = username;
            return true;
        }
        return false;
    }

    public UserType getUserType() {
        return loginS;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public List<User> getUser(String userID, String firstName, String surname) {
        return persistence.getByCriteria(new Criterion<>(User.class, "userID", matches, userID)
                .and("firstName", matches, firstName)
                .and("surname", matches, surname));
    }

    public List<User> searchUsers(String query) {
        return persistence.getByCriteria(new Criterion<>
                (User.class, "userID", matches, query)
                .or("firstName", matches, query)
                .or("surname", matches, query)
                .or("userType", matches, query)
        );
    }

    public boolean commitUser(String userID, String pass, String firstName, String surname, UserType userType) {
        return persistence.commitItem(new User(userID, pass, firstName, surname, userType));
    }

    public boolean deleteUser(String userID) {
        return persistence.deleteItem(new Criterion<>(User.class, "userID", equalTo, userID));
    }

    public List<User> getAllUsers() {
        List<User> results = persistence.getByCriteria(new Criterion<>(User.class));
        return results;
    }
}