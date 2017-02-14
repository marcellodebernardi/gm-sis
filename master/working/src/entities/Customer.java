package entities;
import logic.Searchable;

import java.util.List;

/**
 * Created by EBUBECHUKWU on 08/02/2017.
 */

public class Customer implements Searchable
{
    private int customerID;
    private String customerFirstName;
    private String customerSurname;
    private String customerAddress;
    private String customerPostcode;
    private String customerPhone;
    private String customerEmail;
    private CustomerType customerType;

    // inverse hierarchical database links
    private List<Booking> bookings;
    private List<Vehicle> vehicles;

    public Customer(int cID, String cSurname, String cFirstName, String cAddress, String cPostcode,
                    String cPhone, String cEmail, CustomerType cType)
    {
        customerID = cID;
        customerSurname = cSurname;
        customerFirstName = cFirstName;
        customerAddress = cAddress;
        customerPostcode = cPostcode;
        customerPhone = cPhone;
        customerEmail = cEmail;
        customerType = cType;
    }

    //gets unique identifier for customer
    public int getCustomerID()
    {
        return customerID;
    }

    //sets unique identifier for customer
    public void setCustomerID(int custID)
    {
        customerID = custID;
    }

    //gets customer's surname
    public String getCustomerSurname()
    {
        return customerSurname;
    }

    //sets customer's surname
    public void setCustomerSurname(String custSurname)
    {
        customerSurname = custSurname;
    }

    //gets customer's first name
    public String getCustomerFirstname()
    {
        return customerFirstName;
    }

    //sets customer's first name
    public void setCustomerFirstname(String custFirstname)
    {
        customerFirstName = custFirstname;
    }

    //gets customer's address
    public String getCustomerAddress()
    {
        return customerAddress;
    }

    //sets customer's address
    public void setCustomerAddress(String custAddress)
    {
        customerAddress = custAddress;
    }

    //gets customer's postcode
    public String getCustomerPostcode()
    {
        return customerPostcode;
    }

    //sets customer's postcode
    public void setCustomerPostcode(String custPostcode)
    {
        customerPostcode = custPostcode;
    }

    //gets customer's phone number
    public String getCustomerPhone()
    {
        return customerPhone;
    }

    //sets customer's phone number
    public void setCustomerPhone(String custPhone)
    {
        customerPhone = custPhone;
    }

    //gets customer's email address
    public String getCustomerEmail()
    {
        return customerEmail;
    }

    //sets customer's email address
    public void setCustomerEmail(String custEmail)
    {
        customerEmail = custEmail;
    }

    //gets customer type (private or business)
    public CustomerType getCustomerType()
    {
        return customerType;
    }

    //sets customer type (private or business)
    public void setCustomerType(CustomerType custType)
    {
        customerType = custType;
    }

}
