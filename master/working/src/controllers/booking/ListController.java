package controllers.booking;

import domain.Customer;
import domain.DiagRepBooking;
import domain.Vehicle;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import logic.booking.BookingSystem;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static controllers.booking.ListController.ViewBy.*;

/**
 * @author Marcello De Bernardi
 */
public class ListController {
    private BookingController master;
    private BookingSystem bookingSystem;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;
    private ViewBy currentViewBy;
    private ZonedDateTime selectedDate;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    @FXML private TextField listSearchBar;
    @FXML private ComboBox viewByComboBox;
    @FXML private DatePicker listDatePicker;
    @FXML private TableView<DiagRepBooking> bookingTableView;
    @FXML private TableColumn<DiagRepBooking, String> customerColumn;
    @FXML private TableColumn<DiagRepBooking, String> vehicleColumn;
    @FXML private TableColumn<DiagRepBooking, String> diagnosisDateColumn;
    @FXML private TableColumn<DiagRepBooking, String> diagnosisTimeColumn;
    @FXML private TableColumn<DiagRepBooking, String> repairDateColumn;
    @FXML private TableColumn<DiagRepBooking, String> repairTimeColumn;
    @FXML private TableColumn<DiagRepBooking, Double> billAmountColumn;
    @FXML private TableColumn<DiagRepBooking, String> billSettledColumn;


