package controllers;

        import javafx.event.EventHandler;
        import javafx.fxml.FXML;
        import javafx.geometry.Pos;
        import javafx.scene.control.*;
        import javafx.scene.layout.VBox;
        import javafx.stage.Modality;
        import javafx.stage.WindowEvent;
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
        public Button deleteVButton;
        public Button editVButton;
        Stage addStage;
        Stage deleteStage;
        Stage editSearchStage;
        Stage searchStage;

        @FXML
        public void handleButtonSearch() throws Exception {
                try{
                        if (searchStage != null)
                        {
                                if (searchStage.isShowing())
                                {
                                        System.out.println("Window is already open");
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/SearchVehicle.fxml"));
                        Parent menu = fxmlLoader.load();
                        searchStage = new Stage();
                        searchStage.setTitle("Add Vehicle");
                        searchStage.setScene(new Scene(menu));
                        searchStage.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }

        }




        @FXML
public void handleButtonAdd() throws Exception {
                try{
                        if (addStage != null)
                        {
                                if (addStage.isShowing())
                                {
                                System.out.println("Window is already open");
                                return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/AddVehicle.fxml"));
                        Parent menu = fxmlLoader.load();
                        addStage = new Stage();
                        addStage.setTitle("Add Vehicle");
                        addStage.setScene(new Scene(menu));
                        addStage.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }

}
        @FXML
        public void handleButtonDelete() throws Exception {
                try{
                        if (deleteStage != null)
                        {
                                if (deleteStage.isShowing())
                                {
                                        System.out.println("Window is already open");
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/DeleteVehicle.fxml"));
                        Parent menu = fxmlLoader.load();
                        deleteStage = new Stage();
                        deleteStage.setTitle("Delete Vehicle");
                        deleteStage.setScene(new Scene(menu));
                        deleteStage.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }

        }

        public void handleButtonEditSearch() throws Exception {
                try{
                        if (editSearchStage != null)
                        {
                                if (editSearchStage.isShowing())
                                {
                                        System.out.println("Window is already open");
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/EditSearchVehicle.fxml"));
                        Parent menu = fxmlLoader.load();
                        editSearchStage = new Stage();
                        editSearchStage.setTitle("Edit Search Vehicle");
                        editSearchStage.setScene(new Scene(menu));
                        editSearchStage.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }

        }



}
