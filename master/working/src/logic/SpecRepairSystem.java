package logic;


import domain.*;
import javafx.fxml.FXML;
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
        return persistence.getByCriteria(new Criterion<>(VehicleRepair.class, "vehicleRegNumber", Regex, regNumber));
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

    /**
     * Deletes a vehicle repair booking
     * @param reg the registration of the vehicle which was booked for repair
     * @param spcID the ID of the specialist center responsible for the repair
     * @param date the date on which the repair was scheduled
     * @return true if deletion was successful
     */
    public boolean deleteVehicleRepair(String reg, int spcID, Date date)
    {
      return persistence.deleteItem(new Criterion<>(VehicleRepair.class, "vehicleRegNumber", EqualTo,reg).and("spcID",EqualTo,spcID).and("deliveryDate",EqualTo,date));
    }

    /**
     * Deletes a part repair booking
     * @param partOccurrenceID the ID of the part which was booked for repair
     * @param spcID the ID of the specialist center responsible for the repair
     * @param date the date on which the repair was scheduled
     * @return true if the deletion was successful
     */
    public boolean deletePartRepair(int partOccurrenceID, int spcID, Date date)
    {
        return persistence.deleteItem(new Criterion<>(PartRepair.class, "partOccurrenceID", EqualTo,partOccurrenceID).and("spcID",EqualTo,spcID).and("deliveryDate",EqualTo,date));
    }

    /**
     * This method is designed to delete ALL BOOKINGS RELATED TO A PARTICULAR SRC ONCE IT HAS BEEN DELETED
     * @param spcID The ID of the deleted SRC
     * PLEASE NOTE THAT THIS METHOD **MUST** BE CALLED **BEFORE** EXECUTING DELETION METHOD OF THE SRC OR SQL WILL NOT FIND ANY BOOKINGS OF THE DELETED SRC (EVEN IF THEY EXIST)
     */
    public void deleteAllSubsequentBookings(int spcID)
    {
        persistence.deleteItem(new Criterion<>(PartRepair.class,"spcID",EqualTo,spcID));
        persistence.deleteItem(new Criterion<>(VehicleRepair.class,"spcID",EqualTo,spcID));
    }

    public VehicleRepair findVehicleRepairBooking(int bookingID)
    {
         List<VehicleRepair> vehicleRepairs = persistence.getByCriteria(new Criterion<>(VehicleRepair.class,"spcRepID",EqualTo,bookingID));
         if(vehicleRepairs!=null) {
             return vehicleRepairs.get(0);
         }
         else return null;
    }

    public PartRepair findPartRepairBooking(int bookingID)
    {
        List<PartRepair> partRepairs = persistence.getByCriteria(new Criterion<>(PartRepair.class,"spcRepID",EqualTo,bookingID));
        if(partRepairs!=null) {
            return partRepairs.get(0);
        }
        else return null;
    }

    /**
     * Updates an existing SRC
     *
     * @param update is the updated SRC
     */
    public void updateBookings(SpecRepBooking update)
    {
        persistence.commitItem(update);
    }

    public List<PartRepair> getAllPartRepairs(int partID)
    {
        return persistence.getByCriteria(new Criterion<>(PartRepair.class, "partOccurrenceID",EqualTo, partID));
    }

    public List<Installation> getVehicleInstallations(String reg)
    {
        return persistence.getByCriteria(new Criterion<>(Installation.class, "vehicleRegNumber",Regex,reg));
    }

    public boolean deleteByRepIDV(int rep)
    {
        return persistence.deleteItem(new Criterion<>(VehicleRepair.class,"spcRepID",EqualTo,rep));
    }

    public boolean deleteByRepIDP(int rep)
    {
        return persistence.deleteItem(new Criterion<>(PartRepair.class,"spcRepID",EqualTo,rep));
    }

    public Installation getByInstallationID(int InstallationID)
    {
        try {
            List<Installation> installations = persistence.getByCriteria(new Criterion<>(Installation.class, "installationID", EqualTo, InstallationID));
            return installations.get(0);
        }
        catch (NullPointerException e)
        {
            return null;
        }

    }

    public boolean updateInstallation(Installation installation)
    {
        return persistence.commitItem(installation);
    }

    public boolean deleteInstallation(int InstallationID)
    {
        return persistence.deleteItem(new Criterion<>(Installation.class,"installationID",EqualTo,InstallationID));
    }


}
