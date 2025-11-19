package Domain.SupplierDomain;

import DataAccess.SupplierDAO.AgreementDAO;

import java.time.LocalDate;

public class Agreement {
    private int agreementID;
    private int supplierID;
    private String paymentMethod;
    private LocalDate paymentDate;
    private boolean agreementStatus;

    // Constructor - initializes a new agreement with a supplier ID and payment method.
    // Automatically assigns a unique agreement ID.
    public Agreement(int supplierID, String paymentMethod) {
        agreementStatus = true;
        this.supplierID = supplierID;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDate.now();
    }

    public Agreement(int agreementID, int supplierID, String paymentMethod, LocalDate paymentDate, boolean agreementStatus) {
        this.agreementID = agreementID;
        this.supplierID = supplierID;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.agreementStatus = agreementStatus;
    }

    // Returns the type of agreement. Default is "agreement" (should be overridden in subclasses).
    public String getAgreementType(){return "agreement";}

    // Returns the current status of the agreement (true = active, false = inactive).
    public boolean getAgreementStatus() {return agreementStatus;}

    // Toggles the agreement status (active ↔ inactive).
    public void setAgreementStatus(boolean agreementStatus) {
        this.agreementStatus = agreementStatus;
    }

    public void setAgreementID(int agreementID) {
        this.agreementID = agreementID;
    }

    // Returns the unique ID of the agreement.
    public int getAgreementID() {
        return agreementID;
    }

    // Returns the supplier ID associated with this agreement.
    public int getSupplierID() {
        return supplierID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    // Returns a string that represents all agreement details, including associated products and discounts.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Agreement ID: ").append(agreementID).append("\n");
        sb.append("Supplier ID: ").append(supplierID).append("\n");
        sb.append("Payment Method: ").append(paymentMethod).append("\n");
        sb.append("Status: ").append(agreementStatus ? "Active" : "Inactive").append("\n");
        sb.append("Products:\n");
        return sb.toString();
    }

}
