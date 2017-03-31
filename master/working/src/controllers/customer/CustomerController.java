package controllers.customer;

import controllers.booking.BookingController;
import domain.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;
import logic.booking.BookingSystem;
import logic.customer.CustomerSystem;
import logic.vehicle.VehicleSys;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//import logic.*;

/**
 * Created by EBUBECHUKWU on 19/02/2017.
 */

public class CustomerController implements Initializable {
    private static CustomerController instance;
    final ObservableList tableEntriesCustomer = FXCollections.observableArrayList();
    final ObservableList tableEntriesVehicle = FXCollections.observableArrayList();
    final ObservableList tableEntriesBooking = FXCollections.observableArrayList();
    final ObservableList tableEntriesParts = FXCollections.observableArrayList();
    @FXML
    public CustomerSystem cSystem = CustomerSystem.getInstance();
    ////for 'AddCustomerVehiclePopupView.fxml' instance variables
    Stage addVehicleStage;
    ////for 'CustomerView.fxml' instance variables
    ////left pane (add and edit customer view)
    @FXML
    private Label formName = new Label();
    @FXML
    private TextField customerID, customerFirstname, customerSurname, customerAddress, customerPostcode, customerPhone, customerEmail = new TextField();
    @FXML
    private ComboBox customerType = new ComboBox();
    @FXML
    private Button saveCustomerAndAddVehicleButton, saveCustomerButton, deleteCustomerButton, clearCustomerButton = new Button();
    ////right pane (search customer)
    @FXML
    private TextField customerSearch = new TextField();
    @FXML
    private ComboBox customerTypeSearch = new ComboBox();
    @FXML
    private Button customerSearchButton, newCustomerFormButton, editSelectedCustomerButton, makeBookingButton, deleteSelectedCustomerButton, logoutButton = new Button();
    ////right pane (customer table view)
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerTableColumnID;
    @FXML
    private TableColumn<Customer, String> customerTableColumnFirstname, customerTableColumnSurname, customerTableColumnAddress, customerTableColumnPostcode, customerTableColumnPhone, customerTableColumnEmail;
    @FXML
    private TableColumn<Customer, CustomerType> customerTableColumnType;
    ////(customer vehicle table view)
    @FXML
    private TableView<Vehicle> customerVehicleTable;
    @FXML
    private TableColumn<Vehicle, String> customerVehicleTableColumnRegistrationNumber;
    @FXML
    private TableColumn<Vehicle, VehicleType> customerVehicleTableColumnType;
    @FXML
    private TableColumn<Vehicle, String> customerVehicleTableColumnModel;
    @FXML
    private TableColumn<Vehicle, String> customerVehicleTableColumnManufacturer;
    @FXML
    private TableColumn<Vehicle, Boolean> customerVehicleTableColumnWarranty;
    ////(customer booking table view)
    private BookingController master;
    private BookingSystem bookingSystem;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter timeFormatter;
    @FXML
    private TableView<DiagRepBooking> customerBookingTable;
    @FXML
    private TableColumn<DiagRepBooking, Integer> customerBookingTableColumnBookingID;
    @FXML
    private TableColumn<DiagRepBooking, String> customerBookingTableColumnVehicleRegistrationNumber;
    @FXML
    private TableColumn<DiagRepBooking, String> customerBookingTableColumnRepairDate;
    @FXML
    private TableColumn<DiagRepBooking, String> customerBookingTableColumnRepairTime;
    @FXML
    private TableColumn<DiagRepBooking, Double> customerBookingTableColumnBill;
    @FXML
    private TableColumn<DiagRepBooking, String> customerBookingTableColumnSettlementStatus;
    ////(customer vehicle parts table view)
    @FXML
    private TableView<PartAbstraction> customerVehiclePartTable;
    @FXML
    private TableColumn<PartAbstraction, String> customerVehiclePartTableColumnName;
    @FXML
    private TableColumn<PartAbstraction, String> customerVehiclePartTableColumnDescription;
    ////for 'DeleteCustomerConfirmation.fxml' instance variables
    @FXML
    private Button deleteCustomerConfirmationYes, deleteCustomerConfirmationNo = new Button();
    private VehicleSys vSystem = VehicleSys.getInstance();
    private BookingSystem bSystem = BookingSystem.getInstance();

    @FXML
    private Label AddEditL = new Label();
    @FXML
    //private TextField cvRegistrationNumber, cvManufacturer, cvModel, cvEngineSize, cvColour, cvMileage, cvCompanyName, cvCompanyAddress = new TextField();
    private TextField reg, mod, manuf, eSize, col, mil, wName, wCompAddress, regS, manufS = new TextField();
    @FXML
    //private ComboBox cvSelectVehicle, cvCustomerID, cvVehicleType, cvFuelType, cvWarranty = new ComboBox();
    private ComboBox cID, vType, fType, cByWarranty, VehicleS, typeS, VehicleTS = new ComboBox();
    @FXML
    //private DatePicker cvMOTRenewalDate, cvDateLastServiced, CVExpiationDate = new DatePicker();
    private DatePicker rDateMot, dLastServiced, wExpirationDate = new DatePicker();
    @FXML
    private Button addV, clearV = new Button();


