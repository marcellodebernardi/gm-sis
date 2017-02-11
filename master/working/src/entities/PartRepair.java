package entities;

import java.util.Date;
import entities.PartAbstraction;
/**
 * @author: Muhammad Murad Ahmed 10/02/2017.
 * project: SE31
 * This is the class for sending a particular Vehicle off to a SRC.
 */
public class PartRepair extends SpecRepBooking
{
    private PartAbstraction part;

    /**
     * Constructor for creating a specialist repair booking for a part
     *
     * @param spcRepID
     * @param spcID
     * @param dD
     * @param rD
     * @param cost
     * @param part represents the part being sent off to the SRC
     */
    public PartRepair(int spcRepID, int spcID, Date dD, Date rD, double cost, PartAbstraction part)
    {
        super(spcRepID,spcID, dD,rD, cost);
        this.part = part;
    }

    @Override
    /**
     * Returns the ID of the part booked in for the Specialist Repair Center
     */
    public String getBookingItemID()
    {
        return Integer.toString(part.getPartsID());
    }
}
