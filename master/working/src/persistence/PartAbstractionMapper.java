package persistence;

import entities.PartAbstraction;
import logic.Criterion;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
class PartAbstractionMapper extends Mapper<PartAbstraction> {

    PartAbstractionMapper(MapperFactory factory){
        super(factory);
    }


    String toSELECTQuery(Criterion<PartAbstraction> criteria) {
        return null;
    }


    String toINSERTTransaction(PartAbstraction partAbstraction) {
        return null;
    }


    String toUPDATETransaction(PartAbstraction partAbstraction) {
        return null;
    }


    String toDELETETransaction(Criterion<PartAbstraction> criterion) {
        return null;
    }

    List<PartAbstraction> toObjects(ResultSet results) {
        return null;
    }
}