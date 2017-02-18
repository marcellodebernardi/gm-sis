package entities;

import logic.Searchable;

/**
 *
 */
public class PartOccurrence implements Searchable {
    private int partOccurrenceID;

    // inverse hierarchical database links todo finish up
    private int partAbstractionID;
    private int installationID;
    private int bookingID;
    private int specRepID;

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

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    @Column(name = "specRepID")
    public int getSpecRepID() {
        return specRepID;
    }

    public void setSpecRepID(int specRepID) {
        this.specRepID = specRepID;
    }
}