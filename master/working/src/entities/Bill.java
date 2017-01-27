package entities;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class Bill {
    private int billID;
    private double amount;
    private boolean settled;


    Bill(int billID, double amount) {
        this.billID = billID;
        this.amount = amount;
        settled = false;
    }

    /**
     * Returns the identification number of the bill.
     *
     * @return bill ID
     */
    public int getBillID() {
        return billID;
    }

    /**
     * Returns the amount the customer has to pay.
     *
     * @return amount for customer to pay
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the settlement status of the bill.
     *
     * @return true if bill is settled, false otherwise
     */
    public boolean isSettled() {
        return settled;
    }

    /**
     * Sets the amount of the bill to pay.
     *
     * @param amount amount to pay
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Sets the settlement status of the bill.
     *
     * @param settled true if customer has paid, false if not.
     */
    public void setSettled(boolean settled) {
        this.settled = settled;
    }
}
