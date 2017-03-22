package domain;

import java.util.List;

/**
 * @author muradahmed
 * @version 0.1
 * @since : 0.1
 */

public class SpecialistRepairCenter implements Searchable {
    private int spcID;
    private String name;
    private String address;
    private String phone;
    private String email;

    // hierarchical links
    private List<SpecRepBooking> bookings;


    /**
     * Creates a new SPC
     *
     * @param name
     * @param address
     * @param phone
     * @param email
     * @param bookings
     */
    public SpecialistRepairCenter(String name, String address, String phone, String email, List<SpecRepBooking> bookings) {
        this.spcID = -1;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.bookings = bookings;
    }

    @Reflective
    private SpecialistRepairCenter(@Column(name = "spcID", primary = true) int spcID,
                                   @Column(name = "name") String name,
                                   @Column(name = "address") String address,
                                   @Column(name = "phone") String phone,
                                   @Column(name = "email") String email,
                                   @TableReference(baseType = SpecRepBooking.class,
                                           subTypes = {PartRepair.class, VehicleRepair.class}, key = "spcID")
                                           List<SpecRepBooking> bookings) {
        this.spcID = spcID;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.bookings = bookings;
    }


    @Column(name = "spcID", primary = true)
    public int getSpcID() {
        return this.spcID;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "address")
    public String getAddress() {
        return this.address;
    }

    @Column(name = "phone")
    public String getPhone() {
        return this.phone;
    }

    @Column(name = "email")
    public String getEmail() {
        return this.email;
    }

    @TableReference(baseType = SpecRepBooking.class,
            subTypes = {PartRepair.class, VehicleRepair.class}, key = "spcID")
    public List<SpecRepBooking> getBookings() {
        return this.bookings;
    }

    public void setBookings(List SpecRepBookings) {
        this.bookings = SpecRepBookings;
    }

    public boolean setName(String name) {
        if (!name.equals("")) {
            this.name = name;
            return true;
        }
        return false;
    }

    public boolean setAddress(String address) {
        if (!address.equals("")) {
            this.address = address;
            return true;
        }
        return false;
    }

    public boolean setPhone(String phone) {
        if (phone.length() == 11 && containsChar(phone)) {
            this.phone = phone;
            return true;
        }
        return false;
    }

    public boolean setEmail(String email) {
        if (!email.equals("") && email.contains("@")) {
            this.email = email;
            return true;
        }
        return false;
    }

    private boolean containsChar(String number) {
        try {
            double d = Double.parseDouble(number);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}