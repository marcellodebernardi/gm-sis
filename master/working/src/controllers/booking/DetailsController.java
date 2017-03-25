package controllers.booking;

import domain.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.booking.BookingSystem;
import logic.booking.UnavailableDateException;
import logic.customer.CustomerSystem;
import logic.parts.PartsSystem;
import logic.vehicle.VehicleSys;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import persistence.DatabaseRepository;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.SUNDAY;
import static logic.booking.UnavailableDateException.Appointment.DIAGNOSIS;
import static logic.booking.UnavailableDateException.Cause.HOLIDAY;

/**
 * @author Marcello De Bernardi
 */
public class DetailsController {
    // logic and helpers
    private BookingController master;
    private CustomerSystem customerSystem;
    private BookingSystem bookingSystem;
    private VehicleSys vehicleSystem;
    private PartsSystem partsSystem;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;
    // controller state
    private boolean initialized;
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
    @FXML private TextField mileageTextField;
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
    private PopOver invalidTimePopOver;
    private PopOver invertedTimePopOver;


    public DetailsController() {
        master = BookingController.getInstance();

        customerSystem = CustomerSystem.getInstance();
        bookingSystem = BookingSystem.getInstance();
        vehicleSystem = VehicleSys.getInstance();
        partsSystem = PartsSystem.getInstance(DatabaseRepository.getInstance());

        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        dateFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");

        selectedBooking = new DiagRepBooking();
        detachedParts = new ArrayList<>();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     INITIALIZATION                                                  //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** Calls the initialization helpers and sets the controller state to initialized */
    @FXML private void initialize() {
        master.setController(DetailsController.class, this);

        initializeCustomerTextField();
        initializeVehicleComboBox();
        initializeMechanicComboBox();
        initializeDatePickers();
        initializePartsTableView();

        mileageTextField.setDisable(true);
        initialized = true;
    }

    /** Populates customer search bar autocompletion function with all customers */
    private void initializeCustomerTextField() {
        if (initialized) return;

        List<String> customerInfo = customerSystem.getAllCustomers()
                .stream()
                .map(c -> c.getCustomerID() + ": " + c.getCustomerFirstname() + " " + c.getCustomerSurname())
                .collect(Collectors.toList());

        AutoCompletionBinding<String> customerAutoCompletionBinding
                = TextFields.bindAutoCompletion(customerSearchBar, customerInfo);
        customerAutoCompletionBinding.setOnAutoCompleted(p -> this.pickCustomer());
    }

    /** Sets the cell factory and button cell for the mechanic ComboBox */
    private void initializeVehicleComboBox() {
        if (initialized) return;

        vehicleComboBox.setPrefWidth(Double.MAX_VALUE);

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

    /** Sets the cell factory and button cell for the mechanic ComboBox, and populates it */
    @SuppressWarnings("Duplicates")
    private void initializeMechanicComboBox() {
        if (initialized) return;

        mechanicComboBox.setPrefWidth(Double.MAX_VALUE);

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

        mechanicComboBox.setItems(FXCollections.observableArrayList(bookingSystem.getAllMechanics()));
    }

    /** Sets sizing and dayCellFactories of the DatePickers */
    private void initializeDatePickers() {
        if (initialized) return;

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
        repairDatePicker.setDayCellFactory(dayCellFactory);
        diagnosisDatePicker.setEditable(false);
        repairDatePicker.setEditable(false);
    }

    /** Sets the column width and cell value factories for the parts table */
    private void initializePartsTableView() {
        if (initialized) return;

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
    @FXML private void pickCustomer() {
        Customer c;
        if (validateCustomerSelection() && (c = extractCustomer()) != null) populateVehicle(c.getVehicles());
    }

    @FXML private void pickVehicle() {
        if ((selectedVehicle = vehicleComboBox.getSelectionModel().getSelectedItem()) != null)
            mileageTextField.setText("" + selectedVehicle.getMileage());
        else return;

        if (vehicleValidationPopOver != null && vehicleValidationPopOver.isShowing())
            vehicleValidationPopOver.hide();
    }

    @FXML private void pickMechanic() {
        if (mechanicValidationPopOver != null && mechanicValidationPopOver.isShowing())
            mechanicValidationPopOver.hide();
    }

    @FXML private boolean save() {
        if (!validateCustomerSelection() || !validateVehicleSelection()
                || !dateSelected(diagnosisDatePicker, "/booking/validation/MissingDiagDate.fxml")
                || !timeSelected(diagnosisStartTimeTextField, diagnosisEndTimeTextField, "/booking/validation/MissingDiagTime.fxml")
                || !timeValid(diagnosisStartTimeTextField, diagnosisEndTimeTextField, "/booking/validation/InvalidDiagTime.fxml")
                || !timeNotInverted(diagnosisStartTimeTextField, diagnosisEndTimeTextField)
                || !validateMechanicSelection()) return false;

        // if booking already exists, replace old object with new
        selectedVehicle.getBookingList().removeIf(b -> b.getBookingID() == selectedBooking.getBookingID());
        selectedVehicle.getBookingList().add(selectedBooking);

        selectedBooking.setVehicleRegNumber(extractVehicle().getVehicleRegNumber());
        selectedBooking.setDescription(descriptionTextField.getText());
        selectedBooking.setMechanicID(extractMechanic().getMechanicID());
        selectedBooking.setDiagnosisStart(extractDiagnosisStart());
        selectedBooking.setDiagnosisEnd(extractDiagnosisEnd());

        // repair dates, if set
        try {
            selectedBooking.setRepairStart(extractRepairStart());
            selectedBooking.setRepairEnd(extractRepairEnd());
        }
        catch (DateTimeParseException e) {
            selectedBooking.setRepairStart(null);
            selectedBooking.setRepairEnd(null);
        }
        // mileage, if set
        try {
            selectedVehicle.setMileage(extractInteger(newMileageTextField));
        }
        catch (NumberFormatException e) {
            // do nothing
        }

        // bill
        boolean settled = selectedVehicle.isCoveredByWarranty();
        boolean hasRepair = selectedBooking.getRepairStart() != null;
        double cost = 0;
        double duration = selectedBooking.getDiagnosisEnd().toEpochSecond()
                - selectedBooking.getDiagnosisStart().toEpochSecond();

        if (hasRepair) duration += selectedBooking.getRepairEnd().toEpochSecond()
                - selectedBooking.getRepairStart().toEpochSecond();
        duration /= 3600;

        cost += extractMechanic().getHourlyRate() * duration;
        for (PartOccurrence pO : selectedBooking.getRequiredPartsList()) {
            PartRepair repair = pO.getPartRepair();

            if (repair != null) cost+= repair.getCost();
            cost += pO.getPartAbstraction().getPartPrice();
        }
        VehicleRepair vehicleRepair = selectedBooking.getSpcRepair();
        if (vehicleRepair != null) cost += vehicleRepair.getCost();

        selectedBooking.setBill(new Bill(cost, settled));


        // commit
        try {
            bookingSystem.isClosed(selectedBooking);
            bookingSystem.isHoliday(selectedBooking);
            bookingSystem.clashes(selectedBooking);
            if (vehicleSystem.addEditVehicle(selectedVehicle)) {
                detachedParts.forEach(p -> DatabaseRepository.getInstance().commitItem(p));

                ((ListController) master.getController(ListController.class)).refreshTable();
                if (master.getController(CalendarController.class) != null)
                    ((CalendarController) master.getController(CalendarController.class))
                            .addAppointment(selectedBooking);

                clear();
                return true;
            }
        }
        catch (UnavailableDateException e) {
            invalidTimePopOver = new PopOver(new Label(e.reason().toString()));
            invalidTimePopOver.setCornerRadius(0);
            invalidTimePopOver.setDetachable(false);

            Node targetNode;
            if (e.reason() == HOLIDAY)
                targetNode = e.concerning() == DIAGNOSIS ? diagnosisDatePicker : repairDatePicker;
            else targetNode = e.concerning() == DIAGNOSIS ? diagnosisEndTimeTextField : repairEndTimeTextField;

            invalidTimePopOver.show(targetNode);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML private void delete() {
        if (selectedBooking.getBookingID() != -1 && confirmDelete()) {
            bookingSystem.deleteBookingByID(selectedBooking.getBookingID());
            ((ListController) master
                    .getController(ListController.class))
                    .refreshTable();
            ((CalendarController) master
                    .getController(CalendarController.class))
                    .refreshAgenda(bookingSystem.getAllBookings());
            clear();
        }
    }

    @FXML private void complete() {
        if (!validateCustomerSelection() || !validateVehicleSelection()
                || !dateSelected(diagnosisDatePicker, "/booking/validation/MissingDiagDate.fxml")
                || !timeSelected(diagnosisStartTimeTextField, diagnosisEndTimeTextField, "/booking/validation/MissingDiagTime.fxml")
                || !timeValid(diagnosisStartTimeTextField, diagnosisEndTimeTextField, "/booking/validation/InvalidDiagTime.fxml")
                || !timeNotInverted(diagnosisStartTimeTextField, diagnosisEndTimeTextField)
                || !dateSelected(repairDatePicker, "/booking/validation/MissingRepDate.fxml")
                || !timeSelected(repairStartTimeTextField, repairEndTimeTextField, "/booking/validation/MissingRepTime.fxml")
                || !timeValid(repairStartTimeTextField, repairEndTimeTextField, "/booking/validation/InvalidRepTime.fxml")
                || !timeNotInverted(repairStartTimeTextField, repairEndTimeTextField)
                || !validateMechanicSelection()
                || !validNewMileage()) return;

        selectedBooking.setComplete(true);

        List<PartOccurrence> partsToInstall = selectedBooking.getRequiredPartsList();
        ZonedDateTime installDate = selectedBooking.getRepairEnd();
        String vehicleReg = selectedVehicle.getVehicleRegNumber();

        if (save()) partsToInstall.forEach(part ->
                partsSystem.commitInst(new Installation(installDate, vehicleReg, part)));
    }

    @FXML private void clear() {
        selectedBooking = new DiagRepBooking();
        detachedParts = new ArrayList<>();
        selectedVehicle = null;
        selectedPart = null;

        clearAll();
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

        populateParts(selectedBooking.getRequiredPartsList());
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                   FIELD STATE MANAGERS                                              //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** fills in the given booking's details into the detail pane */
    void populate(DiagRepBooking booking) {
        clear();
        selectedVehicle = booking.getVehicle();
        selectedBooking = selectedVehicle.getBookingList().stream()
                .filter(b -> b.getBookingID() == booking.getBookingID())
                .collect(Collectors.toList()).get(0);
        populateVehicle(booking.getCustomer().getVehicles());
        populateParts(booking.getRequiredPartsList());

        writeCustomer(booking.getCustomer());
        descriptionTextField.setText(booking.getDescription());
        mileageTextField.setText("" + selectedVehicle.getMileage());
        selectVehicle(selectedVehicle);
        selectMechanic(booking.getMechanic());

        diagnosisDatePicker.setValue(booking.getDiagnosisStart().toLocalDate());
        diagnosisStartTimeTextField.setText(booking.getDiagnosisStart().format(timeFormatter));
        diagnosisEndTimeTextField.setText(booking.getDiagnosisEnd().format(timeFormatter));
        if (booking.getRepairStart() != null) {
            repairDatePicker.setValue(booking.getRepairStart().toLocalDate());
            repairStartTimeTextField.setText(booking.getRepairStart().format(timeFormatter));
            repairEndTimeTextField.setText(booking.getRepairEnd().format(timeFormatter));
        }

        disable(booking.isComplete());
    }

    /** sets the given parts to be the list of parts in the table */
    void populateParts(List<PartOccurrence> parts) {
        ObservableList<PartOccurrence> partsObservable = FXCollections.observableArrayList(parts);
        partsTable.setItems(partsObservable);
        partsTable.refresh();
    }

    /** Writes the name of the given customer into the customer search bar */
    private void writeCustomer(Customer customer) {
        customerSearchBar.setText(customer.getCustomerID() + ": " + customer.getCustomerFirstname()
                + " " + customer.getCustomerSurname());
    }

    /** sets the given list of vehicles to be the list of vehicles in the combobox */
    private void populateVehicle(List<Vehicle> vehicles) {
        ObservableList<Vehicle> vehiclesObservable = FXCollections.observableArrayList(vehicles);
        vehicleComboBox.setItems(vehiclesObservable);
    }

    /** Sets the given vehicle as selected in the vehicle combobox */
    private void selectVehicle(Vehicle vehicle) {
        vehicleComboBox.getSelectionModel().select(
                vehicleComboBox.getItems().stream()
                        .filter(v -> v.getVehicleRegNumber().equals(vehicle.getVehicleRegNumber()))
                        .collect(Collectors.toList()).get(0)
        );
    }

    /** Selects the given mechanic in the combobox */
    private void selectMechanic(Mechanic mechanic) {
        mechanicComboBox.getSelectionModel().select(
                mechanicComboBox.getItems().stream()
                        .filter(m -> m.getMechanicID() == mechanic.getMechanicID())
                        .collect(Collectors.toList()).get(0)
        );
    }

    /** Sets every field to enabled or disabled */
    private void disable(boolean state) {
        customerSearchBar.setDisable(state);
        vehicleComboBox.setDisable(state);
        descriptionTextField.setDisable(state);
        diagnosisDatePicker.setDisable(state);
        diagnosisStartTimeTextField.setDisable(state);
        diagnosisEndTimeTextField.setDisable(state);
        repairDatePicker.setDisable(state);
        repairStartTimeTextField.setDisable(state);
        repairEndTimeTextField.setDisable(state);
        partsTable.setDisable(state);
        mechanicComboBox.setDisable(state);
        newMileageTextField.setDisable(state);
    }

    /** Empties all fields in the details pane */
    private void clearAll() {
        customerSearchBar.setText("");
        descriptionTextField.setText("");
        diagnosisStartTimeTextField.setText("");
        diagnosisEndTimeTextField.setText("");
        repairStartTimeTextField.setText("");
        repairEndTimeTextField.setText("");
        mileageTextField.setText("");
        newMileageTextField.setText("");

        vehicleComboBox.getSelectionModel().clearSelection();
        mechanicComboBox.getSelectionModel().clearSelection();

        diagnosisDatePicker.setValue(null);
        repairDatePicker.setValue(null);

        populateVehicle(new ArrayList<>());
        populateParts(new ArrayList<>());

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
        newMileageTextField.setDisable(false);
        setPaneTitleToAdd();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     FIELD EXTRACTORS                                                //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** gets the customer from the search bar */
    private Customer extractCustomer() {
        String query = customerSearchBar.getText();
        return query.matches("[0-9]+.*") ?
                customerSystem.getACustomers(Integer.parseInt(customerSearchBar.getText().split(":")[0]))
                : null;
    }

    /** gets the vehicle from the combo box */
    private Vehicle extractVehicle() {
        return vehicleComboBox.getSelectionModel().getSelectedItem();
    }

    /** gets the selected mechanic from the combo box */
    private Mechanic extractMechanic() {
        return mechanicComboBox.getSelectionModel().getSelectedItem();
    }

    /** returns the diagnosis start time */
    private ZonedDateTime extractDiagnosisStart() throws DateTimeParseException {
        return ZonedDateTime.of(
                diagnosisDatePicker.getValue(),
                LocalTime.parse(diagnosisStartTimeTextField.getText(), timeFormatter),
                ZoneId.systemDefault());
    }

    /** returns the diagnosis end time */
    private ZonedDateTime extractDiagnosisEnd() throws DateTimeParseException {
        return ZonedDateTime.of(
                diagnosisDatePicker.getValue(),
                LocalTime.parse(diagnosisEndTimeTextField.getText(), timeFormatter),
                ZoneId.systemDefault());
    }

    /** returns the repair start time */
    private ZonedDateTime extractRepairStart() throws DateTimeParseException {
        return ZonedDateTime.of(
                repairDatePicker.getValue(),
                LocalTime.parse(repairStartTimeTextField.getText(), timeFormatter),
                ZoneId.systemDefault());
    }

    /** returns the repair end time */
    private ZonedDateTime extractRepairEnd() throws DateTimeParseException {
        return ZonedDateTime.of(
                repairDatePicker.getValue(),
                LocalTime.parse(repairEndTimeTextField.getText(), timeFormatter),
                ZoneId.systemDefault());
    }

    /** gets an integer value from the textfield */
    private int extractInteger(TextField textField) throws NumberFormatException {
        return Integer.parseInt(textField.getText());
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     INPUT VALIDATION                                                //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean validateCustomerSelection() {
        if (customerValidationPopOver == null) {
            try {
                customerValidationPopOver = new PopOver(FXMLLoader.load(getClass()
                        .getResource("/booking/validation/CustomerValidation.fxml")));
                customerValidationPopOver.setDetachable(false);
                customerValidationPopOver.setCornerRadius(0);
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        if (extractCustomer() != null) {
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
                        .getResource("/booking/validation/MissingVehicle.fxml")));
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

    private boolean dateSelected(DatePicker picker, String source) {
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

    private boolean timeSelected(TextField start, TextField end, String source) {
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

    private boolean timeValid(TextField start, TextField end, String source) {
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

    /** Checks that the time in the end field is greater than in the start field */
    private boolean timeNotInverted(TextField start, TextField end) {
        if (invertedTimePopOver == null) {
            try {
                invalidTimePopOver = new PopOver(FXMLLoader.load(getClass()
                        .getResource("/resources/booking/validation/InvertedTime.fxml")
                ));
            }
            catch (IOException e) {
                e.printStackTrace(); // do nothing
                return false;
            }
        }

        if (LocalTime.parse(end.getText(), timeFormatter).isAfter(LocalTime.parse(start.getText(), timeFormatter))) {
            if (invalidTimePopOver.isShowing()) invalidTimePopOver.hide();
            return true;
        }
        else {
            if (!invalidTimePopOver.isShowing()) invalidTimePopOver.show(end);
            return false;
        }
    }

    private boolean validateMechanicSelection() {
        if (mechanicValidationPopOver == null) {
            try {
                mechanicValidationPopOver = new PopOver(FXMLLoader.load(getClass()
                        .getResource("/booking/validation/MissingMechanic.fxml")));
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

    private boolean validNewMileage() {
        if (mileageValidationPopOver == null) {
            try {
                mileageValidationPopOver = new PopOver(FXMLLoader.load(getClass()
                        .getResource("/booking/validation/MileageValidation.fxml")));
                mileageValidationPopOver.setDetachable(false);
                mileageValidationPopOver.setCornerRadius(0);
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            int oldMileage = Integer.parseInt(mileageTextField.getText());
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
    //                                   STRUCTURAL MODIFICATIONS                                          //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    void setPaneTitleToAdd() {
        paneTitle.setText("Add Booking");
    }

    void setPaneTitleToView() {
        paneTitle.setText("View Booking");
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
}
