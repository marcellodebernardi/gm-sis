package logic;

import entities.User;

import static logic.CriterionOperator.EqualTo;

import org.junit.Test;

/**
 * @author Marcello De Bernardi
 */
public class CriterionTests {
    @Test
    public void testSingleCriterion() {
        try {
            System.out.println(new Criterion<>(User.class, "userID", EqualTo, "foo")
                    .setDiff("password", EqualTo, "dolan"));
        }
        catch (CriterionException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}