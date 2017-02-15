package logic;

import entities.Customer;
import entities.CustomerType;
import entities.Vehicle;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import java.util.*;
import static logic.CriterionOperator.*;

/**
 * Created by EBUBECHUKWU on 14/02/2017.
 */
public class CustomerSystem {

    private static CustomerSystem instance;
    private CriterionRepository persistence;


    private CustomerSystem(CriterionRepository p)
    {
        persistence = p;
    }

    public static CustomerSystem getInstance(CriterionRepository persistence)
    {
        if(instance == null)
        {
            instance = new CustomerSystem(persistence);
        }
        return instance;
    }

    public List<Customer> getAllCustomers()
    {
        return persistence.getByCriteria(new Criterion<>(Customer.class,null,null,null));
    }

    public boolean addCustomer(int customerID, String customerFirstname, String customerSurname, String customerAddress, String customerPostcode, String customerPhone, String customerEmail, CustomerType customerType)
    {
        Customer add = new Customer(customerID, customerFirstname, customerSurname, customerAddress,customerPostcode, customerPhone, customerEmail, customerType, null, null);
        return persistence.addItem(Customer.class, add);
    }

    public boolean editCustomer(int customerID, String customerFirstname, String customerSurname, String customerAddress, String customerPostcode, String customerPhone, String customerEmail, CustomerType customerType)
    {
        Customer edit = new Customer(customerID, customerFirstname, customerSurname, customerAddress,customerPostcode, customerPhone, customerEmail, customerType, null, null);
        return persistence.updateItem(Customer.class, edit);
    }

    //public boolean deleteCustomer()
    //{}

    public Customer searchCustomer(String customerFirstname, String customerSurname, String regNumber)
    {
        if(customerFirstname!="")
        {
            List<Customer> customerDetails = persistence.getByCriteria(new Criterion<>(Customer.class, "customerFirstname", EqualTo, customerFirstname));
            return customerDetails.get(0);
        }
        else if(customerSurname!="")
        {
            List<Customer> customerDetails = persistence.getByCriteria(new Criterion<>(Customer.class, "customerSurname", EqualTo, customerSurname));
            return customerDetails.get(0);
        }
        //else if(regNumber!="")
        //{
        //    int customerIDFromRegNumber = Vehicle.getCustomerID();
        //    List<Customer> customerDetails = persistence.getByCriteria(new Criterion<>(Customer.class, "customerID", EqualTo, customerIDFromRegNumber));
        //}
        else
        {
            return null;
        }
    }

}
