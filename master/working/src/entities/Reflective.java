package entities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks the constructor to be used for ORM. Only one constructor should be
 * marked as Reflective.
 *
 * todo add compile-time check for uniqueness etc
 *
 * @author Marcello De Bernardi
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Reflective {
}