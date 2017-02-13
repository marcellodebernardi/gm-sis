package entities;

import logic.Searchable;

/**
 * @author Muhammad Shakib Hoque on 07/02/2017.
 */
public class PartAbstraction implements Searchable {

    private int partsID;
    private String partName;
    private String partDescription;
    private double partPrice;
    private int partStockLevel;

    public PartAbstraction(int partsID, String partName, String partDescription, double partPrice, int partStockLevel){

        this.partsID = partsID;
        this.partName = partName;
        this.partDescription = partDescription;
        this.partPrice = partPrice;
        this.partStockLevel = partStockLevel;

    }

    public int getPartsID(){

        return partsID;
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