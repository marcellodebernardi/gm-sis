package domain;


import logic.booking.BookingSystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private BookingSystem bookingSystem;


    /**
     * Creates new SpecRepBooking
     *
     * @param spcID
     * @param deliveryDate
     * @param returnDate
     * @param cost
     * @param bookingID
     */
    public SpecRepBooking(int spcID, Date deliveryDate, Date returnDate, double cost, int bookingID) {
        bookingSystem = BookingSystem.getInstance();
        this.spcRepID = -1;
        this.spcID = spcID;
        this.deliveryDate = deliveryDate;
        this.returnDate = returnDate;
        this.cost = cost;
        this.bookingID = bookingID;
    }

    // reflection only, do not use todo does superclass need to be reflective?
    @Reflective SpecRepBooking(@Column(name = "spcRepID", primary = true) int spcRepID,
                               @Column(name = "spcID") int spcID,
                               @Column(name = "deliveryDate") Date deliveryDate,
                               @Column(name = "returnDate") Date returnDate,
                               @Column(name = "cost") double cost,
                               @Column(name = "bookingID") int bookingID) {
        bookingSystem = BookingSystem.getInstance();
        this.spcRepID = spcRepID;
        this.spcID = spcID;
        this.deliveryDate = deliveryDate;
        this.returnDate = returnDate;
        this.cost = cost;
        this.bookingID = bookingID;
    }


    public int getSpcRepID() {
        return this.spcRepID;
    }

    public int getSpcID() {
        return this.spcID;
    }

    public void setSpcID(int spcID) {
        if (spcID != -1) {
            this.spcID = spcID;
        }
    }

    public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    public Date getReturnDate() {
        return this.returnDate;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        if (!(cost <= -1)) {
            this.cost = cost;
        }
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        List<DiagRepBooking> bookingList = bookingSystem.getAllBookings();
        List<Integer> bookingInfo = new ArrayList<>();
        for (DiagRepBooking diagRepBooking : bookingList) {
            bookingInfo.add(diagRepBooking.getBookingID());
        }
        if (bookingInfo.contains(bookingID)) {
            this.bookingID = bookingID;
        }
    }

    public boolean setDeliveryDate(Date date) {
        if (!date.before(new Date())) {
            this.deliveryDate = date;
            return true;
        }
        return false;
    }

    public boolean setReturnDate(Date date) {
        if (date.after(this.deliveryDate)) {
            this.returnDate = date;
            return true;
        }
        return false;
    }
}
