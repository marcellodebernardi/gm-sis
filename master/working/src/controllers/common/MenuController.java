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

    @FXML
    private Button UsersButton;
    @FXML
    private Button SRCButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (AuthenticationSystem.getInstance().getUserType().equals(UserType.ADMINISTRATOR)) {
            UsersButton.setDisable(false);
            SRCButton.setDisable(false);

        } else {
            UsersButton.setDisable(true);
            SRCButton.setDisable(true);
        }
    }
    public void openTodayTab() {
        try {
            BorderPane todayPane = FXMLLoader.load(getClass().getResource("/resources/today/TodayPane.fxml"));
            todayPane.setVisible(true);
            Main.getInstance().replaceTabContent(todayPane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openCustomersTab() {
        try {
            BorderPane customerBasePane = FXMLLoader.load(getClass().getResource("/resources/customer/CustomerView.fxml"));
            customerBasePane.setVisible(true);
            Main.getInstance().replaceTabContent(customerBasePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openBookingsTab() {
        BookingController.getInstance().show();
    }

    public void openVehiclesTab() {
        BorderPane VehicleBasePane = new BorderPane();
        try {
            VehicleBasePane = FXMLLoader.load(getClass().getResource("/resources/vehicle/vehiclesBasePane.fxml"));
            VehicleBasePane.setVisible(true);
            Main.getInstance().replaceTabContent(VehicleBasePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openPartsTab() {
        BorderPane partsBasePane;
        try {
            partsBasePane = FXMLLoader.load(getClass().getResource("/resources/parts/PartModule.fxml"));
            partsBasePane.setVisible(true);
            Main.getInstance().replaceTabContent(partsBasePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSRCTab() {
        BorderPane specialistBasePane = new BorderPane();
        try {
            BorderPane leftPane = FXMLLoader.load(getClass().getResource("/spc/SPBookingManagement.fxml"));
            specialistBasePane.setVisible(true);
            specialistBasePane.setCenter(leftPane);
            Main.getInstance().replaceTabContent(specialistBasePane);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSRCManagementTab() {
        BorderPane specialistBasePane = new BorderPane();
        try {
            BorderPane centerPane = FXMLLoader.load(getClass().getResource("/spc/SPCManagement.fxml"));
            specialistBasePane.setVisible(true);
            specialistBasePane.setCenter(centerPane);
            Main.getInstance().replaceTabContent(specialistBasePane);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openUsersTab() {
        BorderPane UserBasePane = new BorderPane();
        try {
            UserBasePane = FXMLLoader.load(getClass().getResource("/resources/user/usersBasePane.fxml"));
            UserBasePane.setVisible(true);
            Main.getInstance().replaceTabContent(UserBasePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
