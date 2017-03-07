package controllers;

import domain.Customer;
import domain.DiagRepBooking;
import domain.Vehicle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.BookingSystem;
import logic.CustomerSystem;
import logic.VehicleSys;
import main.Main;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

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
    private HBox leftBottomButtons;
    private Button addBookingButton;
    private Button deleteBookingButton;
    private Button editBookingButton;
    private ComboBox vehicleComboBox;
    private ComboBox customerComboBox;

    // tables and lists
    private TableView<DiagRepBooking> bookingsTable;

    // display data
    private ObservableList<DiagRepBooking> bookingsObservable;
    private ObservableList<String> customerInfoObservable;
    private ObservableList<String> vehicleInfoObservable;


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
            customerSearchBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    // todo fix to actually search
                    displayCustomerComboBox(custSys.getAllCustomers());
                    displayVehicleComboBox(vehicleSys.getVehiclesList());
                }
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
            dataFieldPane.add(new Label("Diagnosis Start Time"), 0, 3);
            dataFieldPane.add(new TextField("dd/mm/yyyy"), 1, 3);
            dataFieldPane.add(new Label("Diagnosis End Time"), 0, 4);
            dataFieldPane.add(new TextField("dd/mm/yyyy"), 1, 4);
            dataFieldPane.add(new Label("Description"), 0, 5);
            dataFieldPane.add(new TextField("Write a description"), 1, 5);

            // buttons
            leftBottomButtons = new HBox();
            leftBottomButtons.setId("leftBottomButtons");
            addBookingButton = new Button("Add booking");
            addBookingButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    DiagRepBooking booking = new DiagRepBooking(
                            "1",
                            "Description",
                            20,
                            false,
                            1,
                            new DateTime(),
                            new DateTime(),
                            null,
                            null,
                            null,
                            null);
                    bookSys.addBooking(booking);
                }
            });

            leftBottomButtons.getChildren().add(addBookingButton);


            addBookingPane.setTop(topTitle);
            addBookingPane.setCenter(dataFieldPane);
            addBookingPane.setBottom(leftBottomButtons);
        }
        basePane.setLeft(addBookingPane);
    }

    private void displayViewBookingPane() {
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
            bookingSearchBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    displayTable(bookSys.searchBookings(bookingSearchBar.getText()));
                }
            });

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

        vehicleRegColumn.setCellValueFactory(new PropertyValueFactory<DiagRepBooking, String>("vehicleRegNumber"));
        vehicleRegColumn.setCellFactory(TextFieldTableCell.<DiagRepBooking>forTableColumn());

        diagnosisDateColumn.setCellValueFactory(new PropertyValueFactory<DiagRepBooking, String>("bookingID"));
        diagnosisDateColumn.setCellFactory(TextFieldTableCell.<DiagRepBooking>forTableColumn());

        repairDateColumn.setCellValueFactory(new PropertyValueFactory<DiagRepBooking, String>("bookingID"));
        repairDateColumn.setCellFactory(TextFieldTableCell.<DiagRepBooking>forTableColumn());


        bookingIDColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, Integer>,
                ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<DiagRepBooking, Integer> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getBookingID());
            }
        });

        customerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DiagRepBooking, String> p) {
                Customer customer = p.getValue().getCustomer();
                return customer == null ? new ReadOnlyObjectWrapper<>("") : new ReadOnlyObjectWrapper<>(customer.getCustomerSurname());
            }
        });

        vehicleRegColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DiagRepBooking, String> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getVehicleRegNumber());
            }
        });

        diagnosisDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DiagRepBooking, String> p) {
                DateTime date = p.getValue().getDiagnosisStart();
                return new ReadOnlyObjectWrapper<>(date == null ? "" : date.toLocalDate().toString());
            }
        });

        repairDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DiagRepBooking, String> p) {
                DateTime date = p.getValue().getRepairStart();
                return new ReadOnlyObjectWrapper<>(date == null ? "" : date.toLocalDate().toString());
            }
        });

        bookingsTable.getColumns().setAll(bookingIDColumn, customerColumn, vehicleRegColumn,
                diagnosisDateColumn, repairDateColumn);
        bookingsTable.refresh();
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
}
