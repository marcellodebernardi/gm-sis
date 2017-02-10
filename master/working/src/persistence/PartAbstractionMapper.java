package persistence;

import entities.PartAbstraction;

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


    String toSELECTQuery(List<PartAbstraction> partAbstractions) {
        return null;
    }


    String toINSERTQuery(PartAbstraction partAbstraction) {
        return null;
    }


    String toUPDATEQuery(PartAbstraction partAbstraction) {
        return null;
    }


    String toDELETEQuery(PartAbstraction partAbstraction) {
        return null;
    }

    List<PartAbstraction> toObjects(ResultSet results) {
        return null;
    }
}