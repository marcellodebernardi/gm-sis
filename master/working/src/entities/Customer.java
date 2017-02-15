package entities;
import logic.Searchable;

import java.util.List;

/**
 * Created by EBUBECHUKWU on 08/02/2017.
 */

public class Customer implements Searchable
{
    private int customerID;
    private String customerFirstname;
    private String customerSurname;
    private String customerAddress;
    private String customerPostcode;
    private String customerPhone;
    private String customerEmail;
    private CustomerType customerType;

    // hierarchical ownership links
    private List<Booking> bookings;
    private List<Vehicle> vehicles;

    @Reflective
    public Customer(@Simple(name = "customerID") int customerID,
                    @Simple(name = "customerSurname") String customerSurname,
                    @Simple(name = "customerFirstname") String customerFirstname,
                    @Simple(name = "customerAddress") String customerAddress,
                    @Simple(name = "customerPostcode") String customerPostcode,
                    @Simple(name = "customerPhone") String customerPhone,
                    @Simple(name = "customerEmail") String customerEmail,
                    @Simple(name = "customerType") CustomerType customerType,
                    @Complex(baseType = DiagRepBooking.class, specTypes = DiagRepBooking.class, key = "customerID")
                                List<Booking> bookings,
                    @Complex(baseType = Vehicle.class, specTypes = Vehicle.class, key = "customerID")
                                List<Vehicle> vehicles) {
        this.customerID = customerID;
        this.customerSurname = customerSurname;
        this.customerFirstname = customerFirstname;
        this.customerAddress = customerAddress;
        this.customerPostcode = customerPostcode;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerType = customerType;
        this.bookings = bookings;
        this.vehicles = vehicles;
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

    public List<Vehicle> getVehicles() {
        return vehicles;
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

}
