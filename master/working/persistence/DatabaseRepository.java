import java.util.SortedSet;

/**
 * Implementation of the CriterionRepository interface to work with the SQLite DBMS.
 *
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DatabaseRepository implements CriterionRepository {

    @Override
    public SortedSet<Criterion> getByCriteria(boolean patternMatching, Criterion ... criteria) {
        return null; // placeholder
    }

    @Override
    public boolean addItem(Criterion... items) {
        return false; // placeholder
    }

    @Override
    public boolean updateByCriteria(Criterion... items) {
        return false; // placeholder
    }

    @Override
    public boolean deleteByCriteria(boolean patternMatching, Criterion ... criteria) {
        return false; // placeholder
    }
}
