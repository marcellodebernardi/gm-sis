package entities;

import java.util.Date;
import entities.Vehicle;

/**
 * @author: Muhammad Murad Ahmed 10/02/2017.
 * project: SE31
 * This is the class for sending a particular Vehicle off to a SRC.
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
    public VehicleRepair(@Simple(name = "spcRepID") int spcRepID,
                         @Simple(name = "spcID") int spcID,
                         @Simple(name = "deliveryDate") Date deliveryDate,
                         @Simple(name = "returnDate") Date returnDate,
                         @Simple(name = "cost") double cost,
                         @Simple(name = "bookingID") int bookingID,
                         @Simple(name = "vehicleRegNumber") String vehicleRegNumber) {
        super(spcRepID, spcID, deliveryDate, returnDate, cost, bookingID);
        this.vehicleRegNumber = vehicleRegNumber;
    }


    /**
     * Returns the registration number of the vehicle booked in for specialist repair center
     */
    @Override
    public String getBookingItemID() {
        return vehicleRegNumber;
    }
}



