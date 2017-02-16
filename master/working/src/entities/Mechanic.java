package entities;

/**
 * @author Marcello De Bernardi
 *
 */
public class Mechanic {
    private int mechanicID;
    private String firstName;
    private String surname;
    private double hourlyRate;

    @Reflective
    public Mechanic(@Simple(name = "mechanicID", primary = true) int mechanicID,
                    @Simple(name = "firstName") String firstName,
                    @Simple(name = "surname") String surname,
                    @Simple(name = "hourlyRate") double hourlyRate) {
        this.mechanicID = mechanicID;
        this.firstName = firstName;
        this.surname = surname;
        this.hourlyRate = hourlyRate;
    }

    @Simple(name = "mechanicID", primary = true)
    public int getMechanicID() {
        return mechanicID;
    }

    public void setMechanicID(int mechanicID) {
        this.mechanicID = mechanicID;
    }

    @Simple(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Simple(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Simple(name = "hourlyRate")
    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}