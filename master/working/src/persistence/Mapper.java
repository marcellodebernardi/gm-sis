package persistence;

import logic.Criterion;
import logic.Searchable;

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
abstract class Mapper<E extends Searchable> {
    MapperFactory factory;
    final String SELECTSTRING = "SELECT * FROM ";
    final String INSERTSTRING = "INSERT INTO ";
    final String UPDATESTRING = "UPDATE ";
    final String DELETESTRING = "DELETE FROM ";

    Mapper(MapperFactory factory) {
        this.factory = factory;
    }
    /**
     * <p>
     *     Parses the given list of Criterion objects into a logical expression of the form
     * </p>
     * <i>(attribute1 AND attribute3) OR (attribute1 AND attribute2)</i>
     * <p>
     *     embedded into a SELECT SQL statement.
     * </p>
     * @param criteria the criterion objects received from the logic layer
     * @return SQL SELECT statement string
     */
    abstract String toSELECTQuery(Criterion<E> criteria);

    /**
     * <p>
     *     Returns an SQL INSERT statement for the given item.
     * </p>
     * @param item item received from logic layer
     * @return SQL INSERT statement string
     */
    abstract String toINSERTTransaction(E item);

    /**
     * <p>
     *     Returns an SQL UPDATE statement for the given item.
     * </p>
     * @param item item received from logic layer
     * @return SQL UPDATE statement string
     */
    abstract String toUPDATETransaction(E item);

    /**
     * <p>
     *     Returns an SQL DELETE statement for the given item.
     * </p>
     * @param item received from logic layer
     * @return SQL DELETE statement string
     */
    abstract String toDELETETransaction(Criterion<E> criteria);

    /**
     * <p>
     *     Returns a list of business entities to be returned to logic layer from
     *     the given ResultSet.
     * </p>
     * @return list of business entities
     */
    abstract List<E> toObjects(ResultSet results);
}