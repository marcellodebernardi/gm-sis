package domain;

import java.util.Date;

/**
 * @author: Muhammad Murad Ahmed 10/02/2017.
 * project: SE31
 * This is the class for sending a particular Vehicle off to a spc.
 */
public class PartRepair extends SpecRepBooking {
    private int partOccurrenceID;


    /**
     * Creates a new PartRepair
     *
     * @param spcID
     * @param deliveryDate
     * @param returnDate
     * @param cost
     * @param bookingID
     * @param partOccurrenceID
     */
    public PartRepair(int spcID, Date deliveryDate, Date returnDate, double cost, int bookingID,
                      int partOccurrenceID) {
        super(spcID, deliveryDate, returnDate, cost, bookingID);
        this.partOccurrenceID = partOccurrenceID;
    }

    // reflection only, do not use
    @Reflective
    private PartRepair(@Column(name = "spcRepID", primary = true) int spcRepID,
                       @Column(name = "spcID") int spcID,
                       @Column(name = "deliveryDate") Date deliveryDate,
                       @Column(name = "returnDate") Date returnDate,
                       @Column(name = "cost") double cost,
                       @Column(name = "bookingID") int bookingID,
                       @Column(name = "partOccurrenceID") int partOccurrenceID) {
        super(spcRepID, spcID, deliveryDate, returnDate, cost, bookingID);
        this.partOccurrenceID = partOccurrenceID;
    }


    @Column(name = "spcRepID", primary = true)
    public int getSpcRepID() {
        return super.getSpcRepID();
    }

    @Column(name = "spcID", foreign = true)
    public int getspcID() {
        return super.getSpcID();
    }

    @Column(name = "deliveryDate")
    public Date getDeliveryDate() {
        return super.getDeliveryDate();
    }

    @Column(name = "returnDate")
    public Date getReturnDate() {
        return super.getReturnDate();
    }

    @Column(name = "cost")
    public double getCost() {
        return super.getCost();
    }

    @Column(name = "bookingID", foreign = true)
    public int getBookingID() {
        return super.getBookingID();
    }

    public void setBookingID(int bookingID) {
        super.setBookingID(bookingID);
    }

    @Column(name = "partOccurrenceID", foreign = true)
    public int getPartOccurrenceID() {
        return partOccurrenceID;
    }

    public void setPartOccurrenceID(int partOccurrenceID) {
        this.partOccurrenceID = partOccurrenceID;
    }
}