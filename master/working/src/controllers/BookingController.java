package controllers;

import domain.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import jfxtras.scene.control.agenda.Agenda;
import logic.BookingSystem;
import logic.CustomerSystem;
import logic.VehicleSys;
import main.Main;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import persistence.DatabaseRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Marcello De Bernardi
 */
public class BookingController {
    // singleton instance and booking system
    private static BookingController instance;
    private BookingSystem bookSys;
    private CustomerSystem custSys;
    private VehicleSys vehicleSys;

    // scene graph panes
    private BorderPane basePane;
    private BorderPane addBookingPane;
    private BorderPane rightPane;

    // scene graph elements (buttons, search bars, tables)
    // controls at the top of right pane
    private HBox listViewHBox;
    private HBox calendarHBox;
    private Label searchLabel;
    private TextField bookingSearchBar;
    private Button listViewButton;
    private Button calendarViewButton;
    private DatePicker calendarWeekPicker;
    private ComboBox calendarMechanicComboBox;

    // buttons and search bars in left pane
    private TextField customerSearchBar;
    private TextField descriptionField;
    private TextField diagnosisStartTimeField;
    private TextField diagnosisEndTimeField;
    private TextField repairStartTimeField;
    private TextField repairEndTimeField;
    private DatePicker diagnosisDatePicker;
    private DatePicker repairDatePicker;
    private HBox leftBottomButtons;
    private Button addBookingButton;
    private Button deleteBookingButton;
    private Button editBookingButton;
    private Button clearBookingButton;
    private Button subcontractToSPCButton;
    private ComboBox vehicleComboBox;
    private ComboBox customerComboBox;
    private ComboBox mechanicComboBox;

    // tables and lists
    private TableView<DiagRepBooking> bookingsTable;
    private Agenda weekView;
    private TableView<PartOccurrence> partsTable;

    // display data
    private ObservableList<DiagRepBooking> bookingsObservable;
    private ObservableList<String> customerInfoObservable;
    private ObservableList<String> vehicleInfoObservable;
    private ObservableList<String> mechanicInfoObservable;
    private ObservableList<String> calendarMechanicInfoObersvable;
    private ObservableList<PartOccurrence> partsObservable;


    ///////////////// INTERFACE TO APPLICATION /////////////////
    private BookingController() {
        // booking system
        bookSys = BookingSystem.getInstance();
        custSys = CustomerSystem.getInstance();
        vehicleSys = VehicleSys.getInstance();

        // base structure
        basePane = new BorderPane();
        rightPane = new BorderPane();
        rightPane.setId("monthWeekList");
        basePane.setCenter(rightPane);

        // left pane and right pane, main content
        displayAddBookingPane();
        displayTable(bookSys.getAllBookings());
        displayTableTopBar();

    }

    public static BookingController getInstance() {
        if (instance == null) instance = new BookingController();
        return instance;
    }

    /**
     * Return a reference to the root node, as well as send the root node to Main.
     *
     * @return
     */
    public BorderPane show() {
        Main.getInstance().replaceTabContent(basePane);
        return basePane;
    }


