package controllers;

import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.*;
import javafx.fxml.FXML;
import java.util.*;
import logic.*;
import logic.Criterion;
import logic.CriterionOperator;
import logic.CustomerSystem;
import domain.*;
import persistence.DatabaseRepository;



/**
 * Created by EBUBECHUKWU on 19/02/2017.
 */
public class CustomerController {

    DatabaseRepository db = DatabaseRepository.getInstance();

    //@FXML
    //private CustomerSystem cSystem = CustomerSystem.getInstance();

    //for 'CustomerView.fxml' instance variables
    //left pane (add and edit customer view)
    @FXML
    private TextField customerID = new TextField();
    @FXML
    private TextField customerFirstname = new TextField();
    @FXML
    private TextField customerSurname = new TextField();
    @FXML
    private TextField customerAddress = new TextField();
    @FXML
    private TextField customerPostcode = new TextField();
    @FXML
    private TextField customerPhone = new TextField();
    @FXML
    private TextField customerEmail = new TextField();
    @FXML
    private ComboBox customerType = new ComboBox();
    @FXML
    private Button saveCustomerAndAddVehicleButton = new Button();
    @FXML
    private Button saveCustomerButton = new Button();
    @FXML
    private Button deleteCustomerButton = new Button();
    @FXML
    private Button clearCustomerButton = new Button();


    //right pane (search customer)
    @FXML
    private TextField customerSearch = new TextField();
    @FXML
    private Button customerSearchButton = new Button();


    //right pane (customer table view)
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerTableColumnID;
    @FXML
    private TableColumn<Customer, String> customerTableColumnFirstname;
    @FXML
    private TableColumn<Customer, String> customerTableColumnSurname;
    @FXML
    private TableColumn<Customer, String> customerTableColumnAddress;
    @FXML
    private TableColumn<Customer, String> customerTableColumnPostcode;
    @FXML
    private TableColumn<Customer, String> customerTableColumnPhone;
    @FXML
    private TableColumn<Customer, String> customerTableColumnEmail;
    @FXML
    private TableColumn<Customer, String> customerTableColumnType;


    //for 'DeleteCustomerConfirmation.fxml' instance variables
    @FXML
    private Button deleteCustomerConfirmationYes = new Button();
    @FXML
    private Button deleteCustomerConfirmationNo = new Button();



    public void addCustomerToDB() throws Exception
    {
        try
        {
            String cID = customerFirstname.getText();
            String cFirstname = customerFirstname.getText();
            String cSurname = customerSurname.getText();
            String cAddress = customerAddress.getText();
            String cPostcode = customerPostcode.getText();
            String cPhone = customerPhone.getText();
            String cEmail = customerEmail.getText();

            CustomerType cType = null;


            boolean checkFields = false;

            //checking validity of all Customer fields
            if((!cID.equals(""))&&(!cFirstname.equals(""))&&(!cSurname.equals(""))&&(!cAddress.equals(""))&&(!cPostcode.equals(""))&&(cPostcode.length()<=8)&&(!cPhone.equals(""))&&(cPhone.length()>10)&&(cPhone.length()<14)&&(!cEmail.equals(""))&&(cEmail.contains("@")))
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
                Customer newCustomer = new Customer(cFirstname, cSurname, cAddress, cPostcode, cPhone, cEmail, cType, null);
                boolean addingCustomer = db.commitItem(newCustomer);
                if(addingCustomer)
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





    public void errorAlert(String message)
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Message");
        errorAlert.setHeaderText(message);
        errorAlert.showAndWait();
    }

}
