package domain;

import org.joda.time.DateTime;
import org.joda.time.MutableInterval;

import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DiagRepBooking extends Booking {
    private DateTime diagnosisStart;
    private DateTime diagnosisEnd;
    private DateTime repairStart;
    private DateTime repairEnd;
    private MutableInterval diagnosisInterval;
    private MutableInterval repairInterval;

    // direction inversion in database
    private SpecRepBooking specRepBooking;
    private List<PartOccurrence> requiredPartsList;


    /**
     * Creates a new diagnosis and repair booking.
     *
     * @param vehicleRegNumber
     * @param description
     * @param billAmount
     * @param billSettled
     * @param mechanicID
     * @param diagnosisStart
     * @param diagnosisEnd
     * @param repairStart
     * @param repairEnd
     * @param specRepBooking
     */
    public DiagRepBooking(String vehicleRegNumber, String description, double billAmount,
                          boolean billSettled, int mechanicID, DateTime diagnosisStart, DateTime diagnosisEnd,
                          DateTime repairStart, DateTime repairEnd, SpecRepBooking specRepBooking) {
        super(-1, vehicleRegNumber, description, new Bill(billAmount, billSettled), mechanicID);
        this.diagnosisStart = diagnosisStart;
        this.diagnosisEnd = diagnosisEnd;
        this.diagnosisInterval = new MutableInterval(diagnosisStart, diagnosisEnd);
        this.repairStart = repairStart;
        this.repairEnd = repairEnd;
        this.repairInterval = new MutableInterval(repairStart, repairEnd);
        this.specRepBooking = specRepBooking;
    }

    // reflection only, do not use
    @Reflective
    private DiagRepBooking(@Column(name = "bookingID", primary = true) int bookingID,
                           @Column(name = "vehicleRegNumber") String vehicleRegNumber,
                           @Column(name = "description") String description,
                           @Column(name = "billAmount") double billAmount,
                           @Column(name = "billSettled") boolean billSettled,
                           @Column(name = "mechanicID") int mechanicID,
                           @Column(name = "diagnosisStart") DateTime diagnosisStart,
                           @Column(name = "diagnosisEnd") DateTime diagnosisEnd,
                           @Column(name = "repairStart") DateTime repairStart,
                           @Column(name = "repairEnd") DateTime repairEnd,
                           @TableReference(baseType = SpecRepBooking.class, specTypes = {PartRepair.class, VehicleRepair.class}, key = "bookingID")
                                   SpecRepBooking specRepBooking) {
        super(bookingID, vehicleRegNumber, description, new Bill(billAmount, billSettled), mechanicID);
        this.diagnosisStart = diagnosisStart;
        this.diagnosisEnd = diagnosisEnd;
        this.diagnosisInterval = new MutableInterval(diagnosisStart, diagnosisEnd);
        this.repairStart = repairStart;
        this.repairEnd = repairEnd;
        this.repairInterval = new MutableInterval(repairStart, repairEnd);
        this.specRepBooking = specRepBooking;
    }


    /**
     * Get unique ID of this booking.
     *
     * @return booking ID
     */
    @Column(name = "bookingID", primary = true)
    @Override
    public int getBookingID() {
        return super.getBookingID();
    }

    /**
     * Get unique registration number of associated vehicle
     *
     * @return vehicle registration number
     */
    @Column(name = "vehicleRegNumber")
    @Override
    public String getVehicleRegNumber() {
        return super.getVehicleRegNumber();
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
     * Get description of booking as entered by some user.
     *
     * @return booking description
     */
    @Column(name = "description")
    @Override
    public String getDescription() {
        return super.getDescription();
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
     * Get the bill amount associated with this booking.
     *
     * @return booking bill
     */
    @Column(name = "billAmount")
    public double getBillAmount() {
        return super.getBill().getBillAmount();
    }

    /**
     * Get the settling status of the bill associated with this booking
     * @return
     */
    @Column(name = "billSettled")
    public boolean getBillSettled() {
        return super.getBill().isBillSettled();
    }

    /**
     * Returns the ID of the mechanic associated with this booking
     *
     * @return ID of mechanic
     */
    @Column(name = "mechanidID")
    public int getMechanicID() {
        return super.getMechanicID();
    }

    /**
     * Returns the start time of the diagnosis booking.
     *
     * @return start time of diagnosis
     */
    @Column(name = "diagnosisStart")
    public DateTime getDiagnosisStart() {
        return diagnosisStart;
    }

    /**
     * Returns the end time of the diagnosis booking.
     *
     * @return end time of diagnosis
     */
    @Column(name = "diagnosisEnd")
    public DateTime getDiagnosisEnd() {
        return diagnosisEnd;
    }

    /**
     * Returns the start time of the repair booking.
     *
     * @return start time of repair
     */
    @Column(name = "repairStart")
    public DateTime getRepairStart() {
        return diagnosisStart;
    }

    /**
     * Returns the end time of the repair booking.
     *
     * @return end time of repair
     */
    @Column(name = "repairEnd")
    public DateTime getRepairEnd() {
        return diagnosisEnd;
    }

    /**
     * Returns the bill associated with this booking.
     * @return
     */
    public Bill getBill() {
        return getBill();
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
     * Returns the date on which the diagnosis check is to be carried out.
     *
     * @return date of diagnosis check
     */
    public MutableInterval getDiagnosisInterval() {
        return diagnosisInterval;
    }

    /**
     * Sets the diagnosis date.
     */
    public void setDiagnosisInterval(MutableInterval diagnosisInterval) {
        this.diagnosisInterval = diagnosisInterval;
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
    @TableReference(baseType = SpecRepBooking.class, specTypes = {PartRepair.class, VehicleRepair.class}, key = "bookingID")
    public SpecRepBooking getSpecRepBooking() {
        return specRepBooking;
    }

    /**
     * Sets the start time of the repair session.
     *
     * @param repairInterval allotted time slot for repair work
     */
    public void setRepairInterval(MutableInterval repairInterval) {
        this.repairInterval = repairInterval;
    }

    // todo part occurrences using dependency entanglement
}