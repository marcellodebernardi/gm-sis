package domain;

import logic.criterion.Criterion;
import persistence.DatabaseRepository;
import persistence.DependencyConnection;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static logic.criterion.CriterionOperator.equalTo;
import static persistence.DependencyConnection.Directionality.TRANSMITTER;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DiagRepBooking extends Booking implements DependencyConnectable {
    // dependency connections
    List<DependencyConnection> dependencyConnections;

    // times
    private ZonedDateTime diagnosisStart;
    private ZonedDateTime diagnosisEnd;
    private ZonedDateTime repairStart;
    private ZonedDateTime repairEnd;

    // direction inversion in database todo make list of specRepBookings
    private SpecRepBooking specRepBooking;
    private List<PartOccurrence> requiredPartsList;


    public DiagRepBooking() {
        super();
        diagnosisStart = null;
        diagnosisEnd = null;
        repairStart = null;
        repairEnd = null;
        specRepBooking = null;
        requiredPartsList = null;
    }

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
                          boolean billSettled, int mechanicID, boolean complete, ZonedDateTime diagnosisStart,
                          ZonedDateTime diagnosisEnd, ZonedDateTime repairStart, ZonedDateTime repairEnd,
                          SpecRepBooking specRepBooking, List<PartOccurrence> repairParts) {
        super(-1, vehicleRegNumber, description, new Bill(billAmount, billSettled), mechanicID, complete);
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
                           @Column(name = "complete") boolean complete,
                           @Column(name = "diagnosisStart") ZonedDateTime diagnosisStart,
                           @Column(name = "diagnosisEnd") ZonedDateTime diagnosisEnd,
                           @Column(name = "repairStart") ZonedDateTime repairStart,
                           @Column(name = "repairEnd") ZonedDateTime repairEnd,
                           @TableReference(baseType = SpecRepBooking.class, subTypes = {PartRepair.class, VehicleRepair.class}, key = "bookingID")
                                   SpecRepBooking specRepBooking,
                           @TableReference(baseType = PartOccurrence.class, subTypes = {PartOccurrence.class}, key = "bookingID")
                                   List<PartOccurrence> requiredPartsList) {
        super(bookingID, vehicleRegNumber, description, new Bill(billAmount, billSettled), mechanicID, complete);
        this.diagnosisStart = diagnosisStart;
        this.diagnosisEnd = diagnosisEnd;
        this.repairStart = repairStart;
        this.repairEnd = repairEnd;
        this.specRepBooking = specRepBooking;
        this.requiredPartsList = requiredPartsList;
    }


    @Column(name = "bookingID", primary = true) @Override
    public int getBookingID() {
        return super.getBookingID();
    }

    @Column(name = "vehicleRegNumber", foreign = true) @Override
    public String getVehicleRegNumber() {
        return super.getVehicleRegNumber();
    }

    @Override
    public void setVehicleRegNumber(String vehicleRegNumber) {
        super.setVehicleRegNumber(vehicleRegNumber);
    }

    @Column(name = "description") @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Column(name = "billAmount")
    public double getBillAmount() {
        return super.getBill().getBillAmount();
    }

    @Column(name = "billSettled")
    public boolean getBillSettled() {
        return super.getBill().isBillSettled();
    }

    @Column(name = "mechanicID", foreign = true)
    public int getMechanicID() {
        return super.getMechanicID();
    }

    @Column(name = "complete")
    public boolean isComplete() {
        return super.isComplete();
    }

    @Column(name = "diagnosisStart")
    public ZonedDateTime getDiagnosisStart() {
        return diagnosisStart;
    }

    public void setDiagnosisStart(ZonedDateTime diagnosisStart) {
        this.diagnosisStart = diagnosisStart;
    }

    @Column(name = "diagnosisEnd")
    public ZonedDateTime getDiagnosisEnd() {
        return diagnosisEnd;
    }

    public void setDiagnosisEnd(ZonedDateTime diagnosisEnd) {
        this.diagnosisEnd = diagnosisEnd;
    }

    @Column(name = "repairStart")
    public ZonedDateTime getRepairStart() {
        return repairStart;
    }

    public void setRepairStart(ZonedDateTime repairStart) {
        this.repairStart = repairStart;
    }

    @Column(name = "repairEnd")
    public ZonedDateTime getRepairEnd() {
        return repairEnd;
    }

    public void setRepairEnd(ZonedDateTime repairEnd) {
        this.repairEnd = repairEnd;
    }

    @TableReference(baseType = SpecRepBooking.class, subTypes = {PartRepair.class, VehicleRepair.class}, key = "bookingID")
    public SpecRepBooking getSpecRepBooking() {
        return specRepBooking;
    }

    @DependencyHandler
    public void addRequiredPart(PartOccurrence part) {
        if (dependencyConnections == null) dependencyConnections = new ArrayList<>();
        DependencyConnection transmitter
                = new DependencyConnection(TRANSMITTER).host(this);
        dependencyConnections.add(transmitter);
        part.setBooking(transmitter.pair().host(part));

        if (requiredPartsList == null) requiredPartsList = new ArrayList<>();
        requiredPartsList.add(part);
    }

    @DependencyHandler
    public void removeRequiredPart(PartOccurrence part) {
        if (requiredPartsList == null) return;

        final int id = part.getPartOccurrenceID();
        requiredPartsList.removeIf(p -> p.getPartOccurrenceID() == id);
        part.unsetBooking();
    }

    @DependencyHandler
    public List<DependencyConnection> getDependencies() {
        if (dependencyConnections == null) dependencyConnections = new ArrayList<>();
        return dependencyConnections;
    }

    @Lazy public List<PartOccurrence> getRequiredPartsList() {
        if (requiredPartsList == null)
            requiredPartsList = DatabaseRepository
                    .getInstance()
                    .getByCriteria(new Criterion<>(
                            PartOccurrence.class, "bookingID", equalTo, getBookingID()));
        return requiredPartsList;
    }

    @Lazy public Customer getCustomer() {
        List<Vehicle> vehicles = DatabaseRepository
                .getInstance()
                .getByCriteria(
                        new Criterion<>(
                                Vehicle.class,
                                "vehicleRegNumber",
                                equalTo,
                                getVehicleRegNumber()));

        if (vehicles.size() != 0) {
            List<Customer> customers = DatabaseRepository
                    .getInstance()
                    .getByCriteria(
                            new Criterion<>(
                                    Customer.class,
                                    "customerID",
                                    equalTo,
                                    vehicles.get(0).getCustomerID())
                    );
            if (customers.size() != 0) return customers.get(0);
        }
        return null;
    }

    @Lazy public VehicleRepair getSpcRepair() {
        List<VehicleRepair> list = DatabaseRepository.getInstance().getByCriteria(new Criterion<>(VehicleRepair.class)
                .where("bookingID", equalTo, getBookingID()));
        return list != null && list.size() != 0 ? list.get(0) : null;
    }

}