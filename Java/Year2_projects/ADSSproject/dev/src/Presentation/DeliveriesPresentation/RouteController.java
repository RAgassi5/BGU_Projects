package Presentation.DeliveriesPresentation;

import Domain.DeliveriesDomain.Product;
import Domain.DeliveriesDomain.Route;
import Domain.DeliveriesDomain.Shipment;
import Domain.DeliveriesDomain.s_status;
import Service.DeliveriesService.RouteManager;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class RouteController {
    private final RouteManager routeManager;

    /**
     * Creates a new instance of the route controller.
     */
    public RouteController() {
        this.routeManager = RouteManager.getInstance();
    }

    /**
     * Starts the automatic routing of shipments.
     */
    public void startRouting() {
        this.routeManager.startAutoRunning();
    }

    /**
     * Stops the automatic routing of shipments.
     */
    public  void stopProgram() {
        this.routeManager.stopRouting();
    }

    /**
     * Sends out awaiting shipments based on their associated route status.
     * This method delegates the task of determining the next route with awaiting shipments
     * to the route manager. Depending on the status of the returned route,
     * it provides appropriate output describing the shipment status or action required.
     * <p>
     * Key actions performed:
     * - Checks if there are any awaiting shipments.
     * - Notifies the user if no shipments are waiting at the moment.
     * - Handles specific cases such as delayed shipments or overweight shipments.
     * - Logs the details of the shipment, such as date, destination district,
     * and departure time, if the shipment is successfully sent out.
     * <p>
     * The method ensures that the user is well-informed about the state and progress
     * of shipments.
     */
    public void sendOutShipments() {
        Route currentRoute = this.routeManager.sendOutAwaitingShipments();
        if (currentRoute == null) {
            System.out.println("\nNo awaiting shipments at the moment");
        } else if (currentRoute.getStatus() == s_status.DELAYED) {
            System.out.println("\nThe shipment is waiting for a driver");

        } else if (currentRoute.getStatus() == s_status.OVERWEIGHT) {
            System.out.println("\nThe shipment is overweight, please go edit the shipment");

        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            System.out.println("\nThe shipment of " + currentRoute.getRouteDate().format(dateFormatter) + " to district " + currentRoute.getCurrentDistrict() + " was sent out to shipment at " + currentRoute.getDepartureTimeForAllShipments().format(timeFormatter) + " !");
        }
    }


    public boolean checkOwIsEmpty() {
        return this.routeManager.checkOverWeightIsEmpty();
    }

    /**
     * Handles OverWeight by removing products from a specific route.
     *
     * @param scanner a scanner to receive the user's input.
     * @param overWeightRoute an instance of the current overWeight to handle.
     */
    public void editProducts(Scanner scanner, Route overWeightRoute) {
        int overweightAmount = overWeightRoute.getRouteWeight() - this.routeManager.getMaximumCapacity();

        System.out.println("\nCurrent route is overweight by " + overweightAmount + " kgs");
        System.out.println("Please edit a shipment to resolve the overweight issue");

        while (overWeightRoute.getRouteWeight() > this.routeManager.getMaximumCapacity()) {
            System.out.println("\nTotal route weight: " + overWeightRoute.getRouteWeight());
            System.out.println("The current shipment vehicle can carry: " + this.routeManager.getMaximumCapacity());
            System.out.println("The amount of weight to be reduced: " + (overWeightRoute.getRouteWeight() - this.routeManager.getMaximumCapacity()) + "\n");

            overWeightRoute.printAllShipmentIdsInRoute();

            int shipmentId;
            Shipment shipmentToEdit;
            do {
                System.out.print("Enter Shipment ID to edit: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input, please enter a valid integer.");
                    scanner.nextLine();
                }
                shipmentId = scanner.nextInt();
                scanner.nextLine();
                shipmentToEdit = overWeightRoute.getShipmentInRoute(shipmentId);
                if (shipmentToEdit == null) {
                    System.out.println("There is no shipment with the given ID. Please choose another.");
                } else if (shipmentToEdit.getProductsInShipment().isEmpty()) {
                    System.out.println("The chosen shipment has no products. Please choose another.");
                    shipmentToEdit = null;
                }
            } while (shipmentToEdit == null);

            shipmentToEdit.printShipmentProducts();
            System.out.print("Please enter the index of the product to edit: ");
            int productIndex;
            while (true) {
                if (scanner.hasNextInt()) {
                    productIndex = scanner.nextInt();
                    scanner.nextLine(); // Clean
                    if (productIndex >= 0 && productIndex < shipmentToEdit.getProductsInShipment().size()) {
                        break;
                    }
                }
                System.out.print("Invalid index. Please try again: ");
            }

            Product productToEdit = shipmentToEdit.getProductsInShipment().get(productIndex);
            System.out.println("Selected product: ");
            productToEdit.printProduct();

            int unitsToRemove;

            System.out.print("How many units of the product would you like to remove? Units to remove: ");
            while (true) {
                String userInput = scanner.nextLine();
                try{
                    unitsToRemove = Integer.parseInt(userInput);
                    if(unitsToRemove >= 0 && unitsToRemove <= productToEdit.getAmount()){
                        break;
                    }else{
                        System.out.print("The selected amount is invalid. Please renter the amount to remove: ");
                    }
                }catch (Exception e){
                    System.out.print("The selected amount is invalid. Please renter the amount to remove: ");
                }
            }

            if (unitsToRemove == productToEdit.getAmount()) {
                shipmentToEdit.removeProductFromShipment(productToEdit); // remove product
            }
            else {
                shipmentToEdit.updateProductsInShipment(productToEdit, unitsToRemove);
                System.out.println("Your changes have been applied!");
            }
        }

        System.out.println("\nOverweight resolved! Route is now under the allowed limit.");
        routeManager.rescheduleRoute(overWeightRoute); // adding the route back to the ready routes list
    }

    /**
     * a function that remove a shipment from a route to handle overWeight
     * prints to user if fail/success
     */
    public void removeShipmentFromRoute(Scanner scanner, Route routeToRemove) {
        try {
            routeToRemove.printAllShipmentIdsInRoute();
            System.out.println("\nEnter ID to remove: ");
            int shipmentIDToRemove = scanner.nextInt();
            routeToRemove.removeShipmentById(shipmentIDToRemove);
            if(routeToRemove.getRouteLength() == 0){
                System.out.println("The current route is now empty and will be removed from the system.");
                return;
            }
            if(routeToRemove.getRouteWeight() <= routeManager.getMaximumCapacity()){
                routeManager.rescheduleRoute(routeToRemove);
                System.out.println("The overweight shipment was successfully edited and will be rescheduled!");
            }else {
                System.out.println("Route is still overweight. Please edit the shipment to resolve the overweight issue.");
                routeManager.pushOverWeightRoute(routeToRemove);
            }



        }catch (Exception e){
            System.out.println(e.getMessage());
            scanner.nextLine();
            routeManager.pushOverWeightRoute(routeToRemove);
        }
    }

    public Route getCurrentOverWeightRoute() {
        return this.routeManager.getCurrentOverWeightRoute();
    }

    public void pushOverWeightRoute(Route routeToAdd) {
        this.routeManager.pushOverWeightRoute(routeToAdd);
    }
}
