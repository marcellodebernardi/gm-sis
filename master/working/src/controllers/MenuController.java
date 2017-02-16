package controllers;

        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.fxml.FXML;
        import javafx.geometry.Pos;
        import javafx.scene.control.*;
        import javafx.scene.layout.AnchorPane;
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
        Stage primaryStage;
        public void initialize() throws Exception {
                //setStage();
                // setUserType();
        }

        public void setStage()
        {
                primaryStage =   (Stage) UserT.getScene().getWindow();
        }


        @FXML
        public void handleButtonSearch() throws Exception {
                try{
                        if (searchStage != null)
                        {
                                if (searchStage.isShowing())
                                {
                                        showAlert();
                                        searchStage.setAlwaysOnTop(true);
                                        searchStage.setFullScreen(true);
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/SearchVehicle.fxml"));
                        Parent menu = fxmlLoader.load();
                        searchStage = new Stage();
                        searchStage.setTitle("Search Vehicle");
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
                                        showAlert();
                                        addStage.setAlwaysOnTop(true);
                                        return;
                                }
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

        public void showAlert()
        {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("This window is already open, press ok and your window will show on top");
                alert.showAndWait();
        }

        @FXML
        public void handleButtonDelete() throws Exception {
                try{
                        if (deleteStage != null)
                        {
                                if (deleteStage.isShowing())
                                {
                                        showAlert();
                                        deleteStage.setAlwaysOnTop(true);
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
                                        showAlert();
                                        editSearchStage.setAlwaysOnTop(true);
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

        public void setUserT()
        {
                UserT.setText("admin");
        }




}
