package entities;

import org.joda.time.LocalDate;
import org.joda.time.MutableInterval;

import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DiagRepBooking extends Booking {
    private MutableInterval diagnosisInterval;
    private MutableInterval repairInterval;

    // direction inversion in database
    private SpecRepBooking specRepBooking;
    private List<PartOccurrence> requiredPartsList;


    /**
     * <p>
     * Allows manual setting of all fields except the convenience date fields,
     * which are inferred from the diagnosis and repair intervals.
     * </p>
     *
     * @param bookingID         unique ID of this booking
     * @param customerID        ID of the associated customer
     * @param vehicleRegNumber  unique registration number of vehicle
     * @param description       description of booking as entered by some user
     * @param bill              the associated bill
     * @param diagnosisInterval allotted time for diagnosis check
     * @param repairInterval    allotted time for repair work
     * @param specRepBooking    reference to a connected specialist repair booking, can be null
     */
    public DiagRepBooking(int bookingID, int customerID, String vehicleRegNumber, String description,
                          Bill bill, int mechanicID, MutableInterval diagnosisInterval, MutableInterval repairInterval,
                          SpecRepBooking specRepBooking) {
        super(bookingID, customerID, vehicleRegNumber, description, bill, mechanicID);
        this.diagnosisInterval = diagnosisInterval;
        this.repairInterval = repairInterval;
        this.specRepBooking = specRepBooking;
    }


    /**
     * Get unique ID of this booking.
     *
     * @return booking ID
     */
    @Override
    public int getBookingID() {
        return super.getBookingID();
    }

    /**
     * Get unique ID of associated customer.
     *
     * @return customer ID
     */
    @Override
    public int getCustomerID() {
        return super.getCustomerID();
    }

    /**
     * Get unique registration number of associated vehicle
     *
     * @return vehicle registration number
     */
    @Override
    public String getVehicleRegNumber() {
        return super.getVehicleRegNumber();
    }

    /**
     * Get description of booking as entered by some user.
     *
     * @return booking description
     */
    @Override
    public String getDescription() {
        return super.getDescription();
    }

    /**
     * Get the bill associated with this booking.
     *
     * @return booking bill
     */
    @Override
    public Bill getBill() {
        return super.getBill();
    }

    /**
     * Returns the date on which the diagnosis check is to be carried out.
     *
     * @return date of diagnosis check
     */
    public MutableInterval getDiagnosisInterval() {
        return diagnosisInterval;
    }

    /**
     * Returns the allotted time slot for the repair.
     *
     * @return end of repair session
     */
    public MutableInterval getRepairInterval() {
        return repairInterval;
    }

    /**
     * Returns a SpecRepairBooking object representing a specialist repair subcontract.
     *
     * @return a specialist repair booking
     */
    public SpecRepBooking getSpecRepBooking() {
        return specRepBooking;
    }

    /**
     * Set the ID of the associated customer. Note that the reference variable in the
     * corresponding Customer object must also reflect any changes.
     *
     * @param customerID ID of new customer
     */
    @Override
    public void setCustomerID(int customerID) {
        super.setCustomerID(customerID);
    }

    /**
     * Associate the booking to a different vehicle, by giving the registration number
     * of the new vehicle. Note that the list of bookings in the corresponding vehicles must also
     * be updated.
     *
     * @param vehicleRegNumber unique registration number of new vehicle to associate
     */
    @Override
    public void setVehicleRegNumber(String vehicleRegNumber) {
        super.setVehicleRegNumber(vehicleRegNumber);
    }

    /**
     * Sets the description of the booking.
     *
     * @param description new description
     */
    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    /**
     * Associates a new bill to the booking.
     *
     * @param bill the new bill
     */
    @Override
    public void setBill(Bill bill) {
        super.setBill(bill);
    }

    /**
     * Sets the diagnosis date.
     */
    public void setDiagnosisInterval(MutableInterval diagnosisInterval) {
        this.diagnosisInterval = diagnosisInterval;
    }

    /**
     * Sets the start time of the repair session.
     *
     * @param repairInterval allotted time slot for repair work
     */
    public void setRepairInterval(MutableInterval repairInterval) {
        this.repairInterval = repairInterval;
    }

    // todo methods for manipulating parts connected to repair
}
