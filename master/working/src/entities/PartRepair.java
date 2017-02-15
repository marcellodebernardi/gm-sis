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
    private int partOccurrenceID;

    /**
     * Constructor for creating a specialist repair booking for a part
     *
     * @param spcRepID
     * @param spcID
     * @param deliveryDate
     * @param returnDate
     * @param cost
     * @param partOccurrenceID represents the part being sent off to the SRC
     */
    @Reflective
    public PartRepair(@Simple(name = "spcRepID") int spcRepID,
                      @Simple(name = "spcID") int spcID,
                      @Simple(name = "deliveryDate") Date deliveryDate,
                      @Simple(name = "returnDate") Date returnDate,
                      @Simple(name = "cost") double cost,
                      @Simple(name = "bookingID") int bookingID,
                      @Simple(name = "partOccurrenceID") int partOccurrenceID) {
        super(spcRepID, spcID, deliveryDate, returnDate, cost, bookingID);
        this.partOccurrenceID = partOccurrenceID;
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
