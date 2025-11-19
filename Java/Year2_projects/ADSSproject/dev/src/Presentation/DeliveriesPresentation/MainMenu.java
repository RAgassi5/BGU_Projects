package Presentation.DeliveriesPresentation;

import java.util.Scanner;

public class MainMenu {
    private static final String Manager_Password = "admin123";
    private static boolean isManager = false;

    public static void show(Scanner systemScanner) {
        RouteController controller = new RouteController();
        BookingController bookingController = new BookingController();
        controller.startRouting();
        int choice;
        do {
            choice = displayMenu(systemScanner);
            switch (choice) {
                case 1:
                    new BookingMenu().run(systemScanner);
                    break;

                case 2:
                    controller.sendOutShipments();
                    break;

                case 3:
                    System.out.println("\n=== Track Your Shipment ===");
                    System.out.println("Enter your shipment ID:");
                    try {
                        // Read shipment id using nextLine() to avoid buffer issues
                        int ID = Integer.parseInt(systemScanner.nextLine().trim());
                        bookingController.trackShipment(ID);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid shipment ID provided.");
                    }
                    break;

                case 4:
                    if (controller.checkOwIsEmpty()) {
                        System.out.println("\nThere are no overweight routes at the moment. Returning to main menu...");
                    } else {
                        new OverWeightMenu().run(systemScanner, controller);
                    }
                    break;

                case 5:
                    if (isManager) {
                        new ManagementMenu().run(systemScanner);
                    } else {
                        System.out.println("You are not authorized to access this menu. Please contact the system administrator.");
                    }
                    break;

                case 123:
                    if (!isManager) {
                        isManager = verifyManagerPassword(systemScanner);
                        if (!isManager) {
                            System.out.println("Access Denied: Incorrect password");
                        }
                    }
                    break;
                    
                case 0:
                    break;

                default:
                    System.out.println("Invalid input, please select from the given options");
                    break;
            }

        } while (choice != 0);

    }


    private static boolean verifyManagerPassword(Scanner scanner) {
        System.out.println("\n=== Manager Authentication ===");
        System.out.print("Enter manager password: ");
        String password = scanner.nextLine();
        return Manager_Password.equals(password);
    }


    /**
     * a function that displays the main menu
     *
     * @param scanner a scanner to receive the user's input
     * @return the user's choice
     */
    public static int displayMenu(Scanner scanner) {
        int buffer;

        while (true) {
            try {
                System.out.println("\n=== Deliveries Menu ===");
                System.out.println("1. Book a new shipment");
                System.out.println("2. Send out awaiting shipment");
                System.out.println("3. Track a shipment");
                System.out.println("4. Edit an overweight shipment");
                if (isManager) {
                    System.out.println("5. Management menu");
                }
                System.out.println("0. Exit Deliveries Menu");
                System.out.print("Enter your choice: ");
                buffer = scanner.nextInt();
                scanner.nextLine();
                if ((isManager && buffer >= 0 && buffer <= 5) || (!isManager && buffer >= 0 && buffer <= 4) || buffer == 123) {
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
