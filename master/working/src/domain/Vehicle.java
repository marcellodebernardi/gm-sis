package domain;
import java.util.*;

/**
 * @author Dillon Vaghela on 2/9/17.
 */
public class Vehicle implements Searchable {
    private String vehicleRegNumber;
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
    private List<DiagRepBooking> bookingList;

    // inverse hierarchical database links
    private int customerID;

    @Reflective
    public Vehicle(@Column(name = "vehicleRegNumber", primary = true) String vehicleRegNumber,
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
                   @TableReference(baseType = Installation.class, subTypes = Installation.class, key = "vehicleRegNumber")
                               List<Installation> installationList,
                   @TableReference(baseType = Booking.class, subTypes = DiagRepBooking.class, key = "vehicleRegNumber")
                               List<DiagRepBooking> bookingList) {
        this.vehicleRegNumber = vehicleRegNumber;
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
        this.bookingList = bookingList;
    }


    @Column(name = "vehicleRegNumber", primary = true)
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    @Column(name = "customerID", foreign = true)
    public int getCustomerID() {
        return customerID;
    }

    @Column(name = "vehicleType")
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Column(name = "model")
    public String getModel() {
        return model;
    }

    @Column(name = "manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    @Column(name = "engineSize")
    public double getEngineSize() {
        return engineSize;
    }

    @Column(name = "fuelType")
    public FuelType getFuelType() {
        return fuelType;
    }

    @Column(name = "colour")
    public String getColour() {
        return colour;
    }

    @Column(name = "mileage")
    public int getMileage() {
        return mileage;
    }

    @Column(name = "renewalDateMot")
    public Date getRenewalDateMot() {
        return renewalDateMot;
    }

    @Column(name = "dateLastServiced")
    public Date getDateLastServiced() {
        return dateLastServiced;
    }

    @Column(name = "coveredByWarranty")
    public boolean isCoveredByWarranty() {
        return coveredByWarranty;
    }

    @Column(name = "warrantyName")
    public String getWarrantyName() {
        return warrantyName;
    }

    @Column(name = "warrantyCompAddress")
    public String getWarrantyCompAddress() {
        return warrantyCompAddress;
    }

    @Column(name = "warrantyExpirationDate")
    public Date getWarrantyExpirationDate() {
        return warrantyExpirationDate;
    }

    @TableReference(baseType = Installation.class, subTypes = Installation.class, key = "vehicleRegNumber")
    public List<Installation> getInstallationList() {
        return installationList;
    }

    @TableReference(baseType = Booking.class, subTypes = DiagRepBooking.class, key = "vehicleRegNumber")
    public List<DiagRepBooking> getBookingList() {
        return bookingList;
    }


    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setEngineSize(double engineSize) {
        this.engineSize = engineSize;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setRenewalDateMot(Date renewalDateMot) {
        this.renewalDateMot = renewalDateMot;
    }

    public void setDateLastServiced(Date dateLastServiced) {
        this.dateLastServiced = dateLastServiced;
    }

    public void setCoveredByWarranty(boolean coveredByWarranty) {
        this.coveredByWarranty = coveredByWarranty;
    }

    public void setWarrantyName(String warrantyName) {
        this.warrantyName = warrantyName;
    }

    public void setWarrantyCompAddress(String warrantyCompAddress) {
        this.warrantyCompAddress = warrantyCompAddress;
    }

    public void setWarrantyExpirationDate(Date warrantyExpirationDate) {
        this.warrantyExpirationDate = warrantyExpirationDate;
    }

    public void setInstallationList(List<Installation> installationList) {
        this.installationList = installationList;
    }

    public void setBookingList(List<DiagRepBooking> bookingList) {
        this.bookingList = bookingList;
    }
}
