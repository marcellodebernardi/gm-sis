package entities;

/**
 * Marks a constructor parameter which can be mapped directly to a single data element
 * in the persistence layer. That is, any parameters of primite types, Strings etc should
 * have the simple annotation.
 *
 * @author Marcello De Bernardi
 */
public @interface Simple {
    String name();
}
