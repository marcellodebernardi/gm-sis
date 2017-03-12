package controllers;

import domain.Customer;
import domain.DiagRepBooking;
import domain.Mechanic;
import domain.Vehicle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import java.util.ArrayList;
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
    private Button weekViewButton;
    private Button monthViewButton;

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
    private ComboBox vehicleComboBox;
    private ComboBox customerComboBox;
    private ComboBox mechanicComboBox;

    // tables and lists
    private TableView<DiagRepBooking> bookingsTable;
    private Agenda weekView;

    // display data
    private ObservableList<DiagRepBooking> bookingsObservable;
    private ObservableList<String> customerInfoObservable;
    private ObservableList<String> vehicleInfoObservable;
    private ObservableList<String> mechanicInfoObservable;


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

            // more data fields
            dataFieldPane.add(new Label("Diagnosis date"), 0, 3);
            diagnosisDatePicker = new DatePicker();
            dataFieldPane.add(diagnosisDatePicker, 1, 3);
            dataFieldPane.add(new Label("Repair date"), 0, 4);
            repairDatePicker = new DatePicker();
            dataFieldPane.add(repairDatePicker, 1, 4);
            dataFieldPane.add(new Label("Description"), 0, 5);
            descriptionField = new TextField("Write a description");
            dataFieldPane.add(descriptionField, 1, 5);

            // mechanic combobox
            dataFieldPane.add(new Label("Select mechanic"), 0, 6);
            mechanicComboBox = new ComboBox();
            dataFieldPane.add(mechanicComboBox, 1, 6);
            displayMechanicComboBox(bookSys.getAllMechanics()); // todo mechanic list

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
                        new DateTime(diagnosisDatePicker.toString()),
                        new DateTime(repairDatePicker.toString()),
                        null,
                        null,
                        null,
                        null);
                bookSys.addBooking(booking);
            });
            clearBookingButton = new Button("Clear fields");
            clearBookingButton.setOnAction(event -> {
                customerSearchBar.clear();
                descriptionField.clear();
            });

            leftBottomButtons.getChildren().addAll(addBookingButton, clearBookingButton);


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

            listViewButton = new Button("List View");
            listViewButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    displayTableTopBar();
                    displayTable(bookSys.getAllBookings());
                }
            });

            weekViewButton = new Button("Week View");
            weekViewButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    displayCalendarTopBar();
                }
            });

            monthViewButton = new Button("Month View");
            monthViewButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    displayCalendarTopBar();
                }
            });

            calendarHBox.getChildren().addAll(listViewButton, weekViewButton, monthViewButton);
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
            weekViewButton = new Button("Week View");
            weekViewButton.setOnAction(event -> {
                displayCalendarTopBar();
                displayAgenda();
            });

            monthViewButton = new Button("Month View");
            monthViewButton.setOnAction(event -> displayCalendarTopBar());

            listViewHBox.getChildren().addAll(searchLabel, bookingSearchBar, listViewButton, weekViewButton,
                    monthViewButton);
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
            DateTime date = p.getValue().getDiagnosisStart();
            return new ReadOnlyObjectWrapper<>(date == null ? "" : date.toLocalDate().toString());
        });
        repairDateColumn.setCellValueFactory(p -> {
            DateTime date = p.getValue().getRepairStart();
            return new ReadOnlyObjectWrapper<>(date == null ? "" : date.toLocalDate().toString());
        });

        bookingsTable.getColumns().setAll(bookingIDColumn, customerColumn, vehicleRegColumn,
                diagnosisDateColumn, repairDateColumn);
        bookingsTable.refresh();
    }

    private void displayAgenda() {
        if (weekView == null) {
            weekView = new Agenda();
        }
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
}
