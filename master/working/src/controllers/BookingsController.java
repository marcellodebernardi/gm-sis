package controllers;


import domain.Customer;
import domain.DiagRepBooking;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;
import logic.BookingSystem;
import logic.Criterion;
import logic.CustomerSystem;
import main.Main;
import persistence.DatabaseRepository;

import java.io.IOException;
import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class BookingsController {
    private BorderPane basePaneInstance;
    private BookingSystem bookSys;
    @FXML
    private TextField searchBookings;
    @FXML
    private TableView<DiagRepBooking> bookingListTable;
    @FXML
    private TextField searchCustomers;

    public BookingsController() {
        bookSys = BookingSystem.getInstance();
    }

    public void initialize() {
        bookSys = BookingSystem.getInstance();
    }

    /**
     * Open screen for new booking
     */
    @FXML
    public void openNewBookingView() {
        loadBasePaneInstance();
        try {
            BorderPane addView = FXMLLoader.load(getClass().getResource("/resources/booking/addBooking.fxml"));
            basePaneInstance.setLeft(addView);
            basePaneInstance.setVisible(true);
            Main.getInstance().replaceTabContent(basePaneInstance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openMonthView() {
        loadBasePaneInstance();
        try {
            BorderPane monthView = FXMLLoader.load(getClass().getResource("/resources/booking/monthView.fxml"));
            basePaneInstance.setCenter(monthView);
            basePaneInstance.getCenter().setId("monthWeekList");
            basePaneInstance.setVisible(true);
            Main.getInstance().replaceTabContent(basePaneInstance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openWeekView() {
        loadBasePaneInstance();
        try {
            BorderPane weekView = FXMLLoader.load(getClass().getResource("/resources/booking/weekView.fxml"));
            GridPane dayGrid = new GridPane();
            List<DiagRepBooking> dayBookings;

            for (int day = 0; day < 7; day++) {
                dayBookings = bookSys.getDayBookings(day);
                ColumnConstraints constraint = new ColumnConstraints();
                constraint.setPercentWidth(14);
                dayGrid.getColumnConstraints().add(day, constraint);

                for (int hour = 0; hour < 8; hour++) {
                    Button hourBlock = new Button();
                    hourBlock.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    GridPane.setConstraints(hourBlock, day, hour);
                    GridPane.setFillWidth(hourBlock, true);
                    GridPane.setFillHeight(hourBlock, true);
                    dayGrid.getChildren().addAll(hourBlock);
                }
            }
            for (int i = 0; i < 8; i++) {
                RowConstraints constraint = new RowConstraints();
                constraint.setPercentHeight(12.5);
                dayGrid.getRowConstraints().add(i, constraint);
            }

            weekView.setCenter(dayGrid);
            basePaneInstance.setCenter(weekView);
            basePaneInstance.getCenter().setId("monthWeekList");
            basePaneInstance.setVisible(true);
            Main.getInstance().replaceTabContent(basePaneInstance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openListView() {
        loadBasePaneInstance();
        try {
            BorderPane listView = FXMLLoader.load(getClass().getResource("/resources/booking/listView.fxml"));
            TableView<DiagRepBooking> table = constructTable(bookSys.getAllBookings());

            listView.setCenter(table);

            basePaneInstance.setCenter(listView);
            basePaneInstance.getCenter().setId("monthWeekList");
            basePaneInstance.setVisible(true);
            Main.getInstance().replaceTabContent(basePaneInstance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void searchBookings() {
        loadBasePaneInstance();
        try {
            BorderPane listView = FXMLLoader.load(getClass().getResource("/resources/booking/listView.fxml"));
            TableView<DiagRepBooking> table = constructTable(bookSys.searchBookings(searchBookings.getText()));

            listView.setCenter(table);

            basePaneInstance.setCenter(listView);
            basePaneInstance.getCenter().setId("monthWeekList");
            basePaneInstance.setVisible(true);
            Main.getInstance().replaceTabContent(basePaneInstance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    @FXML
    public void searchCustomers() {
        loadBasePaneInstance();
        try {
            BorderPane addPane = FXMLLoader.load(getClass().getResource("/resources/booking/addBooking.fxml"));
            // todo request convenience method in CustomerSystem
            TableView<Customer> table =
                    constructTable(DatabaseRepository.getInstance().getByCriteria(new Criterion<>(Customer.class)));

            listView.setCenter(table);

            basePaneInstance.setCenter(listView);
            basePaneInstance.getCenter().setId("monthWeekList");
            basePaneInstance.setVisible(true);
            Main.getInstance().replaceTabContent(basePaneInstance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    private void loadBasePaneInstance() {
        if (basePaneInstance == null) {
            try {
                basePaneInstance = FXMLLoader.load(getClass().getResource("/resources/booking/bookingsBasePane.fxml"));
                BorderPane addPane = FXMLLoader.load(getClass().getResource("/resources/booking/addBooking.fxml"));
                // ComboBox custDropDown = (ComboBox)addPane.lookup("#customerDropDown");

                /* List<Customer> customers = DatabaseRepository
                        .getInstance()
                        .getByCriteria(new Criterion<>(Customer.class));
                ObservableList<String> customerNames = FXCollections.observableArrayList();
                for (Customer c : customers) {
                    customerNames.add(c.getCustomerID() + ": " + c.getCustomerFirstname() + " " + c.getCustomerSurname());
                } */
                // custDropDown.setItems(customerNames);
                basePaneInstance.setLeft(addPane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates or updates the booking list table. Updates table regardless of whether
     * return value is used.
     *
     * @param bookings
     * @return
     */
    private TableView<DiagRepBooking> constructTable(List<DiagRepBooking> bookings) {
        if (bookingListTable == null) bookingListTable = new TableView<>();
        ObservableList<DiagRepBooking> tableEntries = FXCollections.observableArrayList(bookings);
        bookingListTable.setItems(tableEntries);

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
                return new ReadOnlyObjectWrapper<>(p.getValue().getDiagnosisStart().toLocalDate().toString());
            }
        });

        repairDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DiagRepBooking, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DiagRepBooking, String> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getRepairStart().toLocalDate().toString());
            }
        });

        bookingListTable.getColumns().setAll(bookingIDColumn, customerColumn, vehicleRegColumn,
                diagnosisDateColumn, repairDateColumn);
        bookingListTable.refresh();

        return bookingListTable;
    }
}