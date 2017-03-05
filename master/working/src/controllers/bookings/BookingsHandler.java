package controllers.bookings;

import domain.DiagRepBooking;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import logic.BookingSystem;
import main.Main;

import java.io.IOException;
import java.util.List;

/**
 * @author Marcello De Bernardi
 */
public class BookingsHandler {
    private static BookingsHandler instance;
    private BookingSystem bookSys;

    // base of the bookings view
    private BorderPane view;
    // pane with add/edit booking fields
    private BorderPane leftPane;
    // pane with month/week/list view
    private BorderPane centerPane;

    // table for bookings
    private TableView<DiagRepBooking> bookingsTable;
    // week view for bookings
    private GridPane weekView;
    // list view for customers in info pane
    private ListView customerList;
    private ObservableList<String> customerData;


    /**
     * Modifies the scene graph so as to open the month view in the bookings screen.
     */
    void openMonthView() {
        try {
            centerPane = FXMLLoader.load(getClass().getResource("/booking/MonthPane.fxml"));
            view.setCenter(centerPane);
            centerPane.setId("monthWeekList");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifies the scene graph so as to open the week view in the bookings screen.
     */
    void openWeekView() {
        try {
            centerPane = FXMLLoader.load(getClass().getResource("/booking/WeekPane.fxml"));
            constructWeekView(0);
            view.setCenter(centerPane);
            centerPane.setId("monthWeekList");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Constructs, or updates, the week view in the bookings screen using the given list
     * of bookings.
     */
    void constructWeekView(int week) {
        if (weekView == null) weekView = new GridPane();
        centerPane.setCenter(bookingsTable);

        // populate grid pane
        List<DiagRepBooking> bookingsOfDay;
        for (int day = 0; day < 7; day++) {
            bookingsOfDay = bookSys.getDayBookings(0);

            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(14);
            weekView.getColumnConstraints().add(day, constraint);

            for (int hour = 0; hour < 8; hour++) {
                Button hourBlock = new Button();
                hourBlock.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setConstraints(hourBlock, day, hour);
                GridPane.setFillWidth(hourBlock, true);
                GridPane.setFillHeight(hourBlock, true);
                weekView.getChildren().addAll(hourBlock);
            }
        }
        for (int i = 0; i < 8; i++) {
            RowConstraints constraint = new RowConstraints();
            constraint.setPercentHeight(12.5);
            weekView.getRowConstraints().add(i, constraint);
        }

        view.setCenter(weekView);
        view.getCenter().setId("monthWeekList");
        view.setVisible(true);
        Main.getInstance().replaceTabContent(view);
    }

    /**
     * Constructs, or updates, the list of bookings in the bookings screen using the given
     * list of bookings.
     */
    void constructMonthView(List<DiagRepBooking> bookings) {

    }
}