package logic;

import entities.Reflective;
import entities.Simple;

import javax.lang.model.type.PrimitiveType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 * Criterion objects are used to interact with CriterionRepositories. They encapsulate criteria
 * by which entities in the persistence layer are identified. A Criterion is created like this:
 * </p>
 * <p>
 * <i>new Criterion(MyClass.class, "attributeName", operator, value)</i>
 * </p>
 * <p>
 * where MyClass is the class of relevant objects (bookings, vehicles, etc), "attributeName" is
 * a string matching exactly the name of a field in said class, value is an object of valid type
 * for said field, and operator is one of the four operators defined in the CriterionOperator enum.
 * </p>
 * <p>
 * The four operators are LessThan, MoreThan, EqualTo and Regex. Regex can only be used when the
 * value object is of type String or type Pattern. The other operators can be used unrestrictedly.
 * </p>
 * <p>
 * More complex search criteria can be used with the append methods and(), or(), and setDiff().
 * These methods are called on an existing criterion, and are used to add a new set of criteria
 * to the Criterion with the same argument syntax used in the constructor, minus the class
 * specification. The new set of criteria is connected to the previous one with the logical
 * connective specified in the method name. For example,
 * </p>
 * <p>
 * <i>new Criterion<>(MyClass.class, "att", LessThan, 10).and("att", MoreThan, 5)</i>
 * </p>
 * <p>
 * defines the expression
 * </p>
 * <p>
 * <i>(att < 10) AND (att > 5)</i>
 * </p>
 * <p>
 * More complex queries can be formed by combining attributes, values and operators with the
 * provided logical connectives.
 * </p>
 * <p>
 * Note that several methods in this class throw CriterionException, which IS NOT A CHECKED
 * EXCEPTION. That is, you do not have to explicitly catch it; however, you need to ensure that
 * you write your criteria properly, so that it is not thrown.
 * </p>
 *
 * @author Marcello De Bernardi
 * @version 0.2
 * @since 0.1
 */
public class Criterion<E extends Searchable> {
    private Class<E> eClass;
    private List<String> attributes;
    private List<CriterionOperator> operators;
    private List<Object> values;
    private List<String> logicalConnectives;
    // todo add overloaded methods for inner criteria of different type?


    /**
     * Creates a new Criterion object. The Class has to match the type of object wanted
     * in return. The attribute string has to match one of the non-inherited fields in
     * the class, and the value must be an object of the type of the field in the class
     * (a wrapper object in case the field is primitive).
     * <p>
     * If a CriterionException is throws, use CriterionException.getMessage() to view the
     * cause of the error.
     *
     * @param eClass    class of the objects to be expected in return
     * @param attribute object variable used as search criterion
     * @param operator  operator applied to value
     * @param value     value for search variable
     * @throws CriterionException if criteria are incorrectly specified
     */
    public Criterion(Class<E> eClass, String attribute, CriterionOperator operator, Object value)
            throws CriterionException {
        if (attribute == null || operator == null || value == null) {
            throw new CriterionException("CONSTRUCTOR: null value in required field. Use single-arg "
                    + "constructor for empty criterion");
        }
        // check compatibility of operator and value
        if (!operatorIsCompatible(operator, value)) throw new CriterionException("CONSTRUCTOR: operator incompatible.");

        // extract fields and check if arguments are compatible
        this.eClass = eClass;
        if (isClassCompatible(attribute, value)) {
            attributes = new ArrayList<>();
            operators = new ArrayList<>();
            values = new ArrayList<>();
            logicalConnectives = new ArrayList<>();
            attributes.add(attribute);
            operators.add(operator);
            values.add(value);
            return;
        }
        // throw error if criteria not acceptable
        throw new CriterionException("CONSTRUCTOR: no such field, or value type does not match field type.");
    }

