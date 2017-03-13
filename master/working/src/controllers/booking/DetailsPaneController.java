package controllers.booking;

import domain.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.BookingSystem;
import logic.CustomerSystem;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcello De Bernardi
 */
public class DetailsPaneController {
    private BookingController master;
    private CustomerSystem customerSystem;
    private BookingSystem bookingSystem;

    // customer and vehicle ComboBoxes
    @FXML private TextField customerSearchBar;
    @FXML private ComboBox<String> vehicleComboBox;
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
    }

    @FXML private void initialize() {
        vehicleComboBox.setPrefWidth(Double.MAX_VALUE);
        mechanicComboBox.setPrefWidth(Double.MAX_VALUE);
        diagnosisDatePicker.setPrefWidth(Double.MAX_VALUE);
        repairDatePicker.setPrefWidth(Double.MAX_VALUE);
        populateCustomerTextField(customerSystem.getAllCustomers());
        populateMechanicComboBox(bookingSystem.getAllMechanics());
    }


    //////////////////// EVENT HANDLERS /////////////////////////
        @FXML private void selectCustomer() {
        Customer customer = customerSystem.getACustomers(Integer.parseInt(customerSearchBar
                .getText()
                .split(":")[0]));
        populateVehicleComboBox(customer.getVehicles());
    }


    ///////////////////// DATA MANIPULATIONS /////////////////////
    private void populateCustomerTextField(List<Customer> customers) {
        List<String> customerInfo = new ArrayList<>();
        for (Customer c : customers) {
            customerInfo.add(c.getCustomerID() + ": " + c.getCustomerFirstname() + " " + c.getCustomerSurname());
        }
        TextFields.bindAutoCompletion(customerSearchBar, customerInfo);
    }

    private void populateVehicleComboBox(List<Vehicle> vehicles) {
        List<String> vehicleInfo = new ArrayList<>();
        for (Vehicle v : vehicles) {
            vehicleInfo.add(v.getRegNumber() + ": " + v.getManufacturer() + " " + v.getModel());
        }
        ObservableList<String> vehicleInfoObservable = FXCollections.observableArrayList(vehicleInfo);
        vehicleComboBox.setItems(vehicleInfoObservable);
    }

    private void populateMechanicComboBox(List<Mechanic> mechanics) {
        List<String> mechanicInfo = new ArrayList<>();
        for (Mechanic m : mechanics) {
            mechanicInfo.add(m.getMechanicID() + ": " + m.getFirstName() + " " + m.getSurname());
        }
        ObservableList<String> mechanicInfoObservable = FXCollections.observableArrayList(mechanicInfo);
        mechanicComboBox.setItems(mechanicInfoObservable);
    }

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
}
