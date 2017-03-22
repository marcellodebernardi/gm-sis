package domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Marcello De Bernardi
 *         <p>
 *         Denotes a method that is used for the purposes of handling dependencies with DependencyConnectables.
 */
@Retention(RetentionPolicy.SOURCE) @interface DependencyHandler {
}
