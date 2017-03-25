package logic.customer;

import domain.*;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;
import persistence.DatabaseRepository;

import java.util.List;

import static logic.criterion.CriterionOperator.equalTo;
import static logic.criterion.CriterionOperator.matches;

/**
 * Created by EBUBECHUKWU on 14/02/2017.
 */
public class CustomerSystem {

    private static CustomerSystem instance;
    private CriterionRepository persistence;


    private CustomerSystem() {
        this.persistence = DatabaseRepository.getInstance();
    }

    public static CustomerSystem getInstance() {
        if (instance == null) {
            instance = new CustomerSystem();
        }
        return instance;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = persistence.getByCriteria(new Criterion<>(Customer.class));
        return allCustomers;
    }

    public boolean addCustomer(String customerFirstname, String customerSurname, String customerAddress, String customerPostcode, String customerPhone, String customerEmail, CustomerType customerType) {
        Customer add = new Customer(customerFirstname, customerSurname, customerAddress, customerPostcode, customerPhone, customerEmail, customerType, null);
        return persistence.commitItem(add);
    }

    public boolean editCustomer(Customer customer) {
        return persistence.commitItem(customer);
    }

    public boolean deleteCustomer(int customerID) {
        return persistence.deleteItem(new Criterion<>(Customer.class, "customerID", equalTo, customerID));
    }

    public List<Customer> searchCustomerByFirstname(String customerFirstname) {
        List<Customer> results = persistence.getByCriteria(new Criterion<>(Customer.class,
                "customerFirstname", matches, customerFirstname));
        return results;
    }

    public List<Customer> searchCustomerBySurname(String customerSurname) {
        List<Customer> results = persistence.getByCriteria(new Criterion<>(Customer.class,
                "customerSurname", matches, customerSurname));
        return results;
    }

    public List<Customer> searchCustomerByVehicleRegistrationNumber(String vehicleRegNumber) {
        List<Vehicle> vResult = persistence.getByCriteria(new Criterion<>(Vehicle.class, "vehicleRegNumber", matches, vehicleRegNumber));
        int customerID = vResult.get(0).getCustomerID();
        List<Customer> results = persistence.getByCriteria(new Criterion<>(Customer.class, "customerID", equalTo, customerID));
        return results;
    }

    public List<Customer> searchCustomerByType(CustomerType customerType) {
        List<Customer> results = persistence.getByCriteria(new Criterion<>(Customer.class, "customerType", equalTo, customerType));
        return results;
    }

    public List<Vehicle> searchCustomerVehicles(int customerID) {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class, "customerID", equalTo, customerID));
        return results;
    }

    public List<DiagRepBooking> searchCustomerBookings(String vehicleRegNumber) {
        List<DiagRepBooking> results = persistence.getByCriteria(new Criterion<>(DiagRepBooking.class, "vehicleRegNumber", matches, vehicleRegNumber));
        return results;
    }

    public List<Installation> searchInstallationTable(String vehicleRegNumber) {
        List<Installation> iResult = persistence.getByCriteria(new Criterion<>(Installation.class, "vehicleRegNumber", matches, vehicleRegNumber));
        return iResult;
    }

    public List<PartOccurrence> searchPartOccurrenceTable(int installationID) {
        List<PartOccurrence> result = persistence.getByCriteria(new Criterion<>(PartOccurrence.class, "installationID", equalTo, installationID));
        return result;
    }

    public List<PartAbstraction> searchPartAbstractionTable(int partAbstractionID) {
        List<PartAbstraction> result = persistence.getByCriteria(new Criterion<>(PartAbstraction.class, "partAbstractionID", equalTo, partAbstractionID));
        return result;
    }

    public List<PartAbstraction> searchCustomerVehiclePart(int partAbstractionID) {
        List<PartAbstraction> results = persistence.getByCriteria(new Criterion<>(PartAbstraction.class, "partAbstractionID", equalTo, partAbstractionID));
        return results;
    }

    public Customer getACustomers(int customerID) {
        List<Customer> results = persistence.getByCriteria(new Criterion<>(Customer.class, "customerID", equalTo, customerID));
        return results.size() != 0 ? results.get(0) : null;
    }
}