    /**
     * Creates a criterion object specifying no criteria whatsoever. Is used when all objects of class
     *
     * @param eClass
     */
    public Criterion(Class<E> eClass) {
        this.eClass = eClass;
        attributes = new ArrayList<>();
        operators = new ArrayList<>();
        values = new ArrayList<>();
        logicalConnectives = new ArrayList<>();
    }


    /**
     * Adds a criterion to the Criterion object, connected to previous criteria by an AND
     * logical connective.
     *
     * @param attribute attribute to add to the criterion
     * @param operator  operator connecting attribute to value
     * @param value     value of attribute
     * @return modified Criterion object
     * @throws CriterionException if poorly specified arguments
     */
    public Criterion<E> and(String attribute, CriterionOperator operator, Object value)
            throws CriterionException {
        // check compatibility of operator and value
        if (!operatorIsCompatible(operator, value))
            throw new CriterionException("AND (" + attribute + "): operator incompatible.");

        // check criteria if acceptable
        if (isClassCompatible(attribute, value)) {
            attributes.add(attribute);
            operators.add(operator);
            values.add(value);
            logicalConnectives.add("AND");
            return this;
        }

        // throw error if criteria not acceptable
        throw new CriterionException("AND (" + attribute + "): no such field, or value type "
                + "does not match field type.");
    }

    /**
     * Allows adding a new criterion of a different type to the sequence of criteria. This entails
     * providing the class of the sub-criterion, the name by which it is identified within the
     * parent criterion, and the attributes within the sub-criterion that are to be searched.
     * <p>
     * In SQL terms, it allows subqueries.
     *
     * @param eClass    class of subcriterion
     * @param identity  attribute name by which complex attribute is identified in parent class
     * @param attribute attribute in child class that is to be examin
     * @param operator  operator applying to the attribute
     * @param value     value to use for the attribute
     * @return a new Criterion with the added search criteria
     * @throws CriterionException if poorly specified arguments
     */
    @Deprecated
    public Criterion<E> and(Class<? extends Searchable> eClass, String identity, String attribute,
                            CriterionOperator operator, Object value) throws CriterionException {
        // check value and operator compatibility
        if (!operatorIsCompatible(operator, value))
            throw new CriterionException("AND (" + attribute + "): operator incompatible.");
        // todo implement
        return null;
    }

    /**
     * Adds a criterion to the Criterion object, connected to previous criteria by an OR
     * logical connective.
     *
     * @param attribute attribute to add to the criterion
     * @param operator  operator connecting attribute to value
     * @param value     value of attribute
     * @return modified Criterion object
     * @throws CriterionException if poorly specified arguments
     */
    public Criterion<E> or(String attribute, CriterionOperator operator, Object value)
            throws CriterionException {
        // check compatibility of operator and value
        if (!operatorIsCompatible(operator, value))
            throw new CriterionException("OR (" + attribute + "): operator incompatible.");

        // check criteria if acceptable
        if (isClassCompatible(attribute, value)) {
            attributes.add(attribute);
            operators.add(operator);
            values.add(value);
            logicalConnectives.add("OR");
            return this;
        }

        // throw error if criteria not acceptable
        throw new CriterionException("OR (" + attribute + "): no such field, or value type "
                + "does not match field type.");
    }

    /**
     * Adds a criterion to the Criterion object, connected to previous criteria by a SET DIFFERENCE
     * set operator.
     *
     * @param attribute attribute to add to the criterion
     * @param operator  operator connecting attribute to value
     * @param value     value of attribute
     * @return modified Criterion object
     * @throws CriterionException if poorly specified arguments
     */
    public Criterion<E> setDiff(String attribute, CriterionOperator operator, Object value)
            throws CriterionException {
        // check compatibility of operator and value
        if (!operatorIsCompatible(operator, value))
            throw new CriterionException("SET DIFF (" + attribute + "): operator incompatible.");

        // check criteria if acceptable
        if (isClassCompatible(attribute, value)) {
            attributes.add(attribute);
            operators.add(operator);
            values.add(value);
            logicalConnectives.add("MINUS");
            return this;
        }

        // throw error if criteria not acceptable
        throw new CriterionException("SET DIFF (" + attribute + "): no such field, or value type "
                + "does not match field type.");
    }

    /**
     * <p>
     * Returns a string representing the collected criteria in the following form:
     * </p>
     * <p>
     * <i>ATTRIBUTE1 OPERATOR VALUE</i>    - for a single criterion
     * </p>
     * <p>
     * <i>(ATTRIBUTE1 OPERATOR VALUE) CONNECTIVE (ATTRIBUTE2 OPERATOR VALUE)</i>
     * - for multiple criteria
     * </p>
     * <p>
     * Mostly intended for use by the persistence layer, but can also be used to
     * check validity of criteria.
     * </p>
     *
     * @return String representing search criteria
     */
    public String toString() {
        if (attributes.size() == 0) return "";

        // first element
        String returnString = "" + attributes.get(0);
        switch (operators.get(0)) {
            case EqualTo:
                returnString += " = '" + values.get(0) + "'";
                break;
            case LessThan:
                returnString += " < '" + values.get(0) + "'";
                break;
            case MoreThan:
                returnString += " > '" + values.get(0) + "'";
                break;
            case Regex:
                returnString += " LIKE '" + values.get(0) + "'";
                break;
            default:
                break;
        }

        // additional elements
        if (attributes.size() > 1) {
            for (int i = 1, j = 0; i < attributes.size(); i++, j++) {
                returnString += " " + logicalConnectives.get(j) + " " + attributes.get(i);

                switch (operators.get(0)) {
                    case EqualTo:
                        returnString += " = '" + values.get(i) + "'";
                        break;
                    case LessThan:
                        returnString += " < '" + values.get(i) + "'";
                        break;
                    case MoreThan:
                        returnString += " > '" + values.get(i) + "'";
                        break;
                    case Regex:
                        returnString += " LIKE '" + values.get(i) + "'";
                        break;
                    default:
                        break;
                }
            }
        }
        return returnString;
    }

    /**
     * Returns the class associated with this Criterion.
     *
     * @return class of criterion
     */
    public Class<E> getCriterionClass() {
        return eClass;
    }


    // returns true if attribute and value are compatible with class of Criterion
    private boolean isClassCompatible(String attribute, Object value) {
        // argument types of reflective constructor
        Class<?>[] constructorArgumentTypes = new Class<?>[0];

        for (Constructor<?> c : eClass.getConstructors()) {
            if (c.getDeclaredAnnotations()[0].annotationType().equals(Reflective.class)) {
                constructorArgumentTypes = c.getParameterTypes();
                break;
            }
        }

        // get reflective constructor and check:
        // 1. a parameter is annotated as Simple(name = "attribute")
        // 2. the same parameter is of the same class as value
        try {
            Constructor<E> constructor = eClass.getConstructor(constructorArgumentTypes);
            Annotation[][] annotations = constructor.getParameterAnnotations();

            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i][0].annotationType().equals(Simple.class)) {
                    Simple metadata = (Simple)annotations[i][0];
                    if (metadata.name().equals(attribute)) {
                        return constructorArgumentTypes[i].isPrimitive() ?
                                constructorArgumentTypes[i].toString().substring(0,1)
                                        .equalsIgnoreCase(value.getClass().getSimpleName().substring(0,1))
                                : constructorArgumentTypes[i].getClass().equals(value.getClass());
                    }
                }
            }
            return false;
        }
        catch (NoSuchMethodException e) {
            System.err.print(e.getMessage());
            return false;
        }
    }

    // returns true if operator and value are compatible
    private boolean operatorIsCompatible(CriterionOperator operator, Object value) {
        return !(operator.equals(CriterionOperator.Regex) &&
                !(value.getClass().equals(String.class) || value.getClass().equals(Pattern.class)));
    }
}