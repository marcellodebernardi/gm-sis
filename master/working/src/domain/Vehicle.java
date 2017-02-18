package domain;
import java.util.*;

import logic.Searchable;

/**
 * @author Dillon Vaghela on 2/9/17.
 */
public class Vehicle implements Searchable {
    private String regNumber;
    private VehicleType vehicleType;
    private String model;
    private String manufacturer;
    private double engineSize;
    private FuelType fuelType;
    private String colour;
    private int mileage;
    private Date renewalDateMot;
    private Date dateLastServiced;
    private boolean coveredByWarranty;
    private String warrantyName;
    private String warrantyCompAddress;
    private Date warrantyExpirationDate;

    // hierarchical links
    private List<Installation> installationList;

    // inverse hierarchical database links
    private int customerID;

    @Reflective
    public Vehicle(@Column(name = "regNumber", primary = true) String regNumber,
                   @Column(name = "customerID") int customerID,
                   @Column(name = "vehicleType") VehicleType vehicleType,
                   @Column(name = "model") String model,
                   @Column(name = "manufacturer") String manufacturer,
                   @Column(name = "engineSize") double engineSize,
                   @Column(name = "fuelType") FuelType fuelType,
                   @Column(name = "colour") String colour,
                   @Column(name = "mileage") int mileage,
                   @Column(name = "renewalDateMot") Date renewalDateMot,
                   @Column(name = "dateLastServiced") Date dateLastServiced,
                   @Column(name = "coveredByWarranty") boolean coveredByWarranty,
                   @Column(name = "warrantyName") String warrantyName,
                   @Column(name = "warrantyCompAddress") String warrantyCompAddress,
                   @Column(name = "warrantyExpirationDate") Date warrantyExpirationDate,
                   @TableReference(baseType = Installation.class, specTypes = Installation.class, key = "vehicleRegNumber")
                           List<Installation> installationList) {
        this.regNumber = regNumber;
        this.customerID = customerID;
        this.vehicleType = vehicleType;
        this.model = model;
        this.manufacturer = manufacturer;
        this.engineSize = engineSize;
        this.fuelType = fuelType;
        this.colour = colour;
        this.mileage = mileage;
        this.renewalDateMot = renewalDateMot;
        this.dateLastServiced = dateLastServiced;
        this.coveredByWarranty = coveredByWarranty;
        this.warrantyName = warrantyName;
        this.warrantyCompAddress = warrantyCompAddress;
        this. warrantyExpirationDate = warrantyExpirationDate;
        this.installationList = installationList;
    }


    @Column(name = "regNumber", primary = true)
    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    @Column(name = "vehicleType")
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Column(name = "model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Column(name = "engineSize")
    public double getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(double engineSize) {
        this.engineSize = engineSize;
    }

    @Column(name = "fuelType")
    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    @Column(name = "colour")
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Column(name = "mileage")
    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    @Column(name = "renewalDateMot")
    public Date getRenewalDateMot() {
        return renewalDateMot;
    }

    public void setRenewalDateMot(Date renewalDateMot) {
        this.renewalDateMot = renewalDateMot;
    }

    @Column(name = "dateLastServiced")
    public Date getDateLastServiced() {
        return dateLastServiced;
    }

    public void setDateLastServiced(Date dateLastServiced) {
        this.dateLastServiced = dateLastServiced;
    }

    @Column(name = "coveredByWarranty")
    public boolean isCoveredByWarranty() {
        return coveredByWarranty;
    }

    public void setCoveredByWarranty(boolean coveredByWarranty) {
        this.coveredByWarranty = coveredByWarranty;
    }

    @Column(name = "warrantyName")
    public String getWarrantyName() {
        return warrantyName;
    }

    public void setWarrantyName(String warrantyName) {
        this.warrantyName = warrantyName;
    }

    @Column(name = "warrantyCompAddress")
    public String getWarrantyCompAddress() {
        return warrantyCompAddress;
    }

    public void setWarrantyCompAddress(String warrantyCompAddress) {
        this.warrantyCompAddress = warrantyCompAddress;
    }

    @Column(name = "warrantyExpirationDate")
    public Date getWarrantyExpirationDate() {
        return warrantyExpirationDate;
    }

    public void setWarrantyExpirationDate(Date warrantyExpirationDate) {
        this.warrantyExpirationDate = warrantyExpirationDate;
    }

    @TableReference(baseType = Installation.class, specTypes = Installation.class, key = "vehicleID")
    public List<Installation> getInstallationList() {
        return installationList;
    }

    public void setInstallationList(List<Installation> installationList) {
        this.installationList = installationList;
    }

    @Column(name = "customerID")
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
}
