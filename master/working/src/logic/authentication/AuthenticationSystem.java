package logic.authentication;

import domain.User;
import domain.UserType;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;
import persistence.DatabaseRepository;

import java.util.List;

import static logic.criterion.CriterionOperator.EqualTo;
import static logic.criterion.CriterionOperator.Matches;

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

    public List<User> searchUsers(String userID, String firstName, String surname) {
        List<User> results = persistence.getByCriteria(new Criterion<>(User.class, "userID", Matches, userID).and("firstName", Matches,firstName)
        .and("surname", Matches, surname));
        return results;
    }

    public List<User> searchUsersT(String userID, String firstName, String surname,  UserType userType) {
        List<User> results = persistence.getByCriteria(new Criterion<>(User.class, "userID", Matches, userID).and("firstName", Matches,firstName)
                .and("surname", Matches, surname).and("userType", Matches, userType));
        return results;
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