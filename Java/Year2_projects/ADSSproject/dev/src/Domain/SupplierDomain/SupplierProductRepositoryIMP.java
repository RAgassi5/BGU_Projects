package Domain.SupplierDomain;

import DataAccess.InventoryDAO.ProductDao;
import DataAccess.SupplierDAO.supplierProductDAO;
import DataAccess.SupplierDAO.orderDAO;
import DTO.SupplierDTO.productForOrderDTO;
import DTO.SupplierDTO.supplierProductDTO;
import Domain.InventoryDomain.ProductUnitRepository;

import java.util.*;

public class SupplierProductRepositoryIMP implements Domain.SupplierDomain.supplierProductRepository {
    private final Map<Integer, Map<Integer, Domain.SupplierDomain.SupplierProduct>> productsByProductID = new HashMap<>();
    private final Map<String, Integer> productsByProductName = new HashMap<>();

    private final supplierProductDAO supplierProductDAO;
    private final orderDAO orderDAO;
    private final agreementRepository agreementRepository;
    private supplierRepository supplierRepo;
    private ProductDao productDao = new ProductDao();

    public SupplierProductRepositoryIMP(supplierProductDAO supplierProductDAO, agreementRepository agreementRepository, supplierRepository supplierRepo,orderDAO orderDAO) {
        this.supplierProductDAO = supplierProductDAO;
        this.agreementRepository = agreementRepository;
        this.supplierRepo = supplierRepo;
        this.orderDAO = orderDAO;
    }

    public void setSupplierRepo(supplierRepository repo) {
        this.supplierRepo = repo;
    }
    @Override
    public boolean createSupplierProduct(int catalogID, double price, int supplierID, String productName, String manufacturer, String productType, int agreementID,String weight) {
        int productID = -1;

        if (productsByProductName.containsKey(productName)) {
            productID = productsByProductName.get(productName);
        } else {
            productID = supplierProductDAO.getProductIdByName(productName);
            if (productID > 0) {
                productsByProductName.put(productName, productID);
            } else {
                try {
                    productID = productDao.getProductByDetails(productName, manufacturer, weight);
                }
                catch (Exception e) {
                    return false;
                }
            }
        }
        if(productID == -1) return false;
        SupplierProduct supplierProduct = new SupplierProduct(productID, catalogID, price, supplierID, productName, manufacturer, productType, agreementID);
        supplierProduct.setAgreementType(agreementRepository.getAgreementType(agreementID));
        supplierProductDTO dto = toDTO(supplierProduct);

        try {
            supplierProductDAO.insertSupplierProduct(dto);
            addProdToMap(supplierID, productID, supplierProduct);
            supplierRepo.addProductType(supplierID, productType);
            supplierRepo.addManufacturer(supplierID, manufacturer);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Failed to insert product to DB: " + e.getMessage());
            return false;
        }
    }


    @Override
    public supplierProductDTO hasProduct(int supplierId, int productId) {
        SupplierProduct sp = productsByProductID.getOrDefault(productId, Map.of()).get(supplierId);
        if (sp == null) return null;
        return toDTO(sp);
    }

    @Override
    public boolean isSupplierExist(int productID) {
        return productsByProductID.containsKey(productID);
    }

    @Override
    public void changeProductStatus(int supplierID, int productID, boolean status) {
        SupplierProduct product = getProduct(supplierID, productID);
        if (product != null) {
            product.setStatus(status);
            try {
                supplierProductDAO.updateSupplierProduct(toDTO(product));
            } catch (Exception e) {
                System.err.println("❌ Failed to update product status: " + e.getMessage());
            }
        }
    }

    @Override
    public void updateProductPrice(int supplierID, int productID, double price) {
        SupplierProduct product = getProduct(supplierID, productID);
        if (product != null) {
            product.setPrice(price);
            try {
                supplierProductDAO.updateSupplierProduct(toDTO(product));
            } catch (Exception e) {
                System.err.println("❌ Failed to update product price: " + e.getMessage());
            }
        }
    }

    @Override
    public productForOrderDTO createProductForOrder(supplierProductDTO spDTO, int amount) {
        double percent = agreementRepository.getPercentage(spDTO.catalogID(), amount);
        double finalPrice = amount * (1 - percent * 0.1) * spDTO.price();
        return new productForOrderDTO(spDTO, amount, finalPrice);
    }

