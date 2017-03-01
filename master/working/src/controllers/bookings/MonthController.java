package controllers.bookings;

import javafx.fxml.FXML;

/**
 * @author Marcello De Bernardi
 */
public class MonthController {
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
