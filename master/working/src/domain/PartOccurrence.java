package domain;

import persistence.DependencyConnection;

import java.util.ArrayList;
import java.util.List;

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

    @Reflective
    private PartOccurrence(@Column(name = "partOccurrenceID", primary = true) int partOccurrenceID,
                          @Column(name = "partAbstractionID") int partAbstractionID,
                          @Column(name = "installationID") int installationID,
                          @Column(name = "bookingID") int bookingID,
                          @Column(name = "specRepID") int specRepID){
        this.partOccurrenceID = partOccurrenceID;
        this.partAbstractionID = partAbstractionID;
        this.installationID = installationID;
        this.bookingID = bookingID;
        this.specRepID = specRepID;
    }

    public PartOccurrence(int partAbstractionID, int installationID, int specRepID) {
        partOccurrenceID = -1;
        this.partAbstractionID = partAbstractionID;
        this.installationID = installationID;
        this.bookingID = -1;
        this.specRepID = specRepID;
    }

    @Column(name = "partOccurrenceID", primary = true)
    public int getPartOccurrenceID() {
        return partOccurrenceID;
    }

    public void setPartOccurrenceID(int partOccurrenceID) {
        this.partOccurrenceID = partOccurrenceID;
    }

    @Column(name = "partAbstractionID")
    public int getPartAbstractionID() {
        return partAbstractionID;
    }

    public void setPartAbstractionID(int partAbstractionID) {
        this.partAbstractionID = partAbstractionID;
    }

    @Column(name = "installationID")
    public int getInstallationID() {
        return installationID;
    }

    public void setInstallationID(int installationID) {
        this.installationID = installationID;
    }

    @Column(name = "bookingID")
    public int getBookingID() {
        return bookingID;
    }

    public void setBooking(DependencyConnection pair) {
        if (dependencyConnections == null) dependencyConnections = new ArrayList<>();
        dependencyConnections.add(pair);
    }

    @Column(name = "specRepID")
    public int getSpecRepID() {
        return specRepID;
    }

    public void setSpecRepID(int specRepID) {
        this.specRepID = specRepID;
    }

    public List<DependencyConnection> getDependencies() {
        if (dependencyConnections == null) dependencyConnections = new ArrayList<>();
        return dependencyConnections;
    }
}