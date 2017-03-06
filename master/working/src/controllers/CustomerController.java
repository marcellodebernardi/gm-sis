package controllers;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.util.*;
import javafx.fxml.*;
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
import logic.*;
import logic.Criterion;
import logic.CriterionOperator;
//import logic.CustomerSystem;
import logic.Criterion;
import domain.*;
import persistence.DatabaseRepository;
import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;


/**
 * Created by EBUBECHUKWU on 19/02/2017.
 */
public class CustomerController
{

    //DatabaseRepository db = DatabaseRepository.getInstance();
    @FXML
    private CustomerSystem cSystem = CustomerSystem.getInstance();

    final ObservableList tableEntries = FXCollections.observableArrayList();
    final ObservableList comboEntries = FXCollections.observableArrayList();



    ////for 'CustomerView.fxml' instance variables
    ////left pane (add and edit customer view)
    @FXML
    private TextField customerID, customerFirstname, customerSurname, customerAddress, customerPostcode, customerPhone, customerEmail = new TextField();
    @FXML
    private ComboBox customerType = new ComboBox();
    //@FXML
    //private Button saveCustomerAndAddVehicleButton, saveCustomerButton, deleteCustomerButton, clearCustomerButton = new Button();


    ////right pane (search customer)
    @FXML
    private TextField customerSearch = new TextField();
    @FXML
    private Button customerSearchButton = new Button();


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



    //method for adding customer to database
    public void addCustomerToDB() throws Exception
    {
        try
        {
            //initialising variables
            //String cID = customerID.getText();
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
                    errorAlert("SUCCESSFULLY ADDED!!!");
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
            if(deleteConfirmation("Are you sure you want to delete Customer?") == false)
            {
                return;
            }
            check = true;
            if(check) {
                boolean deletedCustomer = cSystem.deleteCustomer(Integer.parseInt(cID));
                if (deletedCustomer) {
                    errorAlert("Customer has been deleted");
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
            Customer customers = customerTable.getSelectionModel().getSelectedItem();

            customerTable.setDisable(false);
            tableEntries.removeAll(tableEntries);

            for(int i=0; i < searchList.size(); i++)
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
        }
        catch(Exception e)
        {
            System.out.println("Customer Table view Error");
            e.printStackTrace();
        }
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


    public boolean deleteConfirmation(String message)
    {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle("Delete Customer Confirmation");
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

}





