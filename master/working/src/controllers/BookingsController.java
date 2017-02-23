package controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class BookingsController {
    private BorderPane bookingsBasePane;

    public void initialize() {
        try {
            bookingsBasePane = FXMLLoader.load(getClass().getResource("/resources/booking/bookingsBasePane.fxml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Open screen for new booking
     */
    @FXML
    public void openNewBookingView() {

    }

    @FXML
    public void openMonthView() {

    }

    @FXML
    public void openWeekView() {

    }

    @FXML
    public void openListView() {
    }
}