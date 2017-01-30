package persistence;

import logic.Criterion;
import logic.CriterionRepository;

import java.util.List;
import java.sql.*;


/**
 * Implementation of the CriterionRepository interface to work with the SQLite DBMS.
 *
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DatabaseRepository implements CriterionRepository {

    public List<Criterion> getByCriteria(boolean patternMatching, Criterion ... criteria) {
        return null;
    }

    public boolean addItem(Criterion... items) {
        return false; // placeholder
    }

    public boolean updateByCriteria(Criterion... items) {
        return false; // placeholder
    }

    public boolean deleteByCriteria(boolean patternMatching, Criterion ... criteria) {
        return false; // placeholder
    }
}