    public CustomerController() {
        master = BookingController.getInstance();
        bookingSystem = BookingSystem.getInstance();
        timeFormatter = DateTimeFormatter.ofPattern("hh:mm");
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public static CustomerController getInstance() {
        if (instance == null) instance = new CustomerController();
        return instance;
    }

    //method for adding customer to database
    public void addCustomerToDB() throws Exception {
        try {
            //initialising variables
            String cFirstname = firstLetterUpperCase(customerFirstname.getText());
            String cSurname = firstLetterUpperCase(customerSurname.getText());
            String cAddress = firstLetterUpperCase(customerAddress.getText());
            String cPostcode = customerPostcode.getText().toUpperCase();
            String cPhone = customerPhone.getText();
            String cEmail = customerEmail.getText().toLowerCase();
            CustomerType cType = null;
            boolean checkFields = false;
            checkFields = checkCustomerFields();

            if (customerType.getSelectionModel().getSelectedItem().toString().equals("Private")) {
                cType = CustomerType.Private;
            }
            else if (customerType.getSelectionModel().getSelectedItem().toString().equals("Business")) {
                cType = CustomerType.Business;
            }

            if (checkFields) {
                boolean addedCustomer = cSystem.addCustomer(cFirstname, cSurname, cAddress, cPostcode, cPhone, cEmail, cType);
                if (addedCustomer) {
                    tableViewOfCustomersFromDB(cSystem.getAllCustomers());

                    if (addVehicleStage != null) {
                        if (addVehicleStage.isShowing()) {
                            errorAlert("Vehicle window is already open");
                            addVehicleStage.setAlwaysOnTop(true);
                            return;
                        }
                    }
                    else {
                        newCustomerForm();
                        customerVehicleTable.setItems(null);
                        customerVehiclePartTable.setItems(null);
                        customerBookingTable.setItems(null);
                        CustomerVehicleController.getInstance().showVehiclePopup();
                    }
                }
                customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
            }
        }
        catch (Exception e) {
        }
    }

    //method for deleting customer from database
    public void deleteCustomerFromDB() throws Exception {
        try {
            String cID = customerID.getText();
            boolean check = false;

            if (cID.equals("")) {
                errorAlert("Customer has not yet been selected for deletion. Check Customer ID");
                return;
            }
            if (confirmationAlert("Delete Customer Confirmation", "Are you sure you want to delete Customer?") == false) {
                return;
            }
            check = true;
            if (check) {
                boolean deletedCustomer = cSystem.deleteCustomer(Integer.parseInt(cID));
                if (deletedCustomer) {
                    tableViewOfCustomersFromDB(cSystem.getAllCustomers());
                    customerVehicleTable.setItems(null);
                    customerVehiclePartTable.setItems(null);
                    customerBookingTable.setItems(null);
                    newCustomerForm();

                }
                else {
                    errorAlert("Can't find Customer record to delete");
                }
            }
            customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
        }
        catch (Exception e) {
        }
    }

    //method for deleting customer from database
    public void deleteTableSelectedCustomerFromDB() throws Exception {
        customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
        try {
            Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
            String cID = Integer.toString(customer.getCustomerID());
            boolean check = false;

            if (cID.equals("")) {
                errorAlert("Customer has not yet been selected for deletion. Check Customer ID");
                return;
            }
            if (confirmationAlert("Delete Customer Confirmation", "Are you sure you want to delete Customer?") == false) {
                return;
            }
            check = true;
            if (check) {
                boolean deletedCustomer = cSystem.deleteCustomer(Integer.parseInt(cID));
                if (deletedCustomer) {
                    tableViewOfCustomersFromDB(cSystem.getAllCustomers());
                    customerVehicleTable.setItems(null);
                    customerVehiclePartTable.setItems(null);
                    customerBookingTable.setItems(null);
                    newCustomerForm();

                }
                else {
                    errorAlert("Can't find Customer record to delete");
                }
            }
        }
        catch (Exception e) {
        }
    }

    public void searchAllCustomersInDB() {
        try {
            customerVehicleTable.setItems(null);
            customerVehiclePartTable.setItems(null);
            customerBookingTable.setItems(null);

            List<Customer> allCustomers = cSystem.getAllCustomers();
            tableViewOfCustomersFromDB(allCustomers);
            customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
        }
        catch (Exception e) {
        }
    }

    public void searchCustomerTypeInDB() {
        customerVehicleTable.setItems(null);
        customerVehiclePartTable.setItems(null);
        customerBookingTable.setItems(null);

        try {
            CustomerType cType = null;
            List<Customer> customers = new ArrayList<Customer>(0);
            if (customerTypeSearch.getSelectionModel().getSelectedItem() != null) {
                if (customerTypeSearch.getSelectionModel().getSelectedItem().toString().equals("Private")) {
                    cType = CustomerType.Private;
                }
                else {
                    cType = CustomerType.Business;
                }
            }
            customers = cSystem.searchCustomerByType(cType);
            tableViewOfCustomersFromDB(customers);
        }
        catch (Exception e) {
        }
    }

    //method for searching for customer in database
    public void searchCustomerInDB() {
        customerVehicleTable.setItems(null);
        customerVehiclePartTable.setItems(null);
        customerBookingTable.setItems(null);
        customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null

        try {
            List<Customer> searchCustomerList = new ArrayList<Customer>(0);
            searchCustomerList = cSystem.searchCustomerByFirstname(customerSearch.getText());
            if (searchCustomerList.size() == 0) {
                searchCustomerList = cSystem.searchCustomerBySurname(customerSearch.getText());
                tableViewOfCustomersFromDB(searchCustomerList);
                if (searchCustomerList.size() == 0) {
                    searchCustomerList = cSystem.searchCustomerByVehicleRegistrationNumber(customerSearch.getText());
                    tableViewOfCustomersFromDB(searchCustomerList);
                    return;
                }
                return;
            }
            tableViewOfCustomersFromDB(searchCustomerList);
        }
        catch (Exception e) {
        }
    }

    //method for showing customer details in table view
    public void tableViewOfCustomersFromDB(List<Customer> searchList) throws Exception {
        try {
            customerTable.setDisable(false);
            tableEntriesCustomer.removeAll(tableEntriesCustomer);

            //arranging default list from newest to oldest customer
            for (int i = searchList.size() - 1; i >= 0; i--) {
                tableEntriesCustomer.add(searchList.get(i));
            }

            customerTableColumnID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerID"));
            customerTableColumnID.setCellFactory(TextFieldTableCell.<Customer, Integer>forTableColumn(new IntegerStringConverter()));

            customerTableColumnFirstname.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerFirstname"));
            customerTableColumnFirstname.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

            customerTableColumnSurname.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerSurname"));
            customerTableColumnSurname.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

            customerTableColumnAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerAddress"));
            customerTableColumnAddress.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

            customerTableColumnPostcode.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerPostcode"));
            customerTableColumnPostcode.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

            customerTableColumnPhone.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerPhone"));
            customerTableColumnPhone.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

            customerTableColumnEmail.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerEmail"));
            customerTableColumnEmail.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

            customerTableColumnType.setCellValueFactory(new PropertyValueFactory<Customer, CustomerType>("customerType"));
            customerTableColumnType.setCellFactory(TextFieldTableCell.<Customer, CustomerType>forTableColumn(new StringConverter<CustomerType>() {
                @Override
                public String toString(CustomerType object) {
                    return object.toString();
                }

                @Override
                public CustomerType fromString(String string) {
                    if (string.equals("Private")) {
                        return CustomerType.Private;
                    }
                    else {
                        return CustomerType.Business;
                    }
                }
            }));

            customerTable.setItems(tableEntriesCustomer);
        }
        catch (Exception e) {
        }
    }

    public void editTableSelectedCustomerFromDB() throws Exception {
        try {
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            if (customer == null) {
                throw new Exception();
            }
            customerID.setText(Integer.toString(customer.getCustomerID()));
            customerFirstname.setText(customer.getCustomerFirstname());
            customerSurname.setText(customer.getCustomerSurname());
            customerAddress.setText(customer.getCustomerAddress());
            customerPostcode.setText(customer.getCustomerPostcode());
            customerPhone.setText(customer.getCustomerPhone());
            customerEmail.setText(customer.getCustomerEmail());
            customerType.setValue(customer.getCustomerType().toString());
            formName.setText("Edit Customer");
            saveCustomerAndAddVehicleButton.setVisible(false);
            saveCustomerButton.setVisible(true);
            clearCustomerButton.setVisible(false);
            deleteCustomerButton.setVisible(true);

            searchCustomerVehicleInDB();//testing to find customer's vehicles
            customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
        }
        catch (Exception e) {
        }
    }

    public void updateCustomerInDB() throws Exception {
        customerVehicleTable.setItems(null);
        customerVehiclePartTable.setItems(null);
        customerBookingTable.setItems(null);

        try {
            String cFirstname = firstLetterUpperCase(customerFirstname.getText());
            String cSurname = firstLetterUpperCase(customerSurname.getText());
            String cAddress = firstLetterUpperCase(customerAddress.getText());
            String cPostcode = customerPostcode.getText().toUpperCase();
            String cPhone = customerPhone.getText();
            String cEmail = customerEmail.getText().toLowerCase();
            CustomerType cType = null;

            boolean checkFields = false;
            checkFields = checkCustomerFieldsForEdit();

            if (customerType.getSelectionModel().getSelectedItem().toString().equals("Private")) {
                cType = CustomerType.Private;
            }
            else if (customerType.getSelectionModel().getSelectedItem().toString().equals("Business")) {
                cType = CustomerType.Business;
            }

            Customer customer = cSystem.getACustomers(Integer.parseInt(customerID.getText()));

            if (checkFields) {
                customer.setCustomerID(Integer.parseInt(customerID.getText()));
                customer.setCustomerFirstname(cFirstname);
                customer.setCustomerSurname(cSurname);
                customer.setCustomerAddress(cAddress);
                customer.setCustomerPostcode(cPostcode);
                customer.setCustomerPhone(cPhone);
                customer.setCustomerEmail(cEmail);
                customer.setCustomerType(cType);

                boolean editedCustomer = cSystem.editCustomer(customer);

                if (editedCustomer) {
                    newCustomerForm();
                    searchAllCustomersInDB();
                }
                else {
                    errorAlert("Cannot update customer. Ensure all fields have been entered correctly.");
                }
            }
            customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
        }
        catch (Exception e) {
        }
    }

    public void newCustomerForm() {
        customerVehicleTable.setItems(null);
        customerVehiclePartTable.setItems(null);
        customerBookingTable.setItems(null);

        clearCustomerFields();
        formName.setText("Add Customer");
        saveCustomerAndAddVehicleButton.setVisible(true);
        saveCustomerButton.setVisible(false);
        clearCustomerButton.setVisible(true);
        deleteCustomerButton.setVisible(false);
        customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
    }

    public void clearCustomerFields() {
        customerVehicleTable.setItems(null);
        customerVehiclePartTable.setItems(null);
        customerBookingTable.setItems(null);

        customerID.clear();
        customerFirstname.clear();
        customerSurname.clear();
        customerAddress.clear();
        customerPostcode.clear();
        customerPhone.clear();
        customerEmail.clear();
        customerType.setValue(null);
        customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
    }

    public void errorAlert(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Message");
        errorAlert.setHeaderText(message);
        errorAlert.showAndWait();
    }

    public boolean confirmationAlert(String title, String message) {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle(title);
        deleteAlert.setHeaderText(message);
        deleteAlert.showAndWait();
        if (deleteAlert.getResult() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }
    }

    public List<Vehicle> searchCustomerVehicleInDB() {
        try {
            List<Vehicle> searchVehicleList = new ArrayList<Vehicle>(0);
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            if (customer == null) {
                throw new Exception();
            }
            searchVehicleList = cSystem.searchCustomerVehicles(customer.getCustomerID());
            searchCustomerBookingInDB();//testing to find customer's bookings for their vehicles
            return searchVehicleList;
        }
        catch (Exception e) {
        }
        return null;
    }

    public List<DiagRepBooking> searchCustomerBookingInDB() {
        try {
            List<DiagRepBooking> searchBookingList = new ArrayList<DiagRepBooking>(0);
            Vehicle vehicle = customerVehicleTable.getSelectionModel().getSelectedItem();
            searchBookingList = cSystem.searchCustomerBookings(vehicle.getVehicleRegNumber());
            return searchBookingList;
        }
        catch (Exception e) {
        }
        return null;
    }

    public List<PartAbstraction> searchCustomerVehiclePartsInstalledInDB() {
        try {
            List<Installation> installationList = new ArrayList<Installation>(0);
            List<PartOccurrence> partOccurrenceList = new ArrayList<PartOccurrence>(0);
            List<PartAbstraction> partAbstractionList = new ArrayList<PartAbstraction>(0);
            List<PartAbstraction> partList = new ArrayList<PartAbstraction>(0);

            Vehicle vehicle = customerVehicleTable.getSelectionModel().getSelectedItem();
            installationList = cSystem.searchInstallationTable(vehicle.getVehicleRegNumber());
            List<Integer> installationIDList = new ArrayList<Integer>(0);
            List<Integer> partAbstractionIDList = new ArrayList<Integer>(0);

            for (int i = 0; i < installationList.size(); i++) {
                installationIDList.add(installationList.get(i).getInstallationID());
            }

            for (int i = 0; i < installationIDList.size(); i++) {
                partOccurrenceList = cSystem.searchPartOccurrenceTable(installationIDList.get(i));
                partAbstractionIDList.add(partOccurrenceList.get(i).getPartAbstractionID());
            }

            for (int i = 0; i < partAbstractionIDList.size(); i++) {
                partAbstractionList = cSystem.searchPartAbstractionTable(partAbstractionIDList.get(i));
                PartAbstraction pa = new PartAbstraction(partAbstractionList.get(i).getPartName(), partAbstractionList.get(i).getPartDescription(), partAbstractionList.get(i).getPartPrice(), partAbstractionList.get(i).getPartStockLevel(), null);
                partList.add(pa);
            }
            return partList;
        }
        catch (Exception e) {
        }
        return null;
    }

    public void initiateNewBooking() {
        try {
            BookingController.getInstance().show();
        }
        catch (Exception e) {
        }
    }

    public void tableViewOfCustomerVehicleFromDB() throws Exception {
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        try {
            customerVehicleTable.setDisable(false);
            tableEntriesVehicle.removeAll(tableEntriesVehicle);
            List<Vehicle> searchList = searchCustomerVehicleInDB();

            //arranging default list from newest to oldest customer vehicle
            for (int i = searchList.size() - 1; i >= 0; i--) {
                tableEntriesVehicle.add(searchList.get(i));
            }

            customerVehicleTableColumnRegistrationNumber.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("vehicleRegNumber"));
            customerVehicleTableColumnRegistrationNumber.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());

            customerVehicleTableColumnType.setCellValueFactory(new PropertyValueFactory<Vehicle, VehicleType>("vehicleType"));
            customerVehicleTableColumnType.setCellFactory(TextFieldTableCell.<Vehicle, VehicleType>forTableColumn(new StringConverter<VehicleType>() {
                @Override
                public String toString(VehicleType object) {
                    return object.toString();
                }

                @Override
                public VehicleType fromString(String string) {
                    if (string.equals("Car")) {
                        return VehicleType.Car;
                    }
                    else if (string.equals("Van")) {
                        return VehicleType.Van;
                    }
                    else {
                        return VehicleType.Truck;
                    }
                }
            }));

            customerVehicleTableColumnModel.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("model"));
            customerVehicleTableColumnModel.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());

            customerVehicleTableColumnManufacturer.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("manufacturer"));
            customerVehicleTableColumnManufacturer.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());

            customerVehicleTableColumnWarranty.setCellValueFactory(new PropertyValueFactory<Vehicle, Boolean>("coveredByWarranty"));
            customerVehicleTableColumnWarranty.setCellFactory(TextFieldTableCell.<Vehicle, Boolean>forTableColumn(new BooleanStringConverter()));

            customerVehicleTable.setItems(tableEntriesVehicle);

            tableViewOfCustomerBookingFromDB();
            tableViewOfCustomerVehiclePartFromDB();//MAY REMOVE
        }
        catch (Exception e) {
        }
    }

    public void tableViewOfCustomerVehiclePartFromDB() throws Exception {
        try {
            customerVehiclePartTable.setDisable(false);
            tableEntriesParts.removeAll(tableEntriesParts);
            List<PartAbstraction> searchList = searchCustomerVehiclePartsInstalledInDB();

            //arranging default list from newest to oldest customer vehicle part
            for (int i = searchList.size() - 1; i >= 0; i--) {
                tableEntriesParts.add(searchList.get(i));
            }

            customerVehiclePartTableColumnName.setCellValueFactory(new PropertyValueFactory<PartAbstraction, String>("partName"));
            customerVehiclePartTableColumnName.setCellFactory(TextFieldTableCell.<PartAbstraction>forTableColumn());

            customerVehiclePartTableColumnDescription.setCellValueFactory(new PropertyValueFactory<PartAbstraction, String>("partDescription"));
            customerVehiclePartTableColumnDescription.setCellFactory(TextFieldTableCell.<PartAbstraction>forTableColumn());

            customerVehiclePartTable.setItems(tableEntriesParts);

            tableViewOfCustomerBookingFromDB();//for populating
        }
        catch (Exception e) {
        }
    }

    public void tableViewOfCustomerBookingFromDB() throws Exception {
        try {
            customerBookingTable.setDisable(false);
            tableEntriesBooking.removeAll(tableEntriesBooking);
            List<DiagRepBooking> searchList = searchCustomerBookingInDB();

            //arranging default list from newest to oldest customer booking
            for (int i = searchList.size() - 1; i >= 0; i--) {
                tableEntriesBooking.add(searchList.get(i));
            }

            customerBookingTableColumnBookingID.setCellValueFactory(p ->
                    new ReadOnlyObjectWrapper<>(p.getValue().getBookingID())
            );

            customerBookingTableColumnVehicleRegistrationNumber.setCellValueFactory(p ->
                    new ReadOnlyObjectWrapper<>(p.getValue().getVehicleRegNumber())
            );

            customerBookingTableColumnRepairDate.setCellValueFactory(p -> {
                ZonedDateTime date = p.getValue().getRepairStart();
                return new ReadOnlyObjectWrapper<>(date == null ? "" : date.toLocalDate().format(dateFormatter));
            });

            customerBookingTableColumnRepairTime.setCellValueFactory(p -> {
                ZonedDateTime repairStart = p.getValue().getRepairStart();
                ZonedDateTime repairEnd = p.getValue().getRepairEnd();
                return new ReadOnlyObjectWrapper<>(
                        repairStart != null ?
                                repairStart.format(timeFormatter) + " - " + repairEnd.format(timeFormatter)
                                : "");
            });

            customerBookingTableColumnBill.setCellValueFactory(p ->
                    new ReadOnlyObjectWrapper<>(p.getValue().getBillAmount())
            );

            customerBookingTableColumnSettlementStatus.setCellValueFactory(p ->
                    new ReadOnlyObjectWrapper<>(p.getValue().getBillSettled() ? "Yes" : "No")
            );

            customerBookingTable.setItems(tableEntriesBooking);
        }
        catch (Exception e) {
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchAllCustomersInDB();
    }

    public boolean checkCustomerFields() {
        try {
            if ((customerFirstname.getText().equals("")) && (customerSurname.getText().equals("")) && (customerAddress.getText().equals("")) && (customerPostcode.getText().equals("")) && (customerPhone.getText().equals("")) && (customerEmail.getText().equals("")) && (customerType.getSelectionModel().getSelectedItem() == null)) {
                errorAlert("All fields are empty. Add Customer record");
                return false;
            }
            if (customerFirstname.getText().equals("")) {
                errorAlert("'Customer First name' field is empty. Enter First name");
                return false;
            }
            if (customerFirstname.getText().length() < 2) {
                errorAlert("'Customer First name' is too short. Minimum is 2 characters");
                return false;
            }
            if (customerFirstname.getText().length() > 30) {
                errorAlert("'Customer First name' is too long. Maximum is 30 characters");
                return false;
            }
            if ((customerFirstname.getText().contains("`")) || (customerFirstname.getText().contains("¬")) || (customerFirstname.getText().contains("!")) || (customerFirstname.getText().contains("\"")) || (customerFirstname.getText().contains("£")) || (customerFirstname.getText().contains("$")) || (customerFirstname.getText().contains("%")) || (customerFirstname.getText().contains("^")) || (customerFirstname.getText().contains("&")) || (customerFirstname.getText().contains("*")) || (customerFirstname.getText().contains("(")) || (customerFirstname.getText().contains(")")) || (customerFirstname.getText().contains("_")) || (customerFirstname.getText().contains("+")) || (customerFirstname.getText().contains("=")) || (customerFirstname.getText().contains("{")) || (customerFirstname.getText().contains("}")) || (customerFirstname.getText().contains("[")) || (customerFirstname.getText().contains("]")) || (customerFirstname.getText().contains(":")) || (customerFirstname.getText().contains(";")) || (customerFirstname.getText().contains("@")) || (customerFirstname.getText().contains("'")) || (customerFirstname.getText().contains("~")) || (customerFirstname.getText().contains("#")) || (customerFirstname.getText().contains("<")) || (customerFirstname.getText().contains(">")) || (customerFirstname.getText().contains(",")) || (customerFirstname.getText().contains(".")) || (customerFirstname.getText().contains("?")) || (customerFirstname.getText().contains("/")) || (customerFirstname.getText().contains("|")) || (customerFirstname.getText().contains("1")) || (customerFirstname.getText().contains("2")) || (customerFirstname.getText().contains("3")) || (customerFirstname.getText().contains("4")) || (customerFirstname.getText().contains("5")) || (customerFirstname.getText().contains("6")) || (customerFirstname.getText().contains("7")) || (customerFirstname.getText().contains("8")) || (customerFirstname.getText().contains("9")) || (customerFirstname.getText().contains("0"))) {
                errorAlert("'Customer First name' cannot have numbers or symbols except '-'. Only English Alphabet letters a-z or A-Z are allowed");
                return false;
            }

            if (customerSurname.getText().equals("")) {
                errorAlert("'Customer Surname' field is empty. Enter First name");
                return false;
            }
            if (customerSurname.getText().length() < 2) {
                errorAlert("'Customer Surname' is too short. Minimum is 2 characters");
                return false;
            }
            if (customerSurname.getText().length() > 30) {
                errorAlert("'Customer Surname' is too long. Maximum is 30 characters");
                return false;
            }
            if ((customerSurname.getText().contains("`")) || (customerSurname.getText().contains("¬")) || (customerSurname.getText().contains("!")) || (customerSurname.getText().contains("\"")) || (customerSurname.getText().contains("£")) || (customerSurname.getText().contains("$")) || (customerSurname.getText().contains("%")) || (customerSurname.getText().contains("^")) || (customerSurname.getText().contains("&")) || (customerSurname.getText().contains("*")) || (customerSurname.getText().contains("(")) || (customerSurname.getText().contains(")")) || (customerSurname.getText().contains("_")) || (customerSurname.getText().contains("+")) || (customerSurname.getText().contains("=")) || (customerSurname.getText().contains("{")) || (customerSurname.getText().contains("}")) || (customerSurname.getText().contains("[")) || (customerSurname.getText().contains("]")) || (customerSurname.getText().contains(":")) || (customerSurname.getText().contains(";")) || (customerSurname.getText().contains("@")) || (customerSurname.getText().contains("'")) || (customerSurname.getText().contains("~")) || (customerSurname.getText().contains("#")) || (customerSurname.getText().contains("<")) || (customerSurname.getText().contains(">")) || (customerSurname.getText().contains(",")) || (customerSurname.getText().contains(".")) || (customerSurname.getText().contains("?")) || (customerSurname.getText().contains("/")) || (customerSurname.getText().contains("|")) || (customerSurname.getText().contains("1")) || (customerSurname.getText().contains("2")) || (customerSurname.getText().contains("3")) || (customerSurname.getText().contains("4")) || (customerSurname.getText().contains("5")) || (customerSurname.getText().contains("6")) || (customerSurname.getText().contains("7")) || (customerSurname.getText().contains("8")) || (customerSurname.getText().contains("9")) || (customerSurname.getText().contains("0"))) {
                errorAlert("'Customer Surname' cannot have numbers or symbols except '-'. Only English Alphabet letters a-z or A-Z are allowed");
                return false;
            }

            if (customerAddress.getText().equals("")) {
                errorAlert("'Customer Address' field is empty. Enter Address");
                return false;
            }
            if (customerAddress.getText().length() < 4) {
                errorAlert("'Customer Address' field is too short. Minimum is 4 characters");
                return false;
            }
            if (customerAddress.getText().length() > 50) {
                errorAlert("'Customer Address' field is too long. Maximum is 50 characters");
                return false;
            }

            if (customerPostcode.getText().equals("")) {
                errorAlert("'Customer Postcode' field is empty. Enter postcode");
                return false;
            }
            if (customerPostcode.getText().length() < 6) {
                errorAlert("'Customer Postcode' field is too short. Enter valid UK Postcode including required spacing");
                return false;
            }
            if (customerPostcode.getText().length() > 8) {
                errorAlert("'Customer Postcode' field is too long. Enter valid UK Postcode including required spacing");
                return false;
            }

            if (customerPhone.getText().equals("")) {
                errorAlert("'Customer Phone' number field is empty. Enter Phone number");
                return false;
            }
            if (customerPhone.getText().length() < 7) {
                errorAlert("'Customer Phone' number field is too short. Enter valid Phone number between 7 and 15 digits");
                return false;
            }
            if (customerPhone.getText().length() > 15) {
                errorAlert("'Customer Phone' number field is too long. Enter valid Phone number between 7 and 15 digits");
                return false;
            }
            if ((customerPhone.getText().equals("`")) || (customerPhone.getText().equals("¬")) || (customerPhone.getText().equals("!")) || (customerPhone.getText().equals('"')) || (customerPhone.getText().equals("£")) || (customerPhone.getText().equals("$")) || (customerPhone.getText().equals("%")) || (customerPhone.getText().equals("^")) || (customerPhone.getText().equals("&")) || (customerPhone.getText().equals("*")) || (customerPhone.getText().equals("(")) || (customerPhone.getText().equals(")")) || (customerPhone.getText().equals("_")) || (customerPhone.getText().equals("+")) || (customerPhone.getText().equals("=")) || (customerPhone.getText().equals("{")) || (customerPhone.getText().equals("}")) || (customerPhone.getText().equals("[")) || (customerPhone.getText().equals("]")) || (customerPhone.getText().equals(":")) || (customerPhone.getText().equals(";")) || (customerPhone.getText().equals("@")) || (customerPhone.getText().equals("'")) || (customerPhone.getText().equals("~")) || (customerPhone.getText().equals("#")) || (customerPhone.getText().equals("<")) || (customerPhone.getText().equals(">")) || (customerPhone.getText().equals(",")) || (customerPhone.getText().equals(".")) || (customerPhone.getText().equals("?")) || (customerPhone.getText().equals("/")) || (customerPhone.getText().equals("|"))) {
                errorAlert("'Customer Phone' number field is invalid . No symbol is allowed except '+' at the start. Number digits from 0-9 are allowed");
                return false;
            }
            if ((customerPhone.getText().substring(0, 1).equals("1")) || (customerPhone.getText().substring(0, 1).equals("2")) || (customerPhone.getText().substring(0, 1).equals("3")) || (customerPhone.getText().substring(0, 1).equals("4")) || (customerPhone.getText().substring(0, 1).equals("5")) || (customerPhone.getText().substring(0, 1).equals("6")) || (customerPhone.getText().substring(0, 1).equals("7")) || (customerPhone.getText().substring(0, 1).equals("8")) || (customerPhone.getText().substring(0, 1).equals("9"))) {
                errorAlert("'Customer Phone' number field can only start with '+' or '0' NOT 1-9");
                return false;
            }
            String customerPhoneNoPlus = customerPhone.getText().replaceAll("\\+", "");
            if ((!customerPhoneNoPlus.matches("[0-9]+"))) {
                errorAlert("'Customer Phone' number field is invalid. Letters a-z or A-Z are not allowed. No symbol is allowed except '+' at the start. Number digits from 0-9 are allowed");
                return false;
            }
            for (int i = 1; i < customerPhone.getText().length(); i++) {
                if (customerPhone.getText().substring(i, i + 1).equals("+")) {
                    errorAlert("'Customer Phone' number field is invalid. '+' symbol can only be optionally used at the start");
                    return false;
                }
            }

            if (customerEmail.getText().equals("")) {
                errorAlert("'Customer Email' address field is empty. Enter Email address");
                return false;
            }
            if (customerEmail.getText().length() < 5) {
                errorAlert("'Customer Email' address field is too short. Enter valid Email address between 5 to 30 characters long");
                return false;
            }
            if (customerEmail.getText().length() > 30) {
                errorAlert("'Customer Email' address field is too long. Enter valid Email address between 5 to 30 characters long");
                return false;
            }
            if (!customerEmail.getText().contains("@")) {
                errorAlert("'Customer Email' address field does not contain '@' symbol. Enter valid Email address containing '@' and period/dot symbol");
                return false;
            }
            if (!customerEmail.getText().contains(".")) {
                errorAlert("'Customer Email' address field does not contain '.' symbol. Enter valid Email address containing period/dot '.' and '@' symbol");
                return false;
            }

            int emailAtSignPosition = customerEmail.getText().indexOf("@");
            String emailAtSign = customerEmail.getText().substring(emailAtSignPosition);

            if (!customerEmail.getText().matches("([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})")) {
                errorAlert("Invalid email");
                return false;
            }
            boolean verifyEmail = verifyCustomerDoesNotExist(customerEmail.getText());
            if (!verifyEmail) {
                errorAlert("Customer record already exists. Enter a new Email address");
                return false;
            }

            if (customerType.getSelectionModel().getSelectedItem() == null) {
                errorAlert("Pick Customer type");
                return false;
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean checkCustomerFieldsForEdit() {
        try {
            if ((customerFirstname.getText().equals("")) && (customerSurname.getText().equals("")) && (customerAddress.getText().equals("")) && (customerPostcode.getText().equals("")) && (customerPhone.getText().equals("")) && (customerEmail.getText().equals("")) && (customerType.getSelectionModel().getSelectedItem() == null)) {
                errorAlert("All fields are empty. Add Customer record");
                return false;
            }
            if (customerFirstname.getText().equals("")) {
                errorAlert("'Customer First name' field is empty. Enter First name");
                return false;
            }
            if (customerFirstname.getText().length() < 2) {
                errorAlert("'Customer First name' is too short. Minimum is 2 characters");
                return false;
            }
            if (customerFirstname.getText().length() > 30) {
                errorAlert("'Customer First name' is too long. Maximum is 30 characters");
                return false;
            }
            if ((customerFirstname.getText().contains("`")) || (customerFirstname.getText().contains("¬")) || (customerFirstname.getText().contains("!")) || (customerFirstname.getText().contains("\"")) || (customerFirstname.getText().contains("£")) || (customerFirstname.getText().contains("$")) || (customerFirstname.getText().contains("%")) || (customerFirstname.getText().contains("^")) || (customerFirstname.getText().contains("&")) || (customerFirstname.getText().contains("*")) || (customerFirstname.getText().contains("(")) || (customerFirstname.getText().contains(")")) || (customerFirstname.getText().contains("_")) || (customerFirstname.getText().contains("+")) || (customerFirstname.getText().contains("=")) || (customerFirstname.getText().contains("{")) || (customerFirstname.getText().contains("}")) || (customerFirstname.getText().contains("[")) || (customerFirstname.getText().contains("]")) || (customerFirstname.getText().contains(":")) || (customerFirstname.getText().contains(";")) || (customerFirstname.getText().contains("@")) || (customerFirstname.getText().contains("'")) || (customerFirstname.getText().contains("~")) || (customerFirstname.getText().contains("#")) || (customerFirstname.getText().contains("<")) || (customerFirstname.getText().contains(">")) || (customerFirstname.getText().contains(",")) || (customerFirstname.getText().contains(".")) || (customerFirstname.getText().contains("?")) || (customerFirstname.getText().contains("/")) || (customerFirstname.getText().contains("|")) || (customerFirstname.getText().contains("1")) || (customerFirstname.getText().contains("2")) || (customerFirstname.getText().contains("3")) || (customerFirstname.getText().contains("4")) || (customerFirstname.getText().contains("5")) || (customerFirstname.getText().contains("6")) || (customerFirstname.getText().contains("7")) || (customerFirstname.getText().contains("8")) || (customerFirstname.getText().contains("9")) || (customerFirstname.getText().contains("0"))) {
                errorAlert("'Customer First name' cannot have numbers or symbols except '-'. Only English Alphabet letters a-z or A-Z are allowed");
                return false;
            }

            if (customerSurname.getText().equals("")) {
                errorAlert("'Customer Surname' field is empty. Enter First name");
                return false;
            }
            if (customerSurname.getText().length() < 2) {
                errorAlert("'Customer Surname' is too short. Minimum is 2 characters");
                return false;
            }
            if (customerSurname.getText().length() > 30) {
                errorAlert("'Customer Surname' is too long. Maximum is 30 characters");
                return false;
            }
            if ((customerSurname.getText().contains("`")) || (customerSurname.getText().contains("¬")) || (customerSurname.getText().contains("!")) || (customerSurname.getText().contains("\"")) || (customerSurname.getText().contains("£")) || (customerSurname.getText().contains("$")) || (customerSurname.getText().contains("%")) || (customerSurname.getText().contains("^")) || (customerSurname.getText().contains("&")) || (customerSurname.getText().contains("*")) || (customerSurname.getText().contains("(")) || (customerSurname.getText().contains(")")) || (customerSurname.getText().contains("_")) || (customerSurname.getText().contains("+")) || (customerSurname.getText().contains("=")) || (customerSurname.getText().contains("{")) || (customerSurname.getText().contains("}")) || (customerSurname.getText().contains("[")) || (customerSurname.getText().contains("]")) || (customerSurname.getText().contains(":")) || (customerSurname.getText().contains(";")) || (customerSurname.getText().contains("@")) || (customerSurname.getText().contains("'")) || (customerSurname.getText().contains("~")) || (customerSurname.getText().contains("#")) || (customerSurname.getText().contains("<")) || (customerSurname.getText().contains(">")) || (customerSurname.getText().contains(",")) || (customerSurname.getText().contains(".")) || (customerSurname.getText().contains("?")) || (customerSurname.getText().contains("/")) || (customerSurname.getText().contains("|")) || (customerSurname.getText().contains("1")) || (customerSurname.getText().contains("2")) || (customerSurname.getText().contains("3")) || (customerSurname.getText().contains("4")) || (customerSurname.getText().contains("5")) || (customerSurname.getText().contains("6")) || (customerSurname.getText().contains("7")) || (customerSurname.getText().contains("8")) || (customerSurname.getText().contains("9")) || (customerSurname.getText().contains("0"))) {
                errorAlert("'Customer Surname' cannot have numbers or symbols except '-'. Only English Alphabet letters a-z or A-Z are allowed");
                return false;
            }

            if (customerAddress.getText().equals("")) {
                errorAlert("'Customer Address' field is empty. Enter Address");
                return false;
            }
            if (customerAddress.getText().length() < 4) {
                errorAlert("'Customer Address' field is too short. Minimum is 4 characters");
                return false;
            }
            if (customerAddress.getText().length() > 50) {
                errorAlert("'Customer Address' field is too long. Maximum is 50 characters");
                return false;
            }

            if (customerPostcode.getText().equals("")) {
                errorAlert("'Customer Postcode' field is empty. Enter postcode");
                return false;
            }
            if (customerPostcode.getText().length() < 6) {
                errorAlert("'Customer Postcode' field is too short. Enter valid UK Postcode including required spacing");
                return false;
            }
            if (customerPostcode.getText().length() > 8) {
                errorAlert("'Customer Postcode' field is too long. Enter valid UK Postcode including required spacing");
                return false;
            }

            if (customerPhone.getText().equals("")) {
                errorAlert("'Customer Phone' number field is empty. Enter Phone number");
                return false;
            }
            if (customerPhone.getText().length() < 7) {
                errorAlert("'Customer Phone' number field is too short. Enter valid Phone number between 7 and 15 digits");
                return false;
            }
            if (customerPhone.getText().length() > 15) {
                errorAlert("'Customer Phone' number field is too long. Enter valid Phone number between 7 and 15 digits");
                return false;
            }
            if ((customerPhone.getText().equals("`")) || (customerPhone.getText().equals("¬")) || (customerPhone.getText().equals("!")) || (customerPhone.getText().equals('"')) || (customerPhone.getText().equals("£")) || (customerPhone.getText().equals("$")) || (customerPhone.getText().equals("%")) || (customerPhone.getText().equals("^")) || (customerPhone.getText().equals("&")) || (customerPhone.getText().equals("*")) || (customerPhone.getText().equals("(")) || (customerPhone.getText().equals(")")) || (customerPhone.getText().equals("_")) || (customerPhone.getText().equals("+")) || (customerPhone.getText().equals("=")) || (customerPhone.getText().equals("{")) || (customerPhone.getText().equals("}")) || (customerPhone.getText().equals("[")) || (customerPhone.getText().equals("]")) || (customerPhone.getText().equals(":")) || (customerPhone.getText().equals(";")) || (customerPhone.getText().equals("@")) || (customerPhone.getText().equals("'")) || (customerPhone.getText().equals("~")) || (customerPhone.getText().equals("#")) || (customerPhone.getText().equals("<")) || (customerPhone.getText().equals(">")) || (customerPhone.getText().equals(",")) || (customerPhone.getText().equals(".")) || (customerPhone.getText().equals("?")) || (customerPhone.getText().equals("/")) || (customerPhone.getText().equals("|"))) {
                errorAlert("'Customer Phone' number field is invalid . No symbol is allowed except '+' at the start. Number digits from 0-9 are allowed");
                return false;
            }
            if ((customerPhone.getText().substring(0, 1).equals("1")) || (customerPhone.getText().substring(0, 1).equals("2")) || (customerPhone.getText().substring(0, 1).equals("3")) || (customerPhone.getText().substring(0, 1).equals("4")) || (customerPhone.getText().substring(0, 1).equals("5")) || (customerPhone.getText().substring(0, 1).equals("6")) || (customerPhone.getText().substring(0, 1).equals("7")) || (customerPhone.getText().substring(0, 1).equals("8")) || (customerPhone.getText().substring(0, 1).equals("9"))) {
                errorAlert("'Customer Phone' number field can only start with '+' or '0' NOT 1-9");
                return false;
            }
            String customerPhoneNoPlus = customerPhone.getText().replaceAll("\\+", "");
            if ((!customerPhoneNoPlus.matches("[0-9]+"))) {
                errorAlert("'Customer Phone' number field is invalid. Letters a-z or A-Z are not allowed. No symbol is allowed except '+' at the start. Number digits from 0-9 are allowed");
                return false;
            }
            for (int i = 1; i < customerPhone.getText().length(); i++) {
                if (customerPhone.getText().substring(i, i + 1).equals("+")) {
                    errorAlert("'Customer Phone' number field is invalid. '+' symbol can only be optionally used at the start");
                    return false;
                }
            }

            if (customerEmail.getText().equals("")) {
                errorAlert("'Customer Email' address field is empty. Enter Email address");
                return false;
            }
            if (customerEmail.getText().length() < 5) {
                errorAlert("'Customer Email' address field is too short. Enter valid Email address between 5 to 30 characters long");
                return false;
            }
            if (customerEmail.getText().length() > 30) {
                errorAlert("'Customer Email' address field is too long. Enter valid Email address between 5 to 30 characters long");
                return false;
            }
            if (!customerEmail.getText().contains("@")) {
                errorAlert("'Customer Email' address field does not contain '@' symbol. Enter valid Email address containing '@' and period/dot symbol");
                return false;
            }
            if (!customerEmail.getText().contains(".")) {
                errorAlert("'Customer Email' address field does not contain '.' symbol. Enter valid Email address containing period/dot '.' and '@' symbol");
                return false;
            }

            int emailAtSignPosition = customerEmail.getText().indexOf("@");
            String emailAtSign = customerEmail.getText().substring(emailAtSignPosition);

            if (!customerEmail.getText().matches("([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})")) {
                errorAlert("Invalid email");
                return false;
            }

            if (customerType.getSelectionModel().getSelectedItem() == null) {
                errorAlert("Pick Customer type");
                return false;
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean verifyCustomerDoesNotExist(String email) {
        try {
            List<Customer> customerList = cSystem.getAllCustomers();
            for (int i = 0; i < customerList.size(); i++) {
                if (customerList.get(i).getCustomerEmail().equals(email)) {
                    return false;
                }
            }
            return true;
        }
        catch (Exception e) {
        }
        return false;
    }

    public String firstLetterUpperCase(String words) {
        //String words = "";
        char[] wordsArray = words.toCharArray();
        wordsArray[0] = Character.toUpperCase(wordsArray[0]);
        for (int i = 1; i < wordsArray.length; i++) {
            if (Character.isWhitespace(wordsArray[i - 1])) {
                wordsArray[i] = Character.toUpperCase(wordsArray[i]);
            }
        }
        words = String.valueOf(wordsArray);
        return words;
    }
}
