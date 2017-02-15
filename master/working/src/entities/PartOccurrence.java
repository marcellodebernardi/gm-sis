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
    public PartOccurrence(@Simple(name = "partOccurrenceID") int partOccurrenceID,
                          @Simple(name = "partAbstractionID") int partAbstractionID,
                          @Simple(name = "installationID") int installationID){
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