package Domain.SupplierDomain;

public class Discount {
    private int catalogID;
    private int quantityCond;
    private double precentage;
    private int agreementID;

    // Constructor - Initializes a discount with catalog ID, required quantity, and discounted price.
    public Discount(int catalogID, int quantityCond, double precentage,int agreementID) {
        this.catalogID = catalogID;
        this.quantityCond = quantityCond;
        this.precentage = precentage;
        this.agreementID = agreementID;
    }

    // Returns a user-friendly string representation of the discount.
    @Override
    public String toString() {
        return "Buy " + quantityCond + "+ items with " + precentage + " %";
    }

    // Returns the discounted price.
    public double getPrecentage(int amount) {
        if(amount >= quantityCond || amount == -1)return precentage;
        return 0;
    }
    public int getAgreementID() {
        return agreementID;
    }
    public int getCatalogID() {
        return catalogID;
    }
    public int getQuantityCond() {
        return quantityCond;
    }
    public void setQuantityCond(int quantityCond) {
        this.quantityCond = quantityCond;
    }
    public void setPrecentage(double precentage) {
        this.precentage = precentage;
    }
}
