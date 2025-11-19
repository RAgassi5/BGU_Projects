package Domain.SupplierDomain;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import DTO.SupplierDTO.supplierDTO;
import DTO.SupplierDTO.supplierProductDTO;
import DataAccess.SupplierDAO.supplierDAO;
import DataAccess.SupplierDAO.supplierProductDAO;

public class SupplierRepositoryIMP implements supplierRepository {
    private final HashMap<Integer, Supplier> suppliers = new HashMap<>();

    private final agreementRepository agreementRepo;
    private final supplierDAO supplierDAO;
    private final supplierProductDAO supplierProductDAO;
    private final supplierProductRepository supplierProductRepo;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SupplierRepositoryIMP(supplierDAO supplierDAO, supplierProductDAO supplierProductDAO,
                                 agreementRepository agreementRepo, supplierProductRepository supplierProductRepo) {
        this.supplierDAO = supplierDAO;
        this.supplierProductDAO = supplierProductDAO;
        this.agreementRepo = agreementRepo;
        this.supplierProductRepo = supplierProductRepo;
    }

    @Override
    public int addSupplier(String supplierName, String bankAccountNumber) {
        try {
            supplierDTO dto = new supplierDTO(-1, supplierName, bankAccountNumber, true);
            int generatedId = supplierDAO.insertSupplier(dto);
            Supplier newSupplier = new Supplier(generatedId, supplierName, bankAccountNumber, true);
            suppliers.put(generatedId, newSupplier);
            return generatedId;
        } catch (Exception e) {
            System.err.println("❌ Failed to add supplier: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    @Override
    public boolean supplierExists(int supplierID) {
        if (suppliers.containsKey(supplierID)) return true;
        try {
            supplierDTO dto = supplierDAO.getSupplier(supplierID);
            if (dto == null) return false;

            Supplier supplier = new Supplier(dto.supplierID(), dto.supplierName(), dto.bankAccountNumber(), dto.isActive());
            suppliers.put(supplierID, supplier);

            try {
                agreementRepo.loadAgreementsFromDatabase(supplierID);
            } catch (Exception e) {
                System.err.println("❌ Failed to load agreements for supplier " + supplierID + ": " + e.getMessage());
            }

            try {
                List<supplierProductDTO> products = supplierProductDAO.getProductsForSupplier(supplierID);
                for (supplierProductDTO product : products) {
                    supplierProductRepo.loadProductToMemory(product);
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to load products for supplier " + supplierID + ": " + e.getMessage());
            }

            return true;
        } catch (Exception e) {
            System.err.println("❌ Failed to load supplier " + supplierID + ": " + e.getMessage());
            return false;
        }
    }



    public boolean changeSupplierStatus(int supplierID) {
        Supplier supplier = suppliers.get(supplierID);
            if (supplier.isActive()) {
                supplier.setUnActive();
                return true;
            } else {
                supplier.setActive();
                return false;
            }
    }

    @Override
    public void changeBankAccountNumber(String bankAccountNumber, int supplierID) {
        Supplier supplier = suppliers.get(supplierID);
        if (supplier != null) {
            supplier.setBankAccountNumber(bankAccountNumber);
        }
    }

    @Override
    public void addContact(int supplierID, String name, String phone) {
        try {
            supplierDAO.insertContact(supplierID, name, phone);
        } catch (Exception e) {
            System.err.println("Failed to add contact: " + e.getMessage());
        }
    }

    @Override
    public void displaySupplier(int supplierID) {
        System.out.println(suppliers.get(supplierID));
    }

    @Override
    public void displaySupplierTypes(int supplierID) {
        try {
            supplierDAO.getSupplierProductTypes(supplierID).forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Failed to load product types: " + e.getMessage());
        }
    }

    @Override
    public void displaySupplierManufacturers(int supplierID) {
        try {
            supplierDAO.getmanufacturersBySupplier(supplierID).forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Failed to load manufacturers: " + e.getMessage());
        }
    }

    @Override
    public void displayActives() {
        System.out.println("Active suppliers:");
        boolean found = false;
        try {
            List<supplierDTO> activeSuppliers = supplierDAO.getActiveSuppliers();
            for (supplierDTO dto : activeSuppliers) {
                System.out.println("- Supplier ID: " + dto.supplierID() + ", Name: " + dto.supplierName());
                if (!suppliers.containsKey(dto.supplierID())) {
                    Supplier supplier = new Supplier(dto.supplierID(), dto.supplierName(), dto.bankAccountNumber(), dto.isActive());
                    suppliers.put(supplier.getSupplierID(), supplier);
                }
                found = true;
            }
            if (!found) {
                System.out.println("No active suppliers found in database.");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to retrieve active suppliers: " + e.getMessage());
        }
    }




    @Override
    public void addProductType(int supplierID, String productType) {
        try {
            supplierDAO.insertProductType(supplierID, productType);
        } catch (Exception e) {
            System.err.println("Failed to add product type: " + e.getMessage());
        }
    }

    @Override
    public void addManufacturer(int supplierID, String manufacturer) {
        try {
            supplierDAO.insertManufacturer(supplierID, manufacturer);
        } catch (Exception e) {
            System.err.println("Failed to add manufacturer: " + e.getMessage());
        }
    }
    @Override
    public String getSupplierStatus(int supplierID){
        Supplier supplier = suppliers.get(supplierID);
        if(supplier.isActive()) return "active";
        else return "inactive";
    }


}
