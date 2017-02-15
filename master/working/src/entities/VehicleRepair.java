package entities;

import java.util.Date;
import entities.Vehicle;

/**
 * @author: Muhammad Murad Ahmed 10/02/2017.
 * project: SE31
 * This is the class for sending a particular Vehicle off to a SRC.
 */
public class VehicleRepair extends SpecRepBooking
{
    private Vehicle vehicle;

    /**
     * Constructor for creating a specialist repair booking for a vehicle.
     *
     * @param spcRepID
     * @param spcID
     * @param dD
     * @param rD
     * @param cost
     * @param vehicle Represents the vehicle being booked in for repair
     */
    public VehicleRepair(int spcRepID, int spcID, Date dD, Date rD, double cost, Vehicle vehicle)
    {
        super(spcRepID,spcID, dD,rD, cost, 0);
        this.vehicle = vehicle;
    }

    @Override
    /**
     * Returns the registration number of the vehicle booked in for specialist repair center
     */
    public String getBookingItemID() {
        return vehicle.getRegNumber();
    }
}



