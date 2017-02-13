package persistence;

import entities.SpecialistRepairCenter;
import org.junit.Test;
import static org.junit.Assert.*;


/**w3
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class SRCMapperTests {
    // todo
    private MapperFactory factory = MapperFactory.getInstance();


    /* Test SELECT statements returned by Mappers
    @Test
    public void testSRCSelectQuery() {
        List<SpecialistRepairCenter> srcList = new ArrayList<>();
        srcList.add(new SpecialistRepairCenter(123, "test1","test1address", "test1phone", "test1email"));
        srcList.add(new SpecialistRepairCenter(456,"test2","test2address","test2phone","test2email"));

        System.out.println(factory.getMapper(SpecialistRepairCenter.class));
        assertTrue(factory.getMapper(SpecialistRepairCenter.class).toSELECTQuery(srcList)
                .equals("SELECT * FROM SpecialistRepairCenter WHERE (SpcID = 123 AND name = 'test1'"
                        + " AND address = 'test1address' AND phone = 'test1phone' AND email = 'test1email'"
                        + " OR (SpcID = 456 AND name = 'test2'"
                        + " AND address = 'test2address' AND phone = 'test2phone' AND email = 'test2email'"));
    }

     */


    @Test
    public void testSRCtoInsertQuery()
    {
        SpecialistRepairCenter specialistRepairCenter = new SpecialistRepairCenter(123,"test1","test1address","test1phone","test1email");
      System.out.print(factory.getMapper(SpecialistRepairCenter.class));
      assertTrue(factory.getMapper(SpecialistRepairCenter.class).toINSERTTransaction(specialistRepairCenter).equals("INSERT INTO SpecialistRepairCenter VALUES(123, 'test1', " +
              "'test1address', 'test1phone', 'test1email')"));

    }
@Test
    public void testSRCSelectQuery(){
    SpecialistRepairCenter test1 = new SpecialistRepairCenter(123,"test1","test1address","test1phone","test1email");
    System.out.print(factory.getMapper(SpecialistRepairCenter.class));
    assertTrue(factory.getMapper(SpecialistRepairCenter.class).toINSERTTransaction(test1).equals("SELECT *  FROM SpecialistRepairCenter WHERE ( SpcID = 123 AND name = 'test1' "
            + " AND address = 'test1address' AND phone = 'test1phone' AND email = 'test1email' )" ));

}
}
