package logic.criterion;

/**
 * Defines the operators available to Criterions.
 *
 * @author Marcello De Bernardi
 */
public enum CriterionOperator {
    lessThan, moreThan, equalTo, matches, in, after, before;

    public String toString() {
        if (this == lessThan || this == before) return "<";
        else if (this == moreThan || this == after) return ">";
        else if (this == equalTo) return "=";
        else if (this == in) return "IN";
        else return "LIKE";
    }
}
