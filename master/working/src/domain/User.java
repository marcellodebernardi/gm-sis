package domain;

/**
 * @author Marcello De Bernardi
 * @version 1.0
 * @since 0.1
 */
public class User implements Searchable {
    private String userID;
    private String password;
    private String firstName;
    private String surname;
    private UserType userType;

    /**
     * Full constructor allowing all fields to be freely set by application code.
     *
     * @param userID    unique ID distinguishing user, application should first check if available
     * @param password  user's password for logging in
     * @param firstName user's first name
     * @param surname   user's surname
     * @param userType  user's type (administrator / normal)
     */
    @Reflective
    public User(@Column(name = "userID", primary = true) String userID,
                @Column(name = "password") String password,
                @Column(name = "firstName") String firstName,
                @Column(name = "surname") String surname,
                @Column(name = "userType") UserType userType) {
        this.userID = userID;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.userType = userType;
    }


    @Column(name = "userID", primary = true)
    public String getUserID() {
        return userID;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String newName) {
        firstName = newName;
    }

    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String newName) {
        surname = newName;
    }

    @Column(name = "userType")
    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType newUserType) {
        userType = newUserType;
    }
}