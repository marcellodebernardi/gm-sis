package domain;

import logic.criterion.Criterion;
import persistence.DatabaseRepository;

import java.util.List;

import static logic.criterion.CriterionOperator.equalTo;

/**
 * @author Marcello De Bernardi
 * @version 0.2
 * @since 0.1
 */
public abstract class Booking implements Searchable {
    private int bookingID;
    private String description;
    private boolean complete;

    // inverse hierarchical database links
    private String vehicleRegNumber;
    private Bill bill;
    private int mechanicID;


    public Booking() {
        this.bookingID = -1;
        this.description = null;
        this.complete = false;
        this.vehicleRegNumber = null;
        this.bill = null;
        this.mechanicID = -1;
    }

    /**
     * Full constructor which allows setting all fields manually.
     *
     * @param bookingID        unique ID of this booking
     * @param vehicleRegNumber unique registration number of vehicle
     * @param description      description of booking as entered by some user
     * @param bill             the associated bill
     * @param mechanicID       the ID of the associated mechanic
     */
    public Booking(int bookingID, String vehicleRegNumber, String description,
                   Bill bill, int mechanicID, boolean complete) {
        this.bookingID = bookingID;
        this.vehicleRegNumber = vehicleRegNumber;
        this.description = description;
        this.bill = bill;
        this.mechanicID = mechanicID;
        this.complete = complete;
    }


    public int getBookingID() {
        return bookingID;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    @Lazy public Vehicle getVehicle() {
        List<Vehicle> vehicles = DatabaseRepository.getInstance().getByCriteria(new Criterion<>
                (Vehicle.class, "vehicleRegNumber", equalTo, vehicleRegNumber));

        return vehicles != null && vehicles.size() != 0 ? vehicles.get(0) : null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public int getMechanicID() {
        return mechanicID;
    }

    public void setMechanicID(int mechanicID) {
        this.mechanicID = mechanicID;
    }

    @Lazy public Mechanic getMechanic() {
        List<Mechanic> mechanics = DatabaseRepository.getInstance().getByCriteria(new Criterion<>
                (Mechanic.class, "mechanicID", equalTo, mechanicID));

        return mechanics != null && mechanics.size() != 0 ? mechanics.get(0) : null;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
