package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import logic.AuthenticationSystem;

/**
 * @author Dillon Vaghela, Muhammad Shakib Hoque, Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */

public class LoginController {
    public Button loginButton;
    public Button exitButton;
    public TextArea username;
    public PasswordField password;
    private AuthenticationSystem authentication = AuthenticationSystem.getInstance();


    @FXML
    public void loginHandler() throws Exception {
        if (authentication.login(username.getText(), password.getText())) {
            Parent menu = new FXMLLoader(getClass().getResource("/ApplicationPane.fxml")).load();

            Stage stage = new Stage();
            stage.setTitle("GM-SIS");
            stage.setScene(new Scene(menu));
            stage.setMaximized(true);
            stage.show();

            ((Stage) loginButton.getScene().getWindow()).close();

        } else {
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
