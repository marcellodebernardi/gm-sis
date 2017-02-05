package entities;

import java.util.Date;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DiagnosisRepairBooking extends Booking {
    private Date diagnosisDate;
    private Date repairDate;
    private Date repairEndDate;
    // todo private SpecialistRepairBooking spcBooking;
    // todo private List<Part> requiredPartsList;


    /**
     * Full constructor that allows manual setting of all fields.
     *
     * @param bookingID unique ID of this booking
     * @param customerID ID of the associated customer
     * @param vehicleRegNumber unique registration number of vehicle
     * @param description description of booking as entered by some user
     * @param bill the associated bill
     * @param diagnosisDate the Date on which the diagnosis is carried out
     * @param repairDate the Date on which the repair begins
     * @param repairEndDate the Date on which the repair ends, defines the repair duration
     */
    public DiagnosisRepairBooking(int bookingID, int customerID, String vehicleRegNumber, String description,
                                  Bill bill, Date diagnosisDate, Date repairDate, Date repairEndDate) {
        super(bookingID, customerID, vehicleRegNumber, description, bill);
        this.diagnosisDate = diagnosisDate;
        this.repairDate = repairDate;
        this.repairEndDate = repairEndDate;
    }

    @Override
    /**
     * Get unique ID of this booking.
     *
     * @return booking ID
     */
    public int getBookingID() {
        return super.getBookingID();
    }

    @Override
    /**
     * Get unique ID of associated customer.
     *
     * @return customer ID
     */
    public int getCustomerID() {
        return super.getCustomerID();
    }

    @Override
    /**
     * Get unique registration number of associated vehicle
     *
     * @return vehicle registration number
     */
    public String getVehicleRegNumber() {
        return super.getVehicleRegNumber();
    }

    @Override
    /**
     * Get description of booking as entered by some user.
     *
     * @return booking description
     */
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    /**
     * Get the bill associated with this booking.
     *
     * @return booking bill
     */
    public Bill getBill() {
        return super.getBill();
    }

    /**
     * Returns the date on which the diagnosis check is to be carried out.
     *
     * @return date of diagnosis check
     */
    public Date getDiagnosisDate() {
        return diagnosisDate;
    }

    /**
     * Returns the date on which the repair session begins.
     *
     * @return start of repair session
     */
    public Date getRepairDate() {
        return repairDate;
    }

    /**
     * Returns the date on which the repair session ends.
     *
     * @return end of repair session
     */
    public Date getRepairEndDate() {
        return repairEndDate;
    }

    @Override
    /**
     * Set the ID of the associated customer. Note that the reference variable in the
     * corresponding Customer object must also reflect any changes.
     *
     * @param customerID ID of new customer
     */
    public void setCustomerID(int customerID) {
        super.setCustomerID(customerID);
    }

    @Override
    /**
     * Associate the booking to a different vehicle, by giving the registration number
     * of the new vehicle. Note that the list of bookings in the corresponding vehicles must also
     * be updated.
     *
     * @param vehicleRegNumber unique registration number of new vehicle to associate
     */
    public void setVehicleRegNumber(String vehicleRegNumber) {
        super.setVehicleRegNumber(vehicleRegNumber);
    }

    @Override
    /**
     * Sets the description of the booking.
     *
     * @param description new description
     */
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    /**
     * Associates a new bill to the booking.
     * todo figure out if bill will have a reference ID to booking
     *
     * @param bill the new bill
     */
    public void setBill(Bill bill) {
        super.setBill(bill);
    }

    /**
     * Sets the diagnosis date.
     */
    public void setDiagnosisDate(Date diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    /**
     * Sets the start time of the repair session.
     *
     * @param repairDate start of repair session
     */
    public void setRepairDate(Date repairDate) {
        this.repairDate = repairDate;
    }

    /**
     * Sets the end time of the repair session.
     *
     * @param repairEndDate end of repair session
     */
    public void setRepairEndDate(Date repairEndDate) {
        this.repairEndDate = repairEndDate;
    }
}
