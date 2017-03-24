package logic.parts;

import domain.Installation;
import domain.PartAbstraction;
import domain.PartOccurrence;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;
import persistence.DatabaseRepository;

import java.util.ArrayList;
import java.util.List;

import static logic.criterion.CriterionOperator.EqualTo;
import static logic.criterion.CriterionOperator.Matches;
import static logic.criterion.CriterionOperator.MoreThan;

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
        List<PartAbstraction> result = persistence.getByCriteria(new Criterion<>(PartAbstraction.class, "partName", EqualTo, partName));
        return result.size() == 0 ? null : result.get(0);

    }

    public boolean addPart(String partName, String partDescription, Double partPrice, int partStockLevel) {

        PartAbstraction addNewPart = new PartAbstraction(partName, partDescription, partPrice, partStockLevel, null);
        boolean result = persistence.commitItem(addNewPart);
        return result;

    }

    public boolean deletePart(int partAbstractionID) {

        return persistence.deleteItem(new Criterion<>(PartAbstraction.class, "partAbstractionID", EqualTo, partAbstractionID));

    }

    public PartAbstraction getPartbyID(int partAbstractionID) {
        List<PartAbstraction> result = persistence.getByCriteria(new Criterion<>(PartAbstraction.class, "partAbstractionID", EqualTo, partAbstractionID));
        return result.size() == 0 ? null : result.get(0);

    }

    public boolean addPartOccurrence(PartOccurrence partOccurrence) {
        return persistence.commitItem(partOccurrence);
    }

    public List<PartAbstraction> getByName(String query) {
        return persistence.getByCriteria(new Criterion<>(PartAbstraction.class, "partName", EqualTo, query));
    }

    public List<PartOccurrence> getAllFreeOccurrences(PartAbstraction partAbstraction) {
        return persistence.getByCriteria(new Criterion<>(PartOccurrence.class, "installationID", EqualTo, 0)
                .and("partAbstractionID", EqualTo, partAbstraction.getPartAbstractionID()));
    }

    public void deleteOccurrence(PartOccurrence partOccurrence, PartAbstraction partAbstraction) {
        persistence.deleteItem(new Criterion<>(PartOccurrence.class, "partOccurrenceID", EqualTo, partOccurrence.getPartOccurrenceID()).and("partAbstractionID", EqualTo, partAbstraction.getPartAbstractionID()));
    }

    public List<PartAbstraction> getPartAbstractions() {
        return persistence.getByCriteria(new Criterion<>(PartAbstraction.class, "partAbstractionID", MoreThan, 0));
    }

    public List<PartOccurrence> getAllUninstalled(int partAbstractionID) {
        return persistence.getByCriteria(new Criterion<>(PartOccurrence.class, "installationID", EqualTo, 0).and("partAbstractionID", EqualTo, partAbstractionID));
    }

    public PartOccurrence getByInstallationID(int InstallationID) {
        return persistence.getByCriteria(new Criterion<>(PartOccurrence.class, "installationID", EqualTo, InstallationID)).get(0);
    }

    public List<Installation> getAllInstallations() {
        return persistence.getByCriteria(new Criterion<>(Installation.class));
    }

    public PartOccurrence getPartOcc(int partOcc) {
        try {
            List<PartOccurrence> partOccurrences = persistence.getByCriteria(new Criterion<>(PartOccurrence.class, "partOccurrenceID", EqualTo, partOcc));
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
        return persistence.deleteItem(new Criterion<>(Installation.class, "installationID", EqualTo, InstallationID));
    }

    public void commitAbstraction(PartAbstraction partAbstraction)
    {
        persistence.commitItem(partAbstraction);
    }


}


