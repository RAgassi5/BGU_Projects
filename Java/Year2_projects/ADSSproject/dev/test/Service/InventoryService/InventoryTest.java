package Service.InventoryService;

import DTO.InventoryDTO.InventoryStatus;
import Util.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryTest {

    private static Inventory inventory;

    @BeforeAll
    public static void setup() {
        cleanInitialize(); //clean DB for test (based on given ID's)
        inventory = new Inventory(null); //Creates an empty Inventory with null OrderService
    }

    @Test
    @Order(1)
    //Add a new product to Inventory, expect Success.
    public void testAddProductSuccessfully() {
        InventoryStatus status = inventory.addProduct(
                "Milk", "Tnuva", 5.5, 10, 1.0,
                "Dairy", "Food", "1L"
        );
        assertEquals(InventoryStatus.Success, status);
    }

    @Test
    @Order(2)
    //Add a product with conflicting category ranks, expect Failure.
    public void testAddProductWithConflictingCategoryRank() {
        InventoryStatus status = inventory.addProduct(
                "Butter", "Tnuva", 6.0, 10, 0.5,
                "Food", "Dairy", "1L" // Swapped ranks
        );
        assertEquals(InventoryStatus.Failure, status);
    }

    @Test
    @Order(3)
    //Add units to existing product, expect Success.
    public void testAddUnitsFromDeliveryToShelf() {
        InventoryStatus status = inventory.addUnitsFromDelivery(
                1, 25,
                8, 6, 2025,      // arrival
                20, 6, 2025,     // expiration
                "Shelf", 4.5
        );
        assertEquals(InventoryStatus.Success, status);
    }

    @Test
    @Order(4)
    //Set min amount and validate operation succeeds.
    public void testSetMinAmountAndValidate() {
        InventoryStatus status = inventory.setMinAmount(1, 5);
        assertEquals(InventoryStatus.Success, status);
    }

    @Test
    @Order(5)
    //Transfer unit from storage to shelf, expect AlreadyUpdated (already in shelf).
    public void testTransferStorageToShelf() {
        InventoryStatus status = inventory.storageToShelf(1);
        assertTrue(status == InventoryStatus.AlreadyUpdated);
    }

        @Test
    @Order(6)
    //Transfer unit from storage to shelf, expect NotExists (no unitID).
    public void testTransferStorageToShelfNotExist() {
        InventoryStatus status = inventory.storageToShelf(999);
        assertTrue(status == InventoryStatus.NotExist);
    }

    @Test
    @Order(7)
    //Mark a unit as defective, expect one of the valid statuses.
    public void testMarkUnitAsDefective() {
        InventoryStatus status = inventory.setDefective(1);
        assertTrue(
                status == InventoryStatus.Success ||
                status == InventoryStatus.Failure
        );
    }

    @Test
    @Order(8)
    //Get all size categories and ensure known one exists.
    public void testGetAllSizeCategories() {
        var sizes = inventory.getAllSizeCategories();
        assertNotNull(sizes);
        assertTrue(sizes.contains("1L"));
    }


    @Test
    @Order(9)
    // Apply category discount and expect Success.
    public void testSetCategoryDiscount() {
        LocalDate today = LocalDate.now();
        InventoryStatus status = inventory.setDiscountByCat(
                "Dairy", 15.0,
                today.getDayOfMonth(), today.getMonthValue(), today.getYear(),
                today.plusDays(3).getDayOfMonth(), today.plusDays(3).getMonthValue(), today.plusDays(3).getYear()
        );
        assertEquals(InventoryStatus.Success, status);
    }

    @Test
    @Order(10)
    // Try removing non-existing unit, expect NotExist.
    public void testRemoveNonExistentUnit() {
        InventoryStatus status = inventory.removeFromStore(9999);
        assertEquals(InventoryStatus.NotExist, status);
    }

    //Helper to clean DB before tests
    private static void cleanInitialize() {
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {

            // Disable foreign key checks to safely delete rows from all tables
            stmt.executeUpdate("PRAGMA foreign_keys = OFF;");

            // Delete data from all tables
            stmt.executeUpdate("DELETE FROM Shipments;");
            stmt.executeUpdate("DELETE FROM Locations;");
            stmt.executeUpdate("DELETE FROM Drivers;");
            stmt.executeUpdate("DELETE FROM Licenses;");
            stmt.executeUpdate("DELETE FROM Vehicles;");
            stmt.executeUpdate("DELETE FROM Products;");
            stmt.executeUpdate("DELETE FROM ShipmentsProducts;");
            stmt.executeUpdate("DELETE FROM order_items;");
            stmt.executeUpdate("DELETE FROM Units;");
            stmt.executeUpdate("DELETE FROM ProductCostPrices;");
            stmt.executeUpdate("DELETE FROM Categories;");
            stmt.executeUpdate("DELETE FROM SaleDiscounts;");
            stmt.executeUpdate("DELETE FROM ProductDiscounts;");
            stmt.executeUpdate("DELETE FROM CategoryDiscounts;");
            stmt.executeUpdate("DELETE FROM suppliers;");
            stmt.executeUpdate("DELETE FROM agreements;");
            stmt.executeUpdate("DELETE FROM discounts;");
            stmt.executeUpdate("DELETE FROM fixedDeliveryAgreement;");
            stmt.executeUpdate("DELETE FROM flexibleDeliveryAgreement;");
            stmt.executeUpdate("DELETE FROM pickupAgreement;");
            stmt.executeUpdate("DELETE FROM supplier_products;");
            stmt.executeUpdate("DELETE FROM orders;");
            stmt.executeUpdate("DELETE FROM supplier_manufacturers;");
            stmt.executeUpdate("DELETE FROM contacts;");
            stmt.executeUpdate("DELETE FROM supplier_product_types;");

            // Reset auto-increment counters (assuming you are using AUTOINCREMENT)
            stmt.executeUpdate("DELETE FROM sqlite_sequence;");

            // Re-enable foreign key checks
            stmt.executeUpdate("PRAGMA foreign_keys = ON;");

        } catch (SQLException e) {
            System.out.println("Error during clean initialization: " + e.getMessage());
        }
    }
}
