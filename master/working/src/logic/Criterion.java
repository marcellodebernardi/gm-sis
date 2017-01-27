package logic;

import java.util.Map;

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

    /**
     * <p>Returns the values and variable names of the object as a Map<String, String>. This map must
     * conform to the following rules:
     * <ol>
     * <li>One key must be "class", and it must map to the name of the class implementing Criterion</li>
     * <li>For each variable in the class, a key entry must be present</li>
     * <li>For "multi-valued" variables, the key must map to a value of form "value1, value2, ..., valueN"</li>
     * <li>For variables set to null, no key should be added to map
     * </ol>
     * </p>
     *
     * @return a Map<> containing variable names as map keys and variable values as map values.
     */
    Map<String, String> getAttributes();
}
