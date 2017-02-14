package entities;

/**
 * Annotates an entity attribute which needs to be fetched from another table.
 *
 * @author Marcello De Bernardi
 */
public @interface Complex {
    String tableName();
}
