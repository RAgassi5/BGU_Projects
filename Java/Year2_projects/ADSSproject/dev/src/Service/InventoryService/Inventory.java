package Service.InventoryService;

import DTO.SupplierDTO.productForOrderDTO;
import DTO.SupplierDTO.supplierProductDTO;
import Domain.InventoryDomain.*;
import DTO.InventoryDTO.*;
import Domain.InventoryDomain.ProductUnitRepository;
import Service.SupplierService.OrderService;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final ProductUnitRepository productUnitRepository;
    private final CategoryRepository categoryRepository;
    private final DiscountRepository discountRepository;
    private final DateOrganizer dateManager;
    static OrderService orderService;

    // constructs the service inventory and initializes repositories
    public Inventory(OrderService orderService) {
        try {
            this.productUnitRepository = new ProductUnitRepository();
            this.categoryRepository = new CategoryRepository();
            this.discountRepository = new DiscountRepository();
            this.dateManager = new DateOrganizer();
            this.orderService = orderService;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize Inventory repositories: " + e.getMessage());
        }
    }

    // adds a new product using domain structure
    public InventoryStatus addProduct(String productName, String producerName, double salePrice, int minAmount,
                                      double weightPerUnit, String primaryCategory, String secondaryCategory, String sizeCategory) {
        try {
            // validate category ranks
            if (categoryRepository.existsWithDifferentRank(primaryCategory, 1) ||
                categoryRepository.existsWithDifferentRank(secondaryCategory, 2) ||
                categoryRepository.existsWithDifferentRank(sizeCategory, 3)) {
                return InventoryStatus.Failure;
            }

            // add categories to repository if not already present
            categoryRepository.addCategory(new CategoryDTO(primaryCategory, 1));
            categoryRepository.addCategory(new CategoryDTO(secondaryCategory, 2));
            categoryRepository.addCategory(new CategoryDTO(sizeCategory, 3));

            // create and add new product
            UninitializedProduct product = new UninitializedProduct(productName, producerName, salePrice, minAmount,
                    0, 0, weightPerUnit, primaryCategory, secondaryCategory, sizeCategory);

            int newProductID = productUnitRepository.createProduct(product);
            System.out.println("Product has been added successfully, new ProductID: " + newProductID);
            return InventoryStatus.Success;
        } catch (SQLException e) {
            System.out.println("A database error occurred while adding a new product: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // displays a table of all products in the system
    public void getProductIDTable() {
        try {
            List<Product> products = productUnitRepository.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("No products found in the system.");
                return;
            }

            System.out.println("Product ID Table:");
            System.out.println("-----------------");

            int i = 1;
            for (Product product : products) {
                System.out.println(i + ": Product: " + product.getProductName() + " | ID: " + product.getProductID());
                i++;
            }
        } catch (SQLException e) {
            System.out.println("A database error occurred while retrieving product list: " + e.getMessage());
        }
    }

    // displays a table of unit IDs for a given product ID
    public InventoryStatus getUnitIDTable(int productID) {
        try {
            Product product = productUnitRepository.getProductByID(productID);
            if (product == null) {
                return InventoryStatus.Failure;
            }

            List<Integer> units = productUnitRepository.getUnitIDsByProduct(productID);

            if (units.isEmpty()) {
                System.out.println("No units found for Product ID: " + productID);
                return InventoryStatus.Success;
            }

            System.out.println("Unit ID Table:");
            System.out.println("--------------");

            int i = 1;
            for (int unitID : units) {
                System.out.println(i + ": Unit ID: " + unitID);
                i++;
            }

            return InventoryStatus.Success;
        } catch (SQLException e) {
            System.out.println("A database error occurred while retrieving unit list: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    public List<String> getAllSizeCategories() {
        try {
            List<Category> categories = categoryRepository.getAllByRank(3);
            List<String> sizeCategories = new ArrayList<>();
            for (Category category : categories) {
                sizeCategories.add(category.getCategoryName());
            }
            return sizeCategories;
        }
        catch (SQLException E) {
            System.out.println("A database error occurred while retrieving all category list: " + E.getMessage());
            return null;
        }
    }

    // displays all categories by given rank (1=primary, 2=secondary, 3=size)
    public void getCategoryTable(int rank) {
        try {
            List<Category> categories = categoryRepository.getAllByRank(rank);

            if (categories.isEmpty()) {
                System.out.println("No categories found for selected rank.");
                return;
            }

            int i = 1;
            for (Category category : categories) {
                System.out.println(i + ": Category name: " + category.getCategoryName());
                i++;
            }
        } catch (SQLException e) {
            System.out.println("A database error occurred while retrieving category list: " + e.getMessage());
        }
    }

    // updates the system date
    public InventoryStatus setDate(int dd, int mm, int yy) {
        InventoryStatus status = this.dateManager.setDate(dd, mm, yy);
        if (status == InventoryStatus.Success) {
            System.out.println("System date updated to: " + dd + "/" + mm + "/" + yy);
        }
        return status;
    }

    // prints the current system date
    public void printCurrentDate() {
        LocalDate current = this.dateManager.getDate();
        System.out.println("Current system date: " + current.getDayOfMonth() + "/" + current.getMonthValue() + "/" + current.getYear());
    }

    // checks if a product exists in the repository
    public InventoryStatus isExist(int productID) {
        try {
            Product product = productUnitRepository.getProductByID(productID);
            return (product == null) ? InventoryStatus.Failure : InventoryStatus.Success;
        } catch (SQLException e) {
            System.out.println("A database error occurred while checking product existence: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // returns storage amount of a product
    public void getStorageAmount(int productID) {
        try {
            Product product = productUnitRepository.getProductByID(productID);
            if (product == null) {
                System.out.println("Product not found.");
                return;
            }
            System.out.println("Storage quantity: " + product.getStorageAmount());
        } catch (SQLException e) {
            System.out.println("Error fetching storage amount: " + e.getMessage());
        }
    }

    // returns shelf amount of a product
    public void getShelfAmount(int productID) {
        try {
            Product product = productUnitRepository.getProductByID(productID);
            if (product == null) {
                System.out.println("Product not found.");
                return;
            }
            System.out.println("Shelves quantity: " + product.getShelfAmount());
        } catch (SQLException e) {
            System.out.println("Error fetching shelf amount: " + e.getMessage());
        }
    }

    // returns total amount of a product
    public void getTotalAmount(int productID) {
        try {
            Product product = productUnitRepository.getProductByID(productID);
            if (product == null) {
                System.out.println("Product not found.");
            }
            System.out.println("Total quantity: " + product.getTotalAmount());
        } catch (SQLException e) {
            System.out.println("Error fetching total amount: " + e.getMessage());
        }
    }





    // returns minimum required amount of a product
    public void getMinAmount(int productID) {
        try {
            Product product = productUnitRepository.getProductByID(productID);
            if (product == null) {
                System.out.println("Product not found.");
            }
            System.out.println("Minimum quantity: " + product.getMinAmount());
        } catch (SQLException e) {
            System.out.println("Error fetching minimum amount: " + e.getMessage());
        }
    }

    // returns the current sale price with applicable discounts
    public InventoryStatus getSellPrice(int productID) {
        LocalDate currentDate = dateManager.getDate();
        return getSPriceByD(productID, currentDate.getDayOfMonth(), currentDate.getMonthValue(), currentDate.getYear());
    }

    // returns the sale price of a product on a specific date
    public InventoryStatus getSPriceByD(int productID, int dd, int mm, int yy) {
        try {
            Product product = productUnitRepository.getProductByID(productID);
            if (product == null) {
                System.out.println("Product not found.");
                return InventoryStatus.NotExist;
            }
            LocalDate date;
            try {
                date = LocalDate.of(yy, mm, dd);
            } catch (DateTimeException e) {
                return InventoryStatus.Failure;
            }

            double salePrice = product.getSalePrice();

            List<SaleDiscount> allDiscounts = new ArrayList<>();
            allDiscounts.addAll(discountRepository.getProductDiscounts(productID));
            allDiscounts.addAll(discountRepository.getCategoryDiscounts(product.getPrimaryCategory()));
            allDiscounts.addAll(discountRepository.getCategoryDiscounts(product.getSecondaryCategory()));
            allDiscounts.addAll(discountRepository.getCategoryDiscounts(product.getSizeCategory()));

            for (SaleDiscount discount : allDiscounts) {
                if (dateManager.inRange(discount.getStartDate(), discount.getEndDate(), date)) {
                    salePrice = salePrice - (salePrice * discount.getRate() / 100);
                }
            }
            String formatted = String.format("%.2f", salePrice);
            System.out.println("Sell price: " + formatted);
            return InventoryStatus.Success;
        }
        catch (SQLException e) {
            System.out.println("Error calculating sale price: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // returns the current cost price of a product based on the current system date
    public InventoryStatus getCostPrice(int productID) {
            LocalDate currentDate = dateManager.getDate();  // get current system date
            return getCPriceByD(productID, currentDate.getDayOfMonth(), currentDate.getMonthValue(), currentDate.getYear());
    }

    // returns the cost price of a product on a specific date
    public InventoryStatus getCPriceByD(int productID, int day, int month, int year) {
        try {
            LocalDate date = LocalDate.of(year, month, day);
            double costPrice = productUnitRepository.getCostPriceByDate(productID, date);

            if (costPrice == -1) {
                System.out.println("No cost price found for the given product and date.");
                return InventoryStatus.Failure;
            }

            String formatted = String.format("%.2f", costPrice);
            System.out.println("Cost price: " + formatted);
            return InventoryStatus.Success;
        } catch (SQLException e) {
            System.out.println("Error retrieving cost price by date: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
        catch (DateTimeException e) {
            System.out.println("Not a valid Date");
            return InventoryStatus.NotExist;
        }
    }

        // updates the minimum required amount for a specific product
    public InventoryStatus setMinAmount(int productID, int minAmount) {
        try {
            Product product = productUnitRepository.getProductByID(productID);  //get product (exists cause checked in main)
            product.setMinAmount(minAmount);  //update domain object
            return productUnitRepository.updateProduct(product.toDTO());  //update all through repo
        } catch (SQLException e) {
            System.out.println("Error updating minimum amount: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // updates the sale price of a specific product (same logic as minAmount)
    public InventoryStatus setSPrice(int productID, double salePrice) {
        try {
            Product product = productUnitRepository.getProductByID(productID);
            product.setSalePrice(salePrice);
            return productUnitRepository.updateProduct(product.toDTO());
        } catch (SQLException e) {
            System.out.println("Error updating sale price: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // adds new units for a product delivery, either to Shelf or Storage
    public InventoryStatus addUnitsFromDelivery(int productID, int quantity, int arrivalD, int arrivalM, int arrivalY,
                                                int expirationD, int expirationM, int expirationY,
                                                String location, double costPerUnit) {
        try {
            LocalDate arrival = LocalDate.of(arrivalY, arrivalM, arrivalD);
            LocalDate expiration = LocalDate.of(expirationY, expirationM, expirationD);

            int[] ids = productUnitRepository.addUnits(productID, quantity, expiration, arrival, location, costPerUnit);

            System.out.println(quantity + " units have been added to " + location.toLowerCase() + ".");
            System.out.println("New unit IDs:");
            for (int id : ids) {
                System.out.println(id);
            }
            return InventoryStatus.Success;
        }
        catch (SQLException e) {
            System.out.println("Error adding new units: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
        catch (DateTimeException e) {
            return InventoryStatus.Failure;
        }
    }

    // transfers a unit from Storage to Shelf, if eligible
    public InventoryStatus storageToShelf(int unitID) {
        try {
            return productUnitRepository.transferUnitToShelf(unitID);
        } catch (SQLException e) {
            System.out.println("Database error during unit transfer: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // removes a unit from the system by its ID
    public InventoryStatus removeFromStore(int unitID) {
        try {
            Unit toRemove = productUnitRepository.getUnitByID(unitID);
            InventoryStatus st = productUnitRepository.removeUnit(unitID);

            //if product has gone below minimal amount because of the removal
            if (st == InventoryStatus.BelowMin) {
                int productID = toRemove.getProductID();
                Product product = productUnitRepository.getProductByID(productID);
                int toOrder = product.getMinAmount()*5;
                boolean succeed =orderService.generateOrderDueToShortage(product.getProductID(),toOrder);
                if (succeed) {
                    System.out.println("Created order due shortage in product");
                }
                else {
                    System.out.println("Failed to create order due shortage in product");
                }
                st = InventoryStatus.Success;
            }
            return st;
        }
        catch (SQLException e) {
            System.out.println("Database error during unit removal: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // marks a unit as defective and updates it in the database
    public InventoryStatus setDefective(int unitID) {
        try {
            return productUnitRepository.markUnitAsDefective(unitID);
        } catch (SQLException e) {
            System.out.println("Database error during unit defect marking: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // applies a discount to a specific product within a date range
    public InventoryStatus setDiscountByPID(int productID, double discountPercent, int startD, int startM, int startY,
                                            int endD, int endM, int endY) {
        try {
            if (productUnitRepository.getProductByID(productID) == null) {
                return InventoryStatus.NotExist;
            }

            LocalDate startDate = LocalDate.of(startY, startM, startD);
            LocalDate endDate = LocalDate.of(endY, endM, endD);

            ProductDiscountDTO discount = new ProductDiscountDTO(-1, discountPercent, startDate, endDate, productID);
            discountRepository.addProductDiscount(discount);
            return InventoryStatus.Success;
        }
        catch (DateTimeException e) {
            return InventoryStatus.Failure;
        }
        catch (SQLException e) {
            System.out.println("Database error while applying product discount: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // applies a discount to a specific category within a date range
    public InventoryStatus setDiscountByCat(String categoryName, double discountPercent, int startD, int startM, int startY,
                                            int endD, int endM, int endY) {
        try {
            if (categoryRepository.getCategoryByName(categoryName) == null) {
                return InventoryStatus.NotExist;
            }

            LocalDate startDate = LocalDate.of(startY, startM, startD);
            LocalDate endDate = LocalDate.of(endY, endM, endD);

            CategoryDiscountDTO discount = new CategoryDiscountDTO(-1, discountPercent, startDate, endDate, categoryName);
            discountRepository.addCategoryDiscount(discount);
            return InventoryStatus.Success;
        }
        catch (DateTimeException e) {
            return InventoryStatus.Failure;
        }
        catch (SQLException e) {
            System.out.println("Database error while applying category discount: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // prints all products in a given category with their distance from minimum required amount
    public InventoryStatus getStockByCategory(String categoryName) {
        try {
            if (categoryRepository.getCategoryByName(categoryName) == null) {
                return InventoryStatus.Failure;
            }

            List<Product> products = productUnitRepository.getProductsByCategory(categoryName);

            int i = 1;
            for (Product product : products) {
                int totalAmount = product.getTotalAmount();
                int minAmount = product.getMinAmount();
                int distance = totalAmount - minAmount;

                String status = (distance < 0) ? "below" : "above";
                if (distance < 0) {
                    distance = -distance;
                }

                System.out.println(i + ": Product " + product.getProductName() + " ID: " +
                                   product.getProductID() + " is " +
                                   distance + " units " + status + " the minimum required.");
                i++;
            }
            return InventoryStatus.Success;
        }
        catch (SQLException e) {
            System.out.println("Database error while generating stock by category report: " + e.getMessage());
            return InventoryStatus.DBFailure;
        }
    }

    // prints all expired units as of current system date
    public void getExpired() {
        try {
            // get current system date
            LocalDate currentDate = dateManager.getDate();

            // get all expired unit IDs from repository
            List<Integer> expired = productUnitRepository.getAllExpiredUnits(currentDate);

            // handle case where no expired units found
            if (expired.isEmpty()) {
                System.out.println("No expired units in store");
                return;
            }

            // print expired unit IDs, numbered
            int i = 1;
            for (int unitID : expired) {
                System.out.println(i + ": Unit: " + unitID);
                i++;
            }

        } catch (SQLException e) {
            System.out.println("Database error while generating expired units report: " + e.getMessage());
        }
    }

    // prints all products that are below their minimum required amount
    public void getShortages() {
        try {
            // get all products currently below minimum quantity
            List<Product> shortages = productUnitRepository.getShortageProducts();

            // handle case where no shortages found
            if (shortages.isEmpty()) {
                System.out.println("No shortages in store");
                return;
            }

            // print shortage information per product
            int i = 1;
            for (Product product : shortages) {
                int total = product.getTotalAmount();
                int min = product.getMinAmount();
                int missing = min - total;

                System.out.println(i + ": Product " + product.getProductName() +
                                   " ID " + product.getProductID() +
                                   " is missing " + missing + " units.");
                i++;
            }

        } catch (SQLException e) {
            System.out.println("Database error while generating shortage report: " + e.getMessage());
        }
    }

    // prints all defective units in the system
    public void getDefectives() {
        try {
            // retrieve all defective unit IDs from repository
            List<Integer> defectives = productUnitRepository.getAllDefectiveUnits();

            // handle case where no defectives found
            if (defectives.isEmpty()) {
                System.out.println("No defective units in store");
                return;
            }

            // print defective unit IDs, numbered
            int i = 1;
            for (int unitID : defectives) {
                System.out.println(i + ": Unit: " + unitID);
                i++;
            }

        } catch (SQLException e) {
            System.out.println("Database error while generating defective units report: " + e.getMessage());
        }
    }

    public List<supplierProductDTO> displayProducts(){
        return orderService.getProductsForTomorrow();
    }
    public int belowMin(int productID, int quantity){
        try {
             Product product = productUnitRepository.getProductByID(productID);
            return product.getMinAmount() - product.getTotalAmount() - quantity ;
        }
        catch (Exception e) {
            return -1;
        }
    }


    public InventoryStatus choosePFOrders(List<productForOrderDTO> productList) {
         boolean succeed = orderService.createOrderOfPFT(productList);
         if (succeed)
             return InventoryStatus.Success;
         return InventoryStatus.Failure;
    }
}