    ///////////////// STRUCTURAL MODIFICATIONS ////////////////
    /**
     * Displays the pane for adding bookings.
     */
    private void displayAddBookingPane() {
        if (addBookingPane == null) {
            // base
            addBookingPane = new BorderPane();
            addBookingPane.setId("addBookingPane");

            // title
            HBox topTitle = new HBox();
            topTitle.setId("addBookingTitle");
            topTitle.getChildren().add(new Text("Add Booking"));

            // grid
            GridPane dataFieldPane = new GridPane();
            dataFieldPane.add(new Label("Search customers"), 0, 0);

            // customer search and table
            customerSearchBar = new TextField();
            customerSearchBar.setOnKeyPressed(event -> {
                // todo fix to actually search
                displayCustomerComboBox(custSys.getAllCustomers());
                displayVehicleComboBox(vehicleSys.getVehiclesList());
            });
            dataFieldPane.add(customerSearchBar, 1, 0);
            dataFieldPane.add(new Label("Select customer"), 0, 1);
            customerComboBox = new ComboBox();
            dataFieldPane.add(customerComboBox, 1, 1);

            // vehicle combobox
            dataFieldPane.add(new Label("Select vehicle"), 0, 2);
            vehicleComboBox = new ComboBox();
            dataFieldPane.add(vehicleComboBox, 1, 2);

            // diagnosis data and time
            dataFieldPane.add(new Label("Diagnosis date"), 0, 3);
            diagnosisDatePicker = new DatePicker();
            dataFieldPane.add(diagnosisDatePicker, 1, 3);
            dataFieldPane.add(new Label("Diagnosis Time:"), 0, 4);
            diagnosisStartTimeField = new TextField();
            diagnosisEndTimeField = new TextField();
            dataFieldPane.add(diagnosisStartTimeField, 1, 4);
            dataFieldPane.add(diagnosisEndTimeField, 2, 4);

            // repair date and time
            dataFieldPane.add(new Label("Repair date"), 0, 5);
            repairDatePicker = new DatePicker();
            dataFieldPane.add(repairDatePicker, 1, 5);
            dataFieldPane.add(new Label("Repair time"), 0, 6);
            repairStartTimeField = new TextField();
            repairEndTimeField = new TextField();
            dataFieldPane.add(repairStartTimeField, 1, 6);
            dataFieldPane.add(repairEndTimeField, 2, 6);
            dataFieldPane.add(new Label("Description"), 0, 7);
            descriptionField = new TextField("Write a description");
            dataFieldPane.add(descriptionField, 1, 7);

            // mechanic combobox
            dataFieldPane.add(new Label("Select mechanic"), 0, 8);
            mechanicComboBox = new ComboBox();
            dataFieldPane.add(mechanicComboBox, 1, 8);
            displayMechanicComboBox(bookSys.getAllMechanics()); // todo mechanic list

            // repair parts
            TableView<PartOccurrence> partOccurrenceTableView = new TableView<>();
            displayPartsTable(Collections.emptyList());
            dataFieldPane.add(new Label("Parts for repair"), 0, 9);
            dataFieldPane.add(partOccurrenceTableView, 1, 9);

            // buttons
            leftBottomButtons = new HBox();
            leftBottomButtons.setId("leftBottomButtons");
            addBookingButton = new Button("Add booking");
            addBookingButton.setOnAction(event -> {
                Vehicle v = vehicleSys
                        .searchAVehicle(vehicleComboBox
                                .getSelectionModel()
                                .getSelectedItem()
                                .toString()
                                .split(":")[0]
                        );
                int mechanicID = Integer.parseInt(mechanicComboBox
                        .getSelectionModel()
                        .getSelectedItem()
                        .toString()
                        .split(":")[0]);

                DiagRepBooking booking = new DiagRepBooking(
                        v.getRegNumber(),
                        descriptionField.getText(),
                        bookSys.getMechanicByID(mechanicID).getHourlyRate(),
                        v.isCoveredByWarranty(),
                        mechanicID,
                        ZonedDateTime.now(),
                        ZonedDateTime.now(),
                        null,
                        null,
                        null,
                        null);
                bookSys.addBooking(booking);
            });
            subcontractToSPCButton = new Button("Subcontract to SPC");
            subcontractToSPCButton.setOnAction(event -> {
                // todo talk to Murad on how to do this
            });
            clearBookingButton = new Button("Clear fields");
            clearBookingButton.setOnAction(event -> {
                customerSearchBar.clear();
                descriptionField.clear();
            });

            leftBottomButtons.getChildren().addAll(addBookingButton, subcontractToSPCButton, clearBookingButton);


            addBookingPane.setTop(topTitle);
            addBookingPane.setCenter(dataFieldPane);
            addBookingPane.setBottom(leftBottomButtons);
        }
        basePane.setLeft(addBookingPane);
    }

    private void populateBookingPaneFields(DiagRepBooking booking) {
    }

