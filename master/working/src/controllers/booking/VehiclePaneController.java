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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import logic.booking.BookingSystem;
import logic.vehicle.VehicleSys;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Marcello De Bernardi
 */
public class VehiclePaneController {
    private BookingController master;
    private BookingSystem bookingSystem;
    private VehicleSys vehicleSystem;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;

    @FXML private TextField vehicleSearchBar;
    @FXML private TableView<DiagRepBooking> bookingTableView;
    @FXML private TableColumn<DiagRepBooking, String> diagnosisDateColumn;
    @FXML private TableColumn<DiagRepBooking, String> diagnosisTimeColumn;
    @FXML private TableColumn<DiagRepBooking, String> repairDateColumn;
    @FXML private TableColumn<DiagRepBooking, String> repairTimeColumn;
    @FXML private TableColumn<DiagRepBooking, Double> billAmountColumn;
    @FXML private TableColumn<DiagRepBooking, String> billSettledColumn;
    @FXML private TableView<Vehicle> vehicleTableView;
    @FXML private TableColumn<Vehicle, String> vehicleColumn;
    @FXML private TableColumn<Vehicle, String> customerColumn;
    @FXML private TableColumn<Vehicle, String> warrantyColumn;


    public VehiclePaneController() {
        master = BookingController.getInstance();
        bookingSystem = BookingSystem.getInstance();
        vehicleSystem = VehicleSys.getInstance();
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        INITIALIZATION                                               //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML private void initialize() {
        initializeVehicleTable();
        initializeBookingTable();
        populateVehicleTable(vehicleSystem.getVehiclesList());

        master.setController(VehiclePaneController.class, this);
    }

    /** Set the cell value factories and selection listener for the vehicle table */
    private void initializeVehicleTable() {
        DoubleBinding binding = vehicleTableView.widthProperty().subtract(5).divide(3);
        vehicleColumn.prefWidthProperty().bind(binding);
        customerColumn.prefWidthProperty().bind(binding);
        warrantyColumn.prefWidthProperty().bind(binding);

        vehicleColumn.setCellValueFactory(p -> {
            Vehicle v = p.getValue();
            return new ReadOnlyObjectWrapper<>(v.getVehicleRegNumber() + ": " + v.getManufacturer()
                    + " " + v.getModel());
        });
        customerColumn.setCellValueFactory(p -> {
            Customer c = p.getValue().getCustomer();
            return new ReadOnlyObjectWrapper<>(c.getCustomerFirstname() + " " + c.getCustomerSurname());
        });
        warrantyColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(
                        p.getValue().isCoveredByWarranty() ?
                                "Covered - " + p.getValue().getWarrantyName()
                                : ""
                ));

        vehicleTableView.getColumns().setAll(vehicleColumn, customerColumn, warrantyColumn);


        vehicleTableView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    ((DetailsController) master.getController(DetailsController.class)).clear();
                    if (newSelection != null) {
                        populateBookingTable(newSelection.getBookingList());
                    }
                });
    }

    /** Set the cell value factories for each column in the booking table */
    private void initializeBookingTable() {
        DoubleBinding binding = bookingTableView.widthProperty().subtract(5).divide(6);
        diagnosisDateColumn.prefWidthProperty().bind(binding);
        diagnosisTimeColumn.prefWidthProperty().bind(binding);
        repairDateColumn.prefWidthProperty().bind(binding);
        repairTimeColumn.prefWidthProperty().bind(binding);
        billAmountColumn.prefWidthProperty().bind(binding);
        billSettledColumn.prefWidthProperty().bind(binding);

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

        bookingTableView.getColumns().setAll(diagnosisDateColumn, diagnosisTimeColumn, repairDateColumn,
                repairTimeColumn, billAmountColumn, billSettledColumn);


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
    @FXML private void searchVehicles() {
        populateVehicleTable(vehicleSystem.smartSearchVehicle(vehicleSearchBar.getText()));
    }

    /** Switches the the calendar view of bookings */
    @FXML private void openCalendarPane() {
        try {
            master.setCenter(FXMLLoader.load(getClass().getResource("/resources/booking/CalendarPane.fxml")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Switches to the table view of bookings */
    @FXML private void openListPane() {
        try {
            master.setCenter(FXMLLoader.load(getClass().getResource("/resources/booking/ListPane.fxml")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     FIELD POPULATION                                                //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void populateVehicleTable(List<Vehicle> vehicles) {
        vehicleTableView.getItems().clear();
        vehicleTableView.setItems(FXCollections.observableArrayList(vehicles));
    }

    void populateBookingTable(List<DiagRepBooking> bookings) {
        ObservableList<DiagRepBooking> bookingsObservable = FXCollections.observableArrayList(bookings);
        bookingTableView.setItems(bookingsObservable);
        bookingTableView.refresh();
    }
}
