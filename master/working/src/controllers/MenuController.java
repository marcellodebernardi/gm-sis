package controllers;

        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import logic.AuthenticationSystem;
        import javafx.stage.Stage;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.scene.Parent;
        import logic.VehicleSys;
        import controllers.LoginController;

/**
 * Created by DillonVaghela on 2/8/17.
 */
public class MenuController {

        public Label UserT;
        public Button addVButton;

        @FXML
public void handleButtonAdd() throws Exception {
                try{
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/AddVehicle.fxml"));
                        Parent menu = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Add Vehicle");
                        stage.setScene(new Scene(menu));
                        stage.show();
                        LoginController A = new LoginController();
                        A.stage.close();


                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }

}

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
