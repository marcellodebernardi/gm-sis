package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.AuthenticationSystem;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import main.Main;

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
    public Stage stage;
    private AuthenticationSystem auth = AuthenticationSystem.getInstance();



    @FXML
    public void handleButtonClick() throws Exception {

        try{
        auth.login(username.getText(), password.getText());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/Menuv2.fxml"));
        Parent menu = fxmlLoader.load();
        stage = new Stage();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(menu));
        stage.show();


        }
        catch (Exception e)
        {
            System.out.println("The username and/or password is incorrect");
            password.clear();
        }

    }



    @FXML
    public void handleButtonClick2() {
        System.exit(0);
    }

    }
