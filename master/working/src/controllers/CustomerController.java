package controllers;

import logic.CustomerSystem;
import logic.Criterion;
import logic.CriterionOperator;
import domain.*;
import persistence.DatabaseRepository;

import java.text.*;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.Scene;


/**
 * Created by EBUBECHUKWU on 19/02/2017.
 */
public class CustomerController {

    //for 'AddCustomer.fxml' instance variables
    private TextField addCustomerID = new TextField();
    private TextField addCustomerFirstname = new TextField();
    private TextField addCustomerSurname = new TextField();
    private TextField addCustomerAddress = new TextField();
    private TextField addCustomerPostcode = new TextField();
    private TextField addCustomerPhone = new TextField();
    private TextField addCustomerEmail = new TextField();
    private ComboBox addCustomerType = new ComboBox();
    private Button addSaveCustomerButton = new Button();
    private Button addResetCustomerButton = new Button();
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


}
