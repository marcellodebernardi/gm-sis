package controllers;

        import domain.UserType;
        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import logic.AuthenticationSystem;
        import javafx.stage.Stage;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.scene.Parent;

        import java.io.IOException;

/**
 * Created by DillonVaghela on 2/8/17.
 */
public class MenuController {

        public Label UserT;
        public Button addVButton;
        public Button deleteVButton;
        public Button editVButton;
        public Tab userTab;
        public Button addSRBooking;
        public Button editSRBooking;
        public Button deleteSRBooking;
        public Button addCustomer;
        public Button editCustomer;
        public Button searchCustomer;
        Stage addStage;
        Stage deleteStage;
        Stage editSearchStage;
        Stage searchStage;
        Stage addBStage;
        Stage deletebStage;
        Stage editBStage;
        Stage todayBStage;
        Stage allBStage;
        Stage addSpecialistBooking;
        Stage deleteSpecialistBooking;
        Stage addCustomerStage;
        Stage editCustomerStage;
        Stage deleteCustomerStage;
        Stage viewCustomerStage;
        Stage searchCustomerStage;
        Stage PartModule;

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

        public void searchSRC()
        {


        }

        @FXML
        public void addSRCBooking() throws Exception {
                try{
                        if (addSpecialistBooking != null)
                        {
                                if (addSpecialistBooking.isShowing())
                                {
                                        showAlert();
                                        addSpecialistBooking.setAlwaysOnTop(true);
                                        //editBStage.setFullScreen(true);
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/AddSRCB.fxml"));
                        Parent menu = fxmlLoader.load();
                        addSpecialistBooking = new Stage();
                        addSpecialistBooking.setTitle("Add Booking");
                        addSpecialistBooking.setScene(new Scene(menu));
                        addSpecialistBooking.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }

        }

        public void deleteSRCBooking()
        {
                try{
                        if (deleteSpecialistBooking != null)
                        {
                                if (deleteSpecialistBooking.isShowing())
                                {
                                        showAlert();
                                        deleteSpecialistBooking.setAlwaysOnTop(true);
                                        //editBStage.setFullScreen(true);
                                        return; }
                        }
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/DeleteSRB.fxml"));
                        Parent menu = fxmlLoader.load();
                        deleteSpecialistBooking = new Stage();
                        deleteSpecialistBooking.setTitle("Delete Booking");
                        deleteSpecialistBooking.setScene(new Scene(menu));
                        deleteSpecialistBooking.show();
                }
                catch (Exception e)
                {
                        System.out.println("cant open");
                }
        }

        public void editSRCBooking()
        {

        }

    //action listener for opening 'Add Customer' window
    public void addCustomerListener()
    {
        try
        {
            if(addCustomerStage != null)
            {
                if(addCustomerStage.isShowing())
                {
                    showAlert();
                    addCustomerStage.setAlwaysOnTop(true);
                    return;
                }
            }
            else
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/AddCustomer.fxml"));
                Parent menu = fxmlLoader.load();
                addCustomerStage = new Stage();
                addCustomerStage.setTitle("Add Customer");
                addCustomerStage.setScene(new Scene(menu));
                addCustomerStage.show();
            }
        }
        catch(Exception e)
        {
            System.out.println("cant open");
        }
    }

    //action listener for opening 'Edit Customer' window
    public void editCustomerListener()
    {
        try
        {
            if(editCustomerStage != null)
            {
                if(editCustomerStage.isShowing())
                {
                    showAlert();
                    editCustomerStage.setAlwaysOnTop(true);
                    return;
                }
            }
            else
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/EditCustomer.fxml"));
                Parent menu = fxmlLoader.load();
                editCustomerStage = new Stage();
                editCustomerStage.setTitle("Edit Customer");
                editCustomerStage.setScene(new Scene(menu));
                editCustomerStage.show();
            }
        }
        catch(Exception e)
        {
            System.out.println("cant open");
        }
    }

    //action listener for opening 'Search Customer' window
    public void searchCustomerListener()
    {
        try
        {
            if(searchCustomerStage != null)
            {
                if(searchCustomerStage.isShowing())
                {
                    showAlert();
                    searchCustomerStage.setAlwaysOnTop(true);
                    return;
                }
            }
            else
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/SearchCustomer.fxml"));
                Parent menu = fxmlLoader.load();
                searchCustomerStage = new Stage();
                searchCustomerStage.setTitle("Search Customer");
                searchCustomerStage.setScene(new Scene(menu));
                searchCustomerStage.show();
            }
        }
        catch(Exception e)
        {
            System.out.println("cant open");
        }
    }

    public void PartMenu() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/PartModule.fxml"));
        Parent menu = fxmlLoader.load();
        PartModule = new Stage();
        PartModule.setTitle("Main Menu");
        PartModule.setScene(new Scene(menu));
        PartModule.show();

    }


}
