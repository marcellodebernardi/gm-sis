package persistence;

import entities.Booking;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
class BookingMapper extends Mapper<Booking> {
    // todo implement

    String toSelectQuery(List<Booking> bookings) {
        return null;
    }

    String toInsertQuery(Booking booking) {
        return null;
    }

    String toUpdateQuery(Booking booking) {
        return null;
    }

    String toDeleteQuery(Booking booking) {
        return null;
    }

    List<Booking> toObjects(ResultSet results) {
        return null;
    }
}
