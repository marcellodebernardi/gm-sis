package logic;


import entities.SpecialistRepairCenter;
import java.util.List;
import java.util.Collections;
import static logic.CriterionOperator.*;

/**
 * @author muradahmed
 */
public class SpecRepairSystem {

    private static SpecRepairSystem instance;
    private CriterionRepository persistence;


    private SpecRepairSystem(CriterionRepository persistence) {
        this.persistence = persistence;
    }


    /**
     * Returns the singleton instance of the SpecRepairSystem.
     *
     * @return singleton instance of SpecRepairSystem
     */
    public static SpecRepairSystem getInstance(CriterionRepository persistence) {
        if (instance == null) instance = new SpecRepairSystem(persistence);
        return instance;
    }

    /**
     * Returns a list of Specialist Repair Centers
     * @return list of Specialist Repair Centers
     */
    public List<SpecialistRepairCenter>getRepairCenterList(){
        return persistence.getByCriteria(new Criterion<>(SpecialistRepairCenter.class, null, null, null));
    }

    /**
     * Adds a new specialist repair center to the system.
     *
     * @return true if adding of a repair center is successful.
     */
    public boolean addRepairCenter(int spcID, String name, String address, String phone, String email)
    {
        //todo Take relevant information from user required to create a SRC and make an object to pass to persistence
         SpecialistRepairCenter add  = new SpecialistRepairCenter(spcID, name, address, phone, email);
        return persistence.addItem(SpecialistRepairCenter.class, add);
    }

    /**
     * @param delete SRC that is being requested to be deleted
     *
     * @return true if deleting of SRC is successful.
     */
    public boolean deleteRepairCenter(SpecialistRepairCenter delete)
    {
     return persistence.deleteItem(new Criterion<>(SpecialistRepairCenter.class, "spcID", EqualTo, delete.getSpcID()));
    }

}
