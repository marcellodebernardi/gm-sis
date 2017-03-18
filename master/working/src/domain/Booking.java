package domain;

/**
 * @author Marcello De Bernardi
 * @version 0.2
 * @since 0.1
 */
public abstract class Booking implements Searchable {
    private int bookingID;
    private String description;

    // inverse hierarchical database links
    private String vehicleRegNumber;
    private Bill bill;
    private int mechanicID;


    /**
     * Full constructor which allows setting all fields manually.
     *
     * @param bookingID        unique ID of this booking
     * @param vehicleRegNumber unique registration number of vehicle
     * @param description      description of booking as entered by some user
     * @param bill             the associated bill
     * @param mechanicID       the ID of the associated mechanic
     */
    public Booking(int bookingID, String vehicleRegNumber, String description,
                   Bill bill, int mechanicID) {
        this.bookingID = bookingID;
        this.vehicleRegNumber = vehicleRegNumber;
        this.description = description;
        this.bill = bill;
        this.mechanicID = mechanicID;
    }


    public int getBookingID() {
        return bookingID;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public String getDescription() {
        return description;
    }

    public Bill getBill() {
        return bill;
    }

    public int getMechanicID() {
        return mechanicID;
    }


    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public void setMechanicID(int mechanicID) {
        this.mechanicID = mechanicID;
    }
}
