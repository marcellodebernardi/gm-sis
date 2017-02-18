package domain;


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


    /**
     * Creates a new installation
     * @param installationDate
     * @param endWarrantyDate
     * @param vehicleRegNumber
     * @param partAbstractionID
     * @param partOccurrence
     */
    public Installation(Date installationDate, Date endWarrantyDate, String vehicleRegNumber,
                         int partAbstractionID, PartOccurrence partOccurrence){
        this.installationID = -1;
        this.installationDate = installationDate;
        this.endWarrantyDate = endWarrantyDate;
        this.vehicleRegNumber = vehicleRegNumber;
        this.partAbstractionID = partAbstractionID;
        this.partOccurrence = partOccurrence;
    }

    // reflection only, do not use
    @Reflective
    private Installation(@Column(name = "installationID", primary = true) int installationID,
                         @Column(name = "installationDate") Date installationDate,
                         @Column(name = "endWarrantyDate") Date endWarrantyDate,
                         @Column(name = "vehicleRegNumber") String vehicleRegNumber,
                         @Column(name = "partAbstractionID") int partAbstractionID,
                         @TableReference(baseType = Installation.class, specTypes = PartOccurrence.class, key = "installationID")
                                 PartOccurrence partOccurrence){
        this.installationID = installationID;
        this.installationDate = installationDate;
        this.endWarrantyDate = endWarrantyDate;
        this.vehicleRegNumber = vehicleRegNumber;
        this.partAbstractionID = partAbstractionID;
        this.partOccurrence = partOccurrence;
    }


    @Column(name = "installationID", primary = true)
    public int getInstallationID() {
        return installationID;
    }

    public void setInstallationID(int installationID) {
        this.installationID = installationID;
    }

    @Column(name = "installationDate")
    public Date getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }

    @Column(name = "endWarrantyDate")
    public Date getEndWarrantyDate() {
        return endWarrantyDate;
    }

    public void setEndWarrantyDate(Date endWarrantyDate) {
        this.endWarrantyDate = endWarrantyDate;
    }

    @Column(name = "vehicleRegNumber")
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    @Column(name = "partAbstractionID")
    public int getPartAbstractionID() {
        return partAbstractionID;
    }

    public void setPartAbstractionID(int partAbstractionID) {
        this.partAbstractionID = partAbstractionID;
    }

    @TableReference(baseType = Installation.class, specTypes = PartOccurrence.class, key = "installationID")
    public PartOccurrence getPartOccurrence() {
        return partOccurrence;
    }

    public void setPartOccurrence(PartOccurrence partOccurrence) {
        this.partOccurrence = partOccurrence;
    }
}
