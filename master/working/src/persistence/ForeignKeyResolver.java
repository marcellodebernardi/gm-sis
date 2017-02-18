package persistence;

import java.util.List;

/**
 * Con
 *
 * @author Marcello De Bernardi
 */
class ForeignKeyResolver {
    private static ForeignKeyResolver instance;

    private ForeignKeyResolver() {

    }

    static ForeignKeyResolver getInstance() {
        if (instance == null) instance = new ForeignKeyResolver();
        return instance;
    }

    List<String> resolveForeignKeys(List<StatementNode> statementGraph) {
        return null;
    }
}
