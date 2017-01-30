package controllers;

import javafx.scene.control.*;
import logic.AuthenticationSystem;

public class LoginController {

    public Button loginBtn;
    public Button exitBtn;
    public TextArea username;
    public PasswordField password;


    public void handleButtonClick()
    {
        AuthenticationSystem.checkLogin(username.getText(), password.getText());
    }

    public void handleButtonClick2() {

        System.exit(0);
    }

    }
