package persistence;

import logic.Searchable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Marcello De Bernardi
 */
class StatementNode implements Comparable<StatementNode> {
    // graph properties
    private List<StatementNode> dependents;
    private List<StatementNode> lockedDependencies;
    private List<StatementNode> unlockedDependencies;
    private boolean solved;
    // todo separate states for having primary key AND foreign keys?

    // reflection
    private Class<? extends Searchable> table; // string better?
    private Searchable object;
    private Method primaryGetter;

    // payload
    private String primaryKey;
    private Object primaryKeyValue;
    private StatementType statementType;
    private HashMap<String, Object> solvedValues;



    StatementNode(Class<? extends Searchable> table, Searchable object, Method primaryGetter,
                  HashMap<String, Object> solvedValues, String primaryKey) {
        dependents = new ArrayList<>();
        lockedDependencies = new ArrayList<>();
        unlockedDependencies = new ArrayList<>();

        solved = false;
        this.table = table;
        this.object = object;
        this.primaryGetter = primaryGetter;
        this.solvedValues = solvedValues;
        this.primaryKey = primaryKey;
    }

    @Override
    public int compareTo(StatementNode o) {
        if (lockedDependencies.size() < o.getUnresolvedDependencies()) return -1;
        else if (lockedDependencies.size() > o.getUnresolvedDependencies()) return 1;
        else return 0;
        // decide how to compare nodes with same number of unresolved dependencies
    }

    private int getUnresolvedDependencies() {
        return lockedDependencies.size();
    }

    String getPrimaryKey() {
        return primaryKey;
    }

    Object getPrimaryKeyValue() {
        if (solved) return primaryKeyValue;
        try {
            primaryKeyValue = primaryGetter.invoke(object);
            // has no primary key: INSERT
            if (primaryKeyValue == null) {
                statementType = StatementType.INSERT;

                // todo get future primary key from database
            }
            // has primary key: UPDATE
            else statementType = StatementType.UPDATE;

            // unlock, mark solved and return key
            for (StatementNode sN : dependents) {
                sN.unlock(this);
            }
            solved = true;
        }
        catch (IllegalAccessException | InvocationTargetException e) {
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
        if (lockedDependencies.size() != 0) return false;

        for (StatementNode sN : unlockedDependencies) {
            solvedValues.put(sN.getPrimaryKey(), sN.getPrimaryKeyValue());
        }
        return true;
    }



    // generates query string
    public String toString() {
        if (statementType == StatementType.INSERT) {
            // do stuff
        }
        else {
            // do some other stuff
        }
        return null;
    }

    private boolean unlock(StatementNode sN) {
        if (lockedDependencies.contains(sN)) {
            lockedDependencies.remove(sN);
            unlockedDependencies.add(sN);
            return true;
        }
        return false;
    }

    enum StatementType {
        INSERT, UPDATE;
    }
}
