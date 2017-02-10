package persistence;

import entities.*;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class MapperFactoryTests {
    private MapperFactory factory = MapperFactory.getInstance();

    /** Tests that MapperFactory returns the correct Mapper instances */
    @Test
    public void mapperFactoryTest() {
        assertTrue(factory.getMapper(User.class).getClass().equals(UserMapper.class) &&
                factory.getMapper(DiagRepBooking.class).getClass().equals(DiagRepBookingMapper.class) &&
                factory.getMapper(SpecialistRepairCenter.class).getClass().equals(SRCMapper.class) &&
                factory.getMapper(Bill.class).getClass().equals(BillMapper.class) &&
                factory.getMapper(PartAbstraction.class).getClass().equals(PartAbstractionMapper.class));
    }
}
