package entities;

import logic.Searchable;

import java.util.List;

/**
 * @author Muhammad Shakib Hoque on 07/02/2017.
 */
public class PartAbstraction implements Searchable {

    private int partAbstractionID;
    private String partName;
    private String partDescription;
    private double partPrice;
    private int partStockLevel;

    // hierarchical links
    private List<PartOccurrence> occurrenceList;

    public PartAbstraction(int partAbstractionID, String partName, String partDescription, double partPrice,
                           int partStockLevel,
                           @Complex(tableName = "PartOccurrence") List<PartOccurrence> occurrenceList){

        this.partAbstractionID = partAbstractionID;
        this.partName = partName;
        this.partDescription = partDescription;
        this.partPrice = partPrice;
        this.partStockLevel = partStockLevel;
        this.occurrenceList = occurrenceList;
    }

    // todo getters and setters
    public int getPartsID(){
        return partAbstractionID;
    }

    public String getPartName(){

        return partName;
    }

    public String getPartDescription(){

        return partDescription;

    }

    public double getPartPrice(){

        return partPrice;

    }
    public int getPartStockLevel() {

        return partStockLevel;
    }

}