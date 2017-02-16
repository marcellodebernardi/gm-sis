package controllers;

        import entities.UserType;
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
        public Tab userTab;
        Stage addStage;
        Stage deleteStage;
        Stage editSearchStage;
        Stage searchStage;
        Stage addBStage;
        Stage deletebStage;
        Stage editBStage;
        Stage todayBStage;
        Stage allBStage;

        public void initialize() throws Exception {
                setUserType();
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
                                        //searchStage.setFullScreen(true);
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
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/EditVehicle.fxml"));
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

        public void setUserType()
        {
                if (AuthenticationSystem.getInstance().getUserType().equals(UserType.ADMINISTRATOR)) {
                        UserT.setText("admin");
                }
                else {
                        UserT.setText("User");
                        userTab.setDisable(true);
                }
        }

        public void addBooking()
        {
                try{
                        if (addBStage != null)
                        {
                                if (addBStage.isShowing())
                                {
                                        showAlert();
                                        addBStage.setAlwaysOnTop(true);
                                        //addBStage.setFullScreen(true);
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/addBooking.fxml"));
                        Parent menu = fxmlLoader.load();
                        addBStage = new Stage();
                        addBStage.setTitle("Add Booking");
                        addBStage.setScene(new Scene(menu));
                        addBStage.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }
        }

        public void editBooking()
        {
                try{
                        if (editBStage != null)
                        {
                                if (editBStage.isShowing())
                                {
                                        showAlert();
                                        editBStage.setAlwaysOnTop(true);
                                        //editBStage.setFullScreen(true);
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/editSBooking.fxml"));
                        Parent menu = fxmlLoader.load();
                        editBStage = new Stage();
                        editBStage.setTitle("Edit Booking");
                        editBStage.setScene(new Scene(menu));
                        editBStage.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }
        }

        public void deleteBooking()
        {
                try{
                        if (deletebStage != null)
                        {
                                if (deletebStage.isShowing())
                                {
                                        showAlert();
                                        deletebStage.setAlwaysOnTop(true);
                                        //editBStage.setFullScreen(true);
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/deleteBooking.fxml"));
                        Parent menu = fxmlLoader.load();
                        deletebStage = new Stage();
                        deletebStage.setTitle("Delete Booking");
                        deletebStage.setScene(new Scene(menu));
                        deletebStage.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }
        }

        public void todayBooking()
        {
                try{
                        if (todayBStage != null)
                        {
                                if (todayBStage.isShowing())
                                {
                                        showAlert();
                                        todayBStage.setAlwaysOnTop(true);
                                        //editBStage.setFullScreen(true);
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/todayBooking.fxml"));
                        Parent menu = fxmlLoader.load();
                        todayBStage = new Stage();
                        todayBStage.setTitle("Today's Bookings");
                        todayBStage.setScene(new Scene(menu));
                        todayBStage.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }
        }

        public void allBooking()
        {
                try{
                        if (allBStage != null)
                        {
                                if (allBStage.isShowing())
                                {
                                        showAlert();
                                        allBStage.setAlwaysOnTop(true);
                                        //editBStage.setFullScreen(true);
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/allBooking.fxml"));
                        Parent menu = fxmlLoader.load();
                        allBStage = new Stage();
                        allBStage.setTitle("All Bookings");
                        allBStage.setScene(new Scene(menu));
                        allBStage.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }
        }

}
