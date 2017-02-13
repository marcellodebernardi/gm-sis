package entities;
import logic.Criterion;

/**
 * Created by EBUBECHUKWU on 08/02/2017.
 */

public class Customer implements Criterion
{
    private int customerID;
    private String customerSurname;
    private String customerFirstname;
    private String customerAddress;
    private String customerPostcode;
    private String customerPhone;
    private String customerEmail;
    private CustomerType customerType;
    private String vehicleRegistrationNumber;
    private int bookingID;

    public Customer(int cID, String cSurname, String cFirstname, String cAddress, String cPostcode, String cPhone, String cEmail, CustomerType cType, String vRegNumber, int bID)
    {
        customerID = cID;
        customerSurname = cSurname;
        customerFirstname = cFirstname;
        customerAddress = cAddress;
        customerPostcode = cPostcode;
        customerPhone = cPhone;
        customerEmail = cEmail;
        customerType = cType;
        vehicleRegistrationNumber = vRegNumber;
        bookingID = bID;
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
        return customerFirstname;
    }

    //sets customer's first name
    public void setCustomerFirstname(String custFirstname)
    {
        customerFirstname = custFirstname;
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

    //get vehicle registration number from Vehicle class
    public String getVehicleRegistrationNumber()
    {
        return vehicleRegistrationNumber;
    }

    //set vehicle registration number from Vehicle class
    public void setVehicleRegistrationNumber(String vRegNumber)
    {
        vehicleRegistrationNumber = vRegNumber;
    }

    //get booking ID from Booking class
    public int getBookingID()
    {
        return bookingID;
    }

    //set booking ID from Booking class
    public void setBookingID(int bID)
    {
        bookingID = bID;
    }

}
