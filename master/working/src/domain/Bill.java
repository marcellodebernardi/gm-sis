package domain;

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


    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public boolean isBillSettled() {
        return billSettled;
    }

    public void setBillSettled(boolean billSettled) {
        this.billSettled = billSettled;
    }
}