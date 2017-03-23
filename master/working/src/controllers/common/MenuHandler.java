package controllers.common;

import domain.UserType;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import logic.authentication.AuthenticationSystem;

import java.io.IOException;

/**
 * @author Marcello De Bernardi
 */
public class MenuHandler {
    // todo shove back into menucontroller
    private static MenuHandler instance;
    private BorderPane view;
    private Button usersButton;


    private MenuHandler() throws IOException {
        view = FXMLLoader.load(getClass().getResource("/common/applicationPane.fxml"));
        usersButton = (Button) view.lookup("#UsersButton");
        setUserType();
    }

    public static MenuHandler getInstance() {
        try {
            if (instance == null) instance = new MenuHandler();
            return instance;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BorderPane show() {
        return view;
    }


    private void setUserType() {
        if (AuthenticationSystem.getInstance().getUserType().equals(UserType.ADMINISTRATOR)) {
            usersButton.setDisable(false);
        }
        else {
            usersButton.setDisable(true);
        }
    }
}
