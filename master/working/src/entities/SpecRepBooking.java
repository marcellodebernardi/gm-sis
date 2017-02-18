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
     * Creates new SpecRepBooking
     * @param spcID
     * @param deliveryDate
     * @param returnDate
     * @param cost
     * @param bookingID
     */
    public SpecRepBooking(int spcID, Date deliveryDate, Date returnDate, double cost, int bookingID)
    {
        this.spcRepID = -1;
        this.spcID = spcID;
        this.deliveryDate = deliveryDate;
        this.returnDate = returnDate;
        this.cost = cost;
        this.bookingID = bookingID;
    }

    // reflection only, do not use todo does superclass need to be reflective?
    @Reflective
    SpecRepBooking(@Column(name = "spcRepID", primary = true) int spcRepID,
                          @Column(name = "spcID") int spcID,
                          @Column(name = "deliveryDate") Date deliveryDate,
                          @Column(name = "returnDate") Date returnDate,
                          @Column(name = "cost") double cost,
                          @Column(name = "bookingID") int bookingID ){
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
