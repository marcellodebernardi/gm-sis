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
    public Mechanic(@Simple(name = "mechanicID") int mechanicID,
                    @Simple(name = "firstName") String firstName,
                    @Simple(name = "surname") String surname,
                    @Simple(name = "hourlyRate") double hourlyRate) {
        this.mechanicID = mechanicID;
        this.firstName = firstName;
        this.surname = surname;
        this.hourlyRate = hourlyRate;
    }


    public int getMechanicID() {
        return mechanicID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }
}