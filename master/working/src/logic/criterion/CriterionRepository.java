package logic.criterion;

import domain.Searchable;

import java.util.List;

/**
 * <p>
 * <i>PersistenceInterface</i> provides an abstraction of persistence implementations. Objects
 * implementing the interface fulfill the contract of accepting Criterion objects and returning
 * business domain. An implementation of the interface is specific both to a particular entity
 * model as well as to a particular persistence solution.
 * </p>
 *
 * @author Marcello De Bernardi
 * @version 0.2
 * @since 0.1
 */
public interface CriterionRepository {
    /**
     * <p>
     * Takes any number of Criterion objects and returns a List<E extends Searchable> of objects
     * stored in the persistence layer that match these criteria. The Criterion object itself
     * defines the base type of the returned object.
     * </p>
     * <p>
     * <i>new Criterion<>(User.class, "userID", equalTo, "foo").and("password", equalTo, "bar")</i>
     * </p>
     * <p>
     * corresponds to the logical statement
     * </p>
     * <p>
     * <i>Class = User AND userID = 'foo' AND password = 'bar'</i>
     * </p>
     *
     * @param criteria object defining set of criteria connected by AND logical connective
     * @return List of objects of same actual type as the passed working.logic.criterion.Criterion objects
     */
    <E extends Searchable> List<E> getByCriteria(Criterion<E> criteria);

    /**
     * <p>
     * Takes a single Searchable object and adds it to the persistence layer if it is not
     * already present (must be a new entity from a business perspective).
     * </p>
     * <p>
     * When inserting, insertion propagates downward in the entity hierarchy. Updates do not
     * propagate, but insertion triggers updates for any nodes in the hierarchy that are a single
     * link away from the inserted node. Further updates, if necessary, need to be performed manually.
     * </p>
     *
     * @param item Criterion object to add to the database
     * @return true if add successful, false is add unsuccessful
     */
    <E extends Searchable> boolean commitItem(E item);

    /**
     * <p>
     * Takes a Criterion object and deletes the item it specifies from the persistence layer,
     * Objects are identified in the persistence layer by their primary ID variable.
     * </p>
     * <p>
     * Deletion propagates downward in the entity hierarchy. Updates do not propagate, but
     * deletion triggers updates for domain upward of the deleted entity that are one
     * link away from it. Other updates must be performed manually if necessary.
     * </p>
     * <p>
     * Matches operator should be avoided. Using the regex operator on identifiers can
     * because catastrophic loss of database contents.
     * </p>
     *
     * @param criteria Criterion objects to define the objects to remove
     * @return true if successful, false if unsuccessful
     */
    <E extends Searchable> boolean deleteItem(Criterion<E> criteria);
}