package persistence;

import entities.Bill;
import entities.DiagRepBooking;
import entities.User;
import entities.UserType;
import logic.CriterionRepository;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DiagRepMapperTests {
    MapperFactory factory = MapperFactory.getInstance();

    @Test
    public void testDiagRepSELECTQuery() {
        List<DiagRepBooking> bookingList = new ArrayList<>();
        bookingList.add(new DiagRepBooking(
                23,
                25,
                null,
                null,
                null,
                null,
                null,
                null));
        bookingList.add(new DiagRepBooking(
                56,
                -1,
                null,
                null,
                new Bill(12, 200, false),
                null,
                null,
                null));

        assertEquals(factory.getMapper(DiagRepBooking.class).toSelectQuery(bookingList),
                "SELECT * FROM DiagRepBooking WHERE (bookingID = 23 AND customerID = 25) "
                        + "OR (bookingID = 56 AND bill = 12);");
    }
}
