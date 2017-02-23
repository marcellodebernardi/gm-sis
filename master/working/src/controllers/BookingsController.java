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
    private BorderPane basePaneInstance;


    /**
     * Open screen for new booking
     */
    @FXML
    public void openNewBookingView() {
        loadBasePaneInstance();
    }

    @FXML
    public void openMonthView() {
        loadBasePaneInstance();
    }

    @FXML
    public void openWeekView() {
        loadBasePaneInstance();
    }

    @FXML
    public void openListView() {
        loadBasePaneInstance();
    }

    private void loadBasePaneInstance() {
        if (basePaneInstance == null) {
            try {
                basePaneInstance = FXMLLoader.load(getClass().getResource("/resources/booking/bookingsBasePane.fxml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}