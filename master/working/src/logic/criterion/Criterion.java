package logic.criterion;

import domain.Column;
import domain.Reflective;
import domain.Searchable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.regex.Pattern;

import static logic.criterion.CriterionException.Cause.*;
import static logic.criterion.CriterionOperator.in;
import static logic.criterion.CriterionOperator.matches;

/**
 * <p>
 * Criterion objects are used to interact with CriterionRepositories. They encapsulate criteria
 * by which domain in the persistence layer are identified. A Criterion is created like this:
 * </p>
 * <p>
 * <i>new Criterion(MyClass.class, "attributeName", operator, value)</i>
 * </p>
 * <p>
 * where MyClass is the class of relevant objects (bookings, vehicle, etc), "attributeName" is
 * a string matching exactly the name of a field in said class, value is an object of valid type
 * for said field, and operator is one of the four operators defined in the CriterionOperator enum.
 * </p>
 * <p>
 * The four operators are lessThan, moreThan, equalTo and matches. matches can only be used when the
 * value object is of type String or type Pattern. The other operators can be used unrestrictedly.
 * </p>
 * <p>
 * More complex search criteria can be used with the append methods and(), or(), and except().
 * These methods are called on an existing criterion, and are used to add a new set of criteria
 * to the Criterion with the same argument syntax used in the constructor, minus the class
 * specification. The new set of criteria is connected to the previous one with the logical
 * connective specified in the method name. For example,
 * </p>
 * <p>
 * <i>new Criterion<>(MyClass.class, "att", lessThan, 10).and("att", moreThan, 5)</i>
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
    private static boolean DEVELOPMENT_MODE = false;

    private Class<E> eClass;
    private StringBuilder criterionQuery;
    private boolean usesDeprecatedConstructor;


    /**
     * Creates a new Criterion object. The Class has to match the type of object wanted
     * in return. The attribute string has to match one of the non-inherited fields in
     * the class, and the value must be an object of the type of the field in the class
     * (a wrapper object in case the field is primitive).
     * <p>
     * If a CriterionException is throws, use CriterionException.getMessage() to view the
     * because of the error.
     *
     * @param eClass    class of the objects to be expected in return
     * @param attribute object variable used as search criterion
     * @param operator  operator applied to value
     * @param value     value for search variable
     * @throws CriterionException if criteria are incorrectly specified
     */
    @Deprecated
    public Criterion(Class<E> eClass, String attribute, CriterionOperator operator, Object value)
            throws CriterionException {
        this.eClass = eClass;

        // check inputs for correctness
        if (eClass == null || attribute == null || operator == null || value == null)
            throw new CriterionException().because(NULL_INPUTS);
        if (!isRegexCompatible(operator, value) || !isClassCompatible(attribute, value))
            throw new CriterionException().because(ARGUMENTS_INCOMPATIBLE);

        criterionQuery = new StringBuilder(attribute).append(" ").append(operator).append(" ");
        appendValue(value, operator);
        usesDeprecatedConstructor = true;
    }

    @Deprecated
    public Criterion(Class<E> eClass, String attribute, CriterionOperator operator,
                     Criterion<? extends Searchable> subQuery) throws CriterionException {
        this.eClass = eClass;

        // check inputs for correctness
        if (eClass == null || attribute == null || operator == null || subQuery == null)
            throw new CriterionException().because(NULL_INPUTS);
        if (operator != in) throw new CriterionException().because(SUBQUERY_NOT_IN);

        criterionQuery = new StringBuilder(attribute).append(" ").append(operator).append(" ");
        appendSubquery(subQuery, attribute);
        usesDeprecatedConstructor = true;
    }

    /**
     * Creates a criterion object specifying no criteria whatsoever. Is used when all objects of class
     *
     * @param eClass
     */
    public Criterion(Class<E> eClass) {
        this.eClass = eClass;
        criterionQuery = new StringBuilder(50);
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
    public Criterion<E> where(String attribute, CriterionOperator operator, Object value) throws CriterionException {
        if (usesDeprecatedConstructor) throw new CriterionException().because(DEPRECATED_CONSTRUCTOR);
        if (!isRegexCompatible(operator, value) || !isClassCompatible(attribute, value))
            throw new CriterionException().because(ARGUMENTS_INCOMPATIBLE);

        criterionQuery.append(" ").append(attribute).append(" ").append(operator).append(" ");
        appendValue(value, operator);
        return this;
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
    public Criterion<E> and(String attribute, CriterionOperator operator, Object value) throws CriterionException {
        if (!isRegexCompatible(operator, value) || !isClassCompatible(attribute, value))
            throw new CriterionException().because(ARGUMENTS_INCOMPATIBLE);

        criterionQuery.append(" AND ").append(attribute).append(" ").append(operator).append(" ");
        appendValue(value, operator);
        return this;
    }

    public Criterion<E> and(String attribute, CriterionOperator operator, Criterion<? extends Searchable> subquery) {
        if (operator != in) throw new CriterionException().because(SUBQUERY_NOT_IN);

        criterionQuery.append(" AND ").append(attribute).append(" ").append(operator).append(" ");
        appendSubquery(subquery, attribute);
        return this;
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
    public Criterion<E> or(String attribute, CriterionOperator operator, Object value) throws CriterionException {
        if (!isRegexCompatible(operator, value) || !isClassCompatible(attribute, value))
            throw new CriterionException().because(ARGUMENTS_INCOMPATIBLE);

        criterionQuery.append(" OR ").append(attribute).append(" ").append(operator).append(" ");
        appendValue(value, operator);
        return this;
    }

    public Criterion<E> or(String attribute, CriterionOperator operator, Criterion<? extends Searchable> subquery) {
        if (operator != in) throw new CriterionException().because(SUBQUERY_NOT_IN);

        criterionQuery.append(" OR ").append(attribute).append(" ").append(operator).append(" ");
        appendSubquery(subquery, attribute);
        return this;
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
        return criterionQuery.toString();
    }

    /**
     * Returns the class associated with this Criterion.
     *
     * @return class of criterion
     */
    public Class<E> getCriterionClass() {
        return eClass;
    }

    /* HELPER: appends the value to the query, with syntax according to type of value */
    private void appendValue(Object value, CriterionOperator operator) {
        if (value.getClass() == Date.class)
            criterionQuery.append(((Date) value).getTime());
        else if (value.getClass() == LocalDateTime.class)
            criterionQuery.append(((LocalDateTime) value).toInstant(ZoneOffset.UTC).toEpochMilli());
        else if (value.getClass() == ZonedDateTime.class)
            criterionQuery.append(((ZonedDateTime) value).toInstant().toEpochMilli());
        else if (operator == matches)
            criterionQuery.append("'%").append(value).append("%'");
        else
            criterionQuery.append("'").append(value).append("'");
    }

    /* HELPER: appends the given subquery to the Criterion */
    private void appendSubquery(Criterion<? extends Searchable> subquery, String attribute) {
        this.criterionQuery.append("(SELECT ").append(attribute).append(" FROM ")
                .append(subquery.getCriterionClass().getSimpleName())
                .append(" WHERE ")
                .append(subquery.toString())
                .append(")");
    }

    /** HELPER: returns true if attribute and value are compatible with class of Criterion */
    private boolean isClassCompatible(String attribute, Object value) {
        if (!DEVELOPMENT_MODE) return true; // checks not performed in deployment for performance

        // get argument types of reflective constructor
        Class<?>[] constructorArgumentTypes = new Class<?>[0];
        for (Constructor<?> c : eClass.getDeclaredConstructors()) {
            if (c.getDeclaredAnnotations().length != 0
                    && c.getDeclaredAnnotations()[0].annotationType().equals(Reflective.class)) {
                constructorArgumentTypes = c.getParameterTypes();
                // System.out.println("@Reflective constructor identified.");
                break;
            }
        }

        // get reflective constructor and check:
        // 1. a parameter is annotated as Column(name = "attribute")
        // 2. the same parameter is of the same class as value
        try {
            Constructor<E> constructor = eClass.getDeclaredConstructor(constructorArgumentTypes);
            Annotation[][] annotations = constructor.getParameterAnnotations();

            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i][0].annotationType().equals(Column.class)) {
                    Column metadata = (Column) annotations[i][0];
                    if (metadata.name().equals(attribute)) {
                        return constructorArgumentTypes[i].isPrimitive() ?
                                constructorArgumentTypes[i].toString().substring(0, 1)
                                        .equalsIgnoreCase(value.getClass().getSimpleName().substring(0, 1))
                                : constructorArgumentTypes[i].getSimpleName().equals(value.getClass().getSimpleName());
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

    /** HELPER: returns true if operator and value are compatible */
    private boolean isRegexCompatible(CriterionOperator operator, Object value) {
        return !(operator.equals(matches) &&
                !(value.getClass().equals(String.class) || value.getClass().equals(Pattern.class)));
    }
}