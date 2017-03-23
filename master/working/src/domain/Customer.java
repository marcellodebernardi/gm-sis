package domain;

import logic.criterion.CriterionRepository;
import persistence.DatabaseRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EBUBECHUKWU on 08/02/2017.
 */

public class Customer implements Searchable {
    private CriterionRepository persistence;

    private int customerID;
    private String customerFirstname;
    private String customerSurname;
    private String customerAddress;
    private String customerPostcode;
    private String customerPhone;
    private String customerEmail;
    private CustomerType customerType;

    // hierarchical ownership links
    private List<Vehicle> vehicles;


    public Customer(String customerFirstname, String customerSurname, String customerAddress,
                    String customerPostcode, String customerPhone, String customerEmail, CustomerType customerType,
                    List<Vehicle> vehicles) {
        persistence = DatabaseRepository.getInstance();
        customerID = -1;
        this.customerSurname = customerSurname;
        this.customerFirstname = customerFirstname;
        this.customerAddress = customerAddress;
        this.customerPostcode = customerPostcode;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerType = customerType;
        this.vehicles = vehicles;
    }

    // reflection only, do not use
    @Reflective
    private Customer(@Column(name = "customerID", primary = true) int customerID,
                     @Column(name = "customerSurname") String customerSurname,
                     @Column(name = "customerFirstname") String customerFirstname,
                     @Column(name = "customerAddress") String customerAddress,
                     @Column(name = "customerPostcode") String customerPostcode,
                     @Column(name = "customerPhone") String customerPhone,
                     @Column(name = "customerEmail") String customerEmail,
                     @Column(name = "customerType") CustomerType customerType,
                     @TableReference(baseType = Vehicle.class, subTypes = Vehicle.class, key = "customerID")
                             List<Vehicle> vehicles) {
        persistence = DatabaseRepository.getInstance();
        this.customerID = customerID;
        this.customerSurname = customerSurname;
        this.customerFirstname = customerFirstname;
        this.customerAddress = customerAddress;
        this.customerPostcode = customerPostcode;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerType = customerType;
        this.vehicles = vehicles;
    }


    //gets unique identifier for customer
    @Column(name = "customerID", primary = true)
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    //gets customer's surname
    @Column(name = "customerSurname")
    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    //gets customer's first name
    @Column(name = "customerFirstname")
    public String getCustomerFirstname() {
        return customerFirstname;
    }

    public void setCustomerFirstname(String customerFirstname) {
        this.customerFirstname = customerFirstname;
    }

    //gets customer's address
    @Column(name = "customerAddress")
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    //gets customer's postcode
    @Column(name = "customerPostcode")
    public String getCustomerPostcode() {
        return customerPostcode;
    }

    public void setCustomerPostcode(String customerPostcode) {
        this.customerPostcode = customerPostcode;
    }

    //gets customer's phone number
    @Column(name = "customerPhone")
    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    //gets customer's email address
    @Column(name = "customerEmail")
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    //gets customer type (private or business)
    @Column(name = "customerType")
    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    @TableReference(baseType = Vehicle.class, subTypes = Vehicle.class, key = "customerID")
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Lazy
    public List<DiagRepBooking> getBookings() {
        List<DiagRepBooking> bookings = new ArrayList<>();

        for (Vehicle v : getVehicles()) {
            bookings.addAll(v.getBookingList());
        }

        return bookings;
    }
}
