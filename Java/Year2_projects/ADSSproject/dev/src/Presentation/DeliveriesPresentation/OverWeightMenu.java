package Presentation.DeliveriesPresentation;

import Domain.DeliveriesDomain.Route;

import java.util.Scanner;

public class OverWeightMenu {
    private Route overweightRoute;

    public void run(Scanner scanner, RouteController routeController) {
        if(routeController.checkOwIsEmpty()){
            System.out.println("\nThere are no overweight routes at the moment. Returning to main menu...");
            return;
        }
        int choice;

        do {
            if(this.overweightRoute == null){
                try{
                    overweightRoute = routeController.getCurrentOverWeightRoute();
                }catch (Exception e){
                    System.out.println("\nNo more overweight routes to handle. Returning to main menu...");
                    return;
                }
            }

            choice = displayOverWeightMenu(scanner);
            switch (choice) {
                case 1:
                    this.overweightRoute.printRouteProducts();
                    break;
                case 2:
                    routeController.editProducts(scanner, overweightRoute);
                    this.overweightRoute = null;
                    break;
                case 3:
                    routeController.removeShipmentFromRoute(scanner, overweightRoute);
                    this.overweightRoute = null;
                    break;
                case 0:
                   if(this.overweightRoute != null){
                       routeController.pushOverWeightRoute(this.overweightRoute);
                   }
                    System.out.println("Returning to main menu...");
                    return;
            }

        } while (true);


    }


    public static int displayOverWeightMenu(Scanner scanner) {
        int buffer;
        while (true) {
            try {
                System.out.println("\n=== OverWeight Menu ===");
                System.out.println("Welcome to the OverWeight Menu!");
                System.out.println("1. Print current overweight route's products");
                System.out.println("2. Edit products in route");
                System.out.println("3. Remove a shipment from route");
                System.out.println("0. Return to main menu");
                System.out.print("Enter your choice: ");
                buffer = scanner.nextInt();
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
