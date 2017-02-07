package logic;

import java.util.List;
import java.util.ArrayList;
/**
 * Created by Muhammad Shakib Hoque on 07/02/2017.
 *
 */
public class PartsSystem {

    private static PartsSystem instance;
    private CriterionRepository persistence;

    private PartsSystem(CriterionRepository persistence) {

        this.persistence = persistence;
    }

    public PartsSystem getInstance(CriterionRepository persistence) {
        if (instance == null) instance = new PartsSystem(persistence);
        return instance;
    }

    public List<PartsSystem> getPartList() {

        List<PartsSystem> PartsList = new ArrayList<PartsSystem>();

        return PartsList;

    }

    public List<PartsSystem> searchPartList() {

        /** TODO: create search method
         *
         */

        List<PartsSystem> PartsList2 = new ArrayList<PartsSystem>();
        return PartsList2;

    }

    public boolean addPart(){

        return false;

    }

    public boolean deletePart(){

        return false;
    }

    public boolean editPart(){

        return false;

    }


}


