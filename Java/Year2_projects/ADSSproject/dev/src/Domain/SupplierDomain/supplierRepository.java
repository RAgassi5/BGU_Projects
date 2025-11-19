package Domain.SupplierDomain;

public interface supplierRepository {

    int addSupplier(String name, String bankAccountNumber);
    boolean supplierExists(int supplierID);
    boolean changeSupplierStatus(int supplierID);
    void changeBankAccountNumber(String bankAccountNumber, int supplierID);

    void addContact(int supplierID, String name, String phone);
    void displaySupplier(int supplierID);
    void displaySupplierTypes(int supplierID);
    void displaySupplierManufacturers(int supplierID);
    void displayActives();

    void addManufacturer(int supplierID, String manufacturer);
    void addProductType(int supplierID, String productType) ;
    String getSupplierStatus(int supplierID);

    }
