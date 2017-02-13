package persistence;

import entities.Bill;
import entities.DiagRepBooking;
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
        assertEquals(factory.getMapper(DiagRepBooking.class).toSELECTQuery(null),
                "SELECT * FROM DiagRepBooking WHERE (bookingID = 23 AND customerID = 25) "
                        + "OR (bookingID = 56 AND bill = 12);");
    }

}