    /**
     * Displays the top bar for the calendar views.
     */
    private void displayCalendarTopBar() {
        if (calendarHBox == null) {
            calendarHBox = new HBox();
            calendarHBox.setId("calendarToggle");

            displayCalendarMechanicComboBox(bookSys.getAllMechanics());

            calendarWeekPicker = new DatePicker();
            calendarWeekPicker.setOnAction(event ->
                    weekView.setDisplayedLocalDateTime(LocalDateTime.of(calendarWeekPicker.getValue(), LocalTime.now()))
            );

            listViewButton = new Button("List View");
            listViewButton.setOnAction(event -> {
                displayTableTopBar();
                displayTable(bookSys.getAllBookings());
            });

            calendarViewButton = new Button("Calendar View");
            calendarViewButton.setOnAction(event -> displayCalendarTopBar());

            calendarHBox.getChildren().addAll(calendarMechanicComboBox, calendarWeekPicker, listViewButton,
                    calendarViewButton);
        }
        rightPane.setTop(calendarHBox);
    }

    /**
     * Displays the top bar for the table views.
     */
    private void displayTableTopBar() {
        if (listViewHBox == null) {
            listViewHBox = new HBox();
            listViewHBox.setId("calendarToggle");

            searchLabel = new Label("Search");
            bookingSearchBar = new TextField();
            bookingSearchBar.setOnKeyPressed(event ->
                    displayTable(bookSys.searchBookings(bookingSearchBar.getText()))
            );
            listViewButton = new Button("List View");
            listViewButton.setOnAction(event -> {
                displayTableTopBar();
                displayTable(bookSys.getAllBookings());
            });
            calendarViewButton = new Button("Calendar View");
            calendarViewButton.setOnAction(event -> {
                displayCalendarTopBar();
                displayAgenda(null);
            });

            listViewHBox.getChildren().addAll(searchLabel, bookingSearchBar, listViewButton, calendarViewButton);
        }
        rightPane.setTop(listViewHBox);
    }

