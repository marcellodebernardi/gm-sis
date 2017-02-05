package persistence;

import logic.Criterion;

import java.sql.ResultSet;
import java.util.List;

/**
 * A Mapper is an object tasked with handling the object-relational mapping of some specific
 * type of business entity.
 *
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
abstract class Mapper<E extends Criterion> {
    protected final String SELECTSTRING = "SELECT * FROM ";
    protected final String INSERTSTRING = "INSERT";
    protected final String UPDATESTRING = "UPDATE";
    protected final String DELETESTRING = "DELETE";
    // todo fix these

    /**
     *
     * @param criteria
     * @return
     */
    abstract String toSelectQuery(List<E> criteria);

    abstract String toInsertQuery(E item);

    abstract String toUpdateQuery(E item);

    abstract String toDeleteQuery(E item);
    /**
     *
     * @return
     */
    abstract List<E> toObjects(ResultSet results);
}
