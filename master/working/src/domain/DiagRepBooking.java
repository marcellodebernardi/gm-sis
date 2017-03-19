package domain;

import logic.criterion.Criterion;
import logic.criterion.CriterionOperator;
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

    // direction inversion in database todo make list of specRepBookings
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

    @Reflective // reflection only, do not use
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
                           @TableReference(baseType = SpecRepBooking.class, subTypes = {PartRepair.class, VehicleRepair.class}, key = "bookingID")
                                   SpecRepBooking specRepBooking) {
        super(bookingID, vehicleRegNumber, description, new Bill(billAmount, billSettled), mechanicID);
        this.diagnosisStart = diagnosisStart;
        this.diagnosisEnd = diagnosisEnd;
        this.repairStart = repairStart;
        this.repairEnd = repairEnd;
        this.specRepBooking = specRepBooking;
    }


    @Column(name = "bookingID", primary = true) @Override
    public int getBookingID() {
        return super.getBookingID();
    }

    @Column(name = "vehicleRegNumber") @Override
    public String getVehicleRegNumber() {
        return super.getVehicleRegNumber();
    }

    @Column(name = "description") @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Column(name = "billAmount")
    public double getBillAmount() {
        return super.getBill().getBillAmount();
    }

    @Column(name = "billSettled")
    public boolean getBillSettled() {
        return super.getBill().isBillSettled();
    }

    @Column(name = "mechanicID")
    public int getMechanicID() {
        return super.getMechanicID();
    }

    @Column(name = "diagnosisStart")
    public ZonedDateTime getDiagnosisStart() {
        return diagnosisStart;
    }

    @Column(name = "diagnosisEnd")
    public ZonedDateTime getDiagnosisEnd() {
        return diagnosisEnd;
    }

    @Column(name = "repairStart")
    public ZonedDateTime getRepairStart() {
        return repairStart;
    }

    @Column(name = "repairEnd")
    public ZonedDateTime getRepairEnd() {
        return repairEnd;
    }

    @TableReference(baseType = SpecRepBooking.class, subTypes = {PartRepair.class, VehicleRepair.class}, key = "bookingID")
    public SpecRepBooking getSpecRepBooking() {
        return specRepBooking;
    }

    @DependencyHandler
    public void addRequiredPart(PartOccurrence part) {
        if (dependencyConnections == null) dependencyConnections = new ArrayList<>();
        DependencyConnection transmitter = new DependencyConnection(DependencyConnection.Directionality.TRANSMITTER);
        part.setBooking(transmitter.pair());
    }

    @DependencyHandler
    public List<DependencyConnection> getDependencies() {
        if (dependencyConnections == null) dependencyConnections = new ArrayList<>();
        return dependencyConnections;
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

    @Lazy
    public Customer getCustomer() {
        List<Vehicle> vehicles = DatabaseRepository
                .getInstance()
                .getByCriteria(
                        new Criterion<>(
                                Vehicle.class,
                                "vehicleRegNumber",
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


    @Override
    public void setVehicleRegNumber(String vehicleRegNumber) {
        super.setVehicleRegNumber(vehicleRegNumber);
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    public void setDiagnosisStart(ZonedDateTime diagnosisStart) {
        this.diagnosisStart = diagnosisStart;
    }

    public void setDiagnosisEnd(ZonedDateTime diagnosisEnd) {
        this.diagnosisEnd = diagnosisEnd;
    }

    public void setRepairStart(ZonedDateTime repairStart) {
        this.repairStart = repairStart;
    }

    public void setRepairEnd(ZonedDateTime repairEnd) {
        this.repairEnd = repairEnd;
    }

}