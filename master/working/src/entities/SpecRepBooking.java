package entities;


import logic.Searchable;

import java.util.Date;
/**
 * @author: Muhammad Murad Ahmed 10/02/2017.
 * project: SE31
 */
public abstract class SpecRepBooking implements Searchable {


    private int spcRepID;
    private Date deliveryDate;
    private Date returnDate;
    private double cost;

    // inverse hierarchical database links
    private int spcID;
    private int bookingID;

    /**
     * Constructor to create a specialist repair booking
     * @param spcRepID ID representing a unique SRC booking
     * @param spcID ID representing the unique ID of the SRC to which this part is being sent to
     * @param deliveryDate Date representing the date on which the part or vehicle is being sent
     * @param returnDate Date representing the date on which the part or vehicle is expected to return
     * @param cost Double representing the total cost of the SR booking
     */
    @Reflective // todo remove reflective ability and Searchable?
    public SpecRepBooking(@Simple(name = "spcRepID", primary = true) int spcRepID,
                          @Simple(name = "spcID") int spcID,
                          @Simple(name = "deliveryDate") Date deliveryDate,
                          @Simple(name = "returnDate") Date returnDate,
                          @Simple(name = "cost") double cost,
                          @Simple(name = "bookingID") int bookingID ){
        this.spcRepID = spcRepID;
        this.spcID = spcID;
        this.deliveryDate = deliveryDate;
        this.returnDate = returnDate;
        this.cost = cost;
        this.bookingID = bookingID;
    }

    /**
     *
     * @return int representing a particular bookings ID
     */
    public int getSpcRepID()
    {
        return this.spcRepID;
    }

    /**
     *
     * @return int representing the ID of the SRC responsible for this booking
     */
    public int getSpcID()
    {
        return this.spcID;
    }

    /**
     *
     * @return Date of when the booking has been made
     */
    public Date getDeliveryDate()
    {
        return this.deliveryDate;
    }

    /**
     *
     * @return Date of when the part or vehicle is expected to be returned to the garage
     */
    public Date getReturnDate()
    {
        return this.returnDate;
    }

    /**
     *
     * @return the cost of the SRC booking
     */
    public double getCost()
    {
            return this.cost;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }
}
