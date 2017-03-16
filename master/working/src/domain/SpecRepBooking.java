package domain;


import logic.booking.BookingSystem;

import java.util.ArrayList;
import java.util.List;
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

    private BookingSystem bookingSystem = BookingSystem.getInstance();

    // dependency connection


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
     * @return int representing the ID of the spc responsible for this booking
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
     * @return the cost of the spc booking
     */
    public double getCost()
    {
            return this.cost;
    }

    public int getBookingID() {
        return bookingID;
    }

    /**
     * Checks that a booking ID, when updated is from an existing bookig
     *
     * Efficiency could be improved drastically
     *
     * @param bookingID new ID which is modified
     */
    public void setBookingID(int bookingID) {
        List<DiagRepBooking> bookingList = bookingSystem.getAllBookings();
        List<Integer> bookingInfo = new ArrayList<>();
        for(DiagRepBooking diagRepBooking: bookingList)
        {
            bookingInfo.add(diagRepBooking.getBookingID());
        }
        if(bookingInfo.contains(bookingID)) {
            this.bookingID = bookingID;
        }
    }

    public void setSpcID(int spcID)
    {
        if(spcID!=-1) {
            this.spcID = spcID;
        }
    }

    public boolean setDeliveryDate(Date date)
    {
        if(!date.before(new Date())) {
            this.deliveryDate = date;
            return true;
        }
        return false;
    }

    public boolean setReturnDate(Date date)
    {
        if(date.after(this.deliveryDate)) {
            this.returnDate = date;
            return true;
        }
                return false;
    }

    public void setCost(double cost)
    {
        if(cost!=-1)
        {
        this.cost = cost;
        }
    }
}
