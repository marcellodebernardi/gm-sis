package logic;


import domain.PartRepair;
import domain.Searchable;
import domain.SpecialistRepairCenter;
import domain.VehicleRepair;
import org.joda.time.LocalDate;
import persistence.DatabaseRepository;

import java.util.Date;
import java.util.List;

import static logic.CriterionOperator.*;

/**
 * @author muradahmed
 */
public class SpecRepairSystem {

    private static SpecRepairSystem instance;
    private CriterionRepository persistence = DatabaseRepository.getInstance();


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
    public boolean addRepairCenter(String name, String address, String phone, String email)
    {
        //todo Take relevant information from user required to create a SRC and make an object to pass to persistence
         SpecialistRepairCenter add  = new SpecialistRepairCenter(name, address, phone, email, null);
        return persistence.commitItem(add);
    }

    /**
     * @param delete SRC that is being requested to be deleted
     *
     * @return true if deleting of SRC is successful.
     */
    public boolean deleteRepairCenter(int delete)
    {
     return persistence.deleteItem(new Criterion<>(SpecialistRepairCenter.class, "spcID", EqualTo, delete));
    }

    public boolean updateRepairCentre(SpecialistRepairCenter edit)
    {
        return persistence.commitItem(edit);
    }

    public List<VehicleRepair> getVehicleBookings(String regNumber)
    {
        return persistence.getByCriteria(new Criterion<>(VehicleRepair.class, "vehicleReg", EqualTo, regNumber));
    }

    public List<SpecialistRepairCenter>getAllBookings(String spcID)
    {
        return persistence.getByCriteria(new Criterion<>(SpecialistRepairCenter.class, "spcID", EqualTo, spcID));
    }

    public List<VehicleRepair> getOutstandingV()
    {
        ///todo implement way to get all vehicle repairs with return dates past todays date
        return persistence.getByCriteria(new Criterion<>(VehicleRepair.class, "returnDate", MoreThan, new LocalDate().now()));
    }

    public List<PartRepair> getOutstandingP()
    {
        ///todo implement way to get all part repairs with return dates past todays date
        return persistence.getByCriteria(new Criterion<>(PartRepair.class, "returnDate", MoreThan, new LocalDate().now()));
    }

    public List<SpecialistRepairCenter>getByID(int spcID)
    {
        return persistence.getByCriteria(new Criterion<>(SpecialistRepairCenter.class,"spcID", EqualTo, spcID));
    }
}
