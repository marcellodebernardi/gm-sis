package logic;

import entities.DiagRepBooking;
import entities.User;

import static logic.CriterionOperator.EqualTo;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

/**
 * @author Marcello De Bernardi
 */
public class CriterionTests {
    @Test
    public void testSingleCriterion() {
        try {
            System.out.println(new Criterion<>(User.class, "userID", EqualTo, "foo")
                    .and("password", EqualTo, "dolan"));
        }
        catch (CriterionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testCriterionDateHandling() {
        try {
            System.out.println(new Criterion<>(DiagRepBooking.class, "diagnosisStart", EqualTo, new DateTime()));
        }
        catch (CriterionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}