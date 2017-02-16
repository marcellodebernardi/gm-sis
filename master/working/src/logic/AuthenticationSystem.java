package logic;

import entities.User;
import persistence.DatabaseRepository;
import static logic.CriterionOperator.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.AuthenticationSystem;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

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

    // currently only accepted userID is "00000", password "password"
    public boolean login(String username, String password) {
        return persistence.getByCriteria(new Criterion<>(User.class, "userID", EqualTo, username)
                .and("password", EqualTo, password)).size() != 0;
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