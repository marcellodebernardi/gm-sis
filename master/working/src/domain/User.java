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
     * @param userID unique ID distinguishing user, application should first check if available
     * @param password user's password for logging in
     * @param firstName user's first name
     * @param surname user's surname
     * @param userType user's type (administrator / normal)
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

    /**
     * Get ID uniquely identifying each user. Used as login username.
     *
     * @return user identification string
     */
    @Column(name = "userID", primary = true)
    public String getUserID() {
        return userID;
    }

    /**
     * Get user's password. Used for login.
     *
     * @return password string
     */
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    /**
     * Get user's first name for displaying.
     *
     * @return first name string
     */
    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get user's surname for displaying.
     *
     * @return surname string
     */
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    /**
     * Get user's type (administrator / normal). Use to determine login elevation.
     *
     * @return UserType enum
     */
    @Column(name = "userType")
    public UserType getUserType() {
        return userType;
    }

    /**
     * Set user's password. Will only be changed permanently once user object has been
     * updated in the persistence layer.
     *
     * @param newPassword user's new password
     */
    public void setPassword(String newPassword) {
        password = newPassword;
    }

    /**
     * Set user's first name. Will only be changed permanently once user object has been
     * updated in the persistence layer.
     *
     * @param newName new first name of user
     */
    public void setFirstName(String newName) {
        firstName = newName;
    }

    /**
     * Set user's surname. Will only be changed permanently once user object has been
     * updated in the persistence layer.
     *
     * @param newName new surname of user
     */
    public void setSurname(String newName) {
        surname = newName;
    }

    /**
     * Set user's UserType. Will only be changed permanently once user object has been
     * updated in the persistence layer.
     *
     * @param newUserType administrator / normal user
     */
    public void setUserType(UserType newUserType) {
        userType = newUserType;
    }
}