package persistence;

import domain.Searchable;
import domain.User;
import domain.Vehicle;
import logic.Criterion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static logic.CriterionOperator.EqualTo;

/**
 * @author Marcello De Bernardi
 */
class StatementNode implements Comparable<StatementNode> {
    // graph properties
    private List<StatementNode> dependents;
    private List<StatementNode> unresolvedDependencies;
    private List<StatementNode> resolvedDependencies;
    private boolean resolved;
    private boolean complete;

    // reflection
    private Class<? extends Searchable> table;
    private Searchable object;
    private Method primaryGetter;
    private List<Class<?>> numericTypes;
    private List<Class<?>> dateTypes;

    // payload
    private String primaryKey;
    private Object primaryKeyValue;
    private StatementType statementType;
    private HashMap<String, Object> solvedValues;

    // database instance
    private DatabaseRepository persistence;

    StatementNode(Class<? extends Searchable> table, Searchable object, Method primaryGetter,
                  HashMap<String, Object> solvedValues, String primaryKey, DatabaseRepository persistence,
                  List<Class<?>> numericTypes, List<Class<?>> dateTypes) {
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
        this.numericTypes = numericTypes;
        this.dateTypes = dateTypes;
    }

    void addDependent(StatementNode dependent) {
        dependents.add(dependent);
    }

    void addDependency(StatementNode dependency) {
        unresolvedDependencies.add(dependency);
    }

    /**
     * The natural ordering of StatementNodes is in accordance to the number of unresolved
     * dependencies they have. A StatementNode with fewer unresolved dependencies comes
     * before a StatementNode with more unresolved dependencies.
     *
     * @param o the StatementNode to compare to
     * @return 1 if this comes after 0, -1 if this comes before 0, 0 if equal
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

        if (statementType == StatementType.INSERT) {
            // column names
            String keys = solvedValues.keySet().toString();
            keys = keys.replace("[", "(");
            keys = keys.replace("]", "");
            keys += ", " + primaryKey + ")";

            // values
            String values = "";
            String delim = "";
            for (Object value : solvedValues.values()) {
                if (value.getClass() == Date.class) values += delim + ((Date)value).getTime();
                else if (numericTypes.contains(value.getClass())) values += delim + value;
                else values += delim + "'" + value + "'";

                delim = ", ";
            }
            // primary key, with
            values = (table.equals(User.class) || table.equals(Vehicle.class)) ?
                    "(" + values + ", '" + primaryKeyValue + "')" : "(" + values + ", " + primaryKeyValue + ")";

            return "INSERT INTO " + table.getSimpleName() + keys + " VALUES " + values + ";";
        }
        else {
            String newInfo = "";
            String delim = "";

            // todo detect if date and call timeMillis()

            for (String key : solvedValues.keySet()) {
                Object value = solvedValues.get(key);
                newInfo += numericTypes.contains(value.getClass()) ?
                        delim + key + " = " + value : delim + key + " = '" + value + "'";
                delim = ", ";
            }

            return numericTypes.contains(primaryKeyValue.getClass()) ?
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
     * statement.
     *
     * @return primary key value
     */
    Object getPrimaryKeyValue() {
        if (resolved) return primaryKeyValue;

        try {
            primaryKeyValue = primaryGetter.invoke(object);
            // has no primary key: INSERT
            if (primaryKeyValue == null ||
                    (numericTypes.contains(primaryKeyValue.getClass())
                            && primaryKeyValue.equals(-1))) {
                statementType = StatementType.INSERT;
                primaryKeyValue = persistence.getNextID(table.getSimpleName(), primaryKey);
            }
            // has primary key but is User
            else if (table.equals(User.class)) {
                List<User> results = persistence
                        .getByCriteria(
                                new Criterion<>(
                                        User.class,
                                        "userID",
                                        EqualTo,
                                        primaryKeyValue));
                statementType = results.size() == 0 ? StatementType.INSERT : StatementType.UPDATE;
            }
            else if (table.equals(Vehicle.class)) {
                List<Vehicle> results = persistence
                        .getByCriteria(
                                new Criterion<>(
                                        Vehicle.class,
                                        "regNumber",
                                        EqualTo,
                                        primaryKeyValue));
                statementType = results.size() == 0 ? StatementType.INSERT : StatementType.UPDATE;
            }
            // has primary key: UPDATE
            else statementType = StatementType.UPDATE;

            // unlock, mark resolved and return key
            for (StatementNode sN : dependents) {
                sN.unlock(this);
            }
            resolved = true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.print(e.getMessage() + "\nStatementNode failed to obtain primary key.");
        }
        return primaryKeyValue;
    }

    /**
     * Adds the foreign keys to the keys + values hashmap. This method
     * should be called on a StatementNode that has 0 locked dependencies,
     * or it will not run.
     */
    boolean setForeignKeys() {
        if (unresolvedDependencies.size() != 0) return false;

        // this procedure works on the assumption that the foreign key in this object has the
        // same name as the primary key in the dependency. todo vulnerable to changes, fix
        for (StatementNode sN : resolvedDependencies) {
            solvedValues.put(sN.getPrimaryKey(), sN.getPrimaryKeyValue());
        }
        complete = true;
        return true;
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
    private boolean unlock(StatementNode sN) {
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