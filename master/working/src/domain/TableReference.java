package domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotates an entity attribute which needs to be fetched from another table. Contains the
 * polymorphic base type of all objects that may be held in this attribute, all actual types
 * of said objects, as well as the name of the attribute in the other attribute's table that
 * identifies this object.
 *
 * @author Marcello De Bernardi
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TableReference {
    Class<? extends Searchable> baseType();

    Class<? extends Searchable>[] subTypes();

    String key();
}
