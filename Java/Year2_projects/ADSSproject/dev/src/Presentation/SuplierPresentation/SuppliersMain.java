package Presentation.SuplierPresentation;

import DataAccess.SupplierDAO.*;
import Domain.SupplierDomain.*;
import Service.SupplierService.AgreementService;
import Service.SupplierService.OrderService;
import Service.SupplierService.SupplierService;

import java.util.Scanner;

public class SuppliersMain {
    static SupplierUI supplierUI;
    static OrderUI orderUI;

    static AgreementService agreementService;
    static OrderService orderService;
    static SupplierService supplierService;

    static supplierRepository supplierRepo;
    static supplierProductRepository supplierProductRepo;
    static agreementRepository agreementRepo;
    static OrderRepository orderRepo;

    static  Scanner scanner;




    public SuppliersMain() {
        // DAO
        AgreementDAO agreementDAO = new jdbcAgreementDAO();
        supplierDAO supplierDAO = new jdbcSupplierDAO();
        supplierProductDAO supplierProductDAO = new jdbcSupplierProductDAO();
        orderDAO orderDAO = new jdbcOrderDAO();

        // Repository
        agreementRepo = new AgreementRepositoryIMP(agreementDAO);
        supplierProductRepo = new SupplierProductRepositoryIMP(supplierProductDAO, agreementRepo, null,orderDAO);
        supplierRepo = new SupplierRepositoryIMP(supplierDAO, supplierProductDAO, agreementRepo, supplierProductRepo);
        orderRepo = new OrderRepositoryIMP(orderDAO, agreementRepo);
        supplierProductRepo.setSupplierRepo(supplierRepo);

        // Services
        agreementService = new AgreementService(supplierRepo, supplierProductRepo, agreementRepo);
        orderService = new OrderService(supplierProductRepo, orderRepo,supplierRepo);
        supplierService = new SupplierService(supplierRepo, supplierProductRepo, agreementRepo);

        // UI
        supplierUI = new SupplierUI(supplierService, agreementService);
        orderUI = new OrderUI(orderService, supplierService);
    }

    public static void show(Scanner Mscanner) {
        scanner = Mscanner;
        boolean run = true;
        while (run) {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Manage Suppliers");
            System.out.println("2. Manage Orders");
            System.out.println("3. Exit");

            int choice = promptMenuChoice(3);
            switch (choice) {
                case 1 -> supplierUI.manageSupplier();
                case 2 -> orderUI.manageOrder();
                case 3 -> {
                    System.out.println("Goodbye!");
                    run = false;
                }
            }
        }
    }

    public static int promptMenuChoice(int maxOption) {
        int choice = -1;
        while (true) {
            System.out.print("Choose an option (1-" + maxOption + "): ");
            String input = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= maxOption) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please choose between 1 and " + maxOption);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    public static int promptForInt(String message) {
        while (true) {
            try {
                System.out.print(message + " ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public static OrderService getOrderService() {
        return orderService;
    }
    public void supplierBoot() {
        initializationSuppliers initializer = new initializationSuppliers(supplierService, agreementService, orderService);
        initializer.initializeSystemData();
    }
}
