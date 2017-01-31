package persistence;

import logic.Criterion;
import logic.CriterionRepository;
import logic.InconsistentCriteriaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Implementation of the CriterionRepository interface to work with the SQLite DBMS.
 *
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class DatabaseRepository implements CriterionRepository {
    private static DatabaseRepository instance;

    private final String JDBC_DRIVER = null;
    private final String DB_URL = null;
    private final String DB_USER = null;
    private final String DB_PASS = null;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet results;


    /**
     * Constructor for DatabaseRepository. Temporary.
     */
    private DatabaseRepository() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Gets singleton instance of DatabaseRepository.
     *
     * @return new DatabaseRepository
     */
    public static DatabaseRepository getInstance() {
        if (instance == null) instance = new DatabaseRepository();
        return instance;
    }

    @Override
    protected void finalize() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


    public <E extends Criterion> List<E> getByCriteria(boolean patternMatching, Class<E> eClass, Criterion ... criteria)
            throws InconsistentCriteriaException {
        // handle bad input
        if (criteria == null || criteria.length == 0) throw new NullPointerException("No criteria given.");
        for (Criterion crit : criteria) {
            if (eClass != crit.getClass()) throw new InconsistentCriteriaException();
        }

        List<E> returnList = new ArrayList<>();

        try {
            String SQL = "SELECT * FROM " + eClass.getName() + " WHERE " + parseWhere(criteria) + ";";

            statement = connection.prepareStatement(SQL);
            results = statement.executeQuery();

            // iterate over rows
            while(results.next()) {
                switch(eClass.getName()) {
                    case "User":
                        // returnList.add()
                }
            }


        } catch (SQLException e) {
            System.out.print(e.toString());
        }
        return returnList;
    }

    public boolean addItem(Criterion... items) {
        return false; // placeholder
    }

    public boolean updateByCriteria(Criterion... items) {
        return false; // placeholder
    }

    public boolean deleteByCriteria(boolean patternMatching, Criterion... criteria) {
        return false; // placeholder
    }


    /**
     * Takes a list of criteria and returns the WHERE portion of the SQL statement.
     * Returns it in format "(attribute='something' AND attribute='something') OR (...)"
     */
    private <E extends Criterion> String parseWhere(Criterion... criteria) {
        String SQLresult = "";

        for (Criterion crit : criteria) {
            Set<Map.Entry<String, Object>> entrySet = crit.getAttributes().entrySet();
            SQLresult = SQLresult + "(";

            for (Map.Entry<String, Object> entry : entrySet) {
                SQLresult = SQLresult + entry.getKey() + "='" + entry.getValue() + "' AND";
            }

            SQLresult = SQLresult.substring(0, SQLresult.length() - 3) + ") OR";
        }
        if (criteria.length == 1) SQLresult = SQLresult.substring(1, SQLresult.length() - 2);
        return SQLresult;
    }
}