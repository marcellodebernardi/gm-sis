package controllers.customer;

import controllers.booking.BookingController;
import controllers.login.LoginController;
import controllers.user.UserController;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.stage.WindowEvent;
import javafx.util.*;
import javafx.fxml.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.lang.*;
import java.text.ParseException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
//import logic.*;
import logic.criterion.Criterion;
import logic.criterion.CriterionOperator;
import logic.customer.CustomerSystem;
import logic.vehicle.*;
import logic.booking.*;
import logic.criterion.Criterion;
import domain.*;
import domain.DiagRepBooking;
import persistence.DatabaseRepository;
import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;


/**
 * Created by EBUBECHUKWU on 19/02/2017.
 */
public class CustomerController implements Initializable
{
    private static CustomerController instance;

    //DatabaseRepository db = DatabaseRepository.getInstance();
    @FXML
    public CustomerSystem cSystem = CustomerSystem.getInstance();

    final ObservableList tableEntriesCustomer = FXCollections.observableArrayList();
    final ObservableList tableEntriesVehicle = FXCollections.observableArrayList();
    final ObservableList tableEntriesBooking = FXCollections.observableArrayList();
    final ObservableList tableEntriesParts = FXCollections.observableArrayList();
    final ObservableList comboEntries = FXCollections.observableArrayList();
    final ObservableList vehicleCustomerID = FXCollections.observableArrayList();



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
    private Button deleteCustomerConfirmationYes = new Button();
    @FXML
    private Button deleteCustomerConfirmationNo = new Button();


    ////for 'AddCustomerVehiclePopupView.fxml' instance variables
    Stage addVehicleStage;

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


    public static CustomerController getInstance()
    {
        if (instance == null) instance = new CustomerController();
        return instance;
    }

    //method for adding customer to database
    public void addCustomerToDB() throws Exception
    {
        try
        {
            //initialising variables
            String cFirstname = customerFirstname.getText();
            String cSurname = customerSurname.getText();
            String cAddress = customerAddress.getText();
            String cPostcode = customerPostcode.getText();
            String cPhone = customerPhone.getText();
            String cEmail = customerEmail.getText();
            CustomerType cType = null;
            boolean checkFields = false;

            checkFields = checkCustomerFields();

            if(customerType.getSelectionModel().getSelectedItem().toString().equals("Private"))
            {
                cType = CustomerType.Private;
            }
            else if(customerType.getSelectionModel().getSelectedItem().toString().equals("Business"))
            {
                cType = CustomerType.Business;
            }

            if(checkFields)
            {
                boolean addedCustomer = cSystem.addCustomer(cFirstname, cSurname, cAddress, cPostcode, cPhone, cEmail, cType);
                if(addedCustomer)
                {
                    //errorAlert("SUCCESSFULLY ADDED!!!");

                    tableViewOfCustomersFromDB(cSystem.getAllCustomers());


                    if(addVehicleStage != null)
                    {
                        if(addVehicleStage.isShowing())
                        {
                            errorAlert("Vehicle window is already open");
                            addVehicleStage.setAlwaysOnTop(true);
                            return;
                        }
                    }
                    else
                    {
                        newCustomerForm();

                        CustomerVehicleController.getInstance().showVehiclePopup();
                    }

                }
                else
                {
                    errorAlert("Cannot add customer. Ensure all fields have been entered correctly.");
                }
                customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
            }
            else
            {
                //error message if checkFields=false meaning one or more Customer fields were invalid
                errorAlert("Cannot add customer. Ensure all fields have been entered correctly.");
            }
        }
        catch(Exception e)
        {
            System.out.println("Add Customer Error");
            e.printStackTrace();
        }
    }


