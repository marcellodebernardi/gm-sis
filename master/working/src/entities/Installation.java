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
    public Installation(@Simple(name = "installationID", primary = true) int installationID,
                         @Simple(name = "installationDate") Date installationDate,
                         @Simple(name = "endWarrantyDate") Date endWarrantyDate,
                         @Simple(name = "vehicleRegNumber") String vehicleRegNumber,
                         @Simple(name = "partAbstractionID") int partAbstractionID,
                         @Complex(baseType = Installation.class, specTypes = PartOccurrence.class, key = "installationID")
                                 PartOccurrence partOccurrence){
        this.installationID = installationID;
        this.installationDate = installationDate;
        this.endWarrantyDate = endWarrantyDate;
        this.vehicleRegNumber = vehicleRegNumber;
        this.partAbstractionID = partAbstractionID;
        this.partOccurrence = partOccurrence;
    }

    @Simple(name = "installationID", primary = true)
    public int getInstallationID() {
        return installationID;
    }

    public void setInstallationID(int installationID) {
        this.installationID = installationID;
    }

    @Simple(name = "installationDate")
    public Date getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }

    @Simple(name = "endWarrantyDate")
    public Date getEndWarrantyDate() {
        return endWarrantyDate;
    }

    public void setEndWarrantyDate(Date endWarrantyDate) {
        this.endWarrantyDate = endWarrantyDate;
    }

    @Simple(name = "vehicleRegNumber")
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    @Simple(name = "partAbstractionID")
    public int getPartAbstractionID() {
        return partAbstractionID;
    }

    public void setPartAbstractionID(int partAbstractionID) {
        this.partAbstractionID = partAbstractionID;
    }

    @Complex(baseType = Installation.class, specTypes = PartOccurrence.class, key = "installationID")
    public PartOccurrence getPartOccurrence() {
        return partOccurrence;
    }

    public void setPartOccurrence(PartOccurrence partOccurrence) {
        this.partOccurrence = partOccurrence;
    }
}
