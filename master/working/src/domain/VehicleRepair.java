package domain;

import java.util.Date;

/**
 * @author: Muhammad Murad Ahmed 10/02/2017.
 * project: SE31
 * This is the class for sending a particular Vehicle off to a spc.
 */
public class VehicleRepair extends SpecRepBooking {
    private String vehicleRegNumber;

    /**
     * Constructor for creating a specialist repair booking for a vehicle.
     *
     * @param spcRepID
     * @param spcID
     * @param deliveryDate
     * @param returnDate
     * @param cost
     * @param vehicleRegNumber Represents the vehicle being booked in for repair
     */
    @Reflective
    private VehicleRepair(@Column(name = "spcRepID", primary = true) int spcRepID,
                          @Column(name = "spcID") int spcID,
                          @Column(name = "deliveryDate") Date deliveryDate,
                          @Column(name = "returnDate") Date returnDate,
                          @Column(name = "cost") double cost,
                          @Column(name = "bookingID") int bookingID,
                          @Column(name = "vehicleRegNumber") String vehicleRegNumber) {
        super(spcRepID, spcID, deliveryDate, returnDate, cost, bookingID);
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public VehicleRepair(int spcID, Date deliveryDate, Date returnDate, double cost, int bookingID, String vehicleRegNumber) {
        super(spcID, deliveryDate, returnDate, cost, bookingID);
        this.vehicleRegNumber = vehicleRegNumber;
    }

    @Column(name = "spcRepID", primary = true)
    public int getSpcRepID() {
        return super.getSpcRepID();
    }

    @Column(name = "spcID", foreign = true)
    public int getSpcID() {
        return super.getSpcID();
    }

    @Column(name = "deliveryDate")
    public Date getDeliveryDate() {
        return super.getDeliveryDate();
    }

    @Column(name = "returnDate")
    public Date getReturnDate() {
        return super.getReturnDate();
    }

    @Column(name = "cost")
    public double getCost() {
        return super.getCost();
    }

    @Column(name = "bookingID", foreign = true)
    public int getBookingID() {
        return super.getBookingID();
    }

    @Column(name = "vehicleRegNumber", foreign = true)
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

}