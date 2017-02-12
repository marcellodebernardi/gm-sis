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
    private LocalDate diagnosisDate;
    private LocalDate repairDate;
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
                          Bill bill, MutableInterval diagnosisInterval, MutableInterval repairInterval,
                          SpecRepBooking specRepBooking) {
        super(bookingID, customerID, vehicleRegNumber, description, bill);
        this.diagnosisInterval = diagnosisInterval;
        this.repairInterval = repairInterval;
        this.specRepBooking = specRepBooking;
        if (diagnosisInterval != null) diagnosisDate = diagnosisInterval.getStart().toLocalDate();
        if (repairInterval != null) repairDate = repairInterval.getStart().toLocalDate();
    }

    /**
     * <p>
     * Null constructor. Creates a booking with null in all fields. NOTE: only use as a
     * Criterion when fetching all bookings from the database.
     * </p>
     */
    public DiagRepBooking() {
        super(-1, -1, null, null, null);
        diagnosisInterval = null;
        repairInterval = null;
        diagnosisDate = null;
        repairDate = null;
        specRepBooking = null;
        requiredPartsList = null;
    }

    /**
     * <p>
     * Allows construction of DiagRepBooking with specified bookingID, leaving all
     * other fields null. Only for use when identifying a single booking in the persistence
     * layer.
     * </p>
     *
     * @param bookingID unique identification number of the booking
     */
    public DiagRepBooking(int bookingID) {
        super(bookingID, -1, null, null, null);
        diagnosisInterval = null;
        repairInterval = null;
        diagnosisDate = null;
        repairDate = null;
        specRepBooking = null;
        requiredPartsList = null;
    }

    /**
     * <p>
     * Allows creation of booking object with unspecified booking times but specified booking
     * dates. NOTE: should only be used as a Criterion.
     * </p>
     *
     * @param bookingID        unique ID of this booking
     * @param customerID       ID of the associated customer
     * @param vehicleRegNumber unique registration number of vehicle
     * @param description      description of booking as entered by some user
     * @param bill             the associated bill
     * @param diagnosisDate    date on which diagnosis work takes place
     * @param repairDate       date on which repair work takes place
     * @param specRepBooking   specialist repair booking
     */
    public DiagRepBooking(int bookingID, int customerID, String vehicleRegNumber, String description,
                          Bill bill, LocalDate diagnosisDate, LocalDate repairDate,
                          SpecRepBooking specRepBooking) {
        super(bookingID, customerID, vehicleRegNumber, description, bill);
        this.diagnosisDate = diagnosisDate;
        this.repairDate = repairDate;
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
     * Returns the date of the diagnosis work.
     *
     * @return the date (without the time) of the diagnosis
     */
    public LocalDate getDiagnosisDate() {
        return diagnosisDate;
    }

    /**
     * Returns a LocalDate object representing
     *
     * @return the LocalDate of the repair
     */
    public LocalDate getRepairDate() {
        return repairDate;
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
     * todo figure out if bill will have a reference ID to booking
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
        diagnosisDate = diagnosisInterval.getStart().toLocalDate();
    }

    /**
     * Sets the start time of the repair session.
     *
     * @param repairInterval allotted time slot for repair work
     */
    public void setRepairInterval(MutableInterval repairInterval) {
        this.repairInterval = repairInterval;
        repairDate = diagnosisInterval.getStart().toLocalDate();
    }

    // todo methods for manipulating parts connected to repair
}