    public ListController() {
        master = BookingController.getInstance();
        bookingSystem = BookingSystem.getInstance();
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        currentViewBy = WEEK;
        selectedDate = ZonedDateTime.now();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        INITIALIZATION                                               //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** Initializes the controller's scene graph state */
    @FXML private void initialize() {
        populateViewByComboBox();
        viewByComboBox.getSelectionModel().select(ALL);
        listDatePicker.setValue(LocalDate.now());
        selectListPeriod();
        initializeTable();
        refreshTable();
        applyListViewType();

        master.setController(ListController.class, this);
    }

    /** Set the cell value factories for each column in the table */
    @SuppressWarnings("Duplicates")
    private void initializeTable() {
        DoubleBinding binding = bookingTableView.widthProperty().divide(8.5);
        customerColumn.prefWidthProperty().bind(binding);
        diagnosisDateColumn.prefWidthProperty().bind(binding);
        diagnosisTimeColumn.prefWidthProperty().bind(binding);
        repairDateColumn.prefWidthProperty().bind(binding);
        repairTimeColumn.prefWidthProperty().bind(binding);
        billAmountColumn.prefWidthProperty().bind(binding);
        billSettledColumn.prefWidthProperty().bind(binding);


        customerColumn.setCellValueFactory(p -> {
            Customer customer = p.getValue().getCustomer();
            return customer == null ?
                    new ReadOnlyObjectWrapper<>("") :
                    new ReadOnlyObjectWrapper<>(customer.getCustomerFirstname() + " "
                            + customer.getCustomerSurname());
        });
        vehicleColumn.setCellValueFactory(p -> {
            Vehicle vehicle = p.getValue().getVehicle();
            return new ReadOnlyObjectWrapper<>(vehicle.getVehicleRegNumber() + ": "
                    + vehicle.getManufacturer() + " " + vehicle.getModel());
        });
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
                new ReadOnlyObjectWrapper<>((double) ((int) (p.getValue().getBillAmount() * 100)) / 100)
        );
        billSettledColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getBillSettled() ? "Yes" : "No"));

        bookingTableView.getColumns().setAll(customerColumn, vehicleColumn,
                diagnosisDateColumn, diagnosisTimeColumn, repairDateColumn, repairTimeColumn,
                billAmountColumn, billSettledColumn);


        bookingTableView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (oldSelection != null) {
                        // for (PartOccurrence p : ((DetailsController) master.getController(DetailsController.class)).getDetachedParts()) {
                        //    oldSelection.getRequiredPartsList().add(p);
                        // }
                    }
                    if (newSelection != null) {
                        ((DetailsController) master.getController(DetailsController.class))
                                .populate(newSelection);
                        ((DetailsController) master.getController(DetailsController.class))
                                .setPaneTitleToView();
                    }
                });
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        EVENT HANDLERS                                               //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML private void searchBookings() {
        populateListView(bookingSystem.searchBookings(listSearchBar.getText()).stream()
                .filter(b -> b.getDiagnosisEnd().isAfter(startTime) || b.getDiagnosisStart().isBefore(endTime)
                        || (b.getRepairEnd() != null && b.getRepairEnd().isAfter(startTime))
                        || (b.getRepairStart() != null && b.getRepairStart().isBefore(endTime)))
                .collect(Collectors.toList())
        );
    }

    @FXML private void openCalendarPane() {
        try {
            master.setCenter(FXMLLoader.load(getClass().getResource("/resources/booking/CalendarPane.fxml")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void openVehiclePane() {
        try {
            master.setCenter(FXMLLoader.load(getClass().getResource("/resources/booking/VehiclePane.fxml")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void applyListViewType() {
        currentViewBy = ViewBy.asEnum(viewByComboBox.getSelectionModel().getSelectedItem().toString());
        selectListPeriod();
        refreshTable();
    }

    @FXML private void selectListPeriod() {
        selectedDate = ZonedDateTime.of(listDatePicker.getValue(),
                LocalTime.now(),
                ZoneId.systemDefault());

        if (currentViewBy == DAY) {
            startTime = selectedDate.truncatedTo(ChronoUnit.DAYS);
            endTime = startTime.plusDays(1);
        }
        else if (currentViewBy == WEEK) {
            startTime = selectedDate
                    .minusDays(selectedDate.getDayOfWeek().getValue())
                    .truncatedTo(ChronoUnit.DAYS);
            endTime = startTime.plusWeeks(1);
        }
        else if (currentViewBy == MONTH) {
            startTime = selectedDate.minusDays(selectedDate.getDayOfMonth())
                    .truncatedTo(ChronoUnit.DAYS);
            endTime = startTime.plusDays(32);
            endTime = endTime.minusDays(endTime.getDayOfMonth());
        }
        else {
            startTime = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
            endTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.MAX_VALUE), ZoneId.systemDefault());
        }
        refreshTable();
    }

    void refreshTable() {
        populateListView(bookingSystem.getAllBookings().stream()
                .filter(b -> (b.getDiagnosisEnd().isAfter(startTime) && b.getDiagnosisEnd().isBefore(endTime))
                        || (b.getDiagnosisStart().isBefore(endTime) && b.getDiagnosisStart().isAfter(startTime))
                        || (b.getRepairEnd() != null && b.getRepairEnd().isAfter(startTime) && b.getRepairEnd().isBefore(endTime))
                        || (b.getRepairStart() != null && b.getRepairStart().isBefore(endTime) && b.getDiagnosisStart().isAfter(startTime)))
                .collect(Collectors.toList())
        );
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     FIELD POPULATION                                                //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void populateListView(List<DiagRepBooking> bookings) {
        ObservableList<DiagRepBooking> bookingsObservable = FXCollections.observableArrayList(bookings);
        bookingTableView.setItems(bookingsObservable);
        bookingTableView.refresh();
    }

    private void populateViewByComboBox() {
        List<String> options = new ArrayList<>();
        options.add(ALL.toString());
        options.add(DAY.toString());
        options.add(WEEK.toString());
        options.add(ViewBy.MONTH.toString());

        viewByComboBox.setItems(FXCollections.observableArrayList(options));
    }

    /** Enumerator for the temporal mode in which the booking list is operated */
    enum ViewBy {
        ALL, DAY, WEEK, MONTH;

        static ViewBy asEnum(String string) throws IllegalArgumentException {
            if (string.equalsIgnoreCase(DAY.toString())) return DAY;
            else if (string.equalsIgnoreCase(WEEK.toString())) return WEEK;
            else if (string.equalsIgnoreCase(MONTH.toString())) return MONTH;
            else if (string.equalsIgnoreCase(ALL.toString())) return ALL;
            else throw new IllegalArgumentException();
        }

        @Override public String toString() {
            if (this == DAY) return "Day";
            else if (this == WEEK) return "Week";
            else if (this == MONTH) return "Month";
            else return "All";
        }
    }
}
