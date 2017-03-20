package logic.spc;


import domain.*;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;
import persistence.DatabaseRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static logic.criterion.CriterionOperator.*;

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
        //todo Take relevant information from user required to create a spc and make an object to pass to persistence
        SpecialistRepairCenter add  = new SpecialistRepairCenter(name, address, phone, email, null);
        return persistence.commitItem(add);
    }

    /**
     * @param delete spc that is being requested to be deleted
     *
     * @return true if deleting of spc is successful.
     */
    public boolean deleteRepairCenter(int delete)
    {
        return persistence.deleteItem(new Criterion<>(SpecialistRepairCenter.class, "spcID", EqualTo, delete));
    }

    /**
     * @param edit spc that is being updated
     *
     * @return true if modifying an spc is successful.
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
        List<VehicleRepair> vehicleRepairs = persistence.getByCriteria(new Criterion<>(VehicleRepair.class, "vehicleRegNumber", Matches, regNumber));
        if(vehicleRepairs.size()>0)
        {
            return vehicleRepairs;
        }
        else return null;
    }

    /**
     * @param spcID used to get all the bookings of an spc
     *
     * @return true if bookings are found
     */
    public List<SpecialistRepairCenter>getAllBookings(int spcID)
    {
        return persistence.getByCriteria(new Criterion<>(SpecialistRepairCenter.class, "spcID", EqualTo, spcID));
    }

    public List<SpecialistRepairCenter>getBookingsByName(String spcName)
    {
        return persistence.getByCriteria(new Criterion<>(SpecialistRepairCenter.class, "name", Matches, spcName));
    }

    /**
     *
     *
     * @return
     */
    public List<VehicleRepair> getOutstandingV()
    {
        List<VehicleRepair> vehicleRepairs =  persistence.getByCriteria(new Criterion<>(VehicleRepair.class, "vehicleRegNumber", Matches, ""));
        List<VehicleRepair> outstanding = new ArrayList<>();
        for(VehicleRepair v: vehicleRepairs)
        {
            if(v.getReturnDate().after(new Date()))
            {
                outstanding.add(v);
            }
        }
        return outstanding;
    }

    /**
     *
     * @returnad
     */
    public List<PartRepair> getOutstandingP()
    {
        List<PartRepair> partRepairs =  persistence.getByCriteria(new Criterion<>(PartRepair.class, "spcRepID", MoreThan, 0));
        List<PartRepair> outstanding = new ArrayList<>();
        for(PartRepair p: partRepairs)
        {
            if(p.getReturnDate().after(new Date()))
            {
                outstanding.add(p);
            }
        }
        return outstanding;
    }

    /**
     * @param spcID used to identify an spc with a matching ID
     *
     * @return true if an spc is found
     */
    public SpecialistRepairCenter getByID(int spcID)
    {

            List<SpecialistRepairCenter> specialistRepairCenters = persistence.getByCriteria(new Criterion<>(SpecialistRepairCenter.class, "spcID", EqualTo, spcID));
            if (specialistRepairCenters != null) {
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
     * This method is designed to delete ALL BOOKINGS RELATED TO A PARTICULAR spc ONCE IT HAS BEEN DELETED
     * @param spcID The ID of the deleted spc
     * PLEASE NOTE THAT THIS METHOD **MUST** BE CALLED **BEFORE** EXECUTING DELETION METHOD OF THE spc OR SQL WILL NOT FIND ANY BOOKINGS OF THE DELETED spc (EVEN IF THEY EXIST)
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
     * Updates an existing spc
     *
     * @param update is the updated spc
     */
    public void updateBookings(SpecRepBooking update)
    {
        persistence.commitItem(update);
    }

    public List<PartRepair> getAllPartRepairs(int partID)
    {
        return persistence.getByCriteria(new Criterion<>(PartRepair.class, "partOccurrenceID",EqualTo, partID));
    }

    public List<PartRepair> returnAllPartRepairs()
    {
        return persistence.getByCriteria(new Criterion<>(PartRepair.class, "partOccurrenceID", Matches, ""));
    }
    public List<VehicleRepair> returnAllVehicleRepairs()
    {
        return persistence.getByCriteria(new Criterion<>(VehicleRepair.class, "vehicleRegNumber", Matches, ""));
    }

    public List<Installation> getVehicleInstallations(String reg)
    {
        return persistence.getByCriteria(new Criterion<>(Installation.class, "vehicleRegNumber", Matches,reg));
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

    public boolean commitInstallations(Installation installation)
    {
        return persistence.commitItem(installation);
    }

    public boolean deleteInstallation(int InstallationID)
    {
        return persistence.deleteItem(new Criterion<>(Installation.class,"installationID",EqualTo,InstallationID));
    }

    public PartOccurrence getPartOcc(int partOcc)
    {
        try{
            List<PartOccurrence> partOccurrences = persistence.getByCriteria(new Criterion<>(PartOccurrence.class,"partOccurrenceID",EqualTo,partOcc));
            return partOccurrences.get(0);
        }
        catch(NullPointerException e)
        {
            return null;
        }
    }

    public List<VehicleRepair> getBySpcID(int spcID)
    {
        return persistence.getByCriteria(new Criterion<>(VehicleRepair.class,"spcID",EqualTo,spcID));
    }

    public Customer getByCustomerID(int customerID)
    {
        List<Customer> customers = persistence.getByCriteria(new Criterion<>(Customer.class,"customerID",EqualTo, customerID));
        return customers.get(0);
    }


    public VehicleRepair getBySpcRepID(int spcRep)
    {

        List<VehicleRepair> vehicleRepairs = persistence.getByCriteria(new Criterion<>(VehicleRepair.class,"spcRepID",EqualTo,spcRep));
        if(vehicleRepairs.get(0)!=null)
        {
            return vehicleRepairs.get(0);
        }
        return null;


    }

    public Vehicle findVehicle(String reg)
    {
        return persistence.getByCriteria(new Criterion<>(Vehicle.class,"vehicleRegNumber",EqualTo,reg)).get(0);
    }

    public PartOccurrence findAPart(int partID)
    {
        return persistence.getByCriteria(new Criterion<>(PartOccurrence.class,"partOccurrenceID",EqualTo,partID)).get(0);
    }

    public List<Customer> getByName(String query)
    {
        return persistence.getByCriteria(new Criterion<>(Customer.class,"customerFirstname", Matches,query).or("customerSurname", Matches,query));

    }

    public DiagRepBooking findBooking(int bookingID)
    {
        return persistence.getByCriteria(new Criterion<>(DiagRepBooking.class,"bookingID",EqualTo, bookingID)).get(0);
    }


}