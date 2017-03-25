package domain;


import logic.criterion.Criterion;
import logic.criterion.CriterionOperator;
import persistence.DatabaseRepository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by shakib on 14/02/2017.
 */
public class Installation implements Searchable {

    private int installationID;
    private ZonedDateTime installationDate;
    private ZonedDateTime endWarrantyDate;

    // hierarchical links
    private PartOccurrence partOccurrence;

    // inverse hierarchical database links
    private String vehicleRegNumber;


    /**
     * Creates a new installation
     *
     * @param installationDate
     * @param endWarrantyDate
     * @param vehicleRegNumber
     * @param partAbstractionID
     * @param partOccurrence
     */
    @Deprecated
    public Installation(ZonedDateTime installationDate, ZonedDateTime endWarrantyDate, String vehicleRegNumber,
                        int partAbstractionID, PartOccurrence partOccurrence) {
        this.installationID = -1;
        this.installationDate = installationDate;
        this.endWarrantyDate = endWarrantyDate;
        this.vehicleRegNumber = vehicleRegNumber;
        this.partOccurrence = partOccurrence;
    }

    /**
     * Creates a new installation
     *
     * @param installationDate
     * @param vehicleRegNumber
     * @param partOccurrence
     */
    public Installation(ZonedDateTime installationDate, String vehicleRegNumber, PartOccurrence partOccurrence) {
        this.installationID = -1;
        this.installationDate = installationDate;
        this.endWarrantyDate = this.installationDate.plusYears(1);
        this.vehicleRegNumber = vehicleRegNumber;
        this.partOccurrence = partOccurrence;
    }

    // reflection only, do not use
    @Reflective
    private Installation(@Column(name = "installationID", primary = true) int installationID,
                         @Column(name = "installationDate") ZonedDateTime installationDate,
                         @Column(name = "endWarrantyDate") ZonedDateTime endWarrantyDate,
                         @Column(name = "vehicleRegNumber", foreign = true) String vehicleRegNumber,
                         @TableReference(baseType = PartOccurrence.class, subTypes = PartOccurrence.class, key = "installationID")
                                 PartOccurrence partOccurrence) {
        this.installationID = installationID;
        this.installationDate = installationDate;
        this.endWarrantyDate = endWarrantyDate;
        this.vehicleRegNumber = vehicleRegNumber;
        this.partOccurrence = partOccurrence;
    }


    @Column(name = "installationID", primary = true)
    public int getInstallationID() {
        return installationID;
    }

    @Column(name = "installationDate")
    public ZonedDateTime getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(ZonedDateTime installationDate) {
        this.installationDate = installationDate;
    }

    @Column(name = "endWarrantyDate")
    public ZonedDateTime getEndWarrantyDate() {
        return endWarrantyDate;
    }

    public void setEndWarrantyDate(ZonedDateTime endWarrantyDate) {
        this.endWarrantyDate = endWarrantyDate;
    }

    @Column(name = "vehicleRegNumber", foreign = true)
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    @TableReference(baseType = PartOccurrence.class, subTypes = PartOccurrence.class, key = "installationID")
    public PartOccurrence getPartOccurrence() {
        return partOccurrence;
    }

    public void setPartOccurrence(PartOccurrence partOccurrence) {
        this.partOccurrence = partOccurrence;
    }

    @Lazy
    public Customer getCustomer() {
        List<Vehicle> vehicles = DatabaseRepository.getInstance().getByCriteria(new Criterion<>(Vehicle.class, "vehicleRegNumber",
                CriterionOperator.equalTo, getVehicleRegNumber()));

        if (vehicles.size() != 0) {
            List<Customer> customers = DatabaseRepository.getInstance().getByCriteria(new Criterion<>(Customer.class, "customerID",
                    CriterionOperator.equalTo, vehicles.get(0).getCustomerID()));

            if (customers.size() != 0) return customers.get(0);
        }
        return null;
    }

    @Lazy
    public PartAbstraction getPartAbstraction() {
        return partOccurrence.getPartAbstraction();
    }

    @Lazy @Deprecated
    public int getPartAbstractionID() {
        return getPartAbstraction().getPartAbstractionID();
    }

    @Deprecated
    public void setPartAbstractionID(int partAbstractionID) {
        // no longer does anything
    }
}
