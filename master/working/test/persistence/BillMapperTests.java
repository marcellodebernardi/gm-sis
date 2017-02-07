package persistence;

import entities.Bill;
import entities.Booking;
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
public class BillMapperTests {
    private MapperFactory factory = MapperFactory.getInstance();

    /** Test SELECT statements returned by Mappers */
    @Test
    public void testBillSELECTQuery() {
        List<Bill> billList = new ArrayList<>();
        billList.add(new Bill(
                123,
                200.0,
                false));
        billList.add(new Bill(
                145,
                100.0,
                true));

        assertEquals(factory.getMapper(Bill.class).toSelectQuery(billList), "SELECT * FROM Bill WHERE "
                + "(billID = 123 AND amount = 200.0 AND settled = 'false') OR (billID = 145 AND amount = "
                + "100.0 AND settled = 'true');");
    }

    /**
     * Test INSERT statement returned by BillMapper
     */
    @Test
    public void testBillINSERTQuery() {
        Bill bill = new Bill(
                123,
                200.0,
                true);

        assertTrue(factory.getMapper(Bill.class).toInsertQuery(bill)
                .equals("INSERT INTO Bill VALUES (123, 200.0, true);"));
    }

    /**
     * Test UPDATE statement returned by UserMapper
     */
    @Test
    public void testBillUPDATEQuery() {
        Bill bill = new Bill(
                123,
                200.0,
                false);

        assertTrue(factory.getMapper(Bill.class).toUpdateQuery(bill)
                .equals("UPDATE Bill SET amount = 200.0, settled = 'false' "
                        + "WHERE billID = 123;"));
    }

    /**
     * Tests DELETE statement returned by UserMapper
     */
    @Test
    public void testUserDELETEQUery() {
        Bill bill = new Bill(123, 1.0, false);

        assertTrue(factory.getMapper(Bill.class).toDeleteQuery(bill)
                .equals("DELETE FROM Bill WHERE billID = 123;"));
    }
}