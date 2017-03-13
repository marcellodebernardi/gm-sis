package controllers.booking;

import domain.Customer;
import domain.DiagRepBooking;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import logic.BookingSystem;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Marcello De Bernardi
 */
public class ListPaneController {
    private BookingController master;
    private BookingSystem bookingSystem;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;

    @FXML private TextField listSearchBar;
    @FXML private TableView<DiagRepBooking> bookingTableView;
    @FXML private TableColumn<DiagRepBooking, Integer> bookingIDColumn;
    @FXML private TableColumn<DiagRepBooking, String> customerColumn;
    @FXML private TableColumn<DiagRepBooking, String> vehicleRegColumn;
    @FXML private TableColumn<DiagRepBooking, String> diagnosisDateColumn;
    @FXML private TableColumn<DiagRepBooking, String> diagnosisTimeColumn;
    @FXML private TableColumn<DiagRepBooking, String> repairDateColumn;
    @FXML private TableColumn<DiagRepBooking, String> repairTimeColumn;
    @FXML private TableColumn<DiagRepBooking, Double> billAmountColumn;
    @FXML private TableColumn<DiagRepBooking, String> billSettledColumn;


    public ListPaneController() {
        master = BookingController.getInstance();
        bookingSystem = BookingSystem.getInstance();
        timeFormatter = DateTimeFormatter.ofPattern("hh:mm");
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @FXML private void initialize() {
        setBookingTableCellValueFactories();
        populateBookingListView(bookingSystem.getAllBookings());
        setColumnWidths();
    }


    /////////////////// EVENT HANDLERS //////////////////////
    @FXML private void searchBookings() {
        populateBookingListView(bookingSystem.searchBookings(listSearchBar.getText()));
    }

    @FXML private void openCalendarPane() {
        try {
            master.setCenter(FXMLLoader.load(getClass().getResource("/resources/booking/CalendarPane.fxml")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /////////////////// DATA MANIPULATIONS ////////////////////
    private void populateBookingListView(List<DiagRepBooking> bookings) {
        ObservableList<DiagRepBooking> bookingsObservable = FXCollections.observableArrayList(bookings);
        bookingTableView.setItems(bookingsObservable);
        bookingTableView.refresh();
    }

    /* Set the cell value factories for each column in the table */
    private void setBookingTableCellValueFactories() {
        bookingIDColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getBookingID())
        );
        customerColumn.setCellValueFactory(p -> {
            Customer customer = p.getValue().getCustomer();
            return customer == null ?
                    new ReadOnlyObjectWrapper<>("") :
                    new ReadOnlyObjectWrapper<>(customer.getCustomerSurname());
        });
        vehicleRegColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getVehicleRegNumber())
        );
        diagnosisDateColumn.setCellValueFactory(p -> {
            ZonedDateTime date = p.getValue().getDiagnosisStart();
            return new ReadOnlyObjectWrapper<>(date == null ? "" : date.toLocalDate().format(dateFormatter));
        });
        diagnosisTimeColumn.setCellValueFactory(p -> {
            ZonedDateTime diagnosisStart = p.getValue().getDiagnosisStart();
            ZonedDateTime diagnosisEnd = p.getValue().getDiagnosisEnd();
            return new ReadOnlyObjectWrapper<>(
                    diagnosisStart != null ?
                    diagnosisStart.format(timeFormatter) + " - " + diagnosisEnd.format(timeFormatter)
                    : "");
        });
        repairDateColumn.setCellValueFactory(p -> {
            ZonedDateTime date = p.getValue().getRepairStart();
            return new ReadOnlyObjectWrapper<>(date == null ? "" : date.toLocalDate().format(dateFormatter));
        });
        repairTimeColumn.setCellValueFactory(p -> {
            ZonedDateTime repairStart = p.getValue().getRepairStart();
            ZonedDateTime repairEnd = p.getValue().getRepairEnd();
            return new ReadOnlyObjectWrapper<>(
                    repairStart != null ?
                            repairStart.format(timeFormatter) + " - " + repairEnd.format(timeFormatter)
                            : "");
        });
        billAmountColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getBillAmount())
        );
        billSettledColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>( p.getValue().getBillSettled() ? "Yes" : "No"));

        bookingTableView.getColumns().setAll(bookingIDColumn, customerColumn, vehicleRegColumn,
                diagnosisDateColumn, diagnosisTimeColumn, repairDateColumn, repairTimeColumn,
                billAmountColumn, billSettledColumn);
    }


    ///////////////// STRUCTURAL MODIFICATIONS ////////////////////
    private void setColumnWidths() {
        DoubleBinding binding = bookingTableView.widthProperty().subtract(15).divide(9);

        bookingIDColumn.prefWidthProperty().bind(binding);
        customerColumn.prefWidthProperty().bind(binding);
        vehicleRegColumn.prefWidthProperty().bind(binding);
        diagnosisDateColumn.prefWidthProperty().bind(binding);
        diagnosisTimeColumn.prefWidthProperty().bind(binding);
        repairDateColumn.prefWidthProperty().bind(binding);
        repairTimeColumn.prefWidthProperty().bind(binding);
        billAmountColumn.prefWidthProperty().bind(binding);
        billSettledColumn.prefWidthProperty().bind(binding);
    }
}
