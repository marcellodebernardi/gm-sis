package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.AuthenticationSystem;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

/**
 * @author Dillon Vaghela, Muhammad Shakib Hoque
 * @version 0.1
 * @since 0.1
 */

public class LoginController {

    public Button loginBtn;
    public Button exitBtn;
    public TextArea username;
    public PasswordField password;
    private AuthenticationSystem auth = AuthenticationSystem.getInstance();

    @FXML
    public void handleButtonClick() throws Exception {

        auth.login(username.getText(), password.getText());
        //Main.primaryStage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/Menuv2.fxml"));
        Parent menu = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(menu));
        stage.showAndWait();
        stage.close();

    }

    @FXML
    public void handleButtonClick2() {
        System.exit(0);
    }

    }
