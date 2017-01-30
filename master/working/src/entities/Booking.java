package entities;

import logic.Criterion;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public abstract class Booking implements Criterion {

    public Booking(){}

    // TODO consider this kind of persistence solution
    public static Iterable<Booking> getIterable() {
        return null;
        // the idea here is that this method would return a list of all bookings from persistence layer
    }

    public static void saveIterable() {
        // and the idea here is that this method would save a list of all booking into the persistence layer
        // which would entail removing, adding and updating items as necessary
    }





    protected int bookingID;
    protected int customerID;
    protected String vehicleRegNumber;
    protected String description;
    protected Bill bill;

    Booking(int customerID, String vehicleRegNumber, String description) {
        bookingID = 0; // TODO make the persistence layer handle this
        this.customerID = customerID;
        this.vehicleRegNumber = vehicleRegNumber;
        this.description = description;

        bill = null; // TODO figure this out
    }
}
