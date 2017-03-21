package controllers.booking;

import domain.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.Callback;
import logic.booking.BookingSystem;
import logic.customer.CustomerSystem;
import logic.parts.PartsSystem;
import logic.vehicle.VehicleSys;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;
import persistence.DatabaseRepository;

import java.io.IOException;
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
    private PartsSystem partsSystem;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;

    private DiagRepBooking selectedBooking;
    private Vehicle selectedVehicle;
    private PartOccurrence selectedPart;
    private List<PartOccurrence> detachedParts;

    // customer and vehicle ComboBoxes
    @FXML private TextField customerSearchBar;
    @FXML private ComboBox<String> vehicleComboBox;
    // description
    @FXML private TextField descriptionTextField;
    // mechanic and parts
    @FXML private ComboBox<String> mechanicComboBox;
    @FXML private TableView<PartOccurrence> partsTable;
    @FXML private TableColumn<PartOccurrence, String> nameColumn;
    @FXML private TableColumn<PartOccurrence, Double> costColumn;
    @FXML private TableColumn<PartOccurrence, Boolean> subcontractedColumn;
    @FXML private TextField vehicleMileageTextField;
    @FXML private TextField newVehicleMileageTextField;
    @FXML private Button addPartButton;
    @FXML private Button removePartButton;
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
        partsSystem = PartsSystem.getInstance(DatabaseRepository.getInstance());

        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        dateFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");

        detachedParts = new ArrayList<>();

    }

    @FXML private void initialize() {
        master.setController(DetailsPaneController.class, this);

        populateCustomerTextField(customerSystem.getAllCustomers());
        populateMechanicComboBox(bookingSystem.getAllMechanics());

        vehicleComboBox.setPrefWidth(Double.MAX_VALUE);
        mechanicComboBox.setPrefWidth(Double.MAX_VALUE);
        diagnosisDatePicker.setPrefWidth(Double.MAX_VALUE);
        repairDatePicker.setPrefWidth(Double.MAX_VALUE);

        Callback<DatePicker, DateCell> dayCellFactory = datePicker -> new DateCell() {
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

        setPartsTableCellValueFactories();
        setColumnWidths();
        setPartsSelectionListener();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     EVENT LISTENERS                                                 //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML private void selectCustomer() {
        populateVehicleComboBox(getCustomerFromSearchBar().getVehicles());
        selectedBooking = new DiagRepBooking();
    }

    @FXML private void selectVehicle() {
        selectedVehicle = vehicleSystem.searchAVehicle(vehicleComboBox
                .getSelectionModel()
                .getSelectedItem()
                .split(":")[0]);
        vehicleMileageTextField.setText("" + selectedVehicle.getMileage());
        vehicleMileageTextField.setDisable(true);
    }

    @FXML private void saveBooking() {
        if (selectedBooking == null) {
            selectedBooking = new DiagRepBooking();
            selectedVehicle.getBookingList().add(selectedBooking);
        }

        int vehicleMileage = 0;
        try {
            vehicleMileage = Integer.parseInt(vehicleMileageTextField.getText());
        }
        catch (NumberFormatException e) {
            // do nothing, keep 0 as mileage
        }
        selectedVehicle.setMileage(vehicleMileage);

        // todo implement bill
        selectedBooking.setVehicleRegNumber(getVehicleRegFromComboBox());
        selectedBooking.setDescription(getDescriptionFromTextField());
        selectedBooking.setBill(new Bill(0, false));
        selectedBooking.setMechanicID(getMechanicIDFromComboBox());
        selectedBooking.setDiagnosisStart(getDiagnosisStartTime());
        selectedBooking.setDiagnosisEnd(getDiagnosisEndTime());
        selectedBooking.setRepairStart(getRepairStartTime());
        selectedBooking.setRepairEnd(getRepairEndTime());

        boolean committed = DatabaseRepository.getInstance().commitItem(selectedVehicle);
        CalendarPaneController calendarController
                = (CalendarPaneController) master.getController(CalendarPaneController.class);
        ListPaneController listController
                = (ListPaneController) master.getController(ListPaneController.class);

        if (committed) {
            for (PartOccurrence p : detachedParts) {
                DatabaseRepository.getInstance().commitItem(p);
            }

            if (calendarController != null)
                ((CalendarPaneController) master.getController(CalendarPaneController.class))
                        .addBookingAppointment(selectedBooking);
            else if (listController != null)
                ((ListPaneController) master.getController(ListPaneController.class))
                        .refreshBookingTable();

            clearDetails();
        }
    }

    @FXML private void deleteBooking() {

        if (selectedBooking.getBookingID() != -1 && confirmDelete()) {
            bookingSystem.deleteBookingByID(selectedBooking.getBookingID());
            ((ListPaneController) master
                    .getController(ListPaneController.class))
                    .refreshBookingTable();
            ((CalendarPaneController) master
                    .getController(CalendarPaneController.class))
                    .refreshAGenda(bookingSystem.getAllBookings());
            clearDetails();
        }
    }

    @FXML private void clearDetails() {
        selectedBooking = null;
        selectedVehicle = null;
        selectedPart = null;
        detachedParts = new ArrayList<>();

        customerSearchBar.clear();
        populateVehicleComboBox(Collections.emptyList());
        vehicleComboBox.getSelectionModel().clearSelection();
        descriptionTextField.clear();
        diagnosisDatePicker.setValue(null);
        diagnosisStartTimeTextField.clear();
        diagnosisEndTimeTextField.clear();
        repairDatePicker.setValue(null);
        repairStartTimeTextField.clear();
        repairEndTimeTextField.clear();
        mechanicComboBox.getSelectionModel().clearSelection();
        populatePartsTable(Collections.emptyList());
        vehicleMileageTextField.clear();

        customerSearchBar.setDisable(false);
        vehicleComboBox.setDisable(false);
        descriptionTextField.setDisable(false);
        diagnosisDatePicker.setDisable(false);
        diagnosisStartTimeTextField.setDisable(false);
        diagnosisEndTimeTextField.setDisable(false);
        repairDatePicker.setDisable(false);
        repairStartTimeTextField.setDisable(false);
        repairEndTimeTextField.setDisable(false);
        partsTable.setDisable(false);
        mechanicComboBox.setDisable(false);
        vehicleMileageTextField.setDisable(false);

        selectedBooking = new DiagRepBooking();
    }

    @FXML private void completeBooking() {
        selectedBooking.setComplete(true);
        selectedVehicle.setMileage(Integer.parseInt(newVehicleMileageTextField.getText()));
        bookingSystem.commitBooking(selectedBooking);
        populateDetailFields(selectedBooking);
    }

    @FXML private void addPart() {
        try {
            PopOver partMenu = new PopOver(FXMLLoader.load(getClass().getResource("/resources/booking/PartsPopOver.fxml")));
            partMenu.setDetachable(false);
            partMenu.setArrowIndent(100);
            partMenu.setCornerRadius(0);
            partMenu.show(partsTable);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void removeSelectedPart() {
        selectedBooking.removeRequiredPart(selectedPart);

        selectedPart.unsetBooking();
        detachedParts.add(selectedPart);

        populatePartsTable(selectedBooking.getRequiredPartsList());
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                INTER-CONTROLLER COMMUNICATION                                       //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    DiagRepBooking getSelectedBooking() {
        return selectedBooking;
    }

    List<PartOccurrence> getDetachedParts() {
        return detachedParts;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                   FIELD POPULATION                                                  //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // fills in the details of a booking in the details pane */
    void populateDetailFields(DiagRepBooking booking) {
        clearDetails();
        selectedBooking = booking;
        selectedVehicle = vehicleSystem.searchAVehicle(booking.getVehicleRegNumber());

        Customer customer = booking.getCustomer();
        ZonedDateTime diagnosisStart = booking.getDiagnosisStart();
        ZonedDateTime diagnosisEnd = booking.getDiagnosisEnd();
        ZonedDateTime repairStart = booking.getRepairStart();
        ZonedDateTime repairEnd = booking.getRepairEnd();
        Mechanic mechanic = bookingSystem.getMechanicByID(booking.getMechanicID());

        customerSearchBar.setText(customer.getCustomerID() + ": " + customer.getCustomerFirstname() + " "
                + customer.getCustomerSurname());
        vehicleComboBox.getSelectionModel().select(selectedVehicle.getVehicleRegNumber() + ": " + selectedVehicle.getModel());
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

        vehicleMileageTextField.setText("" + selectedVehicle.getMileage());

        customerSearchBar.setDisable(booking.isComplete());
        vehicleComboBox.setDisable(booking.isComplete());
        descriptionTextField.setDisable(booking.isComplete());
        diagnosisDatePicker.setDisable(booking.isComplete());
        diagnosisStartTimeTextField.setDisable(booking.isComplete());
        diagnosisEndTimeTextField.setDisable(booking.isComplete());
        repairDatePicker.setDisable(booking.isComplete());
        repairStartTimeTextField.setDisable(booking.isComplete());
        repairEndTimeTextField.setDisable(booking.isComplete());
        partsTable.setDisable(booking.isComplete());
        mechanicComboBox.setDisable(booking.isComplete());
        newVehicleMileageTextField.setDisable(booking.isComplete());
    }

    // generates rows for the table containing a booking's parts
    void populatePartsTable(List<PartOccurrence> parts) {
        ObservableList<PartOccurrence> partsObservable = FXCollections.observableArrayList(parts);
        partsTable.setItems(partsObservable);
        partsTable.refresh();
    }

    // adds all customers to the customer search bar's autocomplete function
    private void populateCustomerTextField(List<Customer> customers) {
        List<String> customerInfo = new ArrayList<>();
        for (Customer c : customers) {
            customerInfo.add(c.getCustomerID() + ": " + c.getCustomerFirstname() + " " + c.getCustomerSurname());
        }
        TextFields.bindAutoCompletion(customerSearchBar, customerInfo);
    }

    // adds customer's vehicle to the vehicle selection ComboBox
    private void populateVehicleComboBox(List<Vehicle> vehicles) {
        List<String> vehicleInfo = new ArrayList<>();
        for (Vehicle v : vehicles) {
            vehicleInfo.add(v.getVehicleRegNumber() + ": " + v.getManufacturer() + " " + v.getModel());
        }
        ObservableList<String> vehicleInfoObservable = FXCollections.observableArrayList(vehicleInfo);
        vehicleComboBox.setItems(vehicleInfoObservable);
    }

    // adds all mechanics to the ComboBox for mechanic selection
    private void populateMechanicComboBox(List<Mechanic> mechanics) {
        List<String> mechanicInfo = new ArrayList<>();
        for (Mechanic m : mechanics) {
            mechanicInfo.add(m.getMechanicID() + ": " + m.getFirstName() + " " + m.getSurname());
        }
        ObservableList<String> mechanicInfoObservable = FXCollections.observableArrayList(mechanicInfo);
        mechanicComboBox.setItems(mechanicInfoObservable);
    }

    // generates confirmation alert for vehicle deletion
    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Confirmation");
        alert.setHeaderText("Are you certain you want to delete this booking?");
        return alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .isPresent();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     TABLE BUILDERS                                                  //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setPartsTableCellValueFactories() {
        nameColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getPartAbstraction().getPartName())
        );
        costColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getPartAbstraction().getPartPrice())
        );
        subcontractedColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getInstallationID() != -1)
        );

        partsTable.getColumns().setAll(nameColumn, costColumn, subcontractedColumn);
    }

    private void setColumnWidths() {
        DoubleBinding binding = partsTable.widthProperty().divide(3);

        nameColumn.prefWidthProperty().bind(binding);
        costColumn.prefWidthProperty().bind(binding);
        subcontractedColumn.prefWidthProperty().bind(binding);
    }

    // sets the selection listeners for the parts table
    private void setPartsSelectionListener() {
        partsTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) ->
                        selectedPart = newSelection
                );
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     FIELD EXTRACTORS                                                //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // gets the name of the selected customer from the search bar
    private Customer getCustomerFromSearchBar() {
        return customerSystem.getACustomers(Integer.parseInt(customerSearchBar
                .getText()
                .split(":")[0]));
    }

    // gets the selected vehicle registration number from the ComboBox
    private String getVehicleRegFromComboBox() {
        return vehicleComboBox
                .getSelectionModel()
                .getSelectedItem()
                .split(":")[0];
    }

    // gets the description from the appropriate textfield
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

    // gets the list of parts from the list
    private List<PartOccurrence> getPartsListFromList() {
        List<PartOccurrence> parts = new ArrayList<>();
        parts.addAll(partsTable.getItems());
        return parts;
    }
}
