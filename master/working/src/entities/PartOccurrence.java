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
    public PartOccurrence(@Simple(name = "partOccurrenceID", primary = true) int partOccurrenceID,
                          @Simple(name = "partAbstractionID") int partAbstractionID,
                          @Simple(name = "installationID") int installationID,
                          @Simple(name = "bookingID") int bookingID,
                          @Simple(name = "specRepID") int specRepID){
        this.partOccurrenceID = partOccurrenceID;
        this.partAbstractionID = partAbstractionID;
        this.installationID = installationID;
        this.bookingID = bookingID;
        this.specRepID = specRepID;
    }

    @Simple(name = "partOccurrenceID", primary = true)
    public int getPartOccurrenceID() {
        return partOccurrenceID;
    }

    public void setPartOccurrenceID(int partOccurrenceID) {
        this.partOccurrenceID = partOccurrenceID;
    }

    @Simple(name = "partAbstractionID")
    public int getPartAbstractionID() {
        return partAbstractionID;
    }

    public void setPartAbstractionID(int partAbstractionID) {
        this.partAbstractionID = partAbstractionID;
    }

    @Simple(name = "installationID")
    public int getInstallationID() {
        return installationID;
    }

    public void setInstallationID(int installationID) {
        this.installationID = installationID;
    }

    @Simple(name = "bookingID")
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    @Simple(name = "specRepID")
    public int getSpecRepID() {
        return specRepID;
    }

    public void setSpecRepID(int specRepID) {
        this.specRepID = specRepID;
    }
}