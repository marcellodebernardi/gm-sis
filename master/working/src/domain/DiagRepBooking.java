package domain;

import logic.Criterion;
import logic.CriterionOperator;
import persistence.DatabaseRepository;
import persistence.DependencyConnection;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DiagRepBooking extends Booking implements DependencyConnectable {
    // dependency connections
    List<DependencyConnection> dependencyConnections;
    private ZonedDateTime diagnosisStart;
    private ZonedDateTime diagnosisEnd;
    private ZonedDateTime repairStart;
    private ZonedDateTime repairEnd;
    // direction inversion in database
    private SpecRepBooking specRepBooking;
    private List<PartOccurrence> requiredPartsList;


    /**
     * Creates a new diagnosis and repair booking.
     *
     * @param vehicleRegNumber registration number
     * @param description      description of booking
     * @param billAmount       amount for customer (or warranty firm) to pay
     * @param billSettled      whether the bill has been settled or not
     * @param mechanicID       mechanic tasked on the booking
     * @param diagnosisStart   start time of diagnosis
     * @param diagnosisEnd     end time of diagnosis
     * @param repairStart      start time of repair
     * @param repairEnd        end time of repair
     * @param specRepBooking   potential specialist repair booking
     */
    public DiagRepBooking(String vehicleRegNumber, String description, double billAmount,
                          boolean billSettled, int mechanicID, ZonedDateTime diagnosisStart, ZonedDateTime diagnosisEnd,
                          ZonedDateTime repairStart, ZonedDateTime repairEnd, SpecRepBooking specRepBooking,
                          List<PartOccurrence> repairParts) {
        super(-1, vehicleRegNumber, description, new Bill(billAmount, billSettled), mechanicID);
        this.diagnosisStart = diagnosisStart;
        this.diagnosisEnd = diagnosisEnd;
        this.repairStart = repairStart;
        this.repairEnd = repairEnd;
        this.specRepBooking = specRepBooking;
        for (PartOccurrence part : repairParts) {
            addRequiredPart(part);
        }
    }

    // reflection only, do not use
    @Reflective
    private DiagRepBooking(@Column(name = "bookingID", primary = true) int bookingID,
                           @Column(name = "vehicleRegNumber") String vehicleRegNumber,
                           @Column(name = "description") String description,
                           @Column(name = "billAmount") double billAmount,
                           @Column(name = "billSettled") boolean billSettled,
                           @Column(name = "mechanicID") int mechanicID,
                           @Column(name = "diagnosisStart") ZonedDateTime diagnosisStart,
                           @Column(name = "diagnosisEnd") ZonedDateTime diagnosisEnd,
                           @Column(name = "repairStart") ZonedDateTime repairStart,
                           @Column(name = "repairEnd") ZonedDateTime repairEnd,
                           @TableReference(baseType = SpecRepBooking.class, specTypes = {PartRepair.class, VehicleRepair.class}, key = "bookingID")
                                   SpecRepBooking specRepBooking) {
        super(bookingID, vehicleRegNumber, description, new Bill(billAmount, billSettled), mechanicID);
        this.diagnosisStart = diagnosisStart;
        this.diagnosisEnd = diagnosisEnd;
        this.repairStart = repairStart;
        this.repairEnd = repairEnd;
        this.specRepBooking = specRepBooking;
    }


    /**
     * Get unique ID of this booking.
     *
     * @return booking ID
     */
    @Column(name = "bookingID", primary = true) @Override
    public int getBookingID() {
        return super.getBookingID();
    }

    /**
     * Get unique registration number of associated vehicle
     *
     * @return vehicle registration number
     */
    @Column(name = "vehicleRegNumber") @Override
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
    @Column(name = "description") @Override
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
     *
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
    @Column(name = "mechanicID")
    public int getMechanicID() {
        return super.getMechanicID();
    }

    /**
     * Returns the start time of the diagnosis booking.
     *
     * @return start time of diagnosis
     */
    @Column(name = "diagnosisStart")
    public ZonedDateTime getDiagnosisStart() {
        return diagnosisStart;
    }

    /**
     * Returns the end time of the diagnosis booking.
     *
     * @return end time of diagnosis
     */
    @Column(name = "diagnosisEnd")
    public ZonedDateTime getDiagnosisEnd() {
        return diagnosisEnd;
    }

    /**
     * Returns the start time of the repair booking.
     *
     * @return start time of repair
     */
    @Column(name = "repairStart")
    public ZonedDateTime getRepairStart() {
        return repairStart;
    }

    /**
     * Returns the end time of the repair booking.
     *
     * @return end time of repair
     */
    @Column(name = "repairEnd")
    public ZonedDateTime getRepairEnd() {
        return repairEnd;
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


    @Lazy
    public List<PartOccurrence> getRequiredPartsList() {
        if (requiredPartsList == null)
            requiredPartsList = DatabaseRepository
                    .getInstance()
                    .getByCriteria(new Criterion<>(
                            PartOccurrence.class, "bookingID", CriterionOperator.EqualTo, getBookingID()));
        return requiredPartsList;
    }

    @DependencyHandler
    public void addRequiredPart(PartOccurrence part) {
        if (dependencyConnections == null) dependencyConnections = new ArrayList<>();
        DependencyConnection transmitter = new DependencyConnection(DependencyConnection.Directionality.TRANSMITTER);
        part.setBooking(transmitter.pair());
    }

    @Lazy
    public Customer getCustomer() {
        List<Vehicle> vehicles = DatabaseRepository
                .getInstance()
                .getByCriteria(
                        new Criterion<>(
                                Vehicle.class,
                                "regNumber",
                                CriterionOperator.EqualTo,
                                getVehicleRegNumber()));

        if (vehicles.size() != 0) {
            List<Customer> customers = DatabaseRepository
                    .getInstance()
                    .getByCriteria(
                            new Criterion<>(
                                    Customer.class,
                                    "customerID",
                                    CriterionOperator.EqualTo,
                                    vehicles.get(0).getCustomerID())
                    );
            if (customers.size() != 0) return customers.get(0);
        }
        return null;
    }

    @DependencyHandler
    public List<DependencyConnection> getDependencies() {
        if (dependencyConnections == null) dependencyConnections = new ArrayList<>();
        return dependencyConnections;
    }
}