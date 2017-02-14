package entities;

/**
 *
 */
public class PartOccurrence {
    // private Installation installation todo implement
    // ... and other fields todo implement

    private int partInstanceID;
    private int partID;
    private String installationID;

    private PartOccurrence(int partInstanceID, int partID, String installationID){

        this.partInstanceID = partInstanceID;
        this.partID = partID;
        this.installationID = installationID;

    }

    private int getPartInstanceID(){

        return partInstanceID;
    }
    private int getPartID(){

        return partID;
    }

    private String getInstallationID(){

        return installationID;

    }


}
