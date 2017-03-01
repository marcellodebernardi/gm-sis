package controllers.bookings;

import domain.Customer;
import domain.DiagRepBooking;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;
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

    private TableView<DiagRepBooking> bookingsTable;


    private BookingsHandler() throws IOException {
        bookSys = BookingSystem.getInstance();
        view = new BorderPane();
        leftPane = new BorderPane();
        centerPane = new BorderPane();
        view.setLeft(leftPane);
        view.setCenter(centerPane);
    }

    public static BookingsHandler getInstance() {
        try {
            if (instance == null) instance = new BookingsHandler();
            return instance;
        } catch (IOException e) {
            e.getMessage();
            return null;
        }
    }


    /**
     * Returns the scene graph for the booking screen. Creates it if it hasn't been
     * created yet.
     *
     * @return bookings screen scene graph
     */
    public BorderPane show() {
        openListView();
        openNewBookingView();
        return view;
    }

    /**
     * Modifies the scene graph so as to open the list view in the bookings screen.
     */
    void openListView() {
        try {
            centerPane = FXMLLoader.load(getClass().getResource("/booking/TablePane.fxml"));
            constructBookingTable(bookSys.getAllBookings());

            view.setCenter(centerPane);
            centerPane.setId("monthWeekList");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifies the scene graph so as to open the month view in the bookings screen.
     */
    void openMonthView() {
        try {
            BorderPane monthView = FXMLLoader.load(getClass().getResource("/booking/MonthPane.fxml"));
            view.setCenter(monthView);
            view.getCenter().setId("monthWeekList");
            view.setVisible(true);
            Main.getInstance().replaceTabContent(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifies the scene graph so as to open the week view in the bookings screen.
     */
    void openWeekView() {
        try {
            BorderPane weekView = FXMLLoader.load(getClass().getResource("/booking/WeekPane.fxml"));
            GridPane dayGrid = new GridPane();
            List<DiagRepBooking> dayBookings;

            for (int day = 0; day < 7; day++) {
                dayBookings = bookSys.getDayBookings(day);
                ColumnConstraints constraint = new ColumnConstraints();
                constraint.setPercentWidth(14);
                dayGrid.getColumnConstraints().add(day, constraint);

                for (int hour = 0; hour < 8; hour++) {
                    Button hourBlock = new Button();
                    hourBlock.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    GridPane.setConstraints(hourBlock, day, hour);
                    GridPane.setFillWidth(hourBlock, true);
                    GridPane.setFillHeight(hourBlock, true);
                    dayGrid.getChildren().addAll(hourBlock);
                }
            }
            for (int i = 0; i < 8; i++) {
                RowConstraints constraint = new RowConstraints();
                constraint.setPercentHeight(12.5);
                dayGrid.getRowConstraints().add(i, constraint);
            }

            weekView.setCenter(dayGrid);
            view.setCenter(weekView);
            view.getCenter().setId("monthWeekList");
            view.setVisible(true);
            Main.getInstance().replaceTabContent(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifies the scene graph to as to open the New Booking section in the booking screen.
     */
    void openNewBookingView() {
        try {
            leftPane = FXMLLoader.load(getClass().getResource("/booking/InfoPane.fxml"));
            view.setLeft(leftPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs, or updates, the list of bookings in the bookings screen using the given
     * list of bookings.
     *
     * @param bookings list of bookings to put in table
     */
    void constructBookingTable(List<DiagRepBooking> bookings) {
        if (bookingsTable == null) bookingsTable = new TableView<>();
        centerPane.setCenter(bookingsTable);

        ObservableList<DiagRepBooking> tableEntries = FXCollections.observableArrayList(bookings);
        bookingsTable.setItems(tableEntries);

        TableColumn<DiagRepBooking, Integer> bookingIDColumn = new TableColumn<>();
        TableColumn<DiagRepBooking, String> customerColumn = new TableColumn<>();
        TableColumn<DiagRepBooking, String> vehicleRegColumn = new TableColumn<>();
        TableColumn<DiagRepBooking, String> diagnosisDateColumn = new TableColumn<>();
        TableColumn<DiagRepBooking, String> repairDateColumn = new TableColumn<>();

        vehicleRegColumn.setCellValueFactory(new PropertyValueFactory<DiagRepBooking, String>("vehicleRegNumber"));
        vehicleRegColumn.setCellFactory(TextFieldTableCell.<DiagRepBooking>forTableColumn());

        diagnosisDateColumn.setCellValueFactory(new PropertyValueFactory<DiagRepBooking, String>("bookingID"));
        diagnosisDateColumn.setCellFactory(TextFieldTableCell.<DiagRepBooking>forTableColumn());

        repairDateColumn.setCellValueFactory(new PropertyValueFactory<DiagRepBooking, String>("bookingID"));
        repairDateColumn.setCellFactory(TextFieldTableCell.<DiagRepBooking>forTableColumn());


        bookingIDColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, Integer>,
                ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<DiagRepBooking, Integer> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getBookingID());
            }
        });

        customerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DiagRepBooking, String> p) {
                Customer customer = p.getValue().getCustomer();
                return customer == null ? new ReadOnlyObjectWrapper<>("") : new ReadOnlyObjectWrapper<>(customer.getCustomerSurname());
            }
        });

        vehicleRegColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DiagRepBooking, String> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getVehicleRegNumber());
            }
        });

        diagnosisDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DiagRepBooking, String> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getDiagnosisStart().toLocalDate().toString());
            }
        });

        repairDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DiagRepBooking, String> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getRepairStart().toLocalDate().toString());
            }
        });

        bookingsTable.getColumns().setAll(bookingIDColumn, customerColumn, vehicleRegColumn,
                diagnosisDateColumn, repairDateColumn);
        bookingsTable.refresh();
    }

    /** Constructs, or updates, the week view in the bookings screen using the given list
     * of bookings.
     */
    void constructWeekView() {

    }
}