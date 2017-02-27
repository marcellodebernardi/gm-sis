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
    private CriterionRepository persistence = DatabaseRepository.getInstance();


    private CustomerSystem()
    {
        this.persistence = DatabaseRepository.getInstance();
    }

    public static CustomerSystem getInstance()
    {
        if(instance == null)
        {
            instance = new CustomerSystem();
        }
        return instance;
    }

    public List<Customer> getAllCustomers()
    {
        return persistence.getByCriteria(new Criterion<>(Customer.class));
    }

    public boolean addCustomer(String customerFirstname, String customerSurname, String customerAddress, String customerPostcode, String customerPhone, String customerEmail, CustomerType customerType)
    {
        Customer add = new Customer(customerFirstname, customerSurname, customerAddress,customerPostcode, customerPhone, customerEmail, customerType, null);
        return persistence.commitItem(add);
    }

    public boolean editCustomer(int customerID, String customerFirstname, String customerSurname, String customerAddress, String customerPostcode, String customerPhone, String customerEmail, CustomerType customerType)
    {
        Customer edit = new Customer(customerFirstname, customerSurname, customerAddress,customerPostcode, customerPhone, customerEmail, customerType, null);
        return persistence.commitItem(edit);
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

    public List<Customer> getACustomers(int customerID)
    {
        return persistence.getByCriteria(new Criterion<>(Customer.class, "customerID", EqualTo, customerID));
    }

}
