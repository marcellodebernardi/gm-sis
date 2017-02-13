package entities;

import logic.Criterion;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public abstract class Booking implements Criterion {
    private int bookingID;
    private int customerID;
    private String vehicleRegNumber;
    private String description;

    // application-level references
    private Bill bill;
    private Mechanic mechanic;

    /**
     * Full constructor which allows setting all fields manually.
     *
     * @param bookingID        unique ID of this booking
     * @param customerID       ID of the associated customer
     * @param vehicleRegNumber unique registration number of vehicle
     * @param description      description of booking as entered by some user
     * @param bill             the associated bill
     * @param mechanic         the associated mechanic
     */
    public Booking(int bookingID, int customerID, String vehicleRegNumber, String description,
                   Bill bill, Mechanic mechanic) {
        this.bookingID = bookingID;
        this.customerID = customerID;
        this.vehicleRegNumber = vehicleRegNumber;
        this.description = description;
        this.bill = bill;
        this.mechanic = mechanic;
    }

    /**
     * Get unique ID of this booking.
     *
     * @return booking ID
     */
    public int getBookingID() {
        return bookingID;
    }

    /**
     * Get unique ID of associated customer.
     *
     * @return customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Get unique registration number of associated vehicle
     *
     * @return vehicle registration number
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Get description of booking as entered by some user.
     *
     * @return booking description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the bill associated with this booking.
     *
     * @return booking bill
     */
    public Bill getBill() {
        return bill;
    }

    /**
     * Get the ID of the mechanic performing this booking work.
     *
     * @return mechanic ID
     */
    public Mechanic getMechanic() {
        return mechanic;
    }

    /**
     * Set the ID of the associated customer. Note that the reference variable in the
     * corresponding Customer object must also reflect any changes.
     *
     * @param customerID ID of new customer
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Associate the booking to a different vehicle, by giving the registration number
     * of the new vehicle. Note that the list of bookings in the corresponding vehicles must also
     * be updated.
     *
     * @param vehicleRegNumber unique registration number of new vehicle to associate
     */
    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * Sets the description of the booking.
     *
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Associates a new bill to the booking.
     *
     * @param bill the new bill
     */
    public void setBill(Bill bill) {
        this.bill = bill;
    }

    /**
     * Sets the ID of the mechanic performing the work for this booking.
     *
     * @param mechanic new mechanic tasked on this booking
     */
    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }
}
