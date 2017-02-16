package entities;

import java.util.Date;
import entities.Vehicle;

/**
 * @author: Muhammad Murad Ahmed 10/02/2017.
 * project: SE31
 * This is the class for sending a particular Vehicle off to a SRC.
 */
public class VehicleSpecialistRepair extends SpecRepBooking {
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
    public VehicleSpecialistRepair(@Simple(name = "spcRepID") int spcRepID,
                                   @Simple(name = "spcID") int spcID,
                                   @Simple(name = "deliveryDate") Date deliveryDate,
                                   @Simple(name = "returnDate") Date returnDate,
                                   @Simple(name = "cost") double cost,
                                   @Simple(name = "bookingID") int bookingID,
                                   @Simple(name = "vehicleRegNumber") String vehicleRegNumber) {
        super(spcRepID, spcID, deliveryDate, returnDate, cost, bookingID);
        this.vehicleRegNumber = vehicleRegNumber;
    }

    @Simple(name = "spcRepID")
    public int getSpcRepID() {
        return super.getSpcRepID();
    }

    @Simple(name = "spcID")
    public int getSpcID() {
        return super.getSpcID();
    }

    @Simple(name = "deliveryDate")
    public Date getDeliveryDate() {
        return super.getDeliveryDate();
    }

    public Date getReturnDate() {
        return super.getReturnDate();
    }

    @Simple(name = "cost")
    public double getCost() {
        return super.getCost();
    }

    @Simple(name = "bookingID")
    public int getBookingID() {
        return super.getBookingID();
    }

    @Simple(name = "vehicleRegNumber")
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }
}



