package domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotates an entity attribute which needs to be fetched from another table. Contains
 *
 *
 * @author Marcello De Bernardi
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TableReference {
    Class<? extends Searchable> baseType();
    Class<? extends Searchable>[] subTypes();
    String key();
}
