package controllers;

        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import logic.AuthenticationSystem;
        import javafx.stage.Stage;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.scene.Parent;

/**
 * Created by DillonVaghela on 2/8/17.
 */
public class MenuController {

public Label UserT;

public void setUserType()
{

        System.out.println("working");
        try
        {

                UserT.setText("admin");

        }

        catch (Exception e)
        {

                System.out.println("cant set");

        }
}

}
