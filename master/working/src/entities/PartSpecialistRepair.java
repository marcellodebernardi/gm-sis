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

    @Simple(name = "spcRepID")
    public int getSpcRepID() {
        return super.getSpcRepID();
    }

    public void setSpcRepID() {
        // todo implement
    }

    @Simple(name = "spcID")
    public int getspcID() {
        return super.getSpcID();
    }

    public void setSpcID() {
        // todo implement
    }

    @Simple(name = "deliveryDate")
    public Date getDeliveryDate() {
        return super.getDeliveryDate();
    }

    public void setDeliveryDate() {
        // todo implement
    }

    @Simple(name = "returnDate")
    public Date getReturnDate() {
        return super.getReturnDate();
    }

    public void setReturnDate() {
        // todo implement
    }

    @Simple(name = "cost")
    public double getCost() {
        return super.getCost();
    }

    public void setCost() {
        // todo implement
    }

    @Simple(name = "bookingID")
    public int getBookingID() {
        return super.getBookingID();
    }

    public void setBookingID(int bookingID) {
        super.setBookingID(bookingID);
    }

    @Simple(name = "partOccurrenceID")
    public int getPartOccurrenceID() {
        return partOccurrenceID;
    }

    public void setPartOccurrenceID(int partOccurrenceID) {
        this.partOccurrenceID = partOccurrenceID;
    }
}
