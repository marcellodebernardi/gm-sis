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

    //DatabaseRepository db = DatabaseRepository.getInstance();
    @FXML
    public CustomerSystem cSystem = CustomerSystem.getInstance();

    final ObservableList tableEntries = FXCollections.observableArrayList();
    final ObservableList comboEntries = FXCollections.observableArrayList();



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
    private TextField cID, reg, mod, manuf, eSize, col, mil, wName, wCompAddress, regS, manufS = new TextField();
    @FXML
    //private ComboBox cvSelectVehicle, cvCustomerID, cvVehicleType, cvFuelType, cvWarranty = new ComboBox();
    private ComboBox vType, fType, cByWarranty, VehicleS, typeS, VehicleTS = new ComboBox();
    @FXML
    //private DatePicker cvMOTRenewalDate, cvDateLastServiced, CVExpiationDate = new DatePicker();
    private DatePicker rDateMot, dLastServiced, wExpirationDate = new DatePicker();
    @FXML
    private Button addV, clearV = new Button();



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

            //checking validity of all Customer fields
            if((!cFirstname.equals(""))&&(!cSurname.equals(""))&&(!cAddress.equals(""))&&(!cPostcode.equals(""))&&(cPostcode.length()<=8)&&(!cPhone.equals(""))&&(cPhone.length()>10)&&(cPhone.length()<14)&&(!cEmail.equals(""))&&(cEmail.contains("@")))
            {
                if(customerType.getSelectionModel().getSelectedItem().toString().equals("Private"))
                {
                    cType = CustomerType.Private;
                    checkFields = true;
                }
                else if(customerType.getSelectionModel().getSelectedItem().toString().equals("Business"))
                {
                    cType = CustomerType.Business;
                    checkFields = true;
                }
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
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/customer/AddCustomerVehiclePopupView.fxml"));
                        Parent menu = fxmlLoader.load();
                        addVehicleStage = new Stage();
                        addVehicleStage.setTitle("Add Vehicle");
                        addVehicleStage.setScene(new Scene(menu));
                        addVehicleStage.show();
                        //addVehicleStage.showAndWait();
                        //addVehicleStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        //    @Override
                        //    public void handle(WindowEvent event) {
                        //        System.out.println("Add Vehicle stage is closing");
                        //    }
                        //});
                        //addVehicleStage.close();

                        List<Customer> allCustomers = cSystem.getAllCustomers();
                        int cvCustomerID = 0;
                        for(int i=allCustomers.size()-1; i>0; i--)
                        {
                            cvCustomerID = allCustomers.get(i).getCustomerID();
                            System.out.println("Count customers: " + cvCustomerID);
                            break;
                        }

                        Customer customer = cSystem.getACustomers(cvCustomerID);
                        System.out.println("Customer ID: " + cvCustomerID);
                        reg.setText(Integer.toString(cvCustomerID));

                        //cID.setText(Integer.toString(cvCustomerID));
                        System.out.println("Hello World 3");
                    }

                }
                else
                {
                    errorAlert("Cannot add customer. Ensure all fields have been entered correctly.");
                }
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


    //method for deleting customer from database
    public void deleteTableSelectedCustomerFromDB() throws Exception
    {
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
                    errorAlert("Customer has been deleted");

                    tableViewOfCustomersFromDB(cSystem.getAllCustomers());

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
            tableEntries.removeAll(tableEntries);

            //for(int i=0; i < searchList.size(); i++)
            //{
            //    tableEntries.add(searchList.get(i));
            //}

            //arranging default list from newest to oldest customer
            for(int i=searchList.size()-1; i>=0; i--)
            {
                tableEntries.add(searchList.get(i));
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

            customerTable.setItems(tableEntries);
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

            //checking validity of all Customer fields
            if((!cFirstname.equals(""))&&(!cSurname.equals(""))&&(!cAddress.equals(""))&&(!cPostcode.equals(""))&&(cPostcode.length()<=8)&&(!cPhone.equals(""))&&(cPhone.length()>10)&&(cPhone.length()<14)&&(!cEmail.equals(""))&&(cEmail.contains("@")))
            {
                if(customerType.getSelectionModel().getSelectedItem().toString().equals("Private"))
                {
                    cType = CustomerType.Private;
                    checkFields = true;
                }
                else if(customerType.getSelectionModel().getSelectedItem().toString().equals("Business"))
                {
                    cType = CustomerType.Business;
                    checkFields = true;
                }
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
            else
            {
                //error message if checkFields=false meaning one or more Customer fields were invalid
                errorAlert("Cannot update customer. Ensure all fields have been entered correctly.");
            }
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


    //ALL THE LOGIC FOR ADD VEHICLE TO NEW CUSTOMER RECORD


    public void addVehicle() throws Exception
    {


        if(!checkVehicleFormat())
        {
            return;
        }

        try
        {
            boolean check = checkVehicleFields();
            if(check)
            {

            }




            ////
            clearCustomerFields();
            addVehicleStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.out.println("Add Vehicle stage is closing");
                }
            });
            addVehicleStage.close();
        }
        catch(Exception e)
        {
            System.out.println("Add Customer's Vehicle Error");
            e.printStackTrace();
        }
    }


    public void searchCustomerVehicleInDB()
    {
        try
        {
            List<Vehicle> searchVehicleList = new ArrayList<Vehicle>(0);
            System.out.println("testing1: "+ searchVehicleList.size());//testing1
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            if(customer == null)
            {
                throw new Exception();
            }
            searchVehicleList = cSystem.searchCustomerVehicles(customer.getCustomerID());
            System.out.println("testing2: " + searchVehicleList.size());//testing2

            searchCustomerBookingInDB();//testing to find customer's bookings for their vehicles
        }
        catch(Exception e)
        {
            System.out.println("Search Customer Vehicle Error");
            e.printStackTrace();
        }
    }

    public void searchCustomerBookingInDB()
    {
        try
        {
            List<DiagRepBooking> searchBookingList = new ArrayList<DiagRepBooking>(0);
            System.out.println("testing3: " + searchBookingList.size());//testing3
            List<Vehicle> searchVehicleList = new ArrayList<Vehicle>(0);
            System.out.println("testing4: " + searchVehicleList.size());//testing4
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            searchVehicleList = cSystem.searchCustomerVehicles(customer.getCustomerID());
            System.out.println("testing5: " + searchVehicleList.size());//testing5
            for(int i = 0; i < searchVehicleList.size(); i++)
            {
                searchBookingList = cSystem.searchCustomerBookings(searchVehicleList.get(i).getRegNumber());
            }
            System.out.println("testing6: " + searchBookingList.size());//testing6
        }
        catch(Exception e)
        {
            System.out.println("Search Customer Booking Error");
            e.printStackTrace();
        }
    }

    public void searchCustomerVehiclePartsInstalledInDB()
    {
        try
        {

        }
        catch(Exception e)
        {

        }
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

    public void logout()
    {
        try
        {
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

    public void clearVehicleFields()
    {
        reg.clear();
        //cID.setValue(null);
        mod.clear();
        manuf.clear();
        eSize.clear();
        col.clear();
        mil.clear();
        rDateMot.setValue(null);
        dLastServiced.setValue(null);
        wName.clear();
        wCompAddress.clear();
        wExpirationDate.setValue(null);
        vType.setValue(null);
        fType.setValue(null);
        cByWarranty.setValue(null);
        //hiddenWarranty();
    }

    public boolean checkVehicleFields()
    {
        boolean check = false;
        if ((!reg.getText().equals("")) && (!cID.getText().equals(""))  && (!vType.getSelectionModel().getSelectedItem().toString().equals("")) && (!mod.getText().equals("")) && (!manuf.getText().equals("")) && (!eSize.getText().equals("")) && (!fType.getSelectionModel().getSelectedItem().toString().equals("")) && (!col.getText().equals("")) && (!mil.getText().equals("")) && (!rDateMot.getValue().equals("")) && (!dLastServiced.getValue().equals("")) && (!cByWarranty.getSelectionModel().getSelectedItem().toString().equals("")))
        {
            check = true;
            if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True"))
            {
                if (wName.getText().equals("") || wCompAddress.getText().equals("") || wExpirationDate.getValue().equals(""))
                {
                    check = false;
                }
            }
        }
        return check;
    }

    public boolean checkVehicleFormat()
    {
        try {
            Date date = java.sql.Date.valueOf(dLastServiced.getValue());
            date = java.sql.Date.valueOf(rDateMot.getValue());
            if (cByWarranty != null)
            {
                if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True") || cByWarranty.getSelectionModel().getSelectedItem().equals(null))
                {
                    date = java.sql.Date.valueOf(wExpirationDate.getValue());
                }}

            Double engineSize = Double.parseDouble(eSize.getText());
            int mileage = Integer.parseInt(mil.getText());
            if (col.getText().matches(".*\\d+.*"))
            {
                errorAlert("Colour must be a string");
                return false;
            }

            if (vType == null || fType == null)
            {
                errorAlert("Pick fuel type and vehicle Type");
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            errorAlert(e.getMessage() + " and make sure all fields required are not blank");
            return false;
        }
    }


    public void selectVehicle()
    {
        if (VehicleS.getSelectionModel().isSelected(0))
        {
            setVehicle("Civic", "Honda", "1.6", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(1))
        {
            setVehicle("Focus", "Ford", "1.2", "Car", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(2))
        {
            setVehicle("5 Series", "BMw", "2.2", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(3))
        {
            setVehicle("3 Series", "BMw", "2.9", "Car", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(4))
        {
            setVehicle("A Class", "Mercedes", "3.0", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(5))
        {
            setVehicle("Transit", "Ford", "2.2", "Van", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(6))
        {
            setVehicle("Roadster", "Nissan", "1.2", "Truck", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(7))
        {
            setVehicle("Y-8 Van", "Audi", "3.6", "Van", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(8))
        {
            setVehicle("Enzo", "Ferrari", "4.4", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(9))
        {
            setVehicle("Truckster", "Ford", "2.8", "Truck", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(10))
        {
            setVehicle("Hybrid Van", "Renault", "2.3", "Can", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(11))
        {
            setVehicle("Sport", "MG", "2.0", "Car", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(12))
        {
            setVehicle("Model S", "Acura", "2.2", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(13))
        {
            setVehicle("Arnage", "Bentley", "4.0", "Car", "Petrol");
        }
        else
        {
            setVehicle("Fire Truck", "DAF", "3.8", "Truck", "Diesel");
        }
    }

    public void setVehicle(String model, String manufacturer, String engineSize, String vehicleType, String fuelType)
    {
        mod.setText(model);
        manuf.setText(manufacturer);
        eSize.setText(engineSize);
        vType.setValue(vehicleType);
        fType.setValue(fuelType);
    }


    /////

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        searchAllCustomersInDB();
        //setCustomerCombo();
        //searchVehicleA();
        //rDateMot.setDayCellFactory(motDayFactory);
        //dLastServiced.setDayCellFactory(dlsDayFactory);
        //wExpirationDate.setDayCellFactory(weDayFactory);

    }


}


