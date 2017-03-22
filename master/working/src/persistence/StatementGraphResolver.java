package persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Con
 *
 * @author Marcello De Bernardi
 */
class StatementGraphResolver {
    private static StatementGraphResolver instance;

    // database instance
    private DatabaseRepository persistence;


    private StatementGraphResolver() {
    }

    static StatementGraphResolver getInstance() {
        if (instance == null) instance = new StatementGraphResolver();
        return instance;
    }

    void initialize(DatabaseRepository persistence) {
        this.persistence = persistence;
    }


    /**
     * For a correctly formed statement graph stored in a list of nodes, resolves the
     * missing foreign key dependencies and returns a list of SQL queries ready for
     * execution.
     *
     * @param statementGraph statement dependency graph to resolve
     * @return list of resolved statements
     */
    List<String> resolveForeignKeys(List<StatementNode> statementGraph) {
        List<String> transactionList = new ArrayList<>();

        // ALGORITHM: keeps the node graph in a list sorted by ascending number of
        // unresolved dependencies. Steps through the list left to right. At every
        // node, if the node is complete, turns it into a SQL statement and removes
        // the node from the graph; if the node is not complete, resolves its primary
        // key (potentially bringing other nodes to 0 unresolved dependencies) and
        // moves on to the next node. After each such action, the list is sorted again.
        // If the end of the list is reached, return to the start. Terminates when
        // list is empty.

        Collections.sort(statementGraph);

        for (int i = 0; i < statementGraph.size(); i++) {
            StatementNode sN = statementGraph.get(i);

            if (sN.getUnresolvedDependencies() == 0) {
                sN.getPrimaryKeyValue();
                sN.setForeignKeys();
                statementGraph.remove(i);
                transactionList.add(sN.toString());
                i--;
            }
            else sN.getPrimaryKeyValue();

            if (i + 1 == statementGraph.size()) i = 0;

            Collections.sort(statementGraph);
        }
        return transactionList;
    }
}
