package controllers.booking;

import domain.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.booking.BookingSystem;
import logic.customer.CustomerSystem;
import logic.parts.PartsSystem;
import logic.vehicle.VehicleSys;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import persistence.DatabaseRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.DayOfWeek.SUNDAY;

/**
 * @author Marcello De Bernardi
 */
public class DetailsPaneController {
    // logic and helpers
    private BookingController master;
    private CustomerSystem customerSystem;
    private BookingSystem bookingSystem;
    private VehicleSys vehicleSystem;
    private PartsSystem partsSystem;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;
    // controller state
    private DiagRepBooking selectedBooking;
    private Vehicle selectedVehicle;
    private PartOccurrence selectedPart;
    private List<PartOccurrence> detachedParts;
    // TextFields
    @FXML private TextField customerSearchBar;
    @FXML private TextField descriptionTextField;
    @FXML private TextField diagnosisStartTimeTextField;
    @FXML private TextField diagnosisEndTimeTextField;
    @FXML private TextField repairStartTimeTextField;
    @FXML private TextField repairEndTimeTextField;
    @FXML private TextField vehicleMileageTextField;
    @FXML private TextField newMileageTextField;
    // text
    @FXML private Text paneTitle;
    // ComboBoxes
    @FXML private ComboBox<Vehicle> vehicleComboBox;
    @FXML private ComboBox<Mechanic> mechanicComboBox;
    // Tables
    @FXML private TableView<PartOccurrence> partsTable;
    @FXML private TableColumn<PartOccurrence, String> nameColumn;
    @FXML private TableColumn<PartOccurrence, Double> costColumn;
    @FXML private TableColumn<PartOccurrence, Boolean> subcontractedColumn;
    // Buttons
    @FXML private Button addPartButton;
    @FXML private Button removePartButton;
    // DatePickers
    @FXML private DatePicker diagnosisDatePicker;
    @FXML private DatePicker repairDatePicker;
    // PopOvers
    private PopOver customerValidationPopOver;
    private PopOver vehicleValidationPopOver;
    private PopOver mechanicValidationPopOver;
    private PopOver mileageValidationPopOver;


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


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     INITIALIZATION                                                  //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML private void initialize() {
        master.setController(DetailsPaneController.class, this);

        initializeCustomerTextField(customerSystem.getAllCustomers());
        initializeVehicleComboBox();
        initializeMechanicComboBox();
        initializePartsTableView();

        populateMechanicComboBox(bookingSystem.getAllMechanics());

        vehicleComboBox.setPrefWidth(Double.MAX_VALUE);
        mechanicComboBox.setPrefWidth(Double.MAX_VALUE);
        diagnosisDatePicker.setPrefWidth(Double.MAX_VALUE);
        repairDatePicker.setPrefWidth(Double.MAX_VALUE);

