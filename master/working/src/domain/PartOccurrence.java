package domain;

import logic.criterion.Criterion;
import persistence.DatabaseRepository;
import persistence.DependencyConnection;

import java.util.ArrayList;
import java.util.List;

import static logic.criterion.CriterionOperator.equalTo;

/**
 *
 */
public class PartOccurrence implements Searchable, DependencyConnectable {
    // dependency connections
    List<DependencyConnection> dependencyConnections;
    private int partOccurrenceID;
    // inverse hierarchical database links todo finish up
    private int partAbstractionID;
    private int installationID;
    private int bookingID;
    private int specRepID;


    /**
     * Constructor for PartOccurrence.
     *
     * @param partAbstractionID id of the type of part this occurrence belongs to
     * @param installationID    if installed on a vehicle, id of the installation
     * @param specRepID         if sent to SPC, id of specialist repair center
     */
    @Deprecated
    public PartOccurrence(int partAbstractionID, int installationID, int specRepID) {
        partOccurrenceID = -1;
        this.partAbstractionID = partAbstractionID;
        this.installationID = 0;
        this.bookingID = -1;
        this.specRepID = specRepID;
    }

    public PartOccurrence(int partAbstractionID, int specRepID) {
        partOccurrenceID = -1;
        this.partAbstractionID = partAbstractionID;
        this.installationID = -1;
        this.bookingID = -1;
        this.specRepID = specRepID;
    }

    @Reflective
    private PartOccurrence(@Column(name = "partOccurrenceID", primary = true) int partOccurrenceID,
                           @Column(name = "partAbstractionID") int partAbstractionID,
                           @Column(name = "installationID") int installationID,
                           @Column(name = "bookingID") int bookingID,
                           @Column(name = "specRepID") int specRepID) {
        this.partOccurrenceID = partOccurrenceID;
        this.partAbstractionID = partAbstractionID;
        this.installationID = installationID;
        this.bookingID = bookingID;
        this.specRepID = specRepID;
    }


    @Column(name = "partOccurrenceID", primary = true)
    public int getPartOccurrenceID() {
        return partOccurrenceID;
    }

    public void setPartOccurrenceID(int partOccurrenceID) {
        this.partOccurrenceID = partOccurrenceID;
    }

    @Column(name = "partAbstractionID", foreign = true)
    public int getPartAbstractionID() {
        return partAbstractionID;
    }

    public void setPartAbstractionID(int partAbstractionID) {
        this.partAbstractionID = partAbstractionID;
    }

    @Column(name = "installationID", foreign = true)
    public int getInstallationID() {
        return installationID;
    }

    public void setInstallationID(int installationID) {
        this.installationID = installationID;
    }

    @Column(name = "bookingID", foreign = true)
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    @Column(name = "specRepID", foreign = true)
    public int getSpecRepID() {
        return specRepID;
    }

    public void setSpecRepID(int specRepID) {
        this.specRepID = specRepID;
    }

    @DependencyHandler
    public List<DependencyConnection> getDependencies() {
        if (dependencyConnections == null) dependencyConnections = new ArrayList<>();
        return dependencyConnections;
    }

    @DependencyHandler
    public void setBooking(DependencyConnection pair) {
        if (dependencyConnections == null) dependencyConnections = new ArrayList<>();
        dependencyConnections.add(pair);
        bookingID = ((DiagRepBooking) pair.pair().getHost()).getBookingID(); // todo does this break?
    }

    public void unsetBooking() {
        bookingID = -2;
    }

    @Lazy
    public PartAbstraction getPartAbstraction() {
        List<PartAbstraction> partTypes = DatabaseRepository.getInstance().getByCriteria(new Criterion<>(
                PartAbstraction.class,
                "partAbstractionID",
                equalTo,
                partAbstractionID));
        return partTypes != null && partTypes.size() != 0 ? partTypes.get(0) : null;
    }

    @Lazy
    public Installation getInstallation() {
        List<Installation> installations = DatabaseRepository.getInstance().getByCriteria(new Criterion<>(
                Installation.class,
                "installationID",
                equalTo,
                installationID));
        return installations != null && installations.size() != 0 ? installations.get(0) : null;
    }

    /**
     * Returns the part's SPC repair, or null if the part has not been sent for an SPC repair.
     *
     * @return PartRepair if sent to SPC, null if not
     */
    @Lazy public PartRepair getPartRepair() {
        List<PartRepair> repairs = DatabaseRepository.getInstance().getByCriteria(new Criterion<>(PartRepair.class)
                .where("partOccurrenceID", equalTo, this.partOccurrenceID));
        return repairs != null && repairs.size() != 0 ? repairs.get(0) : null;
    }
}