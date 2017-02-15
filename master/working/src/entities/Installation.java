package entities;


import logic.Searchable;

import java.util.Date;

/**
 * Created by shakib on 14/02/2017.
 */
public class Installation implements Searchable {

    private int installationID;
    private Date installationDate;
    private Date endWarrantyDate;

    // hierarchical links
    private PartOccurrence partOccurrence;

    // inverse hierarchical database links
    private String vehicleRegNumber;
    private int partAbstractionID;

    @Reflective
    private Installation(@Simple(name = "installationID") int installationID,
                         @Simple(name = "installationDate") Date installationDate,
                         @Simple(name = "endWarrantyDate") Date endWarrantyDate,
                         @Simple(name = "vehicleRegNumber") String vehicleRegNumber,
                         @Simple(name = "partAbstractionID") int partAbstractionID,
                         @Complex(baseType = PartOccurrence.class, key = "installationID")
                                 PartOccurrence partOccurrence){
        this.installationID = installationID;
        this.installationDate = installationDate;
        this.endWarrantyDate = endWarrantyDate;
        this.vehicleRegNumber = vehicleRegNumber;
        this.partAbstractionID = partAbstractionID;
        this.partOccurrence = partOccurrence;
    }

    // todo getters and setters
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
