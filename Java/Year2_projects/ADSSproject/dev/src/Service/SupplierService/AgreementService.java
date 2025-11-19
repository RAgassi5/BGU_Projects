package Service.SupplierService;

import Domain.SupplierDomain.*;

public class AgreementService {
    private final supplierRepository supplierRepo;
    private final supplierProductRepository supplierProductRepo;
    private final agreementRepository agreementRepository;

    public AgreementService(supplierRepository supplierRepo,
                            supplierProductRepository supplierProductRepo,
                            agreementRepository agreementRepository) {
        this.supplierRepo = supplierRepo;
        this.supplierProductRepo = supplierProductRepo;
        this.agreementRepository = agreementRepository;
    }

    // Adds a new product to the supplier and to the system repository.
    // If a matching agreement exists, the product is also added to that agreement.
    // Always returns the created Product object.
    public boolean addNewProductToAgreement(int catalogID, double price, int supplierID, String productName, int agreementID,String manufacturer,String productType,String weight) {
        //func to find the agreement in DB
        return supplierProductRepo.createSupplierProduct(catalogID, price, supplierID,productName,manufacturer,productType ,agreementID,weight);
    }

    public boolean checkValidAgreement(String type1,int supplierID) {
        return agreementRepository.checkValid(type1,supplierID);
    }

    public void displayAgreement(int supplierID,int agreementID) {
        agreementRepository.displayAgreement(supplierID,agreementID);
    }

    public boolean existAgreement(int supplierID,int agreementID) {
        return agreementRepository.existAgreement(supplierID,agreementID);
    }

    public void changeAgreementStatus(int supplierID,int agreementID, boolean status) {
        agreementRepository.changeStatus(supplierID,agreementID,status);
    }

    public void updateDiscountQuantityCond(int supplierID, int agreementID, int quantityCond) {
        agreementRepository.changeQuantityCondInDiscount(supplierID,agreementID,quantityCond);
    }

    public void updateDiscountPercentage(int supplierID, int agreementID, double percentage) {
        agreementRepository.changePrecentageInDiscount(supplierID,agreementID,percentage);
    }
    public void updateProductPrice(int supplierID, int agreementID, int productID, double price) {
        supplierProductRepo.updateProductPrice(supplierID,productID,price);
    }

    public void createDiscountForProduct(int agreementID, int catalogID, int supplierID, int quantityCond,double percentage) {
        agreementRepository.addDiscount(agreementID,catalogID,supplierID,quantityCond,percentage);
    }


}
