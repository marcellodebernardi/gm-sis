package entities;

import logic.Criterion;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class Vehicle implements Criterion
{
    private String regNumber;
    private int customerID;
    //private VehicleType vehicleType;
    private String model;
    private String manufacturer;
    private double engineSize;
    //private FuelType fuelType;
    private String colour;
    private String mileage;
    //private Date renewalDateMot;
    //private Date dateLastServiced;
    private boolean coveredByWarranty;
    private String warrantyName;
    private String warrantyCompAddress;
    //private Date WarrantyExpirationDate;

    public Vehicle()
    {

    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    //public VehicleType getVehicleType() {
    //    return vehicleType;
    //}

    //public void setVehicleType(VehicleType vehicleType) {
     //   this.vehicleType = vehicleType;
    //}

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(double engineSize) {
        this.engineSize = engineSize;
    }

    //public FuelType getFuelType() {
     //   return fuelType;
    //}

    //public void setFuelType(FuelType fuelType) {
      //  this.fuelType = fuelType;
    //}

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    //public Date getRenewalDateMot() {
    //    return renewalDateMot;
    //}

    //public void setRenewalDateMot(Date renewalDateMot) {
    //   this.renewalDateMot = renewalDateMot;
    //}

    //public Date getDateLastServiced() {
    //   return dateLastServiced;
    //}

    //public void setDateLastServiced(Date dateLastServiced) {
    //   this.dateLastServiced = dateLastServiced;
    //}

    public boolean isCoveredByWarranty() {
        return coveredByWarranty;
    }

    public void setCoveredByWarranty(boolean coveredByWarranty) {
        this.coveredByWarranty = coveredByWarranty;
    }

    public String getWarrantyName() {
        return warrantyName;
    }

    public void setWarrantyName(String warrantyName) {
        this.warrantyName = warrantyName;
    }

    public String getWarrantyCompAddress() {
        return warrantyCompAddress;
    }

    public void setWarrantyCompAddress(String warrantyCompAddress) {
        this.warrantyCompAddress = warrantyCompAddress;
    }

    //public Date getWarrantyExpirationDate() {
    //   return WarrantyExpirationDate;
    //}

    //public void setWarrantyExpirationDate(Date warrantyExpirationDate) {
    //   WarrantyExpirationDate = warrantyExpirationDate;
    //}
}
