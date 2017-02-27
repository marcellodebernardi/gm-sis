package logic;

import domain.User;
import domain.UserType;
import persistence.DatabaseRepository;

import java.util.List;

import static logic.CriterionOperator.EqualTo;

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


    public boolean login(String username, String password) {
        User user;
        List<User> results = persistence.getByCriteria(new Criterion<>(User.class, "userID", EqualTo, username)
                .and("password", EqualTo, password));

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

    public boolean addEditUser(String userID, String pass, String firstName, String surname, UserType userType) {
        return persistence.commitItem(new User(userID, pass, firstName, surname, userType));
    }

    public boolean deleteUser(String userID) {
        return persistence.deleteItem(new Criterion<>(User.class, "userID", EqualTo, userID));
    }

    public User searchAUser(String userID) {
        List<User> results = persistence.getByCriteria(new Criterion<>(User.class,
                "userID", EqualTo, userID));
        return results != null ? results.get(0) : null;
    }

    public List<User> getUsersList() {
        List<User> results = persistence.getByCriteria(new Criterion<>(User.class));
        return results;
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