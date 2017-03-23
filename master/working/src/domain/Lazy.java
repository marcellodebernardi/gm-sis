package domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Marcello De Bernardi
 *         <p>
 *         Annotation with no functional significance. Denotes methods that effectively implement
 *         lazy properties on objects.
 */
@Retention(RetentionPolicy.SOURCE) @interface Lazy {
}
