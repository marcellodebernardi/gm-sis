package controllers.booking;

import domain.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import logic.booking.BookingSystem;
import logic.customer.CustomerSystem;
import logic.vehicle.VehicleSys;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Marcello De Bernardi
 */
public class DetailsPaneController {
    private BookingController master;
    private CustomerSystem customerSystem;
    private BookingSystem bookingSystem;
    private VehicleSys vehicleSystem;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;
    private Callback<DatePicker, DateCell> dayCellFactory;

    private DiagRepBooking selectedBooking;

    // customer and vehicle ComboBoxes
    @FXML private TextField bookingIDTextField;
    @FXML private TextField customerSearchBar;
    @FXML private ComboBox<String> vehicleComboBox;
    // description
    @FXML private TextField descriptionTextField;
    // mechanic and parts
    @FXML private ComboBox<String> mechanicComboBox;
    @FXML private TableView<PartOccurrence> partsTable;
    // dates and times
    @FXML private DatePicker diagnosisDatePicker;
    @FXML private DatePicker repairDatePicker;
    @FXML private TextField diagnosisStartTimeTextField;
    @FXML private TextField diagnosisEndTimeTextField;
    @FXML private TextField repairStartTimeTextField;
    @FXML private TextField repairEndTimeTextField;


    public DetailsPaneController() {
        master = BookingController.getInstance();

        customerSystem = CustomerSystem.getInstance();
        bookingSystem = BookingSystem.getInstance();
        vehicleSystem = VehicleSys.getInstance();

        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        dateFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");

    }

    @FXML private void initialize() {
        bookingIDTextField.setDisable(true);

        populateCustomerTextField(customerSystem.getAllCustomers());
        populateMechanicComboBox(bookingSystem.getAllMechanics());

        vehicleComboBox.setPrefWidth(Double.MAX_VALUE);
        mechanicComboBox.setPrefWidth(Double.MAX_VALUE);
        diagnosisDatePicker.setPrefWidth(Double.MAX_VALUE);
        repairDatePicker.setPrefWidth(Double.MAX_VALUE);

        // todo change to method reference?
        dayCellFactory = datePicker -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item.isBefore(LocalDate.now())) {
                        this.setDisable(true);
                        this.setStyle(" -fx-background-color: #ff6666; ");
                    }
                }
            };

        diagnosisDatePicker.setDayCellFactory(dayCellFactory);
        repairDatePicker.setDayCellFactory(dayCellFactory);

        master.setController(DetailsPaneController.class, this);
    }


    @FXML private void selectCustomer() {
        populateVehicleComboBox(getCustomerFromSearchBar().getVehicles());
    }

    // todo make check for broken shit
    @FXML private void addBooking() {
        System.out.println(getDiagnosisStartTime() + " " + getDiagnosisEndTime());
        System.out.println(getRepairStartTime() + " " + getRepairEndTime());

        // todo implement bill and parts
        selectedBooking.setVehicleRegNumber(getVehicleRegFromComboBox());
        selectedBooking.setDescription(getDescriptionFromTextField());
        selectedBooking.setBill(new Bill(0, false));
        selectedBooking.setMechanicID(getMechanicIDFromComboBox());
        selectedBooking.setDiagnosisStart(getDiagnosisStartTime());
        selectedBooking.setDiagnosisEnd(getDiagnosisEndTime());
        selectedBooking.setRepairStart(getRepairStartTime());
        selectedBooking.setRepairEnd(getRepairEndTime());


        if (bookingSystem.commitBooking(selectedBooking)) {
            ((ListPaneController)master.getController(ListPaneController.class))
                    .refreshBookingTable();
            ((CalendarPaneController)master.getController(CalendarPaneController.class))
                    .addBookingAppointment(selectedBooking);
            clearDetails();
        }
    }

    @FXML private void deleteBooking() {
        if (!bookingIDTextField.getText().equals("") && confirmDelete()) {
            bookingSystem.deleteBookingByID(Integer.parseInt(bookingIDTextField.getText()));
            ((ListPaneController)master.getController(ListPaneController.class)).refreshBookingTable();
            ((CalendarPaneController)master.getController(CalendarPaneController.class)).refreshAGenda();
            clearDetails();
        }
    }

    @FXML private void clearDetails() {
        selectedBooking = null;

        bookingIDTextField.clear();
        customerSearchBar.clear();
        populateVehicleComboBox(Collections.emptyList());
        vehicleComboBox.getSelectionModel().select("");
        descriptionTextField.clear();
        diagnosisDatePicker.setValue(null);
        diagnosisStartTimeTextField.clear();
        diagnosisEndTimeTextField.clear();
        repairDatePicker.setValue(null);
        repairStartTimeTextField.clear();
        repairEndTimeTextField.clear();
        mechanicComboBox.getSelectionModel().clearSelection();
        populatePartsTable(Collections.emptyList());
    }


    /* HELPER: fills in the details of a booking in the details pane */
    void populateDetailFields(DiagRepBooking booking) {
        selectedBooking = booking;

        Customer customer = booking.getCustomer();
        Vehicle vehicle = vehicleSystem.searchAVehicle(booking.getVehicleRegNumber());
        ZonedDateTime diagnosisStart = booking.getDiagnosisStart();
        ZonedDateTime diagnosisEnd = booking.getDiagnosisEnd();
        ZonedDateTime repairStart = booking.getRepairStart();
        ZonedDateTime repairEnd = booking.getRepairEnd();
        Mechanic mechanic = bookingSystem.getMechanicByID(booking.getMechanicID());

        bookingIDTextField.setText(booking.getBookingID() + "");
        customerSearchBar.setText(customer.getCustomerID() + ": " + customer.getCustomerFirstname() + " "
                + customer.getCustomerSurname());
        vehicleComboBox.getSelectionModel().select(vehicle.getRegNumber() + ": " + vehicle.getModel());
        descriptionTextField.setText(booking.getDescription());

        diagnosisDatePicker.setValue(diagnosisStart.toLocalDate());
        diagnosisStartTimeTextField.setText(diagnosisStart.format(timeFormatter));
        diagnosisEndTimeTextField.setText(diagnosisEnd.format(timeFormatter));
        repairDatePicker.setValue(repairStart.toLocalDate());
        repairStartTimeTextField.setText(repairStart.format(timeFormatter));
        repairEndTimeTextField.setText(repairEnd.format(timeFormatter));

        populatePartsTable(booking.getRequiredPartsList());
        mechanicComboBox.getSelectionModel().select(mechanic.getMechanicID() + ": "
                + mechanic.getFirstName() + " " + mechanic.getSurname());

    }

    /* HELPER: adds all customers to the customer search bar's autocomplete function */
    private void populateCustomerTextField(List<Customer> customers) {
        List<String> customerInfo = new ArrayList<>();
        for (Customer c : customers) {
            customerInfo.add(c.getCustomerID() + ": " + c.getCustomerFirstname() + " " + c.getCustomerSurname());
        }
        TextFields.bindAutoCompletion(customerSearchBar, customerInfo);
    }

    /* HELPER: adds customer's vehicle to the vehicle selection ComboBox */
    private void populateVehicleComboBox(List<Vehicle> vehicles) {
        List<String> vehicleInfo = new ArrayList<>();
        for (Vehicle v : vehicles) {
            vehicleInfo.add(v.getRegNumber() + ": " + v.getManufacturer() + " " + v.getModel());
        }
        ObservableList<String> vehicleInfoObservable = FXCollections.observableArrayList(vehicleInfo);
        vehicleComboBox.setItems(vehicleInfoObservable);
    }

    /* HELPER: adds all mechanics to the ComboBox for mechanic selection */
    private void populateMechanicComboBox(List<Mechanic> mechanics) {
        List<String> mechanicInfo = new ArrayList<>();
        for (Mechanic m : mechanics) {
            mechanicInfo.add(m.getMechanicID() + ": " + m.getFirstName() + " " + m.getSurname());
        }
        ObservableList<String> mechanicInfoObservable = FXCollections.observableArrayList(mechanicInfo);
        mechanicComboBox.setItems(mechanicInfoObservable);
    }

    /* HELPER: generates rows for the table containing a booking's parts */
    private void populatePartsTable(List<PartOccurrence> parts) {
        ObservableList<PartOccurrence> partsObservable = FXCollections.observableArrayList(parts);

        partsTable.setItems(partsObservable);

        TableColumn<PartOccurrence, Integer> partOccurrenceID = new TableColumn<>();
        TableColumn<PartOccurrence, String> partAbstractionName = new TableColumn<>();

        partOccurrenceID.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getPartOccurrenceID()));

        partAbstractionName.setCellValueFactory(p -> {
            PartAbstraction stockItem = p.getValue().getPartAbstraction();
            return stockItem == null ?
                    new ReadOnlyObjectWrapper<>("") :
                    new ReadOnlyObjectWrapper<>(stockItem.getPartName());
        });

        partsTable.getColumns().setAll(partOccurrenceID, partAbstractionName);
        partsTable.refresh();
    }

    /* HELPER: generates confirmation alert for vehicle deletion */
    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Confirmation");
        alert.setHeaderText("Are you certain you want to delete this booking?");
        return alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .isPresent();
    }

    /* HELPER: gets the name of the selected customer from the search bar */
    private Customer getCustomerFromSearchBar() {
        return customerSystem.getACustomers(Integer.parseInt(customerSearchBar
                .getText()
                .split(":")[0]));
    }

    /* HELPER: gets the selected vehicle registration number from the ComboBox */
    private String getVehicleRegFromComboBox() {
        return vehicleComboBox
                .getSelectionModel()
                .getSelectedItem()
                .split(":")[0];
    }

    /* HELPER: gets the description from the appropriate textfield */
    private String getDescriptionFromTextField() {
        return descriptionTextField.getText();
    }

    // returns the mechanic selected in the mechanic ComboBox
    private int getMechanicIDFromComboBox() {
        return Integer.parseInt(mechanicComboBox
                .getSelectionModel()
                .getSelectedItem()
                .split(":")[0]);
    }

    // returns the diagnosis start time
    private ZonedDateTime getDiagnosisStartTime() {
        return ZonedDateTime.of(
                diagnosisDatePicker.getValue(),
                LocalTime.parse(diagnosisStartTimeTextField.getText(), timeFormatter),
                ZoneId.systemDefault());
    }

    // returns the diagnosis end time
    private ZonedDateTime getDiagnosisEndTime() {
        return ZonedDateTime.of(
                diagnosisDatePicker.getValue(),
                LocalTime.parse(diagnosisEndTimeTextField.getText(), timeFormatter),
                ZoneId.systemDefault());
    }

    // returns the repair start time
    private ZonedDateTime getRepairStartTime() {
        return ZonedDateTime.of(
                repairDatePicker.getValue(),
                LocalTime.parse(repairStartTimeTextField.getText(), timeFormatter),
                ZoneId.systemDefault());
    }

    // returns the repair end time
    private ZonedDateTime getRepairEndTime() {
        return ZonedDateTime.of(
                repairDatePicker.getValue(),
                LocalTime.parse(repairEndTimeTextField.getText(), timeFormatter),
                ZoneId.systemDefault());
    }

    private List<PartOccurrence> getPartsListFromList() {
        List<PartOccurrence> parts = new ArrayList<>();
        parts.addAll(partsTable.getItems());
        return parts;
    }
}
