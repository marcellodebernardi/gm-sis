package controllers;


import domain.Customer;
import domain.DiagRepBooking;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import logic.BookingSystem;
import main.Main;

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

    public void initialize() {
        bookSys = BookingSystem.getInstance();
    }

    public BookingsController() {
        bookSys = BookingSystem.getInstance();
    }
    /**
     * Open screen for new booking
     */
    @FXML
    public void openNewBookingView() {
        loadBasePaneInstance();
        try {
            FlowPane addView = FXMLLoader.load(getClass().getResource("/resources/booking/addBooking.fxml"));
            basePaneInstance.setLeft(addView);
            basePaneInstance.setVisible(true);
            Main.getInstance().replaceTabContent(basePaneInstance);
        }
        catch (IOException e) {
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



    private void loadBasePaneInstance() {
        if (basePaneInstance == null) {
            try {
                basePaneInstance = FXMLLoader.load(getClass().getResource("/resources/booking/bookingsBasePane.fxml"));
                FlowPane addPane = FXMLLoader.load(getClass().getResource("/resources/booking/addBooking.fxml"));
                basePaneInstance.setLeft(addPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private TableView<DiagRepBooking> constructTable(List<DiagRepBooking> bookings) {
        TableView<DiagRepBooking> table = new TableView<>();
        ObservableList<DiagRepBooking> tableEntries = FXCollections.observableArrayList(bookings);
        table.setItems(tableEntries);


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
                return customer == null ? new ReadOnlyObjectWrapper<>("") : new ReadOnlyObjectWrapper<>(customer.getCustomerSurname()   );
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

        table.getColumns().setAll(bookingIDColumn, customerColumn, vehicleRegColumn,
                diagnosisDateColumn, repairDateColumn);

        return table;
    }
}