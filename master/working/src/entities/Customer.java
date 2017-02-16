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
    private List<DiagRepBooking> bookings;
    private List<Vehicle> vehicles;

    @Reflective
    public Customer(@Simple(name = "customerID", primary = true) int customerID,
                    @Simple(name = "customerSurname") String customerSurname,
                    @Simple(name = "customerFirstname") String customerFirstname,
                    @Simple(name = "customerAddress") String customerAddress,
                    @Simple(name = "customerPostcode") String customerPostcode,
                    @Simple(name = "customerPhone") String customerPhone,
                    @Simple(name = "customerEmail") String customerEmail,
                    @Simple(name = "customerType") CustomerType customerType,
                    @Complex(baseType = DiagRepBooking.class, specTypes = DiagRepBooking.class, key = "customerID")
                                List<DiagRepBooking> bookings,
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
    @Simple(name = "customerID", primary = true)
    public int getCustomerID()
    {
        return customerID;
    }

    //gets customer's surname
    @Simple(name = "customerSurname")
    public String getCustomerSurname()
    {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    //gets customer's first name
    @Simple(name = "customerFirstname")
    public String getCustomerFirstname()
    {
        return customerFirstname;
    }

    public void setCustomerFirstname(String customerFirstname) {
        this.customerFirstname = customerFirstname;
    }

    //gets customer's address
    @Simple(name = "customerAddress")
    public String getCustomerAddress()
    {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    //gets customer's postcode
    @Simple(name = "customerPostcode")
    public String getCustomerPostcode()
    {
        return customerPostcode;
    }

    public void setCustomerPostcode(String customerPostcode) {
        this.customerPostcode = customerPostcode;
    }

    //gets customer's phone number
    @Simple(name = "customerPhone")
    public String getCustomerPhone()
    {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    //gets customer's email address
    @Simple(name = "customerEmail")
    public String getCustomerEmail()
    {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    //gets customer type (private or business)
    @Simple(name = "customerType")
    public CustomerType getCustomerType()
    {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    @Complex(baseType = Vehicle.class, specTypes = Vehicle.class, key = "customerID")
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Complex(baseType = DiagRepBooking.class, specTypes = DiagRepBooking.class, key = "customerID")
    public List<DiagRepBooking> getBookings() {
        return bookings;
    }

    public void setBookings(List<DiagRepBooking> bookings) {
        this.bookings = bookings;
    }
}
