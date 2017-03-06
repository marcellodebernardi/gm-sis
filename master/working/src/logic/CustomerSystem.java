package logic;

import domain.Customer;
import domain.CustomerType;
import persistence.DatabaseRepository;
import java.util.*;
import static logic.CriterionOperator.*;

/**
 * Created by EBUBECHUKWU on 14/02/2017.
 */
public class CustomerSystem {

    private static CustomerSystem instance;
    private CriterionRepository persistence;


    private CustomerSystem() {
        this.persistence = DatabaseRepository.getInstance();
    }

    public static CustomerSystem getInstance() {
        if (instance == null) {
            instance = new CustomerSystem();
        }
        return instance;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = persistence.getByCriteria(new Criterion<>(Customer.class));
        return allCustomers;
    }

    public boolean addCustomer(String customerFirstname, String customerSurname, String customerAddress, String customerPostcode, String customerPhone, String customerEmail, CustomerType customerType) {
        Customer add = new Customer(customerFirstname, customerSurname, customerAddress, customerPostcode, customerPhone, customerEmail, customerType, null);
        return persistence.commitItem(add);
    }

    public boolean editCustomer(int customerID, String customerFirstname, String customerSurname, String customerAddress, String customerPostcode, String customerPhone, String customerEmail, CustomerType customerType) {
        Customer edit = new Customer(customerFirstname, customerSurname, customerAddress, customerPostcode, customerPhone, customerEmail, customerType, null);
        return persistence.commitItem(edit);
    }

    public boolean deleteCustomer(int customerID)
    {
        return persistence.deleteItem(new Criterion<>(Customer.class, "customerID", EqualTo, customerID));
    }

    public List<Customer> searchCustomerByFirstname(String customerFirstname)
    {
        List<Customer> results = persistence.getByCriteria(new Criterion<>(Customer.class,
                "customerFirstname", Regex, customerFirstname));
        return results;

        //List<Customer> customerDetails = persistence.getByCriteria(new Criterion<>(Customer.class, "customerFirstname", Regex, customerFirstname).and("customerSurname", Regex, customerSurname));
        //NOTE: Missing search by vehicle regNumber criteria
        //return customerDetails;
    }

    public List<Customer> searchCustomerBySurname(String customerSurname)
    {
        List<Customer> results = persistence.getByCriteria(new Criterion<>(Customer.class,
                "customerSurname", Regex, customerSurname));
        return results;

        //List<Customer> customerDetails = persistence.getByCriteria(new Criterion<>(Customer.class, "customerFirstname", Regex, customerFirstname).and("customerSurname", Regex, customerSurname));
        //NOTE: Missing search by vehicle regNumber criteria
        //return customerDetails;
    }

//    public Customer searchCustomer(String customerFirstname, String customerSurname, String regNumber) {
//        if (customerFirstname != "") {
//            List<Customer> customerDetails = persistence.getByCriteria(new Criterion<>(Customer.class, "customerFirstname", EqualTo, customerFirstname));
//            return customerDetails.get(0);
//        } else if (customerSurname != "") {
//            List<Customer> customerDetails = persistence.getByCriteria(new Criterion<>(Customer.class, "customerSurname", EqualTo, customerSurname));
//            return customerDetails.get(0);
//        }
//        //else if(regNumber!="")
//        //{
//        //    int customerIDFromRegNumber = Vehicle.getCustomerID();
//        //    List<Customer> customerDetails = persistence.getByCriteria(new Criterion<>(Customer.class, "customerID", EqualTo, customerIDFromRegNumber));
//        //}
//        else {
//            return null;
//        }
//    }

    public Customer getACustomers(int customerID) {
        List<Customer> results =  persistence.getByCriteria(new Criterion<>(Customer.class, "customerID", EqualTo, customerID));
        return results !=null ? results.get(0) : null;
    }

}
