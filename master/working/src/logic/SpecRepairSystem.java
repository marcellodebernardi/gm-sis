package logic;


import domain.*;
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

    /**
     * @param edit SRC that is being updated
     *
     * @return true if modifying an SRC is successful.
     */
    public boolean updateRepairCentre(SpecialistRepairCenter edit)
    {
        return persistence.commitItem(edit);
    }

    /**
     * @param regNumber Registration number used to search all Vehicle repairs with the respective vehicle reg
     *
     * @return true if vehicle reg is valid (will return true even if there are NO bookings with this reg)
     */
    public List<VehicleRepair> getVehicleBookings(String regNumber)
    {
        return persistence.getByCriteria(new Criterion<>(VehicleRepair.class, "vehicleReg", EqualTo, regNumber));
    }

    /**
     * @param spcID used to get all the bookings of an SRC
     *
     * @return true if bookings are found
     */
    public List<SpecialistRepairCenter>getAllBookings(String spcID)
    {
        return persistence.getByCriteria(new Criterion<>(SpecialistRepairCenter.class, "spcID", EqualTo, spcID));
    }

    /**
     * @param date Used to identify all the Vehicles that have not yet been returned
     *
     * @return
     */
    public List<VehicleRepair> getOutstandingV(Date date)
    {
        return persistence.getByCriteria(new Criterion<>(VehicleRepair.class, "returnDate", MoreThan, date));
    }

    /**
     * @param date Used to identify all the Parts that have not yet been returned
     *
     * @return
     */
    public List<PartRepair> getOutstandingP(Date date)
    {
        return persistence.getByCriteria(new Criterion<>(PartRepair.class, "returnDate", MoreThan, date));
    }

    /**
     * @param spcID used to identify an SRC with a matching ID
     *
     * @return true if an SRC is found
     */
    public SpecialistRepairCenter getByID(int spcID)
    {
        List<SpecialistRepairCenter> specialistRepairCenters =  persistence.getByCriteria(new Criterion<>(SpecialistRepairCenter.class,"spcID", EqualTo, spcID));
        if(specialistRepairCenters!=null)
        {
            return specialistRepairCenters.get(0);
        }
         return null;
    }

    /**
     * @param specRepBooking booking to be added
     *
     *@return true if commit is successful
     */
    public boolean addSpecialistBooking(SpecRepBooking specRepBooking)
    {
        return persistence.commitItem(specRepBooking);
    }
}
