package Domain.InventoryDomain;

import DTO.InventoryDTO.*;

import DataAccess.InventoryDAO.ProductCostPriceDao;
import DataAccess.InventoryDAO.ProductDao;
import DataAccess.InventoryDAO.UnitDao;
import Domain.InventoryDomain.Unit;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ProductUnitRepository {
    private final ProductDao productDao;
    private final UnitDao unitDao;
    private final ProductCostPriceDao costPriceDao;

    private final Map<Integer, Product> products;
    private final Map<Integer, Unit> units;
    private final Map<Integer, List<ProductCostPrice>> productCPMap;

    private int productIDCounter;
    private int unitIDCounter;
    boolean createdProduct;
    boolean createdUnit;

    //initializes repository, DAOs, ID counters, and collections
    public ProductUnitRepository() throws SQLException {
        this.productDao = new ProductDao();
        this.unitDao = new UnitDao();
        this.costPriceDao = new ProductCostPriceDao();

        this.products = new HashMap<>();
        this.units = new HashMap<>();
        this.productCPMap = new HashMap<>();

        this.createdProduct = false;
        this.createdUnit = false;

    }

    //creates a new product in DB and memory
    public int createProduct(UninitializedProduct up) throws SQLException {
        if (!this.createdProduct) {
            this.productIDCounter = productDao.getMaxProductID() + 1;
            this.createdProduct = true;
        }
        int ID = productIDCounter++;
        ProductDTO dto = new ProductDTO(ID, up);
        productDao.insertProduct(dto);
        Product product = new Product(dto);
        products.put(dto.getProductID(), product);
        return ID;
    }

    //adds new units to product and inserts them to DB and memory
    public int[] addUnits(int productID, int amount, LocalDate expiry, LocalDate arrive, String location, double cost) throws SQLException {
        int[] unitIDs = new int[amount];
        Product product = getProductByID(productID);

        if (!this.createdUnit) {
            this.unitIDCounter = unitDao.getMaxUnitID() + 1;
            this.createdUnit = true;
        }

        for (int i = 0; i < amount; i++) {
            int id = unitIDCounter++;
            UnitDTO dto = new UnitDTO(id, productID, location, expiry, false);
            unitDao.insertUnit(dto);
            Unit unit = new Unit(dto);
            units.put(id, unit);
            unitIDs[i] = id;

            //update the storage/shelf amount in Product
            if (location.equals("Shelf")) product.incShelf();
            else product.incStorage();
        }

        //insert new cost price to DB and map
        insertCostPrice(new ProductCostPriceDTO(productID, arrive, cost));

        //updateProduct
        updateProduct(product.toDTO());

        return unitIDs;
    }

    //returns product from memory or loads from DB (null if not exists)
    public Product getProductByID(int id) throws SQLException {
        if (!products.containsKey(id)) {
            ProductDTO dto = productDao.getProductByID(id);
            if (dto == null) return null;
            Product product = new Product(dto);
            products.put(id, product);
        }
        return products.get(id);
    }

    //returns unit from memory or loads from DB (null if not exists)
    public Unit getUnitByID(int id) throws SQLException {
        if (!units.containsKey(id)) {
            UnitDTO dto = unitDao.getUnitByID(id);
            if (dto == null) return null;
            Unit unit = new Unit(dto);
            units.put(id, unit);
        }
        return units.get(id);
    }

    //updates a product both in DB and memory
    public InventoryStatus updateProduct(ProductDTO dto) throws SQLException {
        Product existing = getProductByID(dto.getProductID());
        if (existing != null) {
            existing.updateFromDTO(dto);
            productDao.updateProduct(dto);
            return InventoryStatus.Success;
        }
        return InventoryStatus.NotExist;
    }

    //updates a unit both in DB and memory
    public void updateUnit(UnitDTO dto) throws SQLException {
        Unit existing = getUnitByID(dto.getUnitID());
        if (existing != null) {
            existing.updateFromDTO(dto);
            unitDao.updateUnit(dto);
        }
    }

    //deletes unit from DB and memory, updates product amount
    public InventoryStatus removeUnit(int unitID) throws SQLException {
        Unit unit = getUnitByID(unitID);
        if (unit == null)
            return InventoryStatus.NotExist;
        unitDao.removeUnit(unitID);
        units.remove(unitID);

        //updates product amount
        Product parent = getProductByID(unit.getProductID());
        if (parent != null) {
            if (unit.getLocation().equals("Shelf")) parent.decShelf();
            else parent.decStorage();
            updateProduct(parent.toDTO());
            if (parent.getMinAmount() > parent.getTotalAmount())
                return InventoryStatus.BelowMin;
        }
        return InventoryStatus.Success;
    }

    //moves unit to shelf, updates both DB and memory
    public InventoryStatus transferUnitToShelf(int unitID) throws SQLException {
        Unit unit = getUnitByID(unitID);
        if (unit == null)
            return InventoryStatus.NotExist;
        if (unit.getLocation().equals("Storage")) {
            unit.moveToShelf();
            Product product = getProductByID(unit.getProductID());
            if (product != null) {
                product.decStorage();
                product.incShelf();
                updateProduct(product.toDTO());
            }
            updateUnit(unit.toDTO());
            return InventoryStatus.Success;
        }
        return InventoryStatus.AlreadyUpdated;
    }

    //marks unit as defective and updates DB
    public InventoryStatus markUnitAsDefective(int unitID) throws SQLException {
        Unit unit = getUnitByID(unitID);
        if (unit == null)
            return InventoryStatus.NotExist;
        if (!unit.isDefective()) {
            unit.markAsDefective();
            updateUnit(unit.toDTO());
            return InventoryStatus.Success;
        }
        return InventoryStatus.AlreadyUpdated;
    }

    //gets all unit IDs for a specific product
    public List<Integer> getUnitIDsByProduct(int productID) throws SQLException {
        List<UnitDTO> dtos = unitDao.getUnitsByProductID(productID);
        List<Integer> ids = new ArrayList<>();
        for (UnitDTO dto : dtos)
            ids.add(dto.getUnitID());
        return ids;
    }

    //inserts new cost price into DB and memory map (validated in add units)
    public void insertCostPrice(ProductCostPriceDTO dto) throws SQLException {
        costPriceDao.insertCP(dto);
        productCPMap.putIfAbsent(dto.getProductID(), new ArrayList<>());
        productCPMap.get(dto.getProductID()).add(new ProductCostPrice(dto));
    }

    //returns relevant cost price by date
    public double getCostPriceByDate(int productID, LocalDate date) throws SQLException {
        //get all cps from DB
        List<ProductCostPrice> cps = new ArrayList<>();
        for (ProductCostPriceDTO dto : costPriceDao.getCPbyPID(productID))
            cps.add(new ProductCostPrice(dto));

        //sort it to be in chronological order and insert to map
        cps.sort(Comparator.comparing(ProductCostPrice::getStartDate));
        productCPMap.put(productID, cps);

        //find matching cp and return res
        ProductCostPrice result = null;
        for (ProductCostPrice cp : cps) {
            if (cp.getStartDate().isAfter(date))
                break;
            result = cp;
        }
        return (result == null) ? -1 : result.getCostPrice();
    }

    //returns all expired units (loads missing ones from DB)
    public List<Integer> getAllExpiredUnits(LocalDate currentDate) throws SQLException {

        //update map from DB
        List<UnitDTO> dtos = unitDao.getAllUnits();
        for (UnitDTO dto : dtos) {
            if (!units.containsKey(dto.getUnitID()))
                units.put(dto.getUnitID(), new Unit(dto));
        }

        //get all expired units and return
        List<Integer> expired = new ArrayList<>();
        for (Unit u : units.values()) {
            if (u.getExpiryDate().isBefore(currentDate))
                expired.add(u.getUnitID());
        }
        return expired;
    }

    //returns all defective unit IDs
    public List<Integer> getAllDefectiveUnits() throws SQLException {

        //update map from DB
        List<UnitDTO> dtos = unitDao.getAllUnits();
        for (UnitDTO dto : dtos) {
            if (!units.containsKey(dto.getUnitID()))
                units.put(dto.getUnitID(), new Unit(dto));
        }

        //get all defective units and return
        List<Integer> defective = new ArrayList<>();
        for (Unit u : units.values()) {
            if (u.isDefective()) defective.add(u.getUnitID());
        }
        return defective;
    }

    //returns all products below min amount
    public List<Product> getShortageProducts() throws SQLException {

        //update map from DB
        List<ProductDTO> dtos = productDao.getAllProducts();
        for (ProductDTO dto : dtos) {
            if (!products.containsKey(dto.getProductID()))
                products.put(dto.getProductID(), new Product(dto));
        }

        //get all shortage products and return ans
        List<Product> shortage = new ArrayList<>();
        for (Product p : products.values())
            if (p.getTotalAmount() < p.getMinAmount())
                shortage.add(p);
        return shortage;
    }


    //returns all products in system
    public List<Product> getAllProducts() throws SQLException {
        List<Product> res = new ArrayList<>();

        //collect results and update map from DB
        List<ProductDTO> dtos = productDao.getAllProducts();
        for (ProductDTO dto : dtos) {
            Product toAdd = new Product(dto);
            if (!products.containsKey(dto.getProductID()))
                products.put(dto.getProductID(), new Product(dto));
            res.add(toAdd);
        }
        return res;
    }




    //gets all products belonging to a category
    public List<Product> getProductsByCategory(String catName) throws SQLException {
        List<Product> result = new ArrayList<>();
        for (ProductDTO dto : productDao.getAllProducts()) {
            Product p = new Product(dto);
            if (p.belongsToCategory(catName)) //add to res if relevant
                result.add(p);
            if (!products.containsKey(dto.getProductID())) //if not in map update
                products.put(dto.getProductID(), p);
        }
        return result;
    }
}
