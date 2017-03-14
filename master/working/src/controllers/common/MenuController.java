package controllers.common;

import controllers.booking.BookingController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import main.Main;

import java.io.IOException;

/**
 * @author Dillon Vaghela
 *         <p>
 *         todo clean up and add comments
 */
public class MenuController {
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
           // BorderPane centerPane = FXMLLoader.load(getClass().getResource("/resources/SRC/newCenterSRC.fxml"));
            BorderPane leftPane = FXMLLoader.load(getClass().getResource("/resources/SRC/newLeftPane.fxml"));
            specialistBasePane.setVisible(true);
            specialistBasePane.setLeft(leftPane);
           // specialistBasePane.setRight(centerPane);
            Main.getInstance().replaceTabContent(specialistBasePane);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSRCManagementTab() {
        BorderPane specialistBasePane = new BorderPane();
        try {
            BorderPane centerPane = FXMLLoader.load(getClass().getResource("/resources/SRC/newCenterSRC.fxml"));
           // BorderPane leftPane = FXMLLoader.load(getClass().getResource("/resources/SRC/newLeftPane.fxml"));
            specialistBasePane.setVisible(true);
           // specialistBasePane.setLeft(leftPane);
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
