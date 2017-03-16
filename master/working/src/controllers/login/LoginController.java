package controllers.login;

import controllers.common.MenuController;
import controllers.common.MenuHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import logic.authentication.AuthenticationSystem;
import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dillon Vaghela, Muhammad Shakib Hoque, Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */

public class LoginController
{
    public Button loginButton;
    public Button exitButton;
    public TextField username;
    public PasswordField password;
    private AuthenticationSystem authentication = AuthenticationSystem.getInstance();
    private static LoginController instance;//////////


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

    //////////
    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }
}
