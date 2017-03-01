package controllers.bookings;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import logic.BookingSystem;

/**
 * @author Marcello De Bernardi
 */
public class TableController {
    // handler and booking system
    private BookingsHandler handler = BookingsHandler.getInstance();
    private BookingSystem bookSys = BookingSystem.getInstance();

    // javafx nodes
    @FXML
    private TextField bookingSearchBar;


    @FXML
    public void openListView() {
        handler.openListView();
    }

    @FXML
    public void openWeekView() {
        handler.openWeekView();
    }

    @FXML
    public void openMonthView() {
        handler.openMonthView();
    }

    @FXML
    public void searchBookings() {
        handler.constructBookingTable(bookSys.searchBookings(bookingSearchBar.getText()));
    }
}