    //method for deleting customer from database
    public void deleteCustomerFromDB() throws Exception
    {
        try
        {
            String cID = customerID.getText();

            boolean check = false;

            if(cID.equals(""))
            {
                errorAlert("Customer has not yet been selected for deletion. Check Customer ID");
                return;
            }
            if(confirmationAlert("Delete Customer Confirmation", "Are you sure you want to delete Customer?") == false)
            {
                return;
            }
            check = true;
            if(check) {
                boolean deletedCustomer = cSystem.deleteCustomer(Integer.parseInt(cID));
                if (deletedCustomer) {
                    //errorAlert("Customer has been deleted");

                    tableViewOfCustomersFromDB(cSystem.getAllCustomers());

                    tableViewOfCustomerVehicleFromDB();
                    tableViewOfCustomerVehiclePartFromDB();
                    tableViewOfCustomerBookingFromDB();

                    newCustomerForm();

                } else {
                    errorAlert("Can't find Customer record to delete");
                }
            }
            customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
        }
        catch(Exception e)
        {
            System.out.println("Delete Customer Error");
            e.printStackTrace();
        }
    }


    //method for deleting customer from database
    public void deleteTableSelectedCustomerFromDB() throws Exception
    {
        customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
        try
        {
            Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
            String cID = Integer.toString(customer.getCustomerID());

            boolean check = false;

            if(cID.equals(""))
            {
                errorAlert("Customer has not yet been selected for deletion. Check Customer ID");
                return;
            }
            if(confirmationAlert("Delete Customer Confirmation", "Are you sure you want to delete Customer?") == false)
            {
                return;
            }
            check = true;
            if(check) {
                boolean deletedCustomer = cSystem.deleteCustomer(Integer.parseInt(cID));
                if (deletedCustomer) {
                    //errorAlert("Customer has been deleted");

                    tableViewOfCustomersFromDB(cSystem.getAllCustomers());

                    tableViewOfCustomerVehicleFromDB();
                    tableViewOfCustomerVehiclePartFromDB();
                    tableViewOfCustomerBookingFromDB();

                    newCustomerForm();

                } else {
                    errorAlert("Can't find Customer record to delete");
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Delete Customer Error");
            e.printStackTrace();
        }
    }


    public void searchAllCustomersInDB()
    {
        try
        {
            List<Customer> allCustomers = cSystem.getAllCustomers();
            tableViewOfCustomersFromDB(allCustomers);
            customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
        }
        catch (Exception e)
        {
            System.out.println("Search All Customers Error");
            e.printStackTrace();
        }
    }

    public void searchCustomerTypeInDB()
    {
        try
        {
            CustomerType cType = null;
            List<Customer> customers = new ArrayList<Customer>(0);
            if(customerTypeSearch.getSelectionModel().getSelectedItem() != null)
            {
                if(customerTypeSearch.getSelectionModel().getSelectedItem().toString().equals("Private"))
                {
                    cType = CustomerType.Private;
                }
                else
                {
                    cType = CustomerType.Business;
                }
            }
            customers = cSystem.searchCustomerByType(cType);
            tableViewOfCustomersFromDB(customers);

        }
        catch(Exception e)
        {
            System.out.println("Search by Customer Type Error");
            e.printStackTrace();
        }
    }

    //method for searching for customer in database
    public void searchCustomerInDB()
    {
        customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
        try
        {
            List<Customer> searchCustomerList = new ArrayList<Customer>(0);
            searchCustomerList = cSystem.searchCustomerByFirstname(customerSearch.getText());
            if(searchCustomerList.size()==0)
            {
                searchCustomerList = cSystem.searchCustomerBySurname(customerSearch.getText());
                tableViewOfCustomersFromDB(searchCustomerList);
                if(searchCustomerList.size()==0)
                {
                    searchCustomerList = cSystem.searchCustomerByVehicleRegistrationNumber(customerSearch.getText());
                    tableViewOfCustomersFromDB(searchCustomerList);
                    return;
                }
                return;
            }
            tableViewOfCustomersFromDB(searchCustomerList);
        }
        catch(Exception e)
        {
            System.out.println("Search Customer Error");
            e.printStackTrace();
        }
    }


    //method for showing customer details in table view
    public void tableViewOfCustomersFromDB(List<Customer> searchList) throws Exception
    {
        try
        {
            //Customer customers = customerTable.getSelectionModel().getSelectedItem();

            customerTable.setDisable(false);
            tableEntriesCustomer.removeAll(tableEntriesCustomer);

            //arranging default list from newest to oldest customer
            for(int i=searchList.size()-1; i>=0; i--)
            {
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
                    if(string.equals("Private"))
                    {
                        return CustomerType.Private;
                    }
                    else
                    {
                        return CustomerType.Business;
                    }
                }
            }));

            customerTable.setItems(tableEntriesCustomer);
            //searchCustomerVehicleInDB();//testing to find customer's vehicles
        }
        catch(Exception e)
        {
            System.out.println("Customer Table view Error");
            e.printStackTrace();
        }
    }


    public void editTableSelectedCustomerFromDB() throws Exception
    {
        try
        {
            //Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            if(customer == null)
            {
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
        catch(Exception e)
        {
            System.out.println("Edit Customer Error");
            e.printStackTrace();
        }
    }

    public void updateCustomerInDB() throws Exception
    {
        try
        {
            String cFirstname = customerFirstname.getText();
            String cSurname = customerSurname.getText();
            String cAddress = customerAddress.getText();
            String cPostcode = customerPostcode.getText();
            String cPhone = customerPhone.getText();
            String cEmail = customerEmail.getText();
            CustomerType cType = null;

            boolean checkFields = false;

            checkFields = checkCustomerFields();

            if(customerType.getSelectionModel().getSelectedItem().toString().equals("Private"))
            {
                cType = CustomerType.Private;
            }
            else if(customerType.getSelectionModel().getSelectedItem().toString().equals("Business"))
            {
                cType = CustomerType.Business;
            }

            Customer customer = cSystem.getACustomers(Integer.parseInt(customerID.getText()));

            if(checkFields)
            {
                customer.setCustomerID(Integer.parseInt(customerID.getText()));
                customer.setCustomerFirstname(cFirstname);
                customer.setCustomerSurname(cSurname);
                customer.setCustomerAddress(cAddress);
                customer.setCustomerPostcode(cPostcode);
                customer.setCustomerPhone(cPhone);
                customer.setCustomerEmail(cEmail);
                customer.setCustomerType(cType);

                boolean editedCustomer = cSystem.editCustomer(customer);

                if(editedCustomer)
                {
                    //errorAlert("SUCCESSFULLY UPDATED CUSTOMER'S DETAIL!!!");
                    newCustomerForm();
                    searchAllCustomersInDB();
                }
                else
                {
                    errorAlert("Cannot update customer. Ensure all fields have been entered correctly.");
                }
            }
            customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
        }
        catch(Exception e)
        {
            System.out.println("Update Customer Error");
            e.printStackTrace();
        }
    }

    public void newCustomerForm()
    {
        clearCustomerFields();
        formName.setText("Add Customer");
        saveCustomerAndAddVehicleButton.setVisible(true);
        saveCustomerButton.setVisible(false);
        clearCustomerButton.setVisible(true);
        deleteCustomerButton.setVisible(false);
        customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
    }

    public void clearCustomerFields()
    {
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



    public void errorAlert(String message)
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Message");
        errorAlert.setHeaderText(message);
        errorAlert.showAndWait();
    }


    public boolean confirmationAlert(String title, String message)
    {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle(title);
        deleteAlert.setHeaderText(message);
        deleteAlert.showAndWait();
        if(deleteAlert.getResult() == ButtonType.OK)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public List<Vehicle> searchCustomerVehicleInDB()
    {
        try
        {
            List<Vehicle> searchVehicleList = new ArrayList<Vehicle>(0);
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            if(customer == null)
            {
                throw new Exception();
            }
            searchVehicleList = cSystem.searchCustomerVehicles(customer.getCustomerID());

            searchCustomerBookingInDB();//testing to find customer's bookings for their vehicles

            return searchVehicleList;
        }
        catch(Exception e)
        {
            System.out.println("Search Customer Vehicle Error");
            e.printStackTrace();
        }
        return null;
    }

    public List<DiagRepBooking> searchCustomerBookingInDB()
    {
        try
        {
            List<DiagRepBooking> searchBookingList = new ArrayList<DiagRepBooking>(0);
            List<Vehicle> searchVehicleList = new ArrayList<Vehicle>(0);
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            searchVehicleList = cSystem.searchCustomerVehicles(customer.getCustomerID());
            for(int i = 0; i < searchVehicleList.size(); i++)
            {
                searchBookingList = cSystem.searchCustomerBookings(searchVehicleList.get(i).getVehicleRegNumber());
            }

            return searchBookingList;
        }
        catch(Exception e)
        {
            System.out.println("Search Customer Booking Error");
            e.printStackTrace();
        }
        return null;
    }

    public List<PartAbstraction> searchCustomerVehiclePartsInstalledInDB()
    {
        try
        {
            List<PartAbstraction> searchPartList = new ArrayList<PartAbstraction>(0);
            List<Installation> searchInstallationList = new ArrayList<Installation>(0);
            List<PartOccurrence> searchPartOccurrenceList = new ArrayList<PartOccurrence>(0);

            Vehicle vehicle = customerVehicleTable.getSelectionModel().getSelectedItem();
            searchInstallationList = cSystem.searchInstallationTable(vehicle.getVehicleRegNumber());
            System.out.println("Installation: " + searchInstallationList.size());//testing searchInstallationList

            for(int i=0; i<searchInstallationList.size(); i++)
            {
                searchPartOccurrenceList = cSystem.searchPartOccurrenceTable(searchInstallationList.get(i).getInstallationID());
                System.out.println("Part Occurrence " + i + ": " + searchPartOccurrenceList.size());//testing searchPartOccurrenceList
            }
            System.out.println("Part Occurrence: " + searchPartOccurrenceList.size());//testing searchPartOccurrenceList

            for(int i=0; i<searchPartOccurrenceList.size(); i++)
            {
                searchPartList = cSystem.searchPartAbstractionTable(searchPartOccurrenceList.get(i).getPartAbstractionID());
                System.out.println("Part Abstraction " + i + searchPartList.size());//testing searchPartList
            }
            System.out.print("Part Abstraction: " + searchPartList.size());//testing searchPartList

            return searchPartList;

        }
        catch(Exception e)
        {
            System.out.println("Search Customer Vehicle Part Error");
            e.printStackTrace();
        }
        return null;
    }

    public void initiateNewBooking()
    {
        try
        {
            BookingController.getInstance().show();
        }
        catch(Exception e)
        {
            System.out.println("Initiate new booking Error");
            e.printStackTrace();
        }
    }

    public void saveVehicleAndMakeBooking()
    {
        try
        {
            BookingController.getInstance().show();
        }
        catch(Exception e)
        {
            System.out.println("Save and Make Booking Error");
            e.printStackTrace();
        }
    }

    public void logout()
    {
        try
        {
            customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
            if(confirmationAlert("Logout Confirmation - Garage Management System", "You are about to logout") == true)
            {
                LoginController.getInstance().exitHandler();
            }
        }
        catch(Exception e)
        {
            System.out.println("Logout from CustomerController Error");
            e.printStackTrace();
        }
    }

    public void tableViewOfCustomerVehicleFromDB() throws Exception
    {
        if(customerTable.getSelectionModel().getSelectedItem() == null)
        {
            return;
        }

        try
        {
            customerVehicleTable.setDisable(false);
            tableEntriesVehicle.removeAll(tableEntriesVehicle);

            List<Vehicle> searchList = searchCustomerVehicleInDB();

            //arranging default list from newest to oldest customer vehicle
            for(int i=searchList.size()-1; i>=0; i--)
            {
                tableEntriesVehicle.add(searchList.get(i));
            }

            customerVehicleTableColumnRegistrationNumber.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("vehicleRegNumber"));
            customerVehicleTableColumnRegistrationNumber.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());

            customerVehicleTableColumnType.setCellValueFactory(new PropertyValueFactory<Vehicle, VehicleType>("vehicleType"));
            customerVehicleTableColumnType.setCellFactory(TextFieldTableCell.<Vehicle, VehicleType>forTableColumn(new StringConverter<VehicleType>() {
                @Override
                public String toString(VehicleType object)
                {
                    return object.toString();
                }

                @Override
                public VehicleType fromString(String string)
                {
                    if(string.equals("Car"))
                    {
                        return VehicleType.Car;
                    }
                    else if(string.equals("Van"))
                    {
                        return VehicleType.Van;
                    }
                    else
                    {
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
        }
        catch(Exception e)
        {
            System.out.println("tableViewOfCustomerVehicleFromDB Error");
            e.printStackTrace();
        }
    }

    public CustomerController()
    {
        master = BookingController.getInstance();
        bookingSystem = BookingSystem.getInstance();
        timeFormatter = DateTimeFormatter.ofPattern("hh:mm");
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public void tableViewOfCustomerVehiclePartFromDB() throws Exception
    {
        try
        {
            customerVehiclePartTable.setDisable(false);
            tableEntriesParts.removeAll(tableEntriesParts);

            List<PartAbstraction> searchList = searchCustomerVehiclePartsInstalledInDB();

            System.out.println("testing parts. total installed: " + searchList.size());//testing parts

            //arranging default list from newest to oldest customer vehicle part
            for(int i=searchList.size()-1; i>=0; i--)
            {
                tableEntriesParts.add(searchList.get(i));
            }

            customerVehiclePartTableColumnName.setCellValueFactory(new PropertyValueFactory<PartAbstraction, String>("partName"));
            customerVehiclePartTableColumnName.setCellFactory(TextFieldTableCell.<PartAbstraction>forTableColumn());

            customerVehiclePartTableColumnDescription.setCellValueFactory(new PropertyValueFactory<PartAbstraction, String>("partDescription"));
            customerVehiclePartTableColumnDescription.setCellFactory(TextFieldTableCell.<PartAbstraction>forTableColumn());

            customerVehiclePartTable.setItems(tableEntriesParts);
        }
        catch(Exception e)
        {
            System.out.println("tableViewOfCustomerVehiclePartFromDB Error");
            e.printStackTrace();
        }
    }

    public void tableViewOfCustomerBookingFromDB() throws Exception
    {
        try
        {
            customerBookingTable.setDisable(false);
            tableEntriesBooking.removeAll(tableEntriesBooking);

            List<DiagRepBooking> searchList = searchCustomerBookingInDB();

            //arranging default list from newest to oldest customer booking
            for(int i=searchList.size()-1; i>=0; i--)
            {
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
        catch(Exception e)
        {
            System.out.println("tableViewOfCustomerBookingFromDB Error");
            e.printStackTrace();
        }
    }

    /////

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        searchAllCustomersInDB();
    }


    public boolean checkCustomerFields()
    {
        try
        {
            if(customerFirstname.getText().equals(""))
            {
                errorAlert("'Customer First name' field is empty. Enter First name");
                return false;
            }
            if(customerFirstname.getText().length()<2)
            {
                errorAlert("'Customer First name' is too short. Minimum is 2 characters");
                return false;
            }
            if(customerFirstname.getText().length()>30)
            {
                errorAlert("'Customer First name' is too long. Maximum is 30 characters");
                return false;
            }
            if((customerFirstname.getText().contains("`"))||(customerFirstname.getText().contains("¬"))||(customerFirstname.getText().contains("!"))||(customerFirstname.getText().contains("\""))||(customerFirstname.getText().contains("£"))||(customerFirstname.getText().contains("$"))||(customerFirstname.getText().contains("%"))||(customerFirstname.getText().contains("^"))||(customerFirstname.getText().contains("&"))||(customerFirstname.getText().contains("*"))||(customerFirstname.getText().contains("("))||(customerFirstname.getText().contains(")"))||(customerFirstname.getText().contains("_"))||(customerFirstname.getText().contains("+"))||(customerFirstname.getText().contains("="))||(customerFirstname.getText().contains("{"))||(customerFirstname.getText().contains("}"))||(customerFirstname.getText().contains("["))||(customerFirstname.getText().contains("]"))||(customerFirstname.getText().contains(":"))||(customerFirstname.getText().contains(";"))||(customerFirstname.getText().contains("@"))||(customerFirstname.getText().contains("'"))||(customerFirstname.getText().contains("~"))||(customerFirstname.getText().contains("#"))||(customerFirstname.getText().contains("<"))||(customerFirstname.getText().contains(">"))||(customerFirstname.getText().contains(","))||(customerFirstname.getText().contains("."))||(customerFirstname.getText().contains("?"))||(customerFirstname.getText().contains("/"))||(customerFirstname.getText().contains("|"))||(customerFirstname.getText().contains("1"))||(customerFirstname.getText().contains("2"))||(customerFirstname.getText().contains("3"))||(customerFirstname.getText().contains("4"))||(customerFirstname.getText().contains("5"))||(customerFirstname.getText().contains("6"))||(customerFirstname.getText().contains("7"))||(customerFirstname.getText().contains("8"))||(customerFirstname.getText().contains("9"))||(customerFirstname.getText().contains("0")))
            {
                errorAlert("'Customer First name' cannot have numbers or symbols except '-'. Only English Alphabet letters a-z or A-Z are allowed");
                return false;
            }

            if(customerSurname.getText().equals(""))
            {
                errorAlert("'Customer Surname' field is empty. Enter First name");
                return false;
            }
            if(customerSurname.getText().length()<2)
            {
                errorAlert("'Customer Surname' is too short. Minimum is 2 characters");
                return false;
            }
            if(customerSurname.getText().length()>30)
            {
                errorAlert("'Customer Surname' is too long. Maximum is 30 characters");
                return false;
            }
            if((customerSurname.getText().contains("`"))||(customerSurname.getText().contains("¬"))||(customerSurname.getText().contains("!"))||(customerSurname.getText().contains("\""))||(customerSurname.getText().contains("£"))||(customerSurname.getText().contains("$"))||(customerSurname.getText().contains("%"))||(customerSurname.getText().contains("^"))||(customerSurname.getText().contains("&"))||(customerSurname.getText().contains("*"))||(customerSurname.getText().contains("("))||(customerSurname.getText().contains(")"))||(customerSurname.getText().contains("_"))||(customerSurname.getText().contains("+"))||(customerSurname.getText().contains("="))||(customerSurname.getText().contains("{"))||(customerSurname.getText().contains("}"))||(customerSurname.getText().contains("["))||(customerSurname.getText().contains("]"))||(customerSurname.getText().contains(":"))||(customerSurname.getText().contains(";"))||(customerSurname.getText().contains("@"))||(customerSurname.getText().contains("'"))||(customerSurname.getText().contains("~"))||(customerSurname.getText().contains("#"))||(customerSurname.getText().contains("<"))||(customerSurname.getText().contains(">"))||(customerSurname.getText().contains(","))||(customerSurname.getText().contains("."))||(customerSurname.getText().contains("?"))||(customerSurname.getText().contains("/"))||(customerSurname.getText().contains("|"))||(customerSurname.getText().contains("1"))||(customerSurname.getText().contains("2"))||(customerSurname.getText().contains("3"))||(customerSurname.getText().contains("4"))||(customerSurname.getText().contains("5"))||(customerSurname.getText().contains("6"))||(customerSurname.getText().contains("7"))||(customerSurname.getText().contains("8"))||(customerSurname.getText().contains("9"))||(customerSurname.getText().contains("0")))
            {
                errorAlert("'Customer Surname' cannot have numbers or symbols except '-'. Only English Alphabet letters a-z or A-Z are allowed");
                return false;
            }

            if(customerAddress.getText().equals(""))
            {
                errorAlert("'Customer Address' field is empty. Enter Address");
                return false;
            }
            if(customerAddress.getText().length()<4)
            {
                errorAlert("'Customer Address' field is too short. Minimum is 4 characters");
                return false;
            }
            if(customerAddress.getText().length()>50)
            {
                errorAlert("'Customer Address' field is too long. Maximum is 50 characters");
                return false;
            }

            if(customerPostcode.getText().equals(""))
            {
                errorAlert("'Customer Postcode' field is empty. Enter postcode");
                return false;
            }
            if(customerPostcode.getText().length()<6)
            {
                errorAlert("'Customer Postcode' field is too short. Enter valid UK Postcode including required spacing");
                return false;
            }
            if(customerPostcode.getText().length()>8)
            {
                errorAlert("'Customer Postcode' field is too long. Enter valid UK Postcode including required spacing");
                return false;
            }

            if(customerPhone.getText().equals(""))
            {
                errorAlert("'Customer Phone' number field is empty. Enter Phone number");
                return false;
            }
            if(customerPhone.getText().length()<7)
            {
                errorAlert("'Customer Phone' number field is too short. Enter valid Phone number between 7 and 15 digits");
                return false;
            }
            if(customerPhone.getText().length()>15)
            {
                errorAlert("'Customer Phone' number field is too long. Enter valid Phone number between 7 and 15 digits");
                return false;
            }
            if((customerPhone.getText().equals("`"))||(customerPhone.getText().equals("¬"))||(customerPhone.getText().equals("!"))||(customerPhone.getText().equals('"'))||(customerPhone.getText().equals("£"))||(customerPhone.getText().equals("$"))||(customerPhone.getText().equals("%"))||(customerPhone.getText().equals("^"))||(customerPhone.getText().equals("&"))||(customerPhone.getText().equals("*"))||(customerPhone.getText().equals("("))||(customerPhone.getText().equals(")"))||(customerPhone.getText().equals("_"))||(customerPhone.getText().equals("+"))||(customerPhone.getText().equals("="))||(customerPhone.getText().equals("{"))||(customerPhone.getText().equals("}"))||(customerPhone.getText().equals("["))||(customerPhone.getText().equals("]"))||(customerPhone.getText().equals(":"))||(customerPhone.getText().equals(";"))||(customerPhone.getText().equals("@"))||(customerPhone.getText().equals("'"))||(customerPhone.getText().equals("~"))||(customerPhone.getText().equals("#"))||(customerPhone.getText().equals("<"))||(customerPhone.getText().equals(">"))||(customerPhone.getText().equals(","))||(customerPhone.getText().equals("."))||(customerPhone.getText().equals("?"))||(customerPhone.getText().equals("/"))||(customerPhone.getText().equals("|")))
            {
                errorAlert("'Customer Phone' number field is invalid . No symbol is allowed except '+' at the start. Number digits from 0-9 are allowed");
                return false;
            }
            String customerPhoneNoPlus = customerPhone.getText().replaceAll("\\+", "");
            if((!customerPhoneNoPlus.matches("[0-9]+")))
            {
                errorAlert("'Customer Phone' number field is invalid. Letters a-z or A-Z are not allowed. No symbol is allowed except '+' at the start. Number digits from 0-9 are allowed");
                return false;
            }
            for(int i=1; i<customerPhone.getText().length(); i++)
            {
                if(customerPhone.getText().substring(i, i+1).equals("+"))
                {
                    errorAlert("'Customer Phone' number field is invalid. '+' symbol can only be optionally used at the start");
                    return false;
                }
            }

            if(customerEmail.getText().equals(""))
            {
                errorAlert("'Customer Email' address field is empty. Enter Email address");
                return false;
            }
            if(customerEmail.getText().length()<5)
            {
                errorAlert("'Customer Email' address field is too short. Enter valid Email address between 5 to 30 characters long");
                return false;
            }
            if(customerEmail.getText().length()>30)
            {
                errorAlert("'Customer Email' address field is too long. Enter valid Email address between 5 to 30 characters long");
                return false;
            }
            if(!customerEmail.getText().contains("@"))
            {
                errorAlert("'Customer Email' address field does not contain '@' symbol. Enter valid Email address containing '@' and period/dot symbol");
                return false;
            }
            if(!customerEmail.getText().contains("."))
            {
                errorAlert("'Customer Email' address field does not contain '.' symbol. Enter valid Email address containing period/dot '.' and '@' symbol");
                return false;
            }

            if(customerType.getSelectionModel().getSelectedItem() == null)
            {
                errorAlert("Pick Customer Type");
                return false;
            }

            return true;
        }
        catch(Exception e)
        {
            errorAlert(e.getMessage() + ": Add or Update Customer Error");
            return false;
        }
    }
}


