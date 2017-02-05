package persistence;

import entities.Booking;
import entities.User;
import logic.Criterion;

import java.util.Map;
import java.util.TreeMap;

/**
 * Returns Mapper objects of appropriate type to handle object-relational mapping
 * for a particular type of entity.
 *
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
class MapperFactory {
    private Map<String, Mapper<? extends Criterion>> mapperMap = new TreeMap<>();

    MapperFactory() {
        mapperMap.put(User.class.getName(), new UserMapper());
        mapperMap.put(Booking.class.getName(), new BookingMapper());
    }

    /**
     * Returns a reference to the Mapper object associated with the class passed
     * as argument.
     *
     * @param eClass class for which Mapper is desired
     * @param <E> extends Criterion
     * @return Mapper for desired class
     */
    <E extends Criterion> Mapper<E> getMapper(Class<E> eClass) {
        // todo ensure this cannot break
        return (Mapper<E>)mapperMap.get(eClass.getName());
    }
}
