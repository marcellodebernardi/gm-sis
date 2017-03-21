package controllers.common;

import controllers.booking.BookingController;
import domain.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
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
    private BorderPane todayPane;
    private BorderPane customersBasePane;
    private BorderPane bookingBasePane;
    private BorderPane vehicleBasePane;
    private BorderPane partsBasePane;
    private BorderPane specialistBasePane;
    private BorderPane spcManagementBasePane;
    private BorderPane usersBasePane;

    @FXML private Button UsersButton;
    @FXML private Button SRCButton;

    @Override public void initialize(URL location, ResourceBundle resources) {
        if (AuthenticationSystem.getInstance().getUserType().equals(UserType.ADMINISTRATOR)) {
            UsersButton.setDisable(false);
            SRCButton.setDisable(false);

        }
        else {
            UsersButton.setDisable(true);
            SRCButton.setDisable(true);
        }
    }


    @FXML private void logout() {

    }



    public void openTodayTab() {
        try {
            if (todayPane == null) todayPane
                    = FXMLLoader.load(getClass().getResource("/resources/today/TodayPane.fxml"));
            Main.getInstance().replaceTabContent(todayPane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openCustomersTab() {
        try {
            if (customersBasePane == null) customersBasePane
                    = FXMLLoader.load(getClass().getResource("/resources/customer/CustomerView.fxml"));
            Main.getInstance().replaceTabContent(customersBasePane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openBookingsTab() {
        BookingController.getInstance().show(); // todo standardize
    }

    public void openVehiclesTab() {
        try {
            if (vehicleBasePane == null) vehicleBasePane
                    = FXMLLoader.load(getClass().getResource("/resources/vehicle/vehiclesBasePane.fxml"));
            Main.getInstance().replaceTabContent(vehicleBasePane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openPartsTab() {
        try {
            if (partsBasePane == null) partsBasePane
                    = FXMLLoader.load(getClass().getResource("/resources/parts/PartModule.fxml"));
            Main.getInstance().replaceTabContent(partsBasePane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSRCTab() {
        try {
            if (specialistBasePane == null) specialistBasePane
                    = FXMLLoader.load(getClass().getResource("/spc/SPBookingManagement.fxml"));
            Main.getInstance().replaceTabContent(specialistBasePane);


        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSRCManagementTab() {
        try {
            if (spcManagementBasePane == null) spcManagementBasePane =
                    FXMLLoader.load(getClass().getResource("/spc/SPCManagement.fxml"));
            Main.getInstance().replaceTabContent(spcManagementBasePane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openUsersTab() {
        try {
            if (usersBasePane == null) usersBasePane
                    = FXMLLoader.load(getClass().getResource("/resources/user/usersBasePane.fxml"));
            Main.getInstance().replaceTabContent(usersBasePane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
