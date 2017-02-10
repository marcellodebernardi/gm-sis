package persistence;

import entities.Booking;
import entities.DiagRepBooking;
import entities.User;
import entities.UserType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class UserMapperTests {
    private MapperFactory factory = MapperFactory.getInstance();

    /** Test SELECT statements returned by Mappers */
    @Test
    public void testUserSELECTQuery() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(
                "gooby",
                "dolan",
                "Ebube",
                "Abara",
                UserType.NORMAL ));
        userList.add(new User(
                "someUserID",
                "somePassword",
                "Marcello",
                "De Bernardi",
                UserType.ADMINISTRATOR ));

        assertTrue(factory.getMapper(User.class).toSELECTQuery(userList)
                .equals("SELECT * FROM User WHERE (userID = 'gooby' AND password = 'dolan' "
                        + "AND firstName = 'Ebube' AND surname = 'Abara' AND userType = 'NORMAL')"
                        + " OR (userID = 'someUserID' AND password = 'somePassword' AND firstName = "
                        + "'Marcello' AND surname = 'De Bernardi' AND userType = 'ADMINISTRATOR');"));
    }

    /**
     * Test INSERT statement returned by UserMapper
     */
    @Test
    public void testUserINSERTQuery() {
        User user = new User(
                "user",
                "password",
                "Uncle",
                "Dolan",
                UserType.ADMINISTRATOR);

        assertTrue(factory.getMapper(User.class).toINSERTQuery(user)
                .equals("INSERT INTO User VALUES ('user', 'password', 'Uncle', 'Dolan', 'ADMINISTRATOR');"));
    }

    /**
     * Test UPDATE statement returned by UserMapper
     */
    @Test
    public void testUserUPDATEQuery() {
        User user = new User(
                "user",
                "password",
                "Uncle",
                "Dolan",
                UserType.ADMINISTRATOR);

        assertTrue(factory.getMapper(User.class).toUPDATEQuery(user)
                .equals("UPDATE User SET password = 'password', firstName = 'Uncle', "
                + "surname = 'Dolan', userType = 'ADMINISTRATOR' WHERE userID = 'user';"));
    }

    /**
     * Tests DELETE statement returned by UserMapper
     */
    @Test
    public void testUserDELETEQUery() {
        User user = new User(
                "uniqueID",
                null,
                null,
                null,
                null
        );
        assertTrue(factory.getMapper(User.class).toDELETEQuery(user)
                .equals("DELETE FROM User WHERE userID = 'uniqueID';"));
    }
}