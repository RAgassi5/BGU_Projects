package Domain.SupplierDomain;
import DataAccess.SupplierDAO.supplierDAO;


public class Supplier {
    // Static counter to generate unique supplier IDs
    private int supplierID;
    private String supplierName;
    private String bankAccountNumber;
    private boolean isActive;

    // Constructor to create a new Supplier
    public Supplier(String supplierName,String bankAccountNumber) {
        this.supplierName = supplierName;
        this.bankAccountNumber = bankAccountNumber;
        isActive = true;
    }
    public Supplier(int supplierID, String supplierName, String bankAccountNumber, boolean isActive) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.bankAccountNumber = bankAccountNumber;
        this.isActive = isActive;
    }

    // Getters
    public int getSupplierID() {return supplierID;}
    public String getSupplierName() {return supplierName;}
    public String getBankAccountNumber() {return bankAccountNumber;}
    public boolean isActive() {return isActive;}

    // Setters
    public void setBankAccountNumber(String bankAccountNumber){this.bankAccountNumber = bankAccountNumber;}
    public void setActive() {this.isActive = true;}
    public void setUnActive() {this.isActive = false;}

    // Nicely format all supplier details
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Supplier ID: ").append(supplierID).append("\n");
        sb.append("Name: ").append(supplierName).append("\n");
        sb.append("Bank Account: ").append(bankAccountNumber).append("\n");
        sb.append("Active: ").append(isActive ? "Yes" : "No").append("\n");
        return sb.toString();
    }


    }

