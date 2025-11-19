package Presentation;

import java.util.Scanner;


import Presentation.SuplierPresentation.SuppliersMain;
import Presentation.InventoryPresentation.InventoryMain;
import Presentation.DeliveriesPresentation.MainMenu;
import Service.DeliveriesService.RouteManager;


public class MergedMenu {
    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        SuppliersMain sp = new SuppliersMain();
        InventoryMain in = new InventoryMain();
        new InitializationMenu().run(scanner, in, sp);

        boolean exit = false;
        while (!exit) {
            displayMergedMenu();
            int choice = getMenuChoice();
            switch (choice) {
                case 1:
                    SuppliersMain.show(scanner);
                    break;
                case 2:
                    InventoryMain.show(scanner);
                    break;
                case 3:
                    MainMenu.show(scanner);
                    break;
                case 0:
                    System.out.println("Thank you for choosing SUPER-LEE!");
                    System.out.println("We hope to see you again soon :) ");
                    System.out.println("Exiting application...");

                    //shutdown the delivery module processes
                    RouteManager.getInstance().stopRouting();
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
        scanner.close();
    }

    private static void displayMergedMenu() {
        System.out.println("\n=== Group_AR's Super-LEE Menu ===");
        System.out.println("1. Supplier Menu");
        System.out.println("2. Inventory Menu");
        System.out.println("3. Deliveries Menu");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getMenuChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ignored) {
        }
        return choice;
    }
}
