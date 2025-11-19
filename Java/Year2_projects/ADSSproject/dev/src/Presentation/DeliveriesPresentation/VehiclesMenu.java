package Presentation.DeliveriesPresentation;

import java.util.Scanner;

public class VehiclesMenu {
    private final VehicleController vehicleController;

    public VehiclesMenu() {
        this.vehicleController = new VehicleController();
    }

    public void run(Scanner scanner) {
        int choice;

        do {
            choice = displayVehiclesMenu(scanner);
            switch (choice) {
                case 1:
                    if(this.vehicleController.getNumberOfVehiclesInSystem() == 0){
                        System.out.println("\nThere are currently no vehicles in the system!");
                    }else{
                        System.out.println();
                        this.vehicleController.printAllVehiclesInSystem();
                    }
                    break;
                case 2:
                    System.out.println("Enter Vehicle Type:");
                    String vehicleType = scanner.nextLine();
                    System.out.println("Enter Vehicle License Number:");
                    int vehicleNum = scanner.nextInt();
                    scanner.nextLine();
                    if(vehicleType.equalsIgnoreCase("Truck") || vehicleType.equalsIgnoreCase("Van")){
                    this.vehicleController.addVehicle(vehicleType, vehicleNum, 0, 0);
                    }
                    else{
                        System.out.println("Enter Vehicle Weight:");
                        int vehicleWeight = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter Max Vehicle Weight:");
                        int maxVehicleWeight = scanner.nextInt();
                        scanner.nextLine();
                        this.vehicleController.addVehicle(vehicleType, vehicleNum, vehicleWeight, maxVehicleWeight);
                    }
                    break;
                case 3:
                    System.out.println("Enter Vehicle License Number:");
                    int vehicleNumToRemove = scanner.nextInt();
                    scanner.nextLine();
                    this.vehicleController.deleteVehicle(vehicleNumToRemove);

                case 0:
                    break;
            }

        } while (choice != 0);
    }

    public static int displayVehiclesMenu(Scanner scanner) {
        int buffer;
        while (true) {
            try {
                System.out.println("\n === Vehicles Menu === ");
                System.out.println("1. Print All Vehicles In The System ");
                System.out.println("2. Add Vehicle To System ");
                System.out.println("3. Remove Vehicle From System ");
                System.out.println("0. Return to Management Menu");
                System.out.print("Enter your choice: ");

                buffer = scanner.nextInt();
                scanner.nextLine();
                if (buffer >= 0 && buffer <= 3) {
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
