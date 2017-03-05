package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

/**
 * @author Dillon Vaghela
 *         <p>
 *         todo clean up and add comments
 */
public class MenuController {
    // todo move these into module controllers
    Stage addSpecialistBooking = new Stage();
    Stage deleteSpecialistBooking;


    public void openTodayTab() {
        // todo Main.getInstance().replaceTabContent(FXMLLoader.load(getClass().getResource("")));
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
        BookingView handler = BookingView.getInstance();
        Main.getInstance().replaceTabContent(handler.show());
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
            partsBasePane = FXMLLoader.load(getClass().getResource("/resources/PartModule.fxml"));
            partsBasePane.setVisible(true);
            Main.getInstance().replaceTabContent(partsBasePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSRCTab() {
        BorderPane specialistBasePane = new BorderPane();
        try {
            FlowPane centerPane = FXMLLoader.load(getClass().getResource("/resources/SRC/centerSRC.fxml"));
            FlowPane leftPane = FXMLLoader.load(getClass().getResource("/resources/SRC/leftSRC.fxml"));
            specialistBasePane.setVisible(true);
            specialistBasePane.setLeft(leftPane);
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


    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("This window is already open or another vehicle window is open");
        alert.showAndWait();
    }


    public void searchSRC() {
    }


    public void addSRCBooking() {
        try {
            if (addSpecialistBooking != null) {
                if (addSpecialistBooking.isShowing()) {
                    showAlert();
                    addSpecialistBooking.setAlwaysOnTop(true);
                    //editBStage.setFullScreen(true);
                    return;
                }
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SRC/AddSRCB.fxml"));
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
            if (deleteSpecialistBooking != null) {
                if (deleteSpecialistBooking.isShowing()) {
                    showAlert();
                    deleteSpecialistBooking.setAlwaysOnTop(true);
                    //editBStage.setFullScreen(true);
                    return;
                }
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SRC/DeleteSRB.fxml"));
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
    /*public void addCustomerListener() {
        try {
            if (addCustomerStage != null) {
                if (addCustomerStage.isShowing()) {
                    showAlert();
                    addCustomerStage.setAlwaysOnTop(true);
                    return;
                }
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/customer/AddCustomer.fxml"));
                Parent menu = fxmlLoader.load();
                addCustomerStage = new Stage();
                addCustomerStage.setTitle("Add Customer");
                addCustomerStage.setScene(new Scene(menu));
                addCustomerStage.show();
            }
        } catch (Exception e) {
            System.out.println("cant open");
        }
    }*/

    //action listener for opening 'Edit Customer' window
    /*public void editCustomerListener() {
        try {
            if (editCustomerStage != null) {
                if (editCustomerStage.isShowing()) {
                    showAlert();
                    editCustomerStage.setAlwaysOnTop(true);
                    return;
                }
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/customer/EditCustomer.fxml"));
                Parent menu = fxmlLoader.load();
                editCustomerStage = new Stage();
                editCustomerStage.setTitle("Edit Customer");
                editCustomerStage.setScene(new Scene(menu));
                editCustomerStage.show();
            }
        } catch (Exception e) {
            System.out.println("cant open");
        }
    }*/

    //action listener for opening 'Search Customer' window
    /*public void searchCustomerListener() {
        try {
            if (searchCustomerStage != null) {
                if (searchCustomerStage.isShowing()) {
                    showAlert();
                    searchCustomerStage.setAlwaysOnTop(true);
                    return;
                }
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/customer/SearchCustomer.fxml"));
                Parent menu = fxmlLoader.load();
                searchCustomerStage = new Stage();
                searchCustomerStage.setTitle("Search Customer");
                searchCustomerStage.setScene(new Scene(menu));
                searchCustomerStage.show();
            }
        } catch (Exception e) {
            System.out.println("cant open");
        }
    }*/
}
