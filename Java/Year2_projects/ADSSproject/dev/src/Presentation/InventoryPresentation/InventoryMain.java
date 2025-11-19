package Presentation.InventoryPresentation;

import DTO.InventoryDTO.InventoryStatus;
import DTO.SupplierDTO.productForOrderDTO;
import DTO.SupplierDTO.supplierProductDTO;
import Presentation.DeliveriesPresentation.MainMenu;
import Presentation.SuplierPresentation.SuppliersMain;
import Service.InventoryService.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryMain {
    static Inventory inventory;

    public InventoryMain() {
        try {
            inventory = new Inventory(SuppliersMain.getOrderService());
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
            System.out.println("Exiting System...");
        }
    }

    public static void show(Scanner Mscanner) {
        //Start of inventory menu
        int choice = 999; //garbage value for start
        do {
            System.out.println("\n========== Inventory Management System ==========");
            System.out.println("Choose an action: ");
            System.out.println("1. Add a new product to the system");
            System.out.println("2. Display ProductID table");
            System.out.println("3. Display UnitID table by Product ID");
            System.out.println("4. Display Category table by rank");
            System.out.println("5. Manage system date");
            System.out.println("6. Manage Product by Product ID");
            System.out.println("7. Transfer units from storage to shelf by UnitID's");
            System.out.println("8. Remove unit from the system by UnitID");
            System.out.println("9. Set unit as defective by UnitID");
            System.out.println("10. Add a discount to product by Product ID");
            System.out.println("11. Add a discount to a category by CategoryName");
            System.out.println("12. Inventory Reports");
            System.out.println("13. Update a fixed date order for tomorrow");
            System.out.println("0. Exit");
            System.out.println("Enter your choice: ");


            //choice validation (must be number between 0-13)
            if (Mscanner.hasNextInt()) {
                choice = Mscanner.nextInt();
                Mscanner.nextLine(); // clean up newline
                if (choice < 0 || choice > 13) {
                    System.out.println("Invalid input. Please enter a number between 0 and 13.");
                    continue;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number between 0 and 13.");
                Mscanner.nextLine(); // clean invalid input
                continue;
            }

            switch (choice) {
                case 1: {
                    String productName = "";
                    while (productName.isEmpty()) {
                        System.out.println("Enter the product name (or 0 to return to main menu):");
                        productName = Mscanner.nextLine().trim();
                        if (productName.isEmpty()) {
                            System.out.println("Invalid input. Product name cannot be empty. Enter 0 to return to main menu.");
                        }
                    }
                    if (productName.equals("0")) break;

                    String producerName = "";
                    while (producerName.isEmpty()) {
                        System.out.println("Enter the manufacturer name (or 0 to return to main menu):");
                        producerName = Mscanner.nextLine().trim();
                        if (producerName.isEmpty()) {
                            System.out.println("Invalid input. Manufacturer name cannot be empty. Enter 0 to return to main menu.");
                        }
                    }
                    if (producerName.equals("0")) break;

                    double sPrice = -1;
                    while (sPrice < 0) {
                        System.out.println("Enter the product sale price (or 0 to return):");
                        if (Mscanner.hasNextDouble()) {
                            sPrice = Mscanner.nextDouble();
                            if (sPrice < 0) {
                                System.out.println("Invalid input. Price must be non-negative. Enter 0 to return to main menu.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a valid number. Enter 0 to return to main menu.");
                            Mscanner.next(); // Clear invalid input
                        }
                        if (sPrice == 0) break;
                    }
                    if (sPrice == 0) {
                        Mscanner.nextLine();
                        break;
                    }

                    int minAm = -1;
                    while (minAm < 0) {
                        System.out.println("Enter product minimum quantity (or 0 to return):");
                        if (Mscanner.hasNextInt()) {
                            minAm = Mscanner.nextInt();
                            if (minAm < 0) {
                                System.out.println("Invalid input. Minimum quantity must be non-negative. Enter 0 to return to main menu.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a valid integer.");
                            Mscanner.next(); // Clear invalid input
                        }
                        if (minAm == 0) break;
                    }
                    if (minAm == 0) {
                        Mscanner.nextLine();
                        break;
                    }

                    Mscanner.nextLine(); // Clear buffer

                    double unitWeight = -1;
                    while (unitWeight <= 0) {
                        System.out.println("Enter the product weight per unit (greater than 0) or 0 to return:");
                        if (Mscanner.hasNextDouble()) {
                            unitWeight = Mscanner.nextDouble();
                            if (unitWeight <= 0) {
                                System.out.println("Invalid input. Weight must be greater than 0.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a valid number.");
                            Mscanner.next(); // Clear invalid input
                        }
                        if (unitWeight == 0) {
                            Mscanner.nextLine();
                            break;
                        }
                    }
                    if (unitWeight == 0) {
                        break;
                    }
                    Mscanner.nextLine(); // Clear buffer

                    String primCategory = "";
                    while (primCategory.isEmpty()) {
                        System.out.println("Enter the primary product category (or 0 to return):");
                        primCategory = Mscanner.nextLine().trim();
                        if (primCategory.isEmpty()) {
                            System.out.println("Invalid input. Primary category cannot be empty. Enter 0 to return to main menu.");
                        }
                    }
                    if (primCategory.equals("0")) break;

                    String secCategory = "";
                    while (secCategory.isEmpty()) {
                        System.out.println("Enter the secondary product category (or 0 to return):");
                        secCategory = Mscanner.nextLine().trim();
                        if (secCategory.isEmpty()) {
                            System.out.println("Invalid input. Secondary category cannot be empty. Enter 0 to return to main menu.");
                        }
                    }
                    if (secCategory.equals("0")) break;

                    String sizeCategory = "";
                    while (sizeCategory.isEmpty()) {
                        System.out.println("Enter the size product category (or 0 to return):");
                        sizeCategory = Mscanner.nextLine().trim();
                        if (sizeCategory.isEmpty()) {
                            System.out.println("Invalid input. Size category cannot be empty. Enter 0 to return to main menu.");
                        }
                    }
                    if (sizeCategory.equals("0")) break;

                    if (inventory.addProduct(productName, producerName, sPrice, minAm, unitWeight, primCategory, secCategory, sizeCategory) == InventoryStatus.Failure) {
                        System.out.println("Product addition failed");
                    }
                    break;
                }

                case 2: {
                    inventory.getProductIDTable();
                    break;
                }

                case 3: {
                    int ProductID = -1;

                    while (true) {
                        System.out.println("Enter Product ID (or 0 to return to main menu):");
                        if (Mscanner.hasNextInt()) {
                            ProductID = Mscanner.nextInt();
                            Mscanner.nextLine();
                            if (ProductID == 0) {
                                break;
                            }
                            if (ProductID < 0) {
                                System.out.println("Invalid input. Product ID must be a positive number. Enter 0 to return to main menu.");
                                continue;
                            }
                            if (inventory.getUnitIDTable(ProductID) == InventoryStatus.Failure) {
                                System.out.println("Product with ID " + ProductID + " was not found. Enter another ID or 0 to return to main menu.");
                            } else {
                                // assume success already prints
                                break;
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a numeric Product ID. Enter 0 to return to main menu.");
                            Mscanner.next(); // Clear invalid input
                        }
                    }
                    break;
                }

                case 4: {
                    int option = -1;
                    while (true) {
                        //print category rank options
                        System.out.println("Select an option (1-3) or 0 to return to main menu:");
                        System.out.println("1. Print all primary categories");
                        System.out.println("2. Print all secondary categories");
                        System.out.println("3. Print all size categories");
                        System.out.println("0. Return to menu");

                        if (Mscanner.hasNextInt()) {
                            option = Mscanner.nextInt();
                            Mscanner.nextLine(); //clear newline from buffer

                            if (option == 0) break;

                            if (option < 1 || option > 3) {
                                System.out.println("Invalid option. Please enter a number between 1 and 3. Enter 0 to return to main menu.");
                                continue;
                            }

                            //call category display function by rank
                            inventory.getCategoryTable(option);
                            break;

                        } else {
                            System.out.println("Invalid input. Please enter a number. Enter 0 to return to main menu.");
                            Mscanner.next(); // Clear invalid input
                        }
                    }
                    break;
                }

                case 5: {
                    int subChoice = -1;
                    while (true) {
                        // manage system date menu
                        System.out.println("System Date Menu:");
                        System.out.println("1. View current system date");
                        System.out.println("2. Update system date");
                        System.out.println("0. Return to main menu");
                        System.out.print("Enter your choice: ");

                        if (Mscanner.hasNextInt()) {
                            subChoice = Mscanner.nextInt();
                            Mscanner.nextLine(); // clean buffer
                            if (subChoice == 0) break;
                            if (subChoice == 1) {
                                //show current system date
                                inventory.printCurrentDate();
                                break;
                            }
                            if (subChoice == 2) {
                                // read new date from user and update it
                                int[] date = readValidDate(Mscanner);
                                if (date == null) break;
                                if (inventory.setDate(date[0], date[1], date[2]) == InventoryStatus.Failure)
                                    System.out.println("Invalid date given, date did not update.");
                                ;
                                break;
                            }
                            // invalid input in sub-menu
                            System.out.println("Invalid option. Please enter 1, 2 or 0.");
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                            Mscanner.next(); // clear invalid input
                        }
                    }
                    break;
                }


                case 6: {
                    int ProductID = -1;

                    while (true) {
                        System.out.println("Enter Product ID (or 0 to return to main menu):");
                        if (Mscanner.hasNextInt()) {
                            ProductID = Mscanner.nextInt();
                            Mscanner.nextLine(); // clear buffer

                            if (ProductID < 0) {
                                System.out.println("Invalid input. Product ID must be a positive number. Enter 0 to return to main menu.");
                                continue;
                            }

                            if (ProductID == 0)
                                break;

                            if (inventory.isExist(ProductID) == InventoryStatus.Failure) {
                                System.out.println("Product with ID " + ProductID + " was not found. Enter another ID or 0 to return to main menu.");
                            } else {
                                break;
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a numeric Product ID. Enter 0 to return to main menu.");
                            Mscanner.next(); // clear invalid input
                        }
                    }

                    if (ProductID == 0) break;

                    int actionChoice = -1;
                    while (true) {
                        // product-level menu for selected ProductID
                        System.out.println("\n--- Product Menu ---");
                        System.out.println("1. View Product Information");
                        System.out.println("2. Update Product Information");
                        System.out.println("0. Return to previous menu");
                        System.out.print("Select an option: ");

                        if (Mscanner.hasNextInt()) {
                            actionChoice = Mscanner.nextInt();
                            Mscanner.nextLine(); // clear buffer

                            if (actionChoice == 0) break;

                            switch (actionChoice) {
                                case 1:
                                    // view product information menu
                                    int subChoice = -1;
                                    while (true) {
                                        // product info menu for given ProductID
                                        System.out.println("\n--- Product Information Menu ---");
                                        System.out.println("1. Get storage quantity");
                                        System.out.println("2. Get shelves quantity");
                                        System.out.println("3. Get total quantity");
                                        System.out.println("4. Get minimum required quantity");
                                        System.out.println("5. Get current sale price");
                                        System.out.println("6. Get sale price by date");
                                        System.out.println("7. Get current cost price");
                                        System.out.println("8. Get cost price by date");
                                        System.out.println("0. Return to previous menu");
                                        System.out.print("Select an option: ");

                                        if (Mscanner.hasNextInt()) {
                                            subChoice = Mscanner.nextInt();
                                            Mscanner.nextLine(); // clear buffer

                                            if (subChoice == 0) break;

                                            switch (subChoice) {
                                                case 1:
                                                    inventory.getStorageAmount(ProductID);
                                                    break;
                                                case 2:
                                                    inventory.getShelfAmount(ProductID);
                                                    break;
                                                case 3:
                                                    inventory.getTotalAmount(ProductID);
                                                    break;
                                                case 4:
                                                    inventory.getMinAmount(ProductID);
                                                    break;
                                                case 5:
                                                    inventory.getSellPrice(ProductID);
                                                    break;
                                                case 6: {
                                                    int[] date = readValidDate(Mscanner);
                                                    if (date != null)
                                                        if (inventory.getSPriceByD(ProductID, date[0], date[1], date[2]) == InventoryStatus.Failure)
                                                            System.out.println("Invalid date given, please try again");
                                                    ;
                                                    break;
                                                }
                                                case 7:
                                                    if (inventory.getCostPrice(ProductID) == InventoryStatus.Failure)
                                                        System.out.println("Product has not been ordered yet.");
                                                    break;
                                                case 8: {
                                                    int[] date = readValidDate(Mscanner);
                                                    if (date != null) {
                                                        if (inventory.getCPriceByD(ProductID, date[0], date[1], date[2]) == InventoryStatus.Failure)
                                                            System.out.println("No orders found for the given date");
                                                    }
                                                    break;
                                                }
                                                default:
                                                    System.out.println("Invalid option. Please select a number between 0 and 8.");
                                            }
                                        } else {
                                            System.out.println("Invalid input. Please enter a number.");
                                            Mscanner.next(); // clear invalid input
                                        }
                                    }
                                    break;

                                case 2:
                                    // update product information menu
                                    int updateChoice = -1;
                                    while (updateChoice != 0) {
                                        System.out.println("\n--- Update Product Information ---");
                                        System.out.println("1. Update minimum required quantity");
                                        System.out.println("2. Update sale price");
                                        System.out.println("3. Add inventory to shelf or storage");
                                        System.out.println("0. Return to previous menu");
                                        System.out.print("Select an option: ");

                                        if (Mscanner.hasNextInt()) {
                                            updateChoice = Mscanner.nextInt();
                                            Mscanner.nextLine(); // clear buffer

                                            if (updateChoice == 0)
                                                break;

                                            switch (updateChoice) {
                                                case 1: {
                                                    int minAM;
                                                    while (true) {
                                                        System.out.print("Enter a new minimum required quantity for this product ID (0 or more): ");
                                                        if (Mscanner.hasNextInt()) {
                                                            minAM = Mscanner.nextInt();
                                                            Mscanner.nextLine(); // clear buffer
                                                            if (minAM >= 0) {
                                                                if (inventory.setMinAmount(ProductID, minAM) != InventoryStatus.DBFailure)
                                                                    System.out.println("Minimum quantity updated successfully.");
                                                                break;
                                                            } else {
                                                                System.out.println("Quantity must be 0 or greater. Try again.");
                                                            }
                                                        } else {
                                                            System.out.println("Invalid input. Please enter a numeric value.");
                                                            Mscanner.nextLine(); // clear invalid input
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 2: {
                                                    double salePrice;
                                                    while (true) {
                                                        System.out.print("Enter a new sale price for this product ID (0 or more): ");
                                                        if (Mscanner.hasNextDouble()) {
                                                            salePrice = Mscanner.nextDouble();
                                                            Mscanner.nextLine(); // clear buffer
                                                            if (salePrice >= 0) {
                                                                if (inventory.setSPrice(ProductID, salePrice) != InventoryStatus.DBFailure)
                                                                    System.out.println("New sale price updated successfully.");
                                                                break;
                                                            } else {
                                                                System.out.println("Sale price must be 0 or greater. Try again.");
                                                            }
                                                        } else {
                                                            System.out.println("Invalid input. Please enter a numeric value.");
                                                            Mscanner.nextLine(); // clear invalid input
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 3: {
                                                    int quantityToAdd = -1;
                                                    while (true) {
                                                        System.out.print("Enter quantity to add (or 0 to return): ");
                                                        if (Mscanner.hasNextInt()) {
                                                            quantityToAdd = Mscanner.nextInt();
                                                            Mscanner.nextLine(); // clear buffer
                                                            if (quantityToAdd == 0) break;
                                                            if (quantityToAdd > 0) break;
                                                            System.out.println("Quantity must be positive. Try again.");
                                                        } else {
                                                            System.out.println("Invalid input. Please enter a number.");
                                                            Mscanner.nextLine(); // clear invalid input
                                                        }
                                                    }

                                                    if (quantityToAdd == 0) break;

                                                    System.out.println("Enter arrival date:");
                                                    int[] arrivalDate = readValidDate(Mscanner);
                                                    if (arrivalDate == null) break;

                                                    System.out.println("Enter expiration date:");
                                                    int[] expirationDate = readValidDate(Mscanner);
                                                    if (expirationDate == null) break;

                                                    double costPerUnit = -1;
                                                    while (true) {
                                                        System.out.print("Enter cost per unit: ");
                                                        if (Mscanner.hasNextDouble()) {
                                                            costPerUnit = Mscanner.nextDouble();
                                                            Mscanner.nextLine(); // clear buffer
                                                            if (costPerUnit >= 0) break;
                                                            System.out.println("Cost must be 0 or greater.");
                                                        } else {
                                                            System.out.println("Invalid input. Please enter a valid number.");
                                                            Mscanner.nextLine(); // clear invalid input
                                                        }
                                                    }

                                                    System.out.println("Where to store?");
                                                    System.out.println("1. Shelf");
                                                    System.out.println("2. Storage");
                                                    int locationChoice = Mscanner.nextInt();
                                                    Mscanner.nextLine(); // clear buffer

                                                    switch (locationChoice) {
                                                        case 1:
                                                            if (inventory.addUnitsFromDelivery(ProductID, quantityToAdd,
                                                                    arrivalDate[0], arrivalDate[1], arrivalDate[2],
                                                                    expirationDate[0], expirationDate[1], expirationDate[2],
                                                                    "Shelf", costPerUnit) == InventoryStatus.Failure)
                                                                System.out.println("Invalid date / dates given, product has not been added");
                                                            ;
                                                            break;
                                                        case 2:
                                                            if (inventory.addUnitsFromDelivery(ProductID, quantityToAdd,
                                                                    arrivalDate[0], arrivalDate[1], arrivalDate[2],
                                                                    expirationDate[0], expirationDate[1], expirationDate[2],
                                                                    "Storage", costPerUnit) == InventoryStatus.Failure)
                                                                System.out.println("Invalid date / dates given, product has not been added");
                                                            ;
                                                            break;
                                                        default:
                                                            System.out.println("Invalid choice. Returning to update menu.");
                                                            break;
                                                    }
                                                    break;
                                                }
                                                case 0: {
                                                    break;
                                                }
                                                default:
                                                    System.out.println("Invalid option. Please select 0 to 3.");
                                            }
                                        } else {
                                            System.out.println("Invalid input. Please enter a number.");
                                            Mscanner.nextLine(); // clear invalid input
                                        }
                                    }
                                    break;
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                            Mscanner.next(); // clear invalid input
                        }
                    }
                    break;
                }

                case 7: {
                    boolean transferMore = true;
                    while (transferMore) {
                        int unitID = -1;
                        while (true) {
                            System.out.print("Enter Unit ID to transfer (or 0 to return to main menu): ");
                            if (Mscanner.hasNextInt()) {
                                unitID = Mscanner.nextInt();
                                Mscanner.nextLine(); // clear buffer
                                if (unitID == 0) break;
                                if (unitID > 0) break;
                                System.out.println("Invalid ID. Please enter a positive number or 0 to return to main menu.");
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                Mscanner.nextLine(); // clear invalid input
                            }
                        }

                        if (unitID == 0) {
                            System.out.println("Transfer canceled.");
                            break;
                        }
                        InventoryStatus st = inventory.storageToShelf(unitID);
                        if (st == InventoryStatus.NotExist) {
                            System.out.println("Failed to transfer Unit, Unit ID not exists.");
                        } else if (st == InventoryStatus.AlreadyUpdated) {
                            System.out.println("Unit already in shelves.");
                        } else if (st == InventoryStatus.Success) {
                            System.out.println("Unit transferred successfully.");
                        }

                        System.out.print("Do you want to transfer another Unit? (y/n): ");
                        String answer = Mscanner.nextLine();
                        if (!answer.equalsIgnoreCase("y")) {
                            transferMore = false;
                        }
                    }
                    break;
                }

                case 8: {
                    int unitID = -1;
                    while (true) {
                        System.out.println("Enter Unit ID (or 0 to return to main menu):");
                        if (Mscanner.hasNextInt()) {
                            unitID = Mscanner.nextInt();
                            Mscanner.nextLine(); // clear buffer
                            if (unitID == 0) {
                                break;
                            } else {
                                InventoryStatus st = inventory.removeFromStore(unitID);
                                if (st == InventoryStatus.NotExist) {
                                    System.out.println("Failed to remove unit from store. Unit ID does not exist.");
                                } else if (st == InventoryStatus.Success) {
                                    System.out.println("Unit removed successfully.");
                                }
                                break;
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                            Mscanner.nextLine(); // clear invalid input
                        }
                    }
                    break;
                }

                case 9: {
                    int unitID = -1;
                    while (true) {
                        System.out.println("Enter Unit ID to mark as defective (or 0 to return to main menu):");
                        if (Mscanner.hasNextInt()) {
                            unitID = Mscanner.nextInt();
                            Mscanner.nextLine(); // clear buffer
                            if (unitID == 0) {
                                break;
                            } else {
                                InventoryStatus st = inventory.setDefective(unitID);
                                if (st == InventoryStatus.NotExist) {
                                    System.out.println("Failed to mark unit as defective, Unit ID doesn't exists.");
                                } else if (st == InventoryStatus.Success) {
                                    System.out.println("Unit marked as defective successfully.");
                                } else if (st == InventoryStatus.AlreadyUpdated) {
                                    System.out.println("Unit already marked as defective.");
                                }
                                break;
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                            Mscanner.nextLine(); // clear invalid input
                        }
                    }
                    break;
                }

                case 10: {
                    System.out.print("Enter the product ID for discount (or 0 to return to main menu): ");
                    if (Mscanner.hasNextInt()) {
                        int productID = Mscanner.nextInt();
                        Mscanner.nextLine(); // clear buffer
                        if (productID == 0) break;
                        double discountPercent;
                        while (true) {
                            System.out.print("Enter the discount percentage to apply for this product ID (0–100, not 0 / 100): ");
                            if (Mscanner.hasNextDouble()) {
                                discountPercent = Mscanner.nextDouble();
                                Mscanner.nextLine(); // clear buffer
                                if (discountPercent >= 0 && discountPercent < 100) {
                                    break;
                                } else {
                                    System.out.println("Invalid discount percentage. Must be between 0 and 100 (not 0 / 100).");
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a numeric discount percentage.");
                                Mscanner.nextLine(); // clear invalid input
                            }
                        }

                        System.out.println("Enter discount start date (or 0 to return to main menu):");
                        int[] startDate = readValidDate(Mscanner);
                        if (startDate == null || startDate[0] == 0) break;

                        System.out.println("Enter discount end date (or 0 to return to main menu):");
                        int[] endDate = readValidDate(Mscanner);
                        if (endDate == null || endDate[0] == 0) break;

                        InventoryStatus st = inventory.setDiscountByPID(productID, discountPercent, startDate[0],
                                startDate[1], startDate[2], endDate[0], endDate[1], endDate[2]);
                        if (st == InventoryStatus.NotExist) {
                            System.out.println("Failed to apply discount. Please check given product ID.");
                        } else if (st == InventoryStatus.Failure) {
                            System.out.println("Failed to apply discount. Please check given Dates");
                        } else if (st == InventoryStatus.Success) {
                            System.out.println("Discount applied successfully.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                        Mscanner.nextLine(); // clear invalid input
                    }
                    break;
                }


                case 11: {
                    while (true) {
                        System.out.print("Enter the category for discount (or 0 to return to main menu): ");
                        String category = Mscanner.nextLine();
                        if (category.equals("0")) break;

                        System.out.print("Enter the discount percentage to apply for this category (0–100, not 0 / 100): ");
                        if (Mscanner.hasNextDouble()) {
                            double discountPercentCategory = Mscanner.nextDouble();
                            Mscanner.nextLine(); // clear buffer

                            if (discountPercentCategory < 0 || discountPercentCategory >= 100) {
                                System.out.println("Invalid discount percentage. Must be between 0 and 100 (not 0 / 100).");
                                continue;
                            }

                            System.out.println("Enter discount start date (or 0 to return to main menu):");
                            int[] startDate = readValidDate(Mscanner);
                            if (startDate == null || startDate[0] == 0) break;

                            System.out.println("Enter discount end date (or 0 to return to main menu):");
                            int[] endDate = readValidDate(Mscanner);
                            if (endDate == null || endDate[0] == 0) break;

                            InventoryStatus st = inventory.setDiscountByCat(category, discountPercentCategory, startDate[0],
                                    startDate[1], startDate[2], endDate[0], endDate[1], endDate[2]);
                            if (st == InventoryStatus.NotExist) {
                                System.out.println("Failed to apply discount. Please check given product ID.");
                            } else if (st == InventoryStatus.Failure) {
                                System.out.println("Failed to apply discount. Please check given Dates");
                            } else if (st == InventoryStatus.Success) {
                                System.out.println("Discount applied successfully.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a numeric discount percentage.");
                            Mscanner.nextLine(); // clear invalid input
                        }
                        break;
                    }
                    break;
                }

                case 12: {
                    while (true) {
                        System.out.println("\nInventory Reports Menu:");
                        System.out.println("1. Generate an inventory report by Category name");
                        System.out.println("2. Generate an expired units report");
                        System.out.println("3. Generate a products shortage report");
                        System.out.println("4. Generate a defective units report");
                        System.out.println("0. Return to main menu");
                        System.out.print("Enter your choice: ");
                        if (Mscanner.hasNextInt()) {
                            int reportChoice = Mscanner.nextInt();
                            Mscanner.nextLine(); // clear buffer

                            if (reportChoice == 0)
                                break;

                            switch (reportChoice) {
                                case 1:
                                    System.out.print("Enter the category to view current inventory (or 0 to return to previous menu): ");
                                    String category = Mscanner.nextLine();
                                    if (category.equals("0")) break;
                                    if (inventory.getStockByCategory(category) == InventoryStatus.Failure) {
                                        System.out.println("Failed to get stock by category. Category does not exist.");
                                    }
                                    break;

                                case 2:
                                    inventory.getExpired();
                                    break;

                                case 3:
                                    inventory.getShortages();
                                    break;

                                case 4:
                                    inventory.getDefectives();
                                    break;

                                default:
                                    System.out.println("Invalid report option.");
                                    break;
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                            Mscanner.nextLine(); // clear invalid input
                        }
                    }
                    break;
                }
                case 13: {
                    List<productForOrderDTO> productList = new ArrayList<>();
                    List<supplierProductDTO> products = inventory.displayProducts();

                    if (products == null) {
                        System.out.println("Already made fixed order for tomorrow.");
                        break;
                    }

                    if (products.isEmpty()) {
                        System.out.println("No products available for ordering.");
                        break;
                    }

                    System.out.println("\n--- Create Fixed Date Order ---");

                    for (supplierProductDTO spdto : products) {
                        while (true) {
                            System.out.println("\nProduct name: " + spdto.productName() +
                                    " | Product ID: " + spdto.productID() + " | SupplierID: " + spdto.supplierID());
                            System.out.print("Enter quantity to order (or 0 to skip this product): ");

                            if (Mscanner.hasNextInt()) {
                                int quantity = Mscanner.nextInt();
                                Mscanner.nextLine(); // clear buffer

                                if (quantity < 0) {
                                    System.out.println("Invalid input. Quantity must be 0 or greater.");
                                } else if (quantity == 0) {
                                    // user skipped this product
                                    break;
                                } else {
                                    int diff = inventory.belowMin(spdto.productID(), quantity);
                                    if (diff <= 0) {
                                        productList.add(new productForOrderDTO(spdto, quantity, 0));
                                        System.out.println("Product has been added to order");
                                        break;
                                    } else {
                                        System.out.println("You must add " + diff + " more to quantity meet the minimum.");
                                    }
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number.");
                                Mscanner.nextLine(); // clear invalid input
                            }
                        }
                    }

                    if (productList.isEmpty()) {
                        System.out.println("No products selected for ordering.");
                    } else {
                        InventoryStatus status = inventory.choosePFOrders(productList);
                        if (status == InventoryStatus.Failure) {
                            System.out.println("Order failed.");
                        } else {
                            System.out.println("Order created successfully.");
                        }
                    }

                    break;
                        }
                    }
                }
                while (choice != 0);
    }


    //helper function to read a valid date (returns array of [day, month, year])
    private static int[] readValidDate(Scanner scanner) {
        int day, month, year;

        while (true) {
            System.out.print("Enter the requested day (1-31) or 0 to return: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }
            day = scanner.nextInt();
            if (day == 0) return null;
            if (day < 1 || day > 31) {
                System.out.println("Day must be between 1 and 31.");
                continue;
            }
            break;
        }
        while (true) {
            System.out.print("Enter the requested month (1-12) or 0 to return: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }
            month = scanner.nextInt();
            if (month == 0) return null;
            if (month < 1 || month > 12) {
                System.out.println("Month must be between 1 and 12.");
                continue;
            }
            break;
        }
        while (true) {
            System.out.print("Enter the requested year (yyyy) or 0 to return: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }
            year = scanner.nextInt();
            if (year == 0) return null;
            break;
        }
        return new int[]{day, month, year};
    }

    public static List<String>  displaySizeCategory(){
        return inventory.getAllSizeCategories();
    }

    public void inventoryBoot(){
        DemoDataInserter.insertDemoData(inventory);
    }
}