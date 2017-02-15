package logic;

import entities.*;
import logic.Criterion;
import logic.CriterionRepository;
import org.junit.Test;
import static org.junit.Assert.*;

import persistence.DatabaseRepository;

import java.util.List;

import static logic.CriterionOperator.*;


/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class PersistenceTests {
    CriterionRepository persistence = DatabaseRepository.getInstance();

    /**
     * Tests the parsing of lists of criteria into string representing SQL statements.
     */
    @Test
    public void testParseSQLwhere() {
    }

    @Test
    public void getSingleUserFromDatabase() {
        User user = persistence.getByCriteria(new Criterion<>(User.class, "userID", EqualTo, "00000").
                and("password", EqualTo, "password")).get(0);
        System.out.println(user.getUserID() + " " + user.getPassword());
        assertNotNull(user);
    }

    @Test
    public void getAllCustomerFromDatabase() {
        List<Customer> customers = persistence.getByCriteria(
                new Criterion<>(Customer.class));

        for(Customer c : customers) {
            System.out.println(c.getCustomerID() + " " + c.getCustomerFirstname() + " " + c.getCustomerSurname());
            System.out.println("Vehicles: " + c.getVehicles().size());
            if (c.getVehicles().size() > 0) {
                System.out.println(c.getVehicles().get(0).getDateLastServiced());
            }
        }
        assertNotNull(customers);
    }

    @Test
    public void getAllPartOccurrences() {
        List<PartOccurrence> parts = persistence.getByCriteria(new Criterion<>(PartOccurrence.class));

        for (PartOccurrence p : parts)
            System.out.println(p.getPartOccurrenceID());
    }

    @Test
    public void getAllVehicles() {
        List<Vehicle> vehicles = persistence.getByCriteria(new Criterion<>(Vehicle.class));

        for (Vehicle v : vehicles) {
            System.out.println(v.getRegNumber() + " " + v.getCustomerID());
        }
    }

    @Test
    public void getAllBookings() {
        List<DiagRepBooking> bookings = persistence.getByCriteria(new Criterion<>(DiagRepBooking.class));

        if (bookings.get(0).getSpecRepBooking() == null) System.out.println("No spc booking");

        for (DiagRepBooking b : bookings) {
            System.out.println(b.getBookingID() + ", " + b.getCustomerID() + ", " + b.getVehicleRegNumber()
                    + ", " + b.getRepairInterval() + ", spc " + b.getSpecRepBooking().getSpcRepID());
        }
    }
}
