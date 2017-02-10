package entities;

import java.util.Date;
/**
 * @author: Muhammad Murad Ahmed 10/02/2017.
 * project: SE31
 * This is the class for sending a particular Vehicle off to a SRC.
 */
public class PartRepair extends SpecRepBooking {

    private String partID;

    /**
     * Constructor for creating a specialist repair booking for a part
     *
     * @param spcRepID
     * @param spcID
     * @param dD
     * @param rD
     * @param cost
     * @param partID represents the part being sent off to the SRC
     */
    public PartRepair(int spcRepID, int spcID, Date dD, Date rD, double cost, String partID)
    {
        super(spcRepID,spcID, dD,rD, cost);
        this.partID = partID;
    }
}
