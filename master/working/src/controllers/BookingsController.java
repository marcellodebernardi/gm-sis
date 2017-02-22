package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class BookingsController {
    public void addBooking() {

    }

    public void editSBooking() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/booking/EditBooking.fxml"));
            Parent menu = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Edit Booking");
            stage.setScene(new Scene(menu));
            stage.show();

        } catch (Exception e) {
            System.out.println("cant open a");
            System.out.println(e);
        }
    }

    public void deleteBooking() {

    }

    public void editBooking() {

    }
}