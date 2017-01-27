package entities;

import logic.Criterion;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public abstract class Booking implements Criterion {
    protected int bookingID;
    protected int customerID;
    protected String vehicleRegNumber;
    protected String description;
    protected Bill bill;
}