    @Override
    public void showProductOffers(int productID, int quantity) {
        productsByProductID.getOrDefault(productID, Map.of()).values().forEach(sp -> {
            double discount = agreementRepository.getPercentage(sp.getCatalogID(), quantity);
            double price = quantity * sp.getPrice() * (1 - (discount * 0.01));
            System.out.println("supplierID : " + sp.getSupplierID() + " --- price: " + price + " --- for quantity of: " + quantity);
        });
    }

    @Override
    public void displaySuppliersForProduct(int productID) {
        Map<Integer, SupplierProduct> supplierMap = productsByProductID.get(productID);
        if (supplierMap == null || supplierMap.isEmpty()) {
            System.out.println("No suppliers found for product ID " + productID);
            return;
        }
        System.out.println("Suppliers for product ID " + productID + ":");
        supplierMap.keySet().forEach(id -> System.out.println("- Supplier ID: " + id));
    }

    private void addProdToMap(int supplierID, int productID, SupplierProduct sprod) {
        Map<Integer, SupplierProduct> sProductMap = productsByProductID.computeIfAbsent(productID, k -> new HashMap<>());
        sProductMap.put(supplierID, sprod);
        productsByProductName.put(sprod.getProductName(), productID);
    }

    private SupplierProduct getProduct(int supplierID, int productID) {
        Map<Integer, SupplierProduct> supplierMap = productsByProductID.get(productID);
        return (supplierMap != null) ? supplierMap.get(supplierID) : null;
    }

    @Override
    public productForOrderDTO chooseSProductByCheapestOffer(int productID, int amount) {
        SupplierProduct best = null;
        double bestPrice = Double.MAX_VALUE;
        for (SupplierProduct sp : productsByProductID.getOrDefault(productID, Map.of()).values()) {
            double discount = agreementRepository.getPercentage(sp.getCatalogID(), amount);
            double finalPrice = sp.getPrice() * (1 - (discount * 0.01));
            if (finalPrice < bestPrice) {
                bestPrice = finalPrice;
                best = sp;
            }
        }
        return (best == null) ? null : new productForOrderDTO(toDTO(best), amount, amount * bestPrice);
    }

    private supplierProductDTO toDTO(SupplierProduct sp) {
        return new supplierProductDTO(sp.getCatalogID(), sp.getProductID(), sp.getManufacturer(), sp.getSupplierID(), sp.getAgreementID(), sp.getPrice(), sp.getAgreementType(), sp.getProductName(), sp.getProductType());
    }

    @Override
    public void loadProductToMemory(supplierProductDTO productDTO) {
        int productID = productDTO.productID();
        int supplierID = productDTO.supplierID();

        SupplierProduct product = new SupplierProduct(
                productID,
                productDTO.catalogID(),
                productDTO.price(),
                supplierID,
                productDTO.productName(),
                productDTO.manufacturer(),
                productDTO.productType(),
                productDTO.agreementID()
        );

        Map<Integer, SupplierProduct> supplierMap = productsByProductID.computeIfAbsent(productID, k -> new HashMap<>());
        supplierMap.put(supplierID, product);
        productsByProductName.put(productDTO.productName(), productID);
    }

    @Override
    public boolean checkIfSupplierHasProducts(int supplierID) {
        for (Map<Integer, SupplierProduct> supplierMap : productsByProductID.values()) {
            if (supplierMap.containsKey(supplierID)) {
                return true;
            }
        }
        return false;
    }

    public int getProductIdByName(String productName) {
        return supplierProductDAO.getProductIdByName(productName);
    }

    @Override
    public List<supplierProductDTO> getProductsForTomorrowOrder() {
        try {
            if (orderDAO.hasFixedDeliveryTomorrow())
                return null;
        }
        catch (Exception e)
        {
            return Collections.emptyList();
        }

        List<Integer> agreementsID = agreementRepository.getAgreementsIdsForTomorrow();
        if (agreementsID.isEmpty()) return Collections.emptyList();
        List<supplierProductDTO> spdts= new ArrayList<>();
        for (Integer agreementID : agreementsID) {
            try {
                spdts.addAll(supplierProductDAO.getProductsByAgreementID(agreementID));
            }
            catch (Exception e) {
                return Collections.emptyList();
            }
        }
        return spdts;
    }

}
