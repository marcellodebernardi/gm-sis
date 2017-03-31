package persistence;

import domain.Searchable;
import domain.User;
import domain.Vehicle;
import logic.criterion.Criterion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static logic.criterion.CriterionOperator.equalTo;

/**
 * @author Marcello De Bernardi
 */
class StatementNode implements Comparable<StatementNode> {
    // graph properties
    private List<StatementNode> dependents;                     // nodes with foreign keys pointing to this node
    private List<StatementNode> unresolvedDependencies;         // dependencies of this node with no primary key
    private List<StatementNode> resolvedDependencies;           // dependencies of this node, with primary key
    private boolean resolved;                                   // true if has been assigned a primary key
    private boolean complete;                                   // true if resolved and all dependencies are resolved

    // reflection
    private Class<? extends Searchable> table;                  // table to commit into
    private Searchable object;                                  // object to commit
    private Method primaryGetter;                               // getter for primary key of object to commit
    private List<Class<?>> noQuotesTypes;                       // datatypes that must not have quotes around values

    // payload
    private String primaryKey;                                  // the primary key attribute of the object
    private Object primaryKeyValue;                             // the value of the primary key of the object
    private StatementType statementType;                        // INSERT or UPDATE
    private Map<String, Object> solvedValues;                   // att/val pairs ready to be added to SQL statement

    // database instance
    private DatabaseRepository persistence;                     // for helper queries


    StatementNode(Class<? extends Searchable> table, Searchable object, Method primaryGetter,
                  HashMap<String, Object> solvedValues, String primaryKey, DatabaseRepository persistence,
                  List<Class<?>> noQuotesTypes) {
        dependents = new ArrayList<>();
        unresolvedDependencies = new ArrayList<>();
        resolvedDependencies = new ArrayList<>();
        resolved = false;
        complete = false;
        this.table = table;
        this.object = object;
        this.primaryGetter = primaryGetter;
        this.solvedValues = solvedValues;
        this.primaryKey = primaryKey;
        this.persistence = persistence;
        this.noQuotesTypes = noQuotesTypes;
    }

    /** Sets the given node as dependent on the node the method is called on. */
    void addDependent(StatementNode dependent) {
        dependents.add(dependent);
    }

    /** Sets the given node as a dependency of the node the method is called on. */
    void addDependency(StatementNode dependency) {
        unresolvedDependencies.add(dependency);
    }

    /**
     * The natural ordering of StatementNodes is in accordance to the number of unresolved
     * dependencies they have. A StatementNode with fewer unresolved dependencies comes
     * before a StatementNode with moreThan unresolved dependencies.
     *
     * @param o the StatementNode to compare to
     * @return 1 if this comes after 0, -1 if this comes before 0, 0 if equalTo
     */
    @Override
    public int compareTo(StatementNode o) {
        if (unresolvedDependencies.size() < o.unresolvedDependencies.size()) return -1;
        else if (unresolvedDependencies.size() > o.unresolvedDependencies.size()) return 1;
        else if (dependents.size() > o.dependents.size()) return -1;
        else if (dependents.size() < o.dependents.size()) return 1;
        else return -1;
    }

    /**
     * Returns the number of unresolved dependencies.
     */
    int getUnresolvedDependencies() {
        return unresolvedDependencies.size();
    }

