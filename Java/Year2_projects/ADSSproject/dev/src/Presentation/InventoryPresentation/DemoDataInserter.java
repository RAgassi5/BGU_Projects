package Presentation.InventoryPresentation;

import Service.InventoryService.Inventory;


public class DemoDataInserter {
    public static void insertDemoData(Inventory inventory) {

        //Demo Products:
        inventory.addProduct("Bissli Grill", "Osem", 5.0, 20, 50, "Snacks", "Salty", "50 gr");
        inventory.addProduct("Natural Potato Chips", "Elite", 6.5, 30, 100,  "Snacks", "Potato Chips", "100 gr");
        inventory.addProduct("Para Chocolate", "Elite", 12.0, 25, 200, "Sweets", "Chocolate", "200 gr");
        inventory.addProduct("Tnuva Milk 3%", "Tnuva", 7.0, 15, 1000, "Dairy Products", "Milk", "1 l");
        inventory.addProduct("Natural Yogurt", "Tnuva", 4.5, 10, 150, "Dairy Products", "Yogurts", "150 gr");
        inventory.addProduct("Neviot Mineral Water", "Neviot", 3.5, 40, 500, "Drinks", "Water", "500 ml");
        inventory.addProduct("Goldstar Beer", "Tempo", 10.0, 20, 500, "Alcohol", "Beer", "500 ml");
        inventory.addProduct("Angel Sliced Bread", "Angel", 8.0, 18, 600, "Bakery", "Bread", "600 gr");
        inventory.addProduct("Extra Virgin Olive Oil", "Yad Mordechai", 30.0, 12, 750, "Basic Food", "Oils", "750 ml");

        //call insert Units after the insertion of the products
        insertUnits(inventory);

        //call insert Discount after the insertion of the products.
        insertDiscounts(inventory);
    }

    private static void insertUnits(Inventory inventory) {
        //example arrival and expiration dates
        int[] arrivalDate = {1, 5, 2025};
        int[] expiryDry = {1, 1, 2026};
        int[] expiryShort = {15, 5, 2025};
        int[] expiryBeer = {1, 8, 2025};

        //insert for each product
        addUnitsForProduct(inventory, 1, 10, arrivalDate, expiryDry, "Shelf", 3.0);
        addUnitsForProduct(inventory, 1, 15, arrivalDate, expiryDry, "Storage", 3.0);
        addUnitsForProduct(inventory, 2, 5, arrivalDate, expiryDry, "Shelf", 4.0);
        addUnitsForProduct(inventory, 2, 8, arrivalDate, expiryDry, "Storage", 4.0);
        addUnitsForProduct(inventory, 3, 20, arrivalDate, expiryDry, "Shelf", 7.0);
        addUnitsForProduct(inventory, 3, 5, arrivalDate, expiryDry, "Storage", 7.0);
        addUnitsForProduct(inventory, 4, 10, arrivalDate, expiryShort, "Shelf", 4.0);
        addUnitsForProduct(inventory, 4, 10, arrivalDate, expiryShort, "Storage", 4.0);
        addUnitsForProduct(inventory, 5, 3, arrivalDate, expiryShort, "Shelf", 2.5);
        addUnitsForProduct(inventory, 5, 5, arrivalDate, expiryShort, "Storage", 2.5);
        addUnitsForProduct(inventory, 6, 25, arrivalDate, expiryDry, "Shelf", 1.5);
        addUnitsForProduct(inventory, 6, 20, arrivalDate, expiryDry, "Storage", 1.5);
        addUnitsForProduct(inventory, 7, 7, arrivalDate, expiryBeer, "Shelf", 6.0);
        addUnitsForProduct(inventory, 7, 10, arrivalDate, expiryBeer, "Storage", 6.0);
        addUnitsForProduct(inventory, 8, 5, arrivalDate, expiryShort, "Shelf", 5.0);
        addUnitsForProduct(inventory, 8, 8, arrivalDate, expiryShort, "Storage", 5.0);
        addUnitsForProduct(inventory, 9, 4, arrivalDate, expiryDry, "Shelf", 18.0);
        addUnitsForProduct(inventory, 9, 6, arrivalDate, expiryDry, "Storage", 18.0);
    }

    private static void addUnitsForProduct(Inventory inventory, int productId, int amount, int[] arrivalDate, int[] expiryDate, String location, double cost) {
        inventory.addUnitsFromDelivery(productId, amount, arrivalDate[0], arrivalDate[1], arrivalDate[2], expiryDate[0], expiryDate[1], expiryDate[2], location, cost);
    }

    private static void insertDiscounts(Inventory inventory) {
        //example discounts
        inventory.setDiscountByCat("Snacks", 15.0, 1, 7, 2025, 31, 7, 2025);
        inventory.setDiscountByPID(1, 10.0, 1, 6, 2025, 10, 6, 2025);
        inventory.setDiscountByCat("Dairy Products", 20.0, 1, 8, 2025, 31, 8, 2025);
        inventory.setDiscountByPID(8, 5.0, 1, 5, 2025, 15, 5, 2025);
    }
}
