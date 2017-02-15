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
    private PartOccurrence part;

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
    public PartRepair(int spcRepID, int spcID, Date dD, Date rD, double cost, PartOccurrence part)
    {
        super(spcRepID,spcID, dD,rD, cost, 0);
        this.part = part;
    }


    /**
     * Returns the ID of the part booked in for the Specialist Repair Center
     */
    @Override
    public String getBookingItemID()
    {
        return null;
       //return Integer.toString(part.());
    }
}
