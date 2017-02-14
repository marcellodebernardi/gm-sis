package entities;

import logic.Searchable;

/**
 * @author Marcello De Bernardi
 * @version 0.2
 * @since 0.1
 */
public class Bill implements Searchable {
    private double billAmount;
    private boolean billSettled;


    public Bill(double billAmount, boolean billSettled) {
        this.billAmount = billAmount;
        this.billSettled = billSettled;
    }


    /**
     * Returns the billAmount the customer has to pay.
     *
     * @return billAmount for customer to pay
     */
    public double getBillAmount() {
        return billAmount;
    }

    /**
     * Returns the settlement status of the bill.
     *
     * @return true if bill is billSettled, false otherwise
     */
    public boolean isBillSettled() {
        return billSettled;
    }

    /**
     * Sets the settlement status of the bill.
     *
     * @param billSettled true if customer has paid, false if not.
     */
    public void setBillSettled(boolean billSettled) {
        this.billSettled = billSettled;
    }
}