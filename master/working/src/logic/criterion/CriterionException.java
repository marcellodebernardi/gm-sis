package logic.criterion;

/**
 * @author Marcello De Bernardi
 */
class CriterionException extends RuntimeException {
    private Cause cause;

    CriterionException() {
        cause = Cause.UNSPECIFIED;
    }

    CriterionException(String message) {
        super(message);
        cause = Cause.UNSPECIFIED;
    }


    /** Chained method for setting the because of a CriterionException before it is thrown */
    CriterionException because(Cause cause) {
        this.cause = cause;
        return this;
    }

    /** Returns an enum outlining the reason for the exception. */
    Cause because() {
        return cause;
    }


    enum Cause {
        NULL_INPUTS, ARGUMENTS_INCOMPATIBLE, UNSPECIFIED, SUBQUERY_NOT_IN,
        DEPRECATED_CONSTRUCTOR
    }
}
