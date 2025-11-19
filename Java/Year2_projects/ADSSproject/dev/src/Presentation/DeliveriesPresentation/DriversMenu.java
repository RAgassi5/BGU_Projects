package Presentation.DeliveriesPresentation;

import java.util.Scanner;

public class DriversMenu {
    private final DriversController driversController;

    public DriversMenu() {
        driversController = new DriversController();
    }

    public void run(Scanner scanner) {
        int choice;

        do {
            choice = displayDriversMenu(scanner);
            switch (choice) {
                case 1:
                    if (driversController.getNumberOfDrivers() != 0) {
                        System.out.println();
                        driversController.showAllDrivers();
                    } else {
                        System.out.println("\nThere are currently no drivers in the system!");
                    }
                    break;
                case 2:
                    System.out.println("Enter Driver Name:");
                    String driverNameToAdd = scanner.nextLine();
                    driversController.addNewDriverToSystem(driverNameToAdd);
                    break;
                case 3:
                    System.out.println("Enter Driver ID:");
                    int driverIdToDelete = scanner.nextInt();
                    driversController.deleteDriveFromSystem(driverIdToDelete);
                    break;

                case 4:
                    System.out.println("Enter Driver ID:");
                    int driverIdToAddTo = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter License Type:");
                    String licenseTypeToAdd = scanner.nextLine();
                    driversController.addLicenseToDriverInSystem(driverIdToAddTo, licenseTypeToAdd);
                    break;

                case 0:
                    break;
            }

        } while (choice != 0);
    }

    public static int displayDriversMenu(Scanner scanner) {
        int buffer;
        while (true) {
            try {
                System.out.println("\n === Drivers Menu === ");
                System.out.println("1. Print all Drivers");
                System.out.println("2. Add Driver");
                System.out.println("3. Delete Driver");
                System.out.println("4. Add license to driver");
                System.out.println("0. Return to Management Menu");
                System.out.print("Enter your choice: ");

                buffer = scanner.nextInt();
                scanner.nextLine();
                if (buffer >= 0 && buffer <= 4) {
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