    /**
     * Generates the full, ready-for-execution SQL query represented by this StatementNode.
     * Note that the StatementNode must be
     *
     * @return null if not complete, SQL statement string otherwise
     */
    public String toString() {
        if (!complete) {
            System.err.println("StatementNode: calling toString() on incomplete node of type " + table);
            return null;
        }

        // todo change to stringbuilders
        if (statementType == StatementType.INSERT) {
            // construct column name string
            String keys = solvedValues.keySet().toString();
            keys = keys.replace("[", "(");
            keys = keys.replace("]", "");
            keys += ", " + primaryKey + ")";

            // construct value string
            String values = "";
            String delim = "";
            for (Object value : solvedValues.values()) {
                values += delim;

                if (value.getClass() == Date.class)
                    values += ((Date) value).getTime() == 0 ?
                            "null"
                            : ((Date) value).getTime();
                else if (value.getClass() == LocalDateTime.class)
                    values += ((LocalDateTime) value).toEpochSecond(ZoneOffset.UTC) * 1000 == 0;
                else if (value.getClass() == ZonedDateTime.class)
                    values += ((ZonedDateTime) value).toEpochSecond() * 1000;
                else if (value.getClass() == Boolean.class && (Boolean) value)
                    values += "1";
                else if (value.getClass() == Boolean.class && !(Boolean) value)
                    values += "0";
                else if (noQuotesTypes.contains(value.getClass()))
                    values += value;
                else if (noQuotesTypes.contains(value.getClass()) && value.equals(-2))
                    values += -1;
                else
                    values += "'" + value + "'";

                delim = ", ";
            }
            // primary key, with quotes if User or Vehicle (which use strings as IDs)
            values = (noQuotesTypes.contains(primaryKeyValue.getClass())) ?
                    "(" + values + ", " + primaryKeyValue + ")" : "(" + values + ", '" + primaryKeyValue + "')";

            return "INSERT INTO " + table.getSimpleName() + keys + " VALUES " + values + ";";
        }
        else {
            String newInfo = "";
            String delim = "";

            for (String key : solvedValues.keySet()) {
                Object value = solvedValues.get(key);

                if (value.getClass() == Date.class)
                    newInfo += ((Date) value).getTime() == 0 ?
                            delim + key + " = null"
                            : delim + key + " = " + ((Date) value).getTime();
                else if (value.getClass() == ZonedDateTime.class)
                    newInfo += delim + key + " = " + ((ZonedDateTime) value).toInstant().toEpochMilli();
                else if (value.getClass() == LocalDateTime.class)
                    newInfo += delim + key + " = " + ((LocalDateTime) value).toInstant(ZoneOffset.UTC).toEpochMilli();
                else if (value.getClass() == Boolean.class && (Boolean) value)
                    newInfo += delim + key + " = 1";
                else if (value.getClass() == Boolean.class && !(Boolean) value)
                    newInfo += delim + key + " = 0";
                else if (noQuotesTypes.contains(value.getClass()) && value.equals(-2))
                    newInfo += delim + key + " = -1";
                else
                    newInfo += noQuotesTypes.contains(value.getClass()) ? delim + key + " = " + value
                            : delim + key + " = '" + value + "'";

                delim = ", ";
            }

            // add primary key and return
            return noQuotesTypes.contains(primaryKeyValue.getClass()) ?
                    "UPDATE " + table.getSimpleName() + " SET " + newInfo + " WHERE "
                            + primaryKey + " = " + primaryKeyValue + ";"
                    :
                    "UPDATE " + table.getSimpleName() + " SET " + newInfo + " WHERE "
                            + primaryKey + " = '" + primaryKeyValue + "';";
        }
    }

    /**
     * Returns the string giving the name of the primary key involved with this SQL
     * statement.
     *
     * @return primary key name
     */
    String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Returns the value corresponding to the primary key involved with this SQL
     * statement. If the key has not yet been resolved, resolves it and notifies all dependent
     * nodes of the resolution.
     *
     * @return primary key value
     */
    Object getPrimaryKeyValue() {
        if (resolved) return primaryKeyValue;

        try {
            primaryKeyValue = primaryGetter.invoke(object);

            // has no primary key: INSERT statement, have database assign primary key
            if (primaryKeyValue == null ||
                    (noQuotesTypes.contains(primaryKeyValue.getClass()) && primaryKeyValue.equals(-1))) {
                statementType = StatementType.INSERT;
                primaryKeyValue = persistence.getNextID(table.getSimpleName(), primaryKey);
            }
            // has primary key but is User: INSERT if not exists, else UPDATE
            else if (table.equals(User.class)) {
                List<User> results = persistence.getByCriteria(
                        new Criterion<>(User.class, "userID", equalTo, primaryKeyValue));

                statementType = results.size() == 0 ? StatementType.INSERT : StatementType.UPDATE;
            }
            // has primary key but is Vehicle: INSERT if not exists, else UPDATE
            else if (table.equals(Vehicle.class)) {
                List<Vehicle> results = persistence.getByCriteria(
                        new Criterion<>(Vehicle.class, "vehicleRegNumber", equalTo, primaryKeyValue));

                statementType = results.size() == 0 ? StatementType.INSERT : StatementType.UPDATE;
            }
            // has primary key: UPDATE
            else statementType = StatementType.UPDATE;

            // notify dependents that node has been resolved
            for (StatementNode sN : dependents) {
                sN.notifyResolved(this);
            }
            resolved = true;
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            System.err.print(e.getMessage() + "\nStatementNode failed to obtain primary key.");
        }

        return primaryKeyValue;
    }

    /**
     * For a node whose dependencies have all been resolved, sets the resolved primary
     * keys as the foreign keys of this statement.
     */
    boolean setForeignKeys() {
        if (unresolvedDependencies.size() != 0) return false;

        // this procedure works on the assumption that the foreign key in this object has the
        // same name as the primary key in the dependency. todo vulnerable to changes
        for (StatementNode sN : resolvedDependencies) {
            solvedValues.put(sN.getPrimaryKey(), sN.getPrimaryKeyValue());
        }

        return (complete = true);
    }

    /**
     * Takes a reference to another StatementNode and "unlocks" this node's dependency
     * on the node given as argument. That is, if this node is dependent on the argument
     * node, this node is informed of the fact that the argument node has been resolved
     * and thus can be moved to the resolvedDependencies list in this node.
     *
     * @param sN a reference to the resolved node
     * @return true if successful, false if not successful
     */
    private boolean notifyResolved(StatementNode sN) {
        if (unresolvedDependencies.contains(sN)) {
            unresolvedDependencies.remove(sN);
            resolvedDependencies.add(sN);
            return true;
        }
        return false;
    }


    /**
     * Enum used to denote whether a StatementNode represents an INSERT statement or an
     * UPDATE statement.
     */
    enum StatementType {
        INSERT, UPDATE;
    }
}