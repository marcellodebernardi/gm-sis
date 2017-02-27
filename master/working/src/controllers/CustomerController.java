package controllers;

import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import logic.Criterion;
import logic.CriterionOperator;
import logic.CustomerSystem;
import persistence.DatabaseRepository;
import java.util.*;
import domain.Customer;
import domain.CustomerType;
import logic.CustomerSystem;


/**
 * Created by EBUBECHUKWU on 19/02/2017.
 */
public class CustomerController {

    DatabaseRepository db = DatabaseRepository.getInstance();

    //@FXML
    //private CustomerSystem cSystem = CustomerSystem.getInstance();

    //for 'AddCustomer.fxml' instance variables
    @FXML
    private TextField addCustomerID = new TextField();
    @FXML
    private TextField addCustomerFirstname = new TextField();
    @FXML
    private TextField addCustomerSurname = new TextField();
    @FXML
    private TextField addCustomerAddress = new TextField();
    @FXML
    private TextField addCustomerPostcode = new TextField();
    @FXML
    private TextField addCustomerPhone = new TextField();
    @FXML
    private TextField addCustomerEmail = new TextField();
    @FXML
    private ComboBox addCustomerType = new ComboBox();
    @FXML
    private Button addSaveCustomerButton = new Button();
    @FXML
    private Button addResetCustomerButton = new Button();
    @FXML
    private Button addBackCustomerButton = new Button();

    //for 'EditCustomer.fxml' instance variables
    private TextField editCustomerID = new TextField();
    private TextField editCustomerFirstname = new TextField();
    private TextField editCustomerSurname = new TextField();
    private TextField editCustomerAddress = new TextField();
    private TextField editCustomerPostcode = new TextField();
    private TextField editCustomerPhone = new TextField();
    private TextField editCustomerEmail = new TextField();
    private ComboBox editCustomerType = new ComboBox();
    private Button editSaveCustomerButton = new Button();
    private Button editBackCustomerButton = new Button();

    //for 'DeleteCustomerConfirmation.fxml' instance variables
    private Button deleteCustomerConfirmationYes = new Button();
    private Button deleteCustomerConfirmationNo = new Button();

    //for 'SearchCustomer.fxml' instance variables
    private Button searchCustomerInput = new Button();

    //for 'ViewCustomer.fxml' instance variables
    private TextField viewCustomerID = new TextField();
    private TextField viewCustomerFirstname = new TextField();
    private TextField viewCustomerSurname = new TextField();
    private TextField viewCustomerAddress = new TextField();
    private TextField viewCustomerPostcode = new TextField();
    private TextField viewCustomerPhone = new TextField();
    private TextField viewCustomerEmail = new TextField();
    private TextField viewCustomerType = new TextField();
    private Button viewDeleteCustomer = new Button();
    private Button viewHome = new Button();
    private Button viewBack = new Button();


    private TableView<Customer> customerTable;
    private TableColumn<Customer, Integer> customerID;
    private TableColumn<Customer, String> customerFirstname;
    private TableColumn<Customer, String> customerSurname;
    private TableColumn<Customer, String> customerAddress;
    private TableColumn<Customer, String> customerPostcode;
    private TableColumn<Customer, String> customerPhone;
    private TableColumn<Customer, String> customerEmail;
    private TableColumn<Customer, String> customerType;



    public void addCustomerToDB() throws Exception
    {
        try
        {
            String cID = addCustomerFirstname.getText();
            String cFirstname = addCustomerFirstname.getText();
            String cSurname = addCustomerSurname.getText();
            String cAddress = addCustomerAddress.getText();
            String cPostcode = addCustomerPostcode.getText();
            String cPhone = addCustomerPhone.getText();
            String cEmail = addCustomerEmail.getText();
            //CustomerType cType = addCustomerType.getSelectionModel().getSelectedItem().toString().equals("Private");
            CustomerType cType = null;


            boolean checkFields = false;

            //checking validity of all Customer fields
            if((!cID.equals(""))&&(!cFirstname.equals(""))&&(!cSurname.equals(""))&&(!cAddress.equals(""))&&(!cPostcode.equals(""))&&(cPostcode.length()<=8)&&(!cPhone.equals(""))&&(cPhone.length()>10)&&(cPhone.length()<14)&&(!cEmail.equals(""))&&(cEmail.contains("@")))
            {
                if(addCustomerType.getSelectionModel().getSelectedItem().toString().equals("Private"))
                {
                    cType = CustomerType.Private;
                    checkFields = true;
                }
                else if(addCustomerType.getSelectionModel().getSelectedItem().toString().equals("Business"))
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
