package entities;

import logic.Criterion;
/**
 * Created by Muhammad Shakib Hoque on 07/02/2017.
 */
public abstract class PartsAbstraction implements Criterion {

    private int partsID;
    private String partName;
    private String partDescription;
    private double partPrice;

    public PartsAbstraction(int partsID, String partName, String partDescription, double partPrice){

        this.partsID = partsID;
        this.partName = partName;
        this.partDescription = partDescription;
        this.partPrice = partPrice;

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


}
