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
    private String loginFirstName;


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
            loginFirstName = user.getFirstName();
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

    public String getLoginFirstName() {
        return loginFirstName;
    }


    public List<User> searchUsers(String fn, String ln, String id) {
        return persistence.getByCriteria(new Criterion<>
                (User.class, "userID", matches, id)
                .and("firstName", matches, fn)
                .and("surname", matches, ln)
        );
    }

    public List<User> searchUsersUT(String fn, String ln, String id, UserType userType) {
        return persistence.getByCriteria(new Criterion<>
                (User.class, "userType", equalTo, userType)
                .and("firstName", matches, fn)
                .and("surname", matches, ln)
                .and("userID", matches, id)
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