        Callback<DatePicker, DateCell> dayCellFactory = datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now()) || item.getDayOfWeek().equals(SUNDAY)) {
                    this.setDisable(true);
                    this.setStyle(" -fx-background-color: #ff6666; ");
                }
            }
        };

        diagnosisDatePicker.setDayCellFactory(dayCellFactory);
        diagnosisDatePicker.setEditable(false);
        repairDatePicker.setDayCellFactory(dayCellFactory);
        repairDatePicker.setEditable(false);
    }

    // adds all customers to the customer search bar's autocomplete function
    private void initializeCustomerTextField(List<Customer> customers) {
        List<String> customerInfo = new ArrayList<>();

        customers.forEach(c ->
                customerInfo.add(c.getCustomerID() + ": " + c.getCustomerFirstname() + " " + c.getCustomerSurname()));

        AutoCompletionBinding<String> customerAutoCompletionBinding
                = TextFields.bindAutoCompletion(customerSearchBar, customerInfo);
        customerAutoCompletionBinding.setOnAutoCompleted(p -> this.selectCustomer());
    }

    private void initializeVehicleComboBox() {
        vehicleComboBox.setCellFactory(p -> new ListCell<Vehicle>() {
            public void updateItem(Vehicle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText("");
                else setText(item.getVehicleRegNumber() + " " + item.getManufacturer() + " " + item.getModel());
            }
        });
        vehicleComboBox.setButtonCell(new ListCell<Vehicle>() {
            public void updateItem(Vehicle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText("");
                else setText(item.getVehicleRegNumber() + " " + item.getManufacturer() + " " + item.getModel());
            }
        });
    }

    private void initializeMechanicComboBox() {
        mechanicComboBox.setCellFactory(p -> new ListCell<Mechanic>() {
            public void updateItem(Mechanic item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText("");
                else setText(item.getFirstName() + " " + item.getSurname());
            }
        });
        mechanicComboBox.setButtonCell(new ListCell<Mechanic>() {
            public void updateItem(Mechanic item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText("");
                else setText(item.getFirstName() + " " + item.getSurname());
            }
        });
    }

    private void initializePartsTableView() {
        DoubleBinding binding = partsTable.widthProperty().divide(3);

        nameColumn.prefWidthProperty().bind(binding);
        nameColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getPartAbstraction().getPartName())
        );

        costColumn.prefWidthProperty().bind(binding);
        costColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getPartAbstraction().getPartPrice())
        );

        subcontractedColumn.prefWidthProperty().bind(binding);
        subcontractedColumn.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getInstallationID() != -1)
        );

        partsTable.getColumns().setAll(nameColumn, costColumn, subcontractedColumn);
        partsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> selectedPart = newSelection);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     FXML EVENT LISTENERS                                            //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML private void selectCustomer() {
        if (!validateCustomerSelection()) return;

        populateVehicleComboBox(getCustomerFromSearchBar().getVehicles());
        selectedBooking = new DiagRepBooking();
    }

    @FXML private void selectVehicle() {
        selectedVehicle = vehicleComboBox.getSelectionModel().getSelectedItem();

        if (vehicleValidationPopOver != null && vehicleValidationPopOver.isShowing()) vehicleValidationPopOver.hide();
        vehicleMileageTextField.setText("" + selectedVehicle.getMileage());
        vehicleMileageTextField.setDisable(true);
    }

    @FXML private void selectMechanic() {
        if (mechanicValidationPopOver != null && mechanicValidationPopOver.isShowing())
            mechanicValidationPopOver.hide();
    }

    @FXML private void saveBooking() {
        // interrupt if invalid entries
        if (!(validateCustomerSelection() && validateVehicleSelection()
                && dateIsSelected(diagnosisDatePicker,
                "/resources/booking/validation/MissingDiagDatePopOver.fxml")
                && timeIsSelected(diagnosisStartTimeTextField, diagnosisEndTimeTextField,
                "/resources/booking/validation/MissingDiagTimePopOver.fxml")
                && timeIsValid(diagnosisStartTimeTextField, diagnosisEndTimeTextField,
                "/resources/booking/validation/InvalidDiagTimePopOver.fxml")
                && validateMechanicSelection())) return;

        // make sure selectedBooking exists
        if (selectedBooking.getVehicleRegNumber() == null) {
            selectedVehicle.getBookingList().add(selectedBooking);
        }

        // todo implement bill
        selectedBooking.setVehicleRegNumber(vehicleComboBox.getSelectionModel().getSelectedItem().getVehicleRegNumber());
        selectedBooking.setDescription(descriptionTextField.getText());
        selectedBooking.setBill(new Bill(0, false));
        selectedBooking.setMechanicID(mechanicComboBox.getSelectionModel().getSelectedItem().getMechanicID());
        selectedBooking.setDiagnosisStart(getDiagnosisStartTime());
        selectedBooking.setDiagnosisEnd(getDiagnosisEndTime());

        // repair dates
        try {
            selectedBooking.setRepairStart(getRepairStartTime());
            selectedBooking.setRepairEnd(getRepairEndTime());
        }
        catch (DateTimeParseException e) {
            selectedBooking.setRepairStart(null);
            selectedBooking.setRepairEnd(null);
        }

        // mileage, if set
        try {
            selectedVehicle.setMileage(getInteger(newMileageTextField));
        }
        catch (NumberFormatException e) {
            // do nothing
        }

        // commit
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
            clearFields();
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
            clearFields();
        }
    }

    @FXML private void completeBooking() {
        if (!(validateCustomerSelection() && validateVehicleSelection()
                && dateIsSelected(diagnosisDatePicker,
                "/resources/booking/validation/MissingDiagDatePopOver.fxml")
                && timeIsSelected(diagnosisStartTimeTextField, diagnosisEndTimeTextField,
                "/resources/booking/validation/MissingDiagTimePopOver.fxml")
                && timeIsValid(diagnosisStartTimeTextField, diagnosisEndTimeTextField,
                "/resources/booking/validation/InvalidDiagTimePopOver.fxml")
                && dateIsSelected(repairDatePicker,
                "/resources/booking/validation/MissingDiagDatePopOver.fxml")
                && timeIsSelected(repairStartTimeTextField, repairEndTimeTextField,
                "/resources/booking/validation/MissingDiagTimePopOver.fxml")
                && timeIsValid(repairStartTimeTextField, repairEndTimeTextField,
                "/resources/booking/validation/InvalidDiagTimePopOver.fxml")
                && validateMechanicSelection())) return;

        selectedBooking.setComplete(true);
        saveBooking();
    }

    @FXML private void clearFields() {
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
        newMileageTextField.setDisable(false);

        selectedBooking = new DiagRepBooking();
        setPaneTitleToAdd();
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
    //                                     INPUT VALIDATION                                                //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean validateCustomerSelection() {
        if (customerValidationPopOver == null) {
            try {
                customerValidationPopOver = new PopOver(FXMLLoader.load(getClass()
                        .getResource("/booking/validation/CustomerValidationPopOver.fxml")));
                customerValidationPopOver.setDetachable(false);
                customerValidationPopOver.setCornerRadius(0);
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        if (getCustomerFromSearchBar() != null) {
            if (customerValidationPopOver.isShowing()) customerValidationPopOver.hide();
            return true;
        }
        else {
            if (!customerValidationPopOver.isShowing()) customerValidationPopOver.show(customerSearchBar);
            return false;
        }
    }

    private boolean validateVehicleSelection() {
        if (vehicleValidationPopOver == null) {
            try {
                vehicleValidationPopOver = new PopOver(FXMLLoader.load(getClass()
                        .getResource("/booking/validation/MissingVehiclePopOver.fxml")));
                vehicleValidationPopOver.setDetachable(false);
                vehicleValidationPopOver.setCornerRadius(0);
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        if (vehicleComboBox.getSelectionModel().getSelectedItem() != null) {
            if (vehicleValidationPopOver.isShowing()) vehicleValidationPopOver.hide();
            return true;
        }
        else {
            if (!vehicleValidationPopOver.isShowing()) vehicleValidationPopOver.show(vehicleComboBox);
            return false;
        }
    }

    private boolean dateIsSelected(DatePicker picker, String source) {
        PopOver response;
        try {
            response = new PopOver(FXMLLoader.load(getClass().getResource(source)));
            response.setDetachable(false);
            response.setCornerRadius(0);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // check if missing
        if (picker.getValue() != null) {
            if (response.isShowing()) response.hide();
            return true;
        }
        else {
            if (!response.isShowing()) response.show(picker);
            return false;
        }
    }

    private boolean timeIsSelected(TextField start, TextField end, String source) {
        PopOver response;
        try {
            response = new PopOver(FXMLLoader.load(getClass().getResource(source)));
            response.setDetachable(false);
            response.setCornerRadius(0);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // check if missing
        if (start.getText().equals("''") || end.getText().equals("''")) {
            if (!response.isShowing()) response.show(end);
            return false;
        }
        else {
            if (response.isShowing()) response.hide();
            return true;
        }
    }

    private boolean timeIsValid(TextField start, TextField end, String source) {
        PopOver response;
        try {
            response = new PopOver(FXMLLoader.load(getClass().getResource(source)));
            response.setDetachable(false);
            response.setCornerRadius(0);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            LocalTime.parse(start.getText(), timeFormatter);
            LocalTime.parse(end.getText(), timeFormatter);
            if (response.isShowing()) response.hide();
            return true;
        }
        catch (DateTimeParseException e) {
            if (!response.isShowing()) response.show(end);
            return false;
        }
    }

    private boolean validateMechanicSelection() {
        if (mechanicValidationPopOver == null) {
            try {
                mechanicValidationPopOver = new PopOver(FXMLLoader.load(getClass()
                        .getResource("/booking/validation/MissingMechanicPopOver.fxml")));
                mechanicValidationPopOver.setDetachable(false);
                mechanicValidationPopOver.setCornerRadius(0);
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        if (mechanicComboBox.getSelectionModel().getSelectedItem() != null) {
            if (mechanicValidationPopOver.isShowing()) mechanicValidationPopOver.hide();
            return true;
        }
        else {
            if (!mechanicValidationPopOver.isShowing()) mechanicValidationPopOver.show(mechanicComboBox);
            return false;
        }
    }

    private boolean validateNewMileage() {
        if (mileageValidationPopOver == null) {
            try {
                mileageValidationPopOver = new PopOver(FXMLLoader.load(getClass()
                        .getResource("/resources/booking/validation/MileageValidationPopOver.fxml")));
                mileageValidationPopOver.setDetachable(false);
                mileageValidationPopOver.setCornerRadius(0);
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            int oldMileage = Integer.parseInt(vehicleMileageTextField.getText());
            int newMileage = Integer.parseInt(newMileageTextField.getText());

            if (newMileage < oldMileage) {
                if (!mileageValidationPopOver.isShowing()) mileageValidationPopOver.show(newMileageTextField);
                return false;
            }
            else {
                if (mileageValidationPopOver.isShowing()) mileageValidationPopOver.hide();
                return true;
            }
        }
        catch (NumberFormatException e) {
            if (!mileageValidationPopOver.isShowing()) mileageValidationPopOver.show(newMileageTextField);
            return false;
        }
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
        clearFields();
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
        vehicleComboBox.getSelectionModel().select(selectedVehicle);
        descriptionTextField.setText(booking.getDescription());

        diagnosisDatePicker.setValue(diagnosisStart.toLocalDate());
        diagnosisStartTimeTextField.setText(diagnosisStart.format(timeFormatter));
        diagnosisEndTimeTextField.setText(diagnosisEnd.format(timeFormatter));
        repairDatePicker.setValue(repairStart.toLocalDate());
        repairStartTimeTextField.setText(repairStart.format(timeFormatter));
        repairEndTimeTextField.setText(repairEnd.format(timeFormatter));

        populatePartsTable(booking.getRequiredPartsList());

        mechanicComboBox.getSelectionModel().select(mechanic);

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
        newMileageTextField.setDisable(booking.isComplete());
    }

    // generates rows for the table containing a booking's parts
    void populatePartsTable(List<PartOccurrence> parts) {
        ObservableList<PartOccurrence> partsObservable = FXCollections.observableArrayList(parts);
        partsTable.setItems(partsObservable);
        partsTable.refresh();
    }

    // adds customer's vehicle to the vehicle selection ComboBox
    private void populateVehicleComboBox(List<Vehicle> vehicles) {
        ObservableList<Vehicle> vehiclesObservable = FXCollections.observableArrayList(vehicles);
        vehicleComboBox.setItems(vehiclesObservable);
    }

    // adds all mechanics to the ComboBox for mechanic selection
    private void populateMechanicComboBox(List<Mechanic> mechanics) {
        ObservableList<Mechanic> mechanicsObservable = FXCollections.observableArrayList(mechanics);
        mechanicComboBox.setItems(mechanicsObservable);
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
    //                                     FIELD EXTRACTORS                                                //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // gets the name of the selected customer from the search bar
    private Customer getCustomerFromSearchBar() {
        String query = customerSearchBar.getText();
        return query.matches("[0-9]+.*") ?
                customerSystem.getACustomers(Integer.parseInt(customerSearchBar.getText().split(":")[0]))
                : null;
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

    // gets an integer value from the textfield
    private int getInteger(TextField textField) throws NumberFormatException {
        return Integer.parseInt(textField.getText());
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                   STRUCTURAL MODIFICATIONS                                          //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    void setPaneTitleToAdd() {
        paneTitle.setText("Add Booking");
    }

    void setPaneTitleToView() {
        paneTitle.setText("View Booking");
    }
}
