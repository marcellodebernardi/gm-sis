package entities;


import java.util.Date;
/**
 * @author: Muhammad Murad Ahmed 10/02/2017.
 * project: SE31
 */
public abstract class SpecRepBooking {

    private int spcRepID;
    private int spcID;
    private Date deliveryDate;
    private Date returnDate;
    private double cost;

    /**
     * Constructor to create a specialist repair booking
     * @param spcRepID ID representing a unique SRC booking
     * @param spcID ID representing the unique ID of the SRC to which this part is being sent to
     * @param dD Date representing the date on which the part or vehicle is being sent
     * @param rD Date representing the date on which the part or vehicle is expected to return
     * @param cost Double representing the total cost of the SR booking
     */
    public SpecRepBooking(int spcRepID, int spcID, Date dD, Date rD, double cost){

        this.spcRepID = spcRepID;
        this.spcID = spcID;
        this.deliveryDate = dD;
        this.returnDate = rD;
        this.cost = cost;
    }

    /**
     * 0 argument constructor to allow a search of bookings
     */
    public SpecRepBooking()
    {}

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

    public String getBookingItemID()
    {
        return null;
    }




}
