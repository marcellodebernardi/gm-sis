package persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Con
 *
 * @author Marcello De Bernardi
 */
class ForeignKeyResolver {
    private static ForeignKeyResolver instance;

    // database instance
    private DatabaseRepository persistence;


    private ForeignKeyResolver() {}

    static ForeignKeyResolver getInstance() {
        if (instance == null) instance = new ForeignKeyResolver();
        return instance;
    }

    void initialize(DatabaseRepository persistence) {
        this.persistence = persistence;
    }


    /** For a correctly formed statement graph stored in a list of nodes, resolves the
     * missing foreign key dependencies and returns a list of SQL queries ready for
     * execution.
     *
     * @param statementGraph statement dependency graph to resolve
     * @return list of resolved statements
     */
    List<String> resolveForeignKeys(List<StatementNode> statementGraph) {
        List<String> transaction = new ArrayList<>();

        Collections.sort(statementGraph);

        for (int i = 0; i < statementGraph.size(); i++) {
            StatementNode sN = statementGraph.get(i);
            if (sN.getUnresolvedDependencies() == 0) {
                sN.getPrimaryKeyValue();
                sN.setForeignKeys();
                statementGraph.remove(i);
                transaction.add(sN.toString());
                i--;
            }
            else {
                sN.getPrimaryKeyValue();
                if (i + 1 == statementGraph.size()) i = 0; // todo replace with outer loop?
            }
            Collections.sort(statementGraph);
        }
        return transaction;
    }
}
