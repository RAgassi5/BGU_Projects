package Service.SupplierService;


import DTO.SupplierDTO.supplierProductDTO;
import Domain.SupplierDomain.*;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class SupplierService {
    private final supplierProductRepository productRepo;
    private final supplierRepository supplierRepo;
    private final agreementRepository agreementRepo;

    public SupplierService(supplierRepository supplierRepo,
                           supplierProductRepository productRepo,
                           agreementRepository agreementRepo) {
        this.supplierRepo = supplierRepo;
        this.productRepo = productRepo;
        this.agreementRepo = agreementRepo;
    }



    public boolean doesSupplierExist(int supplierId) {
        return supplierRepo.supplierExists(supplierId);
    }

    // This method creates a new supplier and adds it to the supplier repository.
    public int createSupplier(String supplierName,String bankAccountNumber) {
        return supplierRepo.addSupplier(supplierName,bankAccountNumber);
    }


    public boolean changeStatusService(int supplierID) {
        return supplierRepo.changeSupplierStatus(supplierID);
    }

    // Adds a new contact (name and phone) to a specific supplier.
    public void addContactToSupplier(int supplierID, String name, String phone) {
        supplierRepo.addContact(supplierID, name, phone);
    }

    public void changeBankAccountNumber(int supplierID, String bankAccountNumber) {
            supplierRepo.changeBankAccountNumber(bankAccountNumber,supplierID);
    }

    // Creates a new PickUp agreement for a supplier if valid, adds it to the supplier, and returns the agreement ID.
    public int CreatePickUpAgreement(String paymentMethod,int supplierID,String adress ) {
        return(agreementRepo.createPickUp(supplierID,adress,paymentMethod));
    }

    // Creates a new Fixed Delivery agreement for a supplier and returns the agreement ID.
    public int CreateFixedAgreement(String paymentMethod, int supplierID, ArrayList<DayOfWeek> supplyDays ) {
        return agreementRepo.createFixed(paymentMethod,supplierID,supplyDays);
    }

    // Creates a new Flexible Delivery agreement for a supplier and returns the agreement ID.
    public int CreateFlexibleAgreement(String paymentMethod,int supplierID,int supplyDays ) {
        return agreementRepo.createFlexible(paymentMethod,supplierID,supplyDays);
    }


    // Checks if a product exists for a supplier by catalog ID.
    public supplierProductDTO checkIfProductExist(int supplierID, int productID) {
        return productRepo.hasProduct(supplierID, productID);
    }


    // Returns the supplier object itself, letting the UI layer decide what to do with it.
    public void displaySuppDetails(int supplierID) {
        supplierRepo.displaySupplier(supplierID);
    }

    // Returns a list of suppliers ID who supply a given product ID.
    public void displayProductSuppliers(int productID) {
        productRepo.displaySuppliersForProduct(productID);
    }

    // Returns a list of IDs for all active suppliers by checking their active status.
    public void displayActiveSuppliers() {
        supplierRepo.displayActives();

    }

    // Returns a list of product types supplied by the given supplier ID.
    public void displaySupplierTypes(int supplierID) {
       supplierRepo.displaySupplierTypes(supplierID);
    }

    // Returns a list of manufacturers associated with the given supplier ID.
    public void displaysupplierManufacturers(int supplierID) {
        supplierRepo.displaySupplierManufacturers(supplierID);
    }

    public void changeProductStatus(int supplierID, int productID, boolean productStatus) {
        productRepo.changeProductStatus(supplierID,productID,productStatus);
    }

    public String showSupplierStatus(int supplierID) {
        return supplierRepo.getSupplierStatus(supplierID);
    }




}
