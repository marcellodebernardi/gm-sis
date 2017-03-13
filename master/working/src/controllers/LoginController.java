package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import logic.AuthenticationSystem;
import main.Main;

/**
 * @author Dillon Vaghela, Muhammad Shakib Hoque, Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */

public class LoginController {
    public Button loginButton;
    public Button exitButton;
    public TextField username;
    public PasswordField password;
    private AuthenticationSystem authentication = AuthenticationSystem.getInstance();

    @FXML
    public void loginHandler() throws Exception {
        if (authentication.login(username.getText(), password.getText())) {
            BorderPane menu = MenuHandler.getInstance().show();
            Main.getInstance().setRootPane((BorderPane) menu);
            (new MenuController()).openTodayTab();
        }
        else {
            showAlert();
            password.clear();
        }
    }

    @FXML
    public void exitHandler() {
        System.exit(0);
    }

    private void showAlert() {
        // todo change popup to something more elegant
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("The username and/or password is incorrect");
        alert.showAndWait();
    }
}
