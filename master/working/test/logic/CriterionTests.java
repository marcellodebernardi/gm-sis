package logic;

import domain.DiagRepBooking;
import domain.User;

import static logic.CriterionOperator.EqualTo;

import org.junit.Test;

import java.time.ZonedDateTime;

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
            System.out.println(new Criterion<>(DiagRepBooking.class, "diagnosisStart", EqualTo, ZonedDateTime.now()));
        }
        catch (CriterionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}