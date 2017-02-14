package entities;

import logic.Searchable;

/**
 * Annotates an entity attribute which needs to be fetched from another table. Contains
 *
 *
 * @author Marcello De Bernardi
 */
public @interface Complex {
    Class<? extends Searchable> baseType();
    String key();
}
