package logic.criterion;

/**
 * Defines the operators available to Criterions.
 *
 * @author Marcello De Bernardi
 */
public enum CriterionOperator {
    LessThan, MoreThan, EqualTo, Regex;

    public String toString() {
        if (this == LessThan) return "<";
        else if (this == MoreThan) return ">";
        else if (this == EqualTo) return "=";
        else return "LIKE";
    }
}
