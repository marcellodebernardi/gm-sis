package logic.parts;

import domain.Installation;
import domain.PartAbstraction;
import domain.PartOccurrence;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;
import persistence.DatabaseRepository;

import java.util.ArrayList;
import java.util.List;

import static logic.criterion.CriterionOperator.equalTo;
import static logic.criterion.CriterionOperator.moreThan;

/**
 * Created by Muhammad Shakib Hoque on 07/02/2017.
 */
public class PartsSystem {

    private static PartsSystem instance;
    private CriterionRepository persistence = DatabaseRepository.getInstance();
    private PartAbstraction addNewPart;

    private PartsSystem(CriterionRepository persistence) {

        this.persistence = persistence;
    }

    public static PartsSystem getInstance(CriterionRepository persistence) {
        if (instance == null) instance = new PartsSystem(persistence);
        return instance;
    }

    public List<PartAbstraction> getPartList() {

        ArrayList<PartAbstraction> arrayPartsList = new ArrayList<PartAbstraction>();
        return arrayPartsList;

    }

    public PartAbstraction searchPartList(String partName) {
        List<PartAbstraction> result = persistence.getByCriteria(new Criterion<>(PartAbstraction.class, "partName", equalTo, partName));
        return result.size() == 0 ? null : result.get(0);

    }

    public boolean addPart(String partName, String partDescription, Double partPrice, int partStockLevel) {

        PartAbstraction addNewPart = new PartAbstraction(partName, partDescription, partPrice, partStockLevel, null);
        boolean result = persistence.commitItem(addNewPart);
        return result;

    }

    public boolean deletePart(int partAbstractionID) {

        return persistence.deleteItem(new Criterion<>(PartAbstraction.class, "partAbstractionID", equalTo, partAbstractionID));

    }

    public PartAbstraction getPartbyID(int partAbstractionID) {
        List<PartAbstraction> result = persistence.getByCriteria(new Criterion<>(PartAbstraction.class, "partAbstractionID", equalTo, partAbstractionID));
        return result.size() == 0 ? null : result.get(0);

    }

    public boolean addPartOccurrence(PartOccurrence partOccurrence) {
        return persistence.commitItem(partOccurrence);
    }

    public List<PartAbstraction> getByName(String query) {
        return persistence.getByCriteria(new Criterion<>(PartAbstraction.class, "partName", equalTo, query));
    }

    public List<PartOccurrence> getAllFreeOccurrences(PartAbstraction partAbstraction) {
        return persistence.getByCriteria(new Criterion<>(PartOccurrence.class)
                .where("installationID", equalTo, 0)
                .and("bookingID", equalTo, 0)
                .and("partAbstractionID", equalTo, partAbstraction.getPartAbstractionID()));
    }

    public void deleteOccurrence(PartOccurrence partOccurrence, PartAbstraction partAbstraction) {
        persistence.deleteItem(new Criterion<>(PartOccurrence.class, "partOccurrenceID", equalTo, partOccurrence.getPartOccurrenceID()).and("partAbstractionID", equalTo, partAbstraction.getPartAbstractionID()));
    }

    public List<PartAbstraction> getPartAbstractions() {
        return persistence.getByCriteria(new Criterion<>(PartAbstraction.class, "partAbstractionID", moreThan, 0));
    }

    public List<PartOccurrence> getAllUninstalled(int partAbstractionID) {
        return persistence.getByCriteria(new Criterion<>(PartOccurrence.class, "installationID", equalTo, 0).and("partAbstractionID", equalTo, partAbstractionID));
    }

    public PartOccurrence getByInstallationID(int InstallationID) {
        return persistence.getByCriteria(new Criterion<>(PartOccurrence.class, "installationID", equalTo, InstallationID)).get(0);
    }

    public List<Installation> getAllInstallations() {
        return persistence.getByCriteria(new Criterion<>(Installation.class));
    }

    public PartOccurrence getPartOcc(int partOcc) {
        try {
            List<PartOccurrence> partOccurrences = persistence.getByCriteria(new Criterion<>(PartOccurrence.class, "partOccurrenceID", equalTo, partOcc));
            return partOccurrences.get(0);
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public boolean commitInst(Installation installation) {
        return persistence.commitItem(installation);
    }

    public boolean deleteInstallation(int InstallationID) {
        return persistence.deleteItem(new Criterion<>(Installation.class, "installationID", equalTo, InstallationID));
    }

    public void commitAbstraction(PartAbstraction partAbstraction) {
        persistence.commitItem(partAbstraction);
    }


}


