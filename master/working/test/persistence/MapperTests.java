package persistence;

import entities.Booking;
import entities.DiagnosisRepairBooking;
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
public class MapperTests {
    private MapperFactory factory = MapperFactory.getInstance();


    /** Tests that MapperFactory returns the correct Mapper instances */
    @Test
    public void mapperFactoryTest() {
        assertTrue(factory.getMapper(User.class).getClass().equals(UserMapper.class) &&
                (factory.getMapper(Booking.class).getClass().equals(BookingMapper.class)));
    }

    /** Test SELECT statements returned by Mappers */
    @Test
    public void testUserSelectQuery() {
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

        System.out.println(factory.getMapper(User.class).toSelectQuery(userList));
    }

    /** Test SELECT statements returned by Mappers */
    @Test
    public void testBookingSelectQuery() {
    }
}