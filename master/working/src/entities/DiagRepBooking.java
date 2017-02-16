package entities;

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
     * <p>
     * Allows manual setting of all fields except the convenience date fields,
     * which are inferred from the diagnosis and repair intervals.
     * </p>
     *
     * @param bookingID         unique ID of this booking
     * @param customerID        ID of the associated customer
     * @param vehicleRegNumber  unique registration number of vehicle
     * @param description       description of booking as entered by some user
     * @param billAmount        the associated bill's cost
     * @param billSettled       whether the bill has already been settled by customer
     * @param mechanicID        id of mechanic doing booking
     * @param diagnosisStart    start time of diagnosis
     * @param diagnosisEnd      end time of diagnosis
     * @param repairStart       start time of diagnosis
     * @param repairEnd         end time of diagnosis
     * @param specRepBooking    reference to a connected specialist repair booking, can be null
     */
    @Reflective
    public DiagRepBooking(@Simple(name = "bookingID", primary = true) int bookingID,
                          @Simple(name = "customerID") int customerID,
                          @Simple(name = "vehicleRegNumber") String vehicleRegNumber,
                          @Simple(name = "description") String description,
                          @Simple(name = "billAmount") double billAmount,
                          @Simple(name = "billSettled") boolean billSettled,
                          @Simple(name = "mechanicID") int mechanicID,
                          @Simple(name = "diagnosisStart") DateTime diagnosisStart,
                          @Simple(name = "diagnosisEnd") DateTime diagnosisEnd,
                          @Simple(name = "repairStart") DateTime repairStart,
                          @Simple(name = "repairEnd") DateTime repairEnd,
                          @Complex(baseType = SpecRepBooking.class, specTypes = {PartSpecialistRepair.class, VehicleSpecialistRepair.class}, key = "bookingID")
                                  SpecRepBooking specRepBooking) {
        super(bookingID, customerID, vehicleRegNumber, description, new Bill(billAmount, billSettled), mechanicID);
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
    @Simple(name = "bookingID", primary = true)
    @Override
    public int getBookingID() {
        return super.getBookingID();
    }

    /**
     * Get unique ID of associated customer.
     *
     * @return customer ID
     */
    @Simple(name = "customerID")
    @Override
    public int getCustomerID() {
        return super.getCustomerID();
    }

    /**
     * Get unique registration number of associated vehicle
     *
     * @return vehicle registration number
     */
    @Simple(name = "vehicleRegNumber")
    @Override
    public String getVehicleRegNumber() {
        return super.getVehicleRegNumber();
    }

    /**
     * Get description of booking as entered by some user.
     *
     * @return booking description
     */
    @Simple(name = "description")
    @Override
    public String getDescription() {
        return super.getDescription();
    }

    /**
     * Get the bill amount associated with this booking.
     *
     * @return booking bill
     */
    @Simple(name = "billAmount")
    public double getBillAmount() {
        return super.getBill().getBillAmount();
    }

    /**
     * Get the settling status of the bill associated with this booking
     * @return
     */
    @Simple(name = "billSettled")
    public boolean getBillSettled() {
        return super.getBill().isBillSettled();
    }

    /**
     * Returns the ID of the mechanic associated with this booking
     *
     * @return ID of mechanic
     */
    @Simple(name = "mechanidID")
    public int getMechanicID() {
        return super.getMechanicID();
    }

    /**
     * Returns the start time of the diagnosis booking.
     *
     * @return start time of diagnosis
     */
    @Simple(name = "diagnosisStart")
    public DateTime getDiagnosisStart() {
        return diagnosisStart;
    }

    /**
     * Returns the end time of the diagnosis booking.
     *
     * @return end time of diagnosis
     */
    @Simple(name = "diagnosisEnd")
    public DateTime getDiagnosisEnd() {
        return diagnosisEnd;
    }

    /**
     * Returns the start time of the repair booking.
     *
     * @return start time of repair
     */
    @Simple(name = "repairStart")
    public DateTime getRepairStart() {
        return diagnosisStart;
    }

    /**
     * Returns the end time of the repair booking.
     *
     * @return end time of repair
     */
    @Simple(name = "repairEnd")
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
    @Complex(baseType = SpecRepBooking.class, specTypes = {PartSpecialistRepair.class, VehicleSpecialistRepair.class}, key = "bookingID")
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