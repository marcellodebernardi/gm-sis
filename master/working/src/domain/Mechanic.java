package domain;

import logic.criterion.Criterion;
import persistence.DatabaseRepository;

import java.util.List;

import static logic.criterion.CriterionOperator.equalTo;

/**
 * @author Marcello De Bernardi
 */
public class Mechanic implements Searchable {
    private int mechanicID;
    private String firstName;
    private String surname;
    private double hourlyRate;


    @Reflective
    private Mechanic(@Column(name = "mechanicID", primary = true) int mechanicID,
                     @Column(name = "firstName") String firstName,
                     @Column(name = "surname") String surname,
                     @Column(name = "hourlyRate") double hourlyRate) {
        this.mechanicID = mechanicID;
        this.firstName = firstName;
        this.surname = surname;
        this.hourlyRate = hourlyRate;
    }


    @Column(name = "mechanicID", primary = true)
    public int getMechanicID() {
        return mechanicID;
    }

    public void setMechanicID(int mechanicID) {
        this.mechanicID = mechanicID;
    }

    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Column(name = "hourlyRate")
    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Lazy
    public List<DiagRepBooking> getBookings() {
        return DatabaseRepository.getInstance().getByCriteria(new Criterion<>
                (DiagRepBooking.class, "mechanicID", equalTo, mechanicID));
    }
}