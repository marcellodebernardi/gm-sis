package controllers.common;

import controllers.booking.BookingController;
import controllers.login.LoginController;
import domain.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logic.authentication.AuthenticationSystem;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dillon Vaghela
 *         <p>
 *         todo clean up and add comments
 */
public class MenuController implements Initializable {
    private static MenuController instance;
    private BorderPane customersBasePane;
    private BorderPane vehicleBasePane;
    private BorderPane partsBasePane;
    private BorderPane specialistBasePane;
    private BorderPane spcManagementBasePane;
    private BorderPane usersBasePane;
    @FXML private Button UsersButton;
    @FXML private Button SRCButton;
    @FXML private Button logoutButton;
    @FXML private Button userGreeting;

    public static MenuController getInstance() {
        if (instance == null) instance = new MenuController();
        return instance;
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        if (AuthenticationSystem.getInstance().getUserType().equals(UserType.ADMINISTRATOR)) {
            UsersButton.setDisable(false);
            SRCButton.setDisable(false);

        }
        else {
            UsersButton.setDisable(true);
            SRCButton.setDisable(true);
        }
        userGreeting.setText(userGreeting.getText() + AuthenticationSystem.getInstance().getLoginFirstName());
    }

    public void openApplication() {
        openBookingsTab();
    }

    public void openCustomerTab() {
        openCustomersTab();
    }

    @FXML private void openCustomersTab() {
        try {
            if (customersBasePane == null) customersBasePane
                    = FXMLLoader.load(getClass().getResource("/resources/customer/CustomerView.fxml"));
            Main.getInstance().replaceTabContent(customersBasePane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void openBookingsTab() {
        BookingController.getInstance().show(); // todo standardize
    }

    @FXML private void openVehiclesTab() {
        try {
            if (vehicleBasePane == null) vehicleBasePane
                    = FXMLLoader.load(getClass().getResource("/resources/vehicle/vehiclesBasePane.fxml"));
            Main.getInstance().replaceTabContent(vehicleBasePane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void openPartsTab() {
        try {
            if (partsBasePane == null) partsBasePane
                    = FXMLLoader.load(getClass().getResource("/resources/parts/PartModule.fxml"));
            Main.getInstance().replaceTabContent(partsBasePane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void openSRCTab() {
        try {
            if (specialistBasePane == null) specialistBasePane
                    = FXMLLoader.load(getClass().getResource("/spc/SPBookingManagement.fxml"));
            Main.getInstance().replaceTabContent(specialistBasePane);


        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void openSRCManagementTab() {
        try {
            if (spcManagementBasePane == null) spcManagementBasePane =
                    FXMLLoader.load(getClass().getResource("/spc/SPCManagement.fxml"));
            Main.getInstance().replaceTabContent(spcManagementBasePane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void openUsersTab() {
        try {
            if (usersBasePane == null) usersBasePane
                    = FXMLLoader.load(getClass().getResource("/resources/user/usersBasePane.fxml"));
            Main.getInstance().replaceTabContent(usersBasePane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            if (confirmationAlert("Logout Confirmation - Garage Management System", "You are about to logout") == true) {
                Stage stage = ((Stage) logoutButton.getScene().getWindow());
                stage.close();
                LoginController.getInstance().logoutHandler();
            }
        }
        catch (Exception e) {
            System.out.println("Logout Error");
            e.printStackTrace();
        }
    }

    public boolean confirmationAlert(String title, String message) {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle(title);
        deleteAlert.setHeaderText(message);
        deleteAlert.showAndWait();
        if (deleteAlert.getResult() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }
    }
}
