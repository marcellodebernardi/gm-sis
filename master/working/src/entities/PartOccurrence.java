package entities;

/**
 *
 */
public class PartOccurrence {
    private int partOccurrenceID;

    // inverse hierarchical database links todo finish up
    private int partAbstractionID;
    private int installationID;
    private int bookingID;
    private int specRepID;


    public PartOccurrence(int partOccurrenceID, int partAbstractionID, int installationID){
        this.partOccurrenceID = partOccurrenceID;
        this.partAbstractionID = partAbstractionID;
        this.installationID = installationID;
    }


    public int getPartOccurrenceID(){
        return partOccurrenceID;
    }

    public int getPartAbstractionID(){
        return partAbstractionID;
    }

    public int getInstallationID(){
        return installationID;
    }
}