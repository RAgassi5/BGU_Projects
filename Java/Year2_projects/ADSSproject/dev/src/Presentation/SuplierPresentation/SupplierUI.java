package Presentation.SuplierPresentation;

import Presentation.InventoryPresentation.InventoryMain;
import Service.SupplierService.AgreementService;
import Service.SupplierService.SupplierService;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SupplierUI {
    static Scanner scanner = new Scanner(System.in);
    private SupplierService supplierService;
    private AgreementService agreementService;

    public SupplierUI(SupplierService supplierService, AgreementService agreementService) {
        this.agreementService = agreementService;
        this.supplierService = supplierService;
    }

    public void manageSupplier() {
        boolean run = true;
        while (run) {
            System.out.println("\n========= Supplier Management =========");
            System.out.println("1. Add a new supplier");
            System.out.println("2. Edit existing supplier");
            System.out.println("3. View supplier details");
            System.out.println("4. Search & display suppliers");
            System.out.println("5. Back to main menu");
            System.out.println("=======================================");

            int choice = SuppliersMain.promptMenuChoice(5);
            switch (choice) {
                case 1 -> addNewSupplier();
                case 2 -> editSupplierDetails();
                case 3 -> displaySupplierDetails();
                case 4 -> showSearchAndDisplayMenu();
                case 5 -> run = false;
            }
        }
    }

    private void showSearchAndDisplayMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- Display & Search Menu -----");
            System.out.println("1. Check or change supplier status");
            System.out.println("2. Display suppliers by product");
            System.out.println("3. Display supplier product types");
            System.out.println("4. Display supplier manufacturers");
            System.out.println("5. Back");

            int choice = SuppliersMain.promptMenuChoice(6);
            switch (choice) {
                case 1 -> checkOrChangeSupplierStatus();
                case 2 -> displaySuppliersByProduct();
                case 3 -> displaySupplierTypes();
                case 4 -> displaySupplierManufacturers();
                case 5 -> showActiveSuppliers();
                case 6 -> back = true;
            }
        }
    }


    private void addNewSupplier() {
        System.out.println("Enter supplier name:");
        String supplierName = scanner.nextLine();
        System.out.println("Enter bank account number:");
        String bankAccountNumber = scanner.nextLine();
        int supplierId = supplierService.createSupplier(supplierName, bankAccountNumber);
        addContactToSupplier(supplierId);
        System.out.println("Supplier " + supplierId + " added successfully.");
    }


    private void checkOrChangeSupplierStatus() {
        int supplierID = SuppliersMain.promptForInt("Enter supplier ID:");
        if (!supplierService.doesSupplierExist(supplierID)) {
            System.out.println("Supplier does not exist.");
            return;
        }
        System.out.println("Supplier " + supplierID +" status: " + supplierService.showSupplierStatus(supplierID));
        System.out.print("Would you like to change the supplier status? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            if (supplierService.changeStatusService(supplierID)) {
                System.out.println("Supplier deactivated.");
            } else {
                System.out.println("Supplier activated.");
            }
        }
    }

    private void displaySupplierDetails() {
        int supplierID = SuppliersMain.promptForInt("Enter supplier ID:");
        if (!supplierService.doesSupplierExist(supplierID)) {
            System.out.println("Supplier does not exist.");
            return;
        }
        supplierService.displaySuppDetails(supplierID);
    }

    private void displaySuppliersByProduct() {
        int productID = SuppliersMain.promptForInt("Enter product ID:");
        supplierService.displayProductSuppliers(productID);
    }

    private void displaySupplierTypes() {
        int supplierID = SuppliersMain.promptForInt("Enter supplier ID:");
        if (!supplierService.doesSupplierExist(supplierID)) {
            System.out.println("Supplier does not exist.");
            return;
        }
        supplierService.displaySupplierTypes(supplierID);
    }

    private void displaySupplierManufacturers() {
        int supplierID = SuppliersMain.promptForInt("Enter supplier ID:");
        if (!supplierService.doesSupplierExist(supplierID)) {
            System.out.println("Supplier does not exist.");
            return;
        }
        supplierService.displaysupplierManufacturers(supplierID);
    }



    private void changeProductStatus(int supplierID) {
        if (!supplierService.doesSupplierExist(supplierID)) {
            System.out.println("Supplier does not exist.");
            return;
        }
        int productID = SuppliersMain.promptForInt("Enter product ID:");
        if (supplierService.checkIfProductExist(supplierID, productID) == null) {
            System.out.println("Product does not exist.");
            return;
        }
        System.out.print("Do you want to activate the product? (1 - Activate, 2 - Deactivate): ");
        String input = scanner.nextLine().trim();
        boolean status;
        while (true) {
            if (input.equals("1")) {
                status = true;
                break;
            } else if (input.equals("2")) {
                status = false;
                break;
            } else {
                System.out.print("Invalid input. Please enter 1 for Activate or 2 for Deactivate: ");
                input = scanner.nextLine().trim();
            }
        }
        supplierService.changeProductStatus(supplierID, productID, status);
    }


    private void showActiveSuppliers() {
        supplierService.displayActiveSuppliers();
    }

    private void editSupplierDetails() {
        int supplierID = SuppliersMain.promptForInt("Enter supplier ID:");
        if (!supplierService.doesSupplierExist(supplierID)) {
            System.out.println("Supplier does not exist.");
            return;
        }
        boolean run = true;
        while (run) {
            System.out.println("Edit supplier:");
            System.out.println("1. Add new contact");
            System.out.println("2. Edit agreement");
            System.out.println("3. Create new agreement");
            System.out.println("4. change bank account number");
            System.out.println("5. Change product status");
            System.out.println("6. Back to supplier menu");

            int choice = SuppliersMain.promptMenuChoice(6);
            switch (choice) {
                case 1 -> addContactToSupplier(supplierID);
                case 2 -> editAgreementFlow(supplierID);
                case 3 -> createNewAgreement(supplierID);
                case 4 -> changeSupplierBankACCOUNTNumber(supplierID);
                case 5 -> changeProductStatus(supplierID);
                case 6 -> run = false;
            }
        }
    }

    private void addContactToSupplier(int supplierID) {
        System.out.println("Enter contact name:");
        String name = scanner.nextLine();
        System.out.println("Enter contact phone:");
        String phone = scanner.nextLine();
        supplierService.addContactToSupplier(supplierID, name, phone);
        System.out.println("Contact added successfully.");
    }

    private void createNewAgreement(int supplierID) {
        System.out.println("Choose agreement type:");
        System.out.println("1. PickUp");
        System.out.println("2. FixedDelivery");
        System.out.println("3. FlexibleDelivery");

        int choice = SuppliersMain.promptMenuChoice(3);
        String paymentMethod = promptPaymentMethod();


        int agreementID = -1;
        switch (choice) {
            case 1 -> {
                System.out.println("Enter pickup address:");
                String address = scanner.nextLine();
                if (!agreementService.checkValidAgreement("PickUp", supplierID)) {
                    System.out.println("PickUp agreement already exists.");
                    return;
                }
                agreementID = supplierService.CreatePickUpAgreement(paymentMethod, supplierID, address);
            }
            case 2 -> agreementID = supplierService.CreateFixedAgreement(paymentMethod, supplierID, chooseSupplyDays());
            case 3 -> {
                if (!agreementService.checkValidAgreement("Flexible Delivery", supplierID)) {
                    System.out.println("Flexible Delivery agreement already exists.");
                    return;
                }
                System.out.println("Enter supply days:");
                int supplyDays = SuppliersMain.promptForInt("Days:");
                agreementID = supplierService.CreateFlexibleAgreement(paymentMethod, supplierID, supplyDays);
            }
        }
        if (agreementID != -1) {
            addNewProduct(supplierID, agreementID);
        }
    }

    private ArrayList<DayOfWeek> chooseSupplyDays() {
        while (true) {
            System.out.println("Choose supply days by number (separated by commas):");
            System.out.println("1 - Sunday");
            System.out.println("2 - Monday");
            System.out.println("3 - Tuesday");
            System.out.println("4 - Wednesday");
            System.out.println("5 - Thursday");
            System.out.println("6 - Friday");
            System.out.println("7 - Saturday");
            System.out.print("Enter days (e.g., 1,3,5): ");

            String input = scanner.nextLine();
            String[] parts = input.split(",");
            ArrayList<DayOfWeek> days = new ArrayList<>();

            boolean valid = true;
            for (String part : parts) {
                try {
                    int dayNum = Integer.parseInt(part.trim());
                    if (dayNum < 1 || dayNum > 7) {
                        System.out.println("Invalid day number: " + dayNum);
                        valid = false;
                        break;
                    }
                    DayOfWeek day = DayOfWeek.of(dayNum);
                    if (!days.contains(day)) {
                        days.add(day);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input: " + part.trim());
                    valid = false;
                    break;
                }
            }

            if (valid && !days.isEmpty()) {
                return days;
            } else {
                System.out.println("❌ Invalid input. Please try again.\n");
            }
        }
    }

    private void editAgreementFlow(int supplierID) {
        System.out.print("Enter Agreement ID to edit: ");
        int agreementId = Integer.parseInt(scanner.nextLine());

        if (!agreementService.existAgreement(supplierID, agreementId)) {
            System.out.println("Agreement ID " + agreementId + " does not belong to Supplier ID " + supplierID);
            return;
        }
        agreementService.displayAgreement(supplierID, agreementId);

        boolean done = false;
        while (!done) {
            System.out.println("\n--- Edit Agreement ---");
            System.out.println("1. Change agreement status (active/inactive)");
            System.out.println("2. Add product to agreement");
            System.out.println("3. Edit discount quantity condition");
            System.out.println("4. Edit discount percentage");
            System.out.println("5. Edit product price under agreement");
            System.out.println("6. Back to previous menu");

            int choice = SuppliersMain.promptMenuChoice(6);

            switch (choice) {
                case 1 -> {
                    System.out.print("Set agreement as active? (yes/no): ");
                    String statusInput = scanner.nextLine().trim().toLowerCase();
                    boolean newStatus = statusInput.equals("yes") || statusInput.equals("y");
                    agreementService.changeAgreementStatus(supplierID, agreementId, newStatus);
                }
                case 2 -> addNewProduct(supplierID, agreementId);
                case 3 -> {
                    int quantity = SuppliersMain.promptForInt("Enter new discount quantity condition:");
                    agreementService.updateDiscountQuantityCond(supplierID, agreementId, quantity);
                }
                case 4 -> {
                    System.out.print("Enter new discount percentage: ");
                    double percent = Double.parseDouble(scanner.nextLine());
                    agreementService.updateDiscountPercentage(supplierID, agreementId, percent);
                }
                case 5 -> {
                    int productId = SuppliersMain.promptForInt("Enter product ID:");
                    System.out.print("Enter new product price: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    agreementService.updateProductPrice(supplierID, agreementId, productId, price);
                }
                case 6 -> done = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void changeSupplierBankACCOUNTNumber(int supplierID) {
        System.out.print("Enter new bank account number: ");
        String bankAccount = scanner.nextLine().trim();

        while (bankAccount.isEmpty() || !bankAccount.matches("\\d+")) {
            System.out.println("Invalid input. Bank account must contain digits only and cannot be empty.");
            System.out.print("Please enter again: ");
            bankAccount = scanner.nextLine().trim();
        }
        supplierService.changeBankAccountNumber(supplierID, bankAccount);
        System.out.println("Bank account updated successfully.");
    }

    private void addNewProduct(int supplierID, int agreementID) {
        System.out.println("Adding new product:");
        int catalogID = SuppliersMain.promptForInt("Enter catalog ID:");
        System.out.println("Enter product name:");
        String name = scanner.nextLine();
        System.out.println("Enter product price:");
        double price = SuppliersMain.promptForInt("Price:");
        System.out.println("Enter manufacturer:");
        String manufacturer = scanner.nextLine();
        System.out.println("Enter product type:");
        String type = scanner.nextLine();
        String weight = promptForCategory();
        if(!agreementService.addNewProductToAgreement(catalogID, price, supplierID, name, agreementID, manufacturer, type,weight))
            System.out.println("Please ask inventory manager to add this product to the system");
        System.out.println("Product added.");
        System.out.print("Would you like to add a discount for this product? (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.equals("yes") || input.equals("y")) {
            System.out.print("Enter minimum quantity for discount: ");
            int quantityCond = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter discount percentage: ");
            double percentage = Double.parseDouble(scanner.nextLine());

            agreementService.createDiscountForProduct(agreementID, catalogID, supplierID, quantityCond, percentage);
            System.out.println("Discount added to product.");
        } else {
            System.out.println("No discount added.");
        }
    }

    private String promptPaymentMethod() {
        while (true) {
            System.out.println("Choose payment method:");
            System.out.println("1. Credit");
            System.out.println("2. Cash");
            System.out.println("3. Bank Transfer");
            System.out.print("Your choice: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return "Credit";
                case "2":
                    return "Cash";
                case "3":
                    return "Bank Transfer";
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    public String promptForCategory(){
        List<String> category = InventoryMain.displaySizeCategory();

        if (category == null || category.isEmpty()) {
            System.out.println("No size categories available.");
            return null;
        }

        System.out.println("Choose product size category:");
        for (int i = 0; i < category.size(); i++) {
            System.out.println((i + 1) + ". " + category.get(i));
        }

        int choice = -1;
        while (true) {
            System.out.print("Enter the number of your choice: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // clear buffer
                if (choice >= 1 && choice <= category.size()) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please select a number between 1 and " + category.size() + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear invalid input
            }
        }

        return category.get(choice - 1);

    }


}
