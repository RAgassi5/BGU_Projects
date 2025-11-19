package Presentation;

import Presentation.InventoryPresentation.InventoryMain;
import Presentation.SuplierPresentation.SuppliersMain;
import Service.DeliveriesService.BootStarter;
import Util.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class InitializationMenu {
    public void run(Scanner scanner, InventoryMain in, SuppliersMain sp) {
        int choice;
        do {
            choice = displayInitializationMenu(scanner);
            switch (choice) {
                case 1:
                    cleanInitialize();
                    System.out.println("An empty Database has been initialized.");
                    break;
                case 2:
                    cleanInitialize();
                    new BootStarter().start();
                    in.inventoryBoot();
                    sp.supplierBoot();
                    System.out.println("Preset data has been loaded into the database.");
                    break;

                case 3:
                    break;
            }

        } while (choice != 1 && choice != 2 && choice != 3);
    }


    public static int displayInitializationMenu(Scanner scanner) {
        int buffer;
        while (true) {
            try {
                System.out.println("Welcome!");
                System.out.println("How would you like to initialize the system?");
                System.out.println("1. Empty Initialization");
                System.out.println("2. Preset Initialization");
                System.out.println("3. Continue with current database");
                System.out.print("Enter your choice: ");
                buffer = scanner.nextInt();
                scanner.nextLine();
                if (buffer == 1 || buffer == 2 || buffer == 3) {
                    break;
                } else {
                    System.out.println("Invalid input, please select from the given options");
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please select from the given options");
                scanner.nextLine();
            }
        }
        return buffer;
    }


    private void cleanInitialize() {
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
