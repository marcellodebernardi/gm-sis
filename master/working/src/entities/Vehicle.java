package entities;
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
    private List<Installation> installationList; // todo getter setter

    // inverse hierarchical database links
    private int customerID;

    @Reflective
    public Vehicle(@Simple(name = "regNumber", primary = true) String regNumber,
                   @Simple(name = "customerID") int customerID,
                   @Simple(name = "vehicleType") VehicleType vehicleType,
                   @Simple(name = "model") String model,
                   @Simple(name = "manufacturer") String manufacturer,
                   @Simple(name = "engineSize") double engineSize,
                   @Simple(name = "fuelType") FuelType fuelType,
                   @Simple(name = "colour") String colour,
                   @Simple(name = "mileage") int mileage,
                   @Simple(name = "renewalDateMot") Date renewalDateMot,
                   @Simple(name = "dateLastServiced") Date dateLastServiced,
                   @Simple(name = "coveredByWarranty") boolean coveredByWarranty,
                   @Simple(name = "warrantyName") String warrantyName,
                   @Simple(name = "warrantyCompAddress") String warrantyCompAddress,
                   @Simple(name = "warrantyExpirationDate") Date warrantyExpirationDate,
                   @Complex(baseType = Installation.class, specTypes = Installation.class, key = "vehicleRegNumber")
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


    @Simple(name = "regNumber", primary = true)
    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    @Simple(name = "vehicleType")
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Simple(name = "model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Simple(name = "manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Simple(name = "engineSize")
    public double getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(double engineSize) {
        this.engineSize = engineSize;
    }

    @Simple(name = "fuelType")
    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    @Simple(name = "colour")
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Simple(name = "mileage")
    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    @Simple(name = "renewalDateMot")
    public Date getRenewalDateMot() {
        return renewalDateMot;
    }

    public void setRenewalDateMot(Date renewalDateMot) {
        this.renewalDateMot = renewalDateMot;
    }

    @Simple(name = "dateLastServiced")
    public Date getDateLastServiced() {
        return dateLastServiced;
    }

    public void setDateLastServiced(Date dateLastServiced) {
        this.dateLastServiced = dateLastServiced;
    }

    @Simple(name = "coveredByWarranty")
    public boolean isCoveredByWarranty() {
        return coveredByWarranty;
    }

    public void setCoveredByWarranty(boolean coveredByWarranty) {
        this.coveredByWarranty = coveredByWarranty;
    }

    @Simple(name = "warrantyName")
    public String getWarrantyName() {
        return warrantyName;
    }

    public void setWarrantyName(String warrantyName) {
        this.warrantyName = warrantyName;
    }

    @Simple(name = "warrantyCompAddress")
    public String getWarrantyCompAddress() {
        return warrantyCompAddress;
    }

    public void setWarrantyCompAddress(String warrantyCompAddress) {
        this.warrantyCompAddress = warrantyCompAddress;
    }

    @Simple(name = "warrantyExpirationDate")
    public Date getWarrantyExpirationDate() {
        return warrantyExpirationDate;
    }

    public void setWarrantyExpirationDate(Date warrantyExpirationDate) {
        this.warrantyExpirationDate = warrantyExpirationDate;
    }

    @Complex(baseType = Installation.class, specTypes = Installation.class, key = "vehicleID")
    public List<Installation> getInstallationList() {
        return installationList;
    }

    public void setInstallationList(List<Installation> installationList) {
        this.installationList = installationList;
    }

    @Simple(name = "customerID")
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
}
