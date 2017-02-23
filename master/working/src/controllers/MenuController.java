package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Dillon Vaghela
 *         <p>
 *         todo clean up and add comments
 */
public class MenuController {

    // todo move these into module controllers
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

    // todo what is all this?
    Stage addBStage;
    Stage addSpecialistBooking = new Stage();
    Stage deleteSpecialistBooking;
    Stage addCustomerStage;
    Stage editCustomerStage;
    Stage searchCustomerStage;
    Stage PartModule;
    Stage addUser = new Stage();
    Stage deleteUser = new Stage();
    Stage editUser = new Stage();
    Stage searchUser = new Stage();


    /*
    public void initialize() throws Exception {
        setUserType();
    }
    */

    public void openTodayTab() {
        // todo Main.getInstance().replaceTabContent(FXMLLoader.load(getClass().getResource("")));
    }

    public void openCustomersTab() {

    }

    public void openBookingsTab() {
        BorderPane bookingsBasePane;
        try {
            bookingsBasePane = FXMLLoader.load(getClass().getResource("/resources/booking/bookingsBasePane.fxml"));
            bookingsBasePane.setVisible(true);
            Main.getInstance().replaceTabContent(bookingsBasePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openVehiclesTab() {
        BorderPane VehicleBasePane = new BorderPane();
        try {
            FlowPane addEdit = FXMLLoader.load(getClass().getResource("/resources/vehicle/AddVehicle.fxml"));
            FlowPane view = FXMLLoader.load(getClass().getResource("/resources/vehicle/SearchVehicle.fxml"));
            VehicleBasePane.setVisible(true);
            VehicleBasePane.setLeft(addEdit);
            VehicleBasePane.setCenter(view);
            Main.getInstance().replaceTabContent(VehicleBasePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openPartsTab() {
        BorderPane partsBasePane;
        try {
            partsBasePane = FXMLLoader.load(getClass().getResource("/resources/PartModule.fxml"));
            partsBasePane.setVisible(true);
            Main.getInstance().replaceTabContent(partsBasePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSRCTab() {
        // todo
    }

    public void openUsersTab() {
        // todo
    }


    @FXML





    /*
    public void setUserType() {
        if (AuthenticationSystem.getInstance().getUserType().equals(UserType.ADMINISTRATOR)) {
            UserT.setText("admin");
        } else {
            UserT.setText("User");
            userTab.setDisable(true);
        }
    }
    */

    public void addBooking() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/addBooking.fxml"));
            Parent menu = fxmlLoader.load();
            addBStage = new Stage();
            addBStage.setTitle("Add Booking");
            addBStage.setScene(new Scene(menu));
            addBStage.show();
        } catch (Exception e) {
            System.out.println("cant open");
        }
    }


    public void searchSRC() {


    }


    public void addSRCBooking() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/AddSRCB.fxml"));
            Parent menu = fxmlLoader.load();
            addSpecialistBooking = new Stage();
            addSpecialistBooking.setTitle("Add Booking");
            addSpecialistBooking.setScene(new Scene(menu));
            addSpecialistBooking.show();
        } catch (Exception e) {
            System.out.println("cant open");
        }

    }

    public void deleteSRCBooking() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/DeleteSRB.fxml"));
            Parent menu = fxmlLoader.load();
            deleteSpecialistBooking = new Stage();
            deleteSpecialistBooking.setTitle("Delete Booking");
            deleteSpecialistBooking.setScene(new Scene(menu));
            deleteSpecialistBooking.show();
        } catch (Exception e) {
            System.out.println("cant open");
        }
    }

    public void editSRCBooking() {

    }

    //action listener for opening 'Add Customer' window
    public void addCustomerListener() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/AddCustomer.fxml"));
            Parent menu = fxmlLoader.load();
            addCustomerStage = new Stage();
            addCustomerStage.setTitle("Add Customer");
            addCustomerStage.setScene(new Scene(menu));
            addCustomerStage.show();
        } catch (Exception e) {
            System.out.println("cant open");
        }
    }

    //action listener for opening 'Edit Customer' window
    public void editCustomerListener() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/EditCustomer.fxml"));
            Parent menu = fxmlLoader.load();
            editCustomerStage = new Stage();
            editCustomerStage.setTitle("Edit Customer");
            editCustomerStage.setScene(new Scene(menu));
            editCustomerStage.show();

        } catch (Exception e) {
            System.out.println("cant open");
        }
    }

    //action listener for opening 'Search Customer' window
    public void searchCustomerListener() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/SearchCustomer.fxml"));
            Parent menu = fxmlLoader.load();
            searchCustomerStage = new Stage();
            searchCustomerStage.setTitle("Search Customer");
            searchCustomerStage.setScene(new Scene(menu));
            searchCustomerStage.show();
        } catch (Exception e) {
            System.out.println("cant open");
        }
    }

    public void PartMenu() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/PartModule.fxml"));
        Parent menu = fxmlLoader.load();
        PartModule = new Stage();
        PartModule.setTitle("Parts Module");
        PartModule.setScene(new Scene(menu));
        PartModule.show();

    }

    public void addUser() throws Exception {
        if (StageCheckerU()) {
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/addUser.fxml"));
        Parent menu = fxmlLoader.load();
        addUser = new Stage();
        addUser.setTitle("Search Customer");
        addUser.setScene(new Scene(menu));
        addUser.show();
    }

    public void deleteUser() throws Exception {
        if (StageCheckerU()) {
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/deleteUser.fxml"));
        Parent menu = fxmlLoader.load();
        deleteUser = new Stage();
        deleteUser.setTitle("Search Customer");
        deleteUser.setScene(new Scene(menu));
        deleteUser.show();
    }

    public void editUser() throws Exception {
        if (StageCheckerU()) {
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/editUser.fxml"));
        Parent menu = fxmlLoader.load();
        editUser = new Stage();
        editUser.setTitle("Search Customer");
        editUser.setScene(new Scene(menu));
        editUser.show();
    }

    public void searchUser() throws Exception {
        if (StageCheckerU()) {
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/searchUser.fxml"));
        Parent menu = fxmlLoader.load();
        searchUser = new Stage();
        searchUser.setTitle("Search Customer");
        searchUser.setScene(new Scene(menu));
        searchUser.show();
    }


    public boolean StageCheckerU() {
        if (addUser.isShowing() || deleteUser.isShowing() || editUser.isShowing() || searchUser.isShowing()) {
            ArrayList<Stage> list = new ArrayList<Stage>();
            list.add(addUser);
            list.add(deleteUser);
            list.add(editUser);
            list.add(searchUser);
        }
        return false;
    }
}
