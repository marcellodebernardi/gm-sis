package domain;

import logic.criterion.Criterion;
import logic.criterion.CriterionOperator;
import persistence.DatabaseRepository;
import persistence.DependencyConnection;

import java.util.ArrayList;
import java.util.List;

import static logic.criterion.CriterionOperator.EqualTo;

/**
 *
 */
public class PartOccurrence implements Searchable, DependencyConnectable {
    private int partOccurrenceID;

    // inverse hierarchical database links todo finish up
    private int partAbstractionID;
    private int installationID;
    private int bookingID;
    private int specRepID;

    // dependency connections
    List<DependencyConnection> dependencyConnections;


    /**
     * Constructor for PartOccurrence.
     *
     * @param partAbstractionID id of the type of part this occurrence belongs to
     * @param installationID    if installed on a vehicle, id of the installation
     * @param specRepID         if sent to SPC, id of specialist repair center
     */
    public PartOccurrence(int partAbstractionID, int installationID, int specRepID) {
        partOccurrenceID = -1;
        this.partAbstractionID = partAbstractionID;
        this.installationID = installationID;
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

    @Column(name = "partAbstractionID")
    public int getPartAbstractionID() {
        return partAbstractionID;
    }

    @Column(name = "installationID")
    public int getInstallationID() {
        return installationID;
    }

    @Column(name = "bookingID")
    public int getBookingID() {
        return bookingID;
    }

    @Column(name = "specRepID")
    public int getSpecRepID() {
        return specRepID;
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
    }

    @Lazy
    public PartAbstraction getPartAbstraction() {
        List<PartAbstraction> partTypes = DatabaseRepository.getInstance().getByCriteria(new Criterion<>(
                PartAbstraction.class,
                "partAbstractionID",
                EqualTo,
                partOccurrenceID));
        return partTypes != null && partTypes.size() != 0? partTypes.get(0) : null;
    }

    @Lazy
    public Installation getInstallation() {
        List<Installation> installations = DatabaseRepository.getInstance().getByCriteria(new Criterion<>(
                Installation.class,
                "installationID",
                EqualTo,
                installationID));
        return installations != null && installations.size() != 0 ? installations.get(0) : null;
    }


    public void setPartOccurrenceID(int partOccurrenceID) {
        this.partOccurrenceID = partOccurrenceID;
    }

    public void setPartAbstractionID(int partAbstractionID) {
        this.partAbstractionID = partAbstractionID;
    }

    public void setInstallationID(int installationID) {
        this.installationID = installationID;
    }

    public void setSpecRepID(int specRepID) {
        this.specRepID = specRepID;
    }
}