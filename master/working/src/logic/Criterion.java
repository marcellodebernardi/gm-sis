package logic;

import java.util.SortedMap;

/**
 * * <p>
 * A Criterion object is used to interact with a <i>CriterionRepository</i>. It defines a set of
 * required attributes that may be matched to data in a persistence layer. For example:
 * </p>
 * <i>Criterion criteria = new MyClass("Birthday", null, null)</i>
 * <p>
 * creates a Criterion object that will match any persistent data entity with the string "Birthday"
 * in the corresponding field.
 * </p>
 * <p>
 * Any object implementing this interface may be passed to an object implementing the interface
 * <i>PersistenceInterface</i>, with the values of its attributes representing the parameters of
 * the query being made to the persistence layer.
 * </p>
 *
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public interface Criterion {
}
