package Presentation.DeliveriesPresentation;

import java.util.Scanner;

public class ManagementMenu {


    public ManagementMenu() {

    }

    public void run(Scanner scanner) {
        int choice;
        do {


            choice = display(scanner);
            switch (choice) {
                case 1:
                    new DriversMenu().run(scanner);
                    break;
                case 2:
                    new VehiclesMenu().run(scanner);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid input, please select from the given options");
                    break;
            }

        } while (choice != 0);
    }

    public static int display(Scanner scanner) {
        int buffer;
        while (true) {
            try {
                System.out.println("\n === Management Menu ===");
                System.out.println("What would you like to take care of?");
                System.out.println("1. Drivers");
                System.out.println("2. Vehicles");
                System.out.println("0. Return to main menu");
                System.out.print("Enter your choice: ");


                buffer = scanner.nextInt();
                if (buffer >= 0 && buffer <= 2) {
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
}
