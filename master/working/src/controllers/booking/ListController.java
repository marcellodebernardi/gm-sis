package controllers.booking;

import domain.Customer;
import domain.DiagRepBooking;
import domain.PartOccurrence;
import domain.Vehicle;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import logic.booking.BookingSystem;
import logic.criterion.Criterion;
import persistence.DatabaseRepository;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static logic.criterion.CriterionOperator.equalTo;

/**
 * @author Marcello De Bernardi
 */
public class ListController {
    private BookingController master;
    private BookingSystem bookingSystem;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;
    private Filter currentFilter;
    private ViewBy currentViewBy;
    private ZonedDateTime selectedDay;

    @FXML private TextField listSearchBar;
    @FXML private ComboBox filterComboBox; // todo
    @FXML private ComboBox viewByComboBox;
    @FXML private DatePicker listDatePicker;
    @FXML private TableView<DiagRepBooking> bookingTableView;
    @FXML private TableColumn<DiagRepBooking, String> customerColumn;
    @FXML private TableColumn<DiagRepBooking, String> vehicleRegColumn;
    @FXML private TableColumn<DiagRepBooking, String> manufacturerColumn;
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
        currentFilter = Filter.FUTURE;
        currentViewBy = ViewBy.WEEK;
        selectedDay = ZonedDateTime.now();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        INITIALIZATION                                               //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML private void initialize() {
        populateFilterComboBox();
        filterComboBox.getSelectionModel().select(0);
        populateViewByComboBox();
        viewByComboBox.getSelectionModel().select(0);
        initializeTable();
        refreshTable();

        master.setController(ListController.class, this);
    }

    /** Set the cell value factories for each column in the table */
    private void initializeTable() {
        DoubleBinding binding = bookingTableView.widthProperty().subtract(15).divide(9);
        customerColumn.prefWidthProperty().bind(binding);
        vehicleRegColumn.prefWidthProperty().bind(binding);
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
        vehicleRegColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getVehicleRegNumber())
        );
        manufacturerColumn.setCellValueFactory(p -> {
            Vehicle vehicle = DatabaseRepository.getInstance().getByCriteria(new Criterion<>
                    (Vehicle.class, "vehicleRegNumber", equalTo, p.getValue().getVehicleRegNumber())).get(0);
            return new ReadOnlyObjectWrapper<>(vehicle.getManufacturer());
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
                new ReadOnlyObjectWrapper<>(p.getValue().getBillAmount())
        );
        billSettledColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getBillSettled() ? "Yes" : "No"));

        bookingTableView.getColumns().setAll(customerColumn, vehicleRegColumn, manufacturerColumn,
                diagnosisDateColumn, diagnosisTimeColumn, repairDateColumn, repairTimeColumn,
                billAmountColumn, billSettledColumn);


        bookingTableView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (oldSelection != null) {
                        for (PartOccurrence p : ((DetailsController) master.getController(DetailsController.class)).getDetachedParts()) {
                            oldSelection.getRequiredPartsList().add(p);
                        }
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
        populateListView(bookingSystem.searchBookings(listSearchBar.getText()));
    }

    @FXML private void openCalendarPane() {
        try {
            master.setCenter(FXMLLoader.load(getClass().getResource("/resources/booking/CalendarPane.fxml")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void applyListFilter() {
        currentFilter = Filter.asEnum(filterComboBox.getSelectionModel().getSelectedItem().toString());
        refreshTable();
    }

    @FXML private void applyListViewType() {
        currentViewBy = ViewBy.asEnum(viewByComboBox.getSelectionModel().getSelectedItem().toString());
        refreshTable();
    }

    @FXML private void selectListPeriod() {
        selectedDay = ZonedDateTime.parse(listDatePicker.getValue().format(dateFormatter), dateFormatter);

    }

    void refreshTable() {
        switch (currentFilter) {
            case ALL:
                populateListView(bookingSystem.getAllBookings());
                break;
            case FUTURE:
                populateListView(bookingSystem.getFutureBookings());
                break;
            case PAST:
                populateListView(bookingSystem.getPastBookings());
                break;
            default:
                System.err.println("Unrecognized list filter applied.");
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     FIELD POPULATION                                                //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void populateListView(List<DiagRepBooking> bookings) {
        ObservableList<DiagRepBooking> bookingsObservable = FXCollections.observableArrayList(bookings);
        bookingTableView.setItems(bookingsObservable);
        bookingTableView.refresh();
    }

    private void populateFilterComboBox() {
        List<String> options = new ArrayList<>();
        options.add(Filter.ALL.toString());
        options.add(Filter.PAST.toString());
        options.add(Filter.FUTURE.toString());

        filterComboBox.setItems(FXCollections.observableArrayList(options));
    }

    private void populateViewByComboBox() {
        List<String> options = new ArrayList<>();
        options.add(ViewBy.DAY.toString());
        options.add(ViewBy.WEEK.toString());
        options.add(ViewBy.MONTH.toString());

        viewByComboBox.setItems(FXCollections.observableArrayList(options));
    }


    enum Filter {
        ALL, PAST, FUTURE;

        static Filter asEnum(String string) throws IllegalArgumentException {
            if (string.equalsIgnoreCase(ALL.toString())) return ALL;
            else if (string.equalsIgnoreCase(PAST.toString())) return PAST;
            else if (string.equalsIgnoreCase(FUTURE.toString())) return FUTURE;
            else throw new IllegalArgumentException();
        }

        @Override public String toString() {
            if (this == ALL) return "All";
            else if (this == PAST) return "Past";
            else return "Future";
        }
    }

    enum ViewBy {
        DAY, WEEK, MONTH;

        static ViewBy asEnum(String string) throws IllegalArgumentException {
            if (string.equalsIgnoreCase(DAY.toString())) return DAY;
            else if (string.equalsIgnoreCase(WEEK.toString())) return WEEK;
            else if (string.equalsIgnoreCase(MONTH.toString())) return MONTH;
            else throw new IllegalArgumentException();
        }

        @Override public String toString() {
            if (this == DAY) return "Day";
            else if (this == WEEK) return "Week";
            else return "Month";
        }
    }
}
