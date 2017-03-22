package domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a constructor parameter which can be mapped directly to a single data element
 * in the persistence layer. That is, any parameters of primitive types, Strings etc should
 * have the simple annotation.
 *
 * @author Marcello De Bernardi
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name();

    boolean primary() default false;

    boolean foreign() default false;
}
