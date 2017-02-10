package entities;

import java.util.Date;

/**
 * @author: Muhammad Murad Ahmed 10/02/2017.
 * project: SE31
 * This is the class for sending a particular Vehicle off to a SRC.
 */
public class VehicleRepair extends SpecRepBooking{
    private String vehicleReg;

    /**
     * Constructor for creating a specialist repair booking for a vehicle.
     *
     * @param spcRepID
     * @param spcID
     * @param dD
     * @param rD
     * @param cost
     * @param vehicleReg Represents the registration of the vehicle being sent to SRC
     */
    public VehicleRepair(int spcRepID, int spcID, Date dD, Date rD, double cost, String vehicleReg)
    {
        super(spcRepID,spcID, dD,rD, cost);
        this.vehicleReg = vehicleReg;
    }

}



