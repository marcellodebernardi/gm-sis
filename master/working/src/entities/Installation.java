package entities;

import java.util.Date;

/**
 * Created by shakib on 14/02/2017.
 */
public class Installation {

    private int installationID;
    private Date installationDate;
    private Date endWarrantyDate;

    // inverse hierarchical database links todo finish up
    private String vehicleRegNumber;
    private PartOccurrence partOccurrence;
    private int partAbstractionID;

    private Installation(int installationID, Date installationDate, Date endWarrantyDate, String vehicleRegNumber){
        this.installationID = installationID;
        this.installationDate = installationDate;
        this.endWarrantyDate = endWarrantyDate;
        this.vehicleRegNumber = vehicleRegNumber;
    }

    private int installationID(){
        return installationID;
    };
    private Date installationDate(){
        return installationDate;
    }
    private Date endWarrantyDate(){
        return endWarrantyDate;
    }
    private String vehicleRegNumber(){
        return vehicleRegNumber;
    }
}
