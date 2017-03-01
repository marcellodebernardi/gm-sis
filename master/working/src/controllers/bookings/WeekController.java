package controllers.bookings;

import javafx.fxml.FXML;

/**
 * @author Marcello De Bernardi
 */
public class WeekController {
    private BookingsHandler handler = BookingsHandler.getInstance();

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
}
