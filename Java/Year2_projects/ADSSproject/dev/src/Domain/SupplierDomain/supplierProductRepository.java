package Domain.SupplierDomain;

import DTO.SupplierDTO.productForOrderDTO;
import DTO.SupplierDTO.supplierProductDTO;

import java.util.List;

public interface supplierProductRepository {



    boolean createSupplierProduct(int catalogID, double price,int supplierID, String productName,String manufacturer,String productType,int agreementID,String weight);
    supplierProductDTO hasProduct(int supplierId, int productId);
    void displaySuppliersForProduct(int productId);
    productForOrderDTO chooseSProductByCheapestOffer(int productID, int amount);
    boolean isSupplierExist(int productID);
    void changeProductStatus(int supplierID, int productID, boolean status);
    void updateProductPrice(int supplierID, int productID, double price);
    productForOrderDTO createProductForOrder(supplierProductDTO spDTO,int amount);
    void showProductOffers(int productID,int quantity);
    void loadProductToMemory(supplierProductDTO product);
    void setSupplierRepo(supplierRepository repo);
    boolean checkIfSupplierHasProducts(int supplierID) ;
    int getProductIdByName(String productName);
    List<supplierProductDTO> getProductsForTomorrowOrder();

    }