    /**
     * Constructs, or updates, the list of bookings in the bookings screen using the given
     * list of bookings.
     *
     * @param bookings list of bookings to put in table
     */
    private void displayTable(List<DiagRepBooking> bookings) {
        if (bookingsTable == null) bookingsTable = new TableView<>();
        bookingsObservable = FXCollections.observableArrayList(bookings);

        bookingsTable.setItems(bookingsObservable);
        rightPane.setCenter(bookingsTable);

        TableColumn<DiagRepBooking, Integer> bookingIDColumn = new TableColumn<>();
        TableColumn<DiagRepBooking, String> customerColumn = new TableColumn<>();
        TableColumn<DiagRepBooking, String> vehicleRegColumn = new TableColumn<>();
        TableColumn<DiagRepBooking, String> diagnosisDateColumn = new TableColumn<>();
        TableColumn<DiagRepBooking, String> repairDateColumn = new TableColumn<>();

        vehicleRegColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleRegNumber"));
        vehicleRegColumn.setCellFactory(TextFieldTableCell.<DiagRepBooking>forTableColumn());

        diagnosisDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        diagnosisDateColumn.setCellFactory(TextFieldTableCell.<DiagRepBooking>forTableColumn());

        repairDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        repairDateColumn.setCellFactory(TextFieldTableCell.<DiagRepBooking>forTableColumn());

        // table columns
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
            return new ReadOnlyObjectWrapper<>(date == null ? "" : date.toLocalDate().toString());
        });
        repairDateColumn.setCellValueFactory(p -> {
            ZonedDateTime date = p.getValue().getRepairStart();
            return new ReadOnlyObjectWrapper<>(date == null ? "" : date.toLocalDate().toString());
        });

        bookingsTable.getColumns().setAll(bookingIDColumn, customerColumn, vehicleRegColumn,
                diagnosisDateColumn, repairDateColumn);
        bookingsTable.refresh();
    }

    private void displayAgenda(Mechanic mechanic) {
        if (weekView == null) {
            weekView = new Agenda();
        }
        List<DiagRepBooking> bookings = bookSys.getAllBookings();

        for (DiagRepBooking booking : bookings) {
            if (booking.getDiagnosisStart() != null && booking.getDiagnosisEnd() != null)
            weekView.appointments().add(new Agenda.AppointmentImplLocal()
                    .withStartLocalDateTime(LocalDateTime.ofInstant(booking.getDiagnosisStart().toInstant(),
                            booking.getDiagnosisStart().getZone()))
                    .withEndLocalDateTime(LocalDateTime.ofInstant(booking.getDiagnosisEnd().toInstant(),
                            booking.getDiagnosisEnd().getZone()))
                    .withDescription(booking.getDescription())
            );
            else if (booking.getRepairStart() != null && booking.getRepairEnd() != null)
            weekView.appointments().add(new Agenda.AppointmentImplLocal()
                    .withStartLocalDateTime(LocalDateTime.ofInstant(booking.getRepairStart().toInstant(),
                            booking.getRepairStart().getZone()))
                    .withEndLocalDateTime(LocalDateTime.ofInstant(booking.getRepairEnd().toInstant(),
                            booking.getRepairEnd().getZone()))
                    .withDescription(booking.getDescription())
            );
        }

        weekView.setAllowDragging(true);
        weekView.setAllowResize(true);
        rightPane.setCenter(weekView);
    }

    /**
     * Display the customer list in the add booking menu.
     *
     * @param customers
     */
    private void displayCustomerComboBox(List<Customer> customers) {
        if (customerComboBox == null) customerComboBox = new ComboBox();
        List<String> customerInfo = new ArrayList<>();
        for (Customer c : customers) {
            customerInfo.add(c.getCustomerID() + ", " + c.getCustomerFirstname() + " " + c.getCustomerSurname());
        }

        customerInfoObservable = FXCollections.observableArrayList(customerInfo);
        customerComboBox.setItems(customerInfoObservable);
    }

    private void displayVehicleComboBox(List<Vehicle> vehicles) {
        if (vehicleComboBox == null) vehicleComboBox = new ComboBox();
        List<String> vehicleInfo = new ArrayList<>();
        for (Vehicle v : vehicles) {
            vehicleInfo.add(v.getRegNumber() + ": " + v.getManufacturer() + " " + v.getModel());
        }

        vehicleInfoObservable = FXCollections.observableArrayList(vehicleInfo);
        vehicleComboBox.setItems(vehicleInfoObservable);
    }

    private void displayMechanicComboBox(List<Mechanic> mechanics) {
        if (mechanicComboBox == null) mechanicComboBox = new ComboBox();
        List<String> mechanicInfo = new ArrayList<>();
        for (Mechanic m : mechanics) {
            mechanicInfo.add(m.getMechanicID() + ": " + m.getFirstName() + " " + m.getSurname());
        }
        mechanicInfoObservable = FXCollections.observableArrayList(mechanicInfo);
        mechanicComboBox.setItems(mechanicInfoObservable);
    }

    private void displayCalendarMechanicComboBox(List<Mechanic> mechanics) {
        if (calendarMechanicComboBox == null) calendarMechanicComboBox = new ComboBox();
        List<String> mechanicInfo = new ArrayList<>();
        for (Mechanic m : mechanics) {
            mechanicInfo.add(m.getMechanicID() + ": " + m.getFirstName() + " " + m.getSurname());
        }
        calendarMechanicComboBox.setOnAction(event -> {
            Mechanic mechanic = bookSys.getMechanicByID(Integer.parseInt(calendarMechanicComboBox
                    .getSelectionModel()
                    .getSelectedItem()
                    .toString()
                    .split(":")[0])
            );
            displayAgenda(mechanic);
        });
        calendarMechanicInfoObersvable = FXCollections.observableArrayList(mechanicInfo);
        calendarMechanicComboBox.setItems(calendarMechanicInfoObersvable);
    }

    private void displayPartsTable(List<PartOccurrence> parts) {
        if (partsTable == null) partsTable = new TableView<>();
        partsObservable = FXCollections.observableArrayList(parts);

        partsTable.setItems(partsObservable);
        rightPane.setCenter(partsTable);

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
}
