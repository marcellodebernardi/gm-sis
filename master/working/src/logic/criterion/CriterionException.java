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


    /** Chained method for setting the cause of a CriterionException before it is thrown */
    CriterionException because(Cause cause) {
        this.cause = cause;
        return this;
    }


    /** Returns an enum outlining the reason for the exception. */
    Cause getCauseOrigin() {
        return cause;
    }


    enum Cause {
        NULL_INPUTS, NO_SUCH_ATTRIBUTE, ARGUMENTS_INCOMPATIBLE, NOT_REGEXABLE, UNSPECIFIED, SUBQUERY_NOT_IN;
    }
}
