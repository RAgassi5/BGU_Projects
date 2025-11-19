package Service.DeliveriesService;

import Domain.DeliveriesDomain.District;
import Domain.DeliveriesDomain.Location;
import Domain.DeliveriesDomain.Product;
import Domain.DeliveriesDomain.Shipment;

import java.time.LocalDate;

/**
 * The BootStarter class is responsible for initializing and setting up
 * the required data for the system, including drivers, vehicles, and shipments.
 * It utilizes the service-layer functionality provided by the DriversManager,
 * VehiclesManager, and BookingSystem components.
 *
 * This class populates the system with predefined data such as driver information,
 * vehicle details, and shipment records, along with their associated business logic.
 */
public class BootStarter {

    private final DriversManager driversManager;
    private final BookingSystem bookingSystem;
    private final VehiclesManager vehiclesManager;

    public BootStarter() {
        this.bookingSystem = BookingSystem.getInstance();
        this.vehiclesManager = VehiclesManager.getInstance();
        this.driversManager = DriversManager.getInstance();
    }

    public void start() {
        // --- Setup Drivers and Licenses ---
        // Note: We are assuming that the first driver added gets ID = 1, second ID = 2, etc.
        driversManager.addDriver("David");   // ID assumed = 1
        driversManager.setDriverLicense(1, "Truck");
        driversManager.setDriverLicense(1, "Van");

        driversManager.addDriver("Shlomi");   // ID assumed = 2
        driversManager.setDriverLicense(2, "Truck");

        driversManager.addDriver("Lina");     // ID assumed = 3
        driversManager.setDriverLicense(3, "Van");

        driversManager.addDriver("Omer");     // ID assumed = 4
        driversManager.setDriverLicense(4, "Truck");
        driversManager.setDriverLicense(4, "Van");

        driversManager.addDriver("Tal");      // ID assumed = 5
        driversManager.setDriverLicense(5, "Van");

        driversManager.addDriver("Noa");      // ID assumed = 6
        driversManager.setDriverLicense(6, "Van");

        driversManager.addDriver("Eli");      // ID assumed = 7
        driversManager.setDriverLicense(7, "Truck");

        driversManager.addDriver("Dana");     // ID assumed = 8
        driversManager.setDriverLicense(8, "Truck");
        driversManager.setDriverLicense(8, "Van");

        // --- Setup Vehicles using VehiclesManager ---
        vehiclesManager.addVehicle("Truck", 2023345, 0, 0);
        vehiclesManager.addVehicle("Van", 129990, 0, 0);
        vehiclesManager.addVehicle("Truck", 2025555, 0, 0);
        vehiclesManager.addVehicle("Van", 930055, 0, 0);
        vehiclesManager.addVehicle("Van", 165401, 0, 0);
        vehiclesManager.addVehicle("Truck", 2026666, 0, 0);
        vehiclesManager.addVehicle("Van", 136679, 0, 0);
        vehiclesManager.addVehicle("Van", 340903, 0, 0);

        // --- Setup Shipments using BookingSystem ---
        // Define shipment dates
        LocalDate date1 = LocalDate.of(2025, 6, 21);
        LocalDate date2 = LocalDate.of(2025, 6, 23);
        LocalDate date3 = LocalDate.of(2025, 7, 12);
        LocalDate date4 = LocalDate.of(2025, 6, 21);

        // Create origin and destination Locations
        Location origin1 = new Location("John", "0549982030", "Rager St.", 21, District.South);
        Location origin2 = new Location("Reggie", "05233345065", "Weingate St.", 120, District.North);
        Location origin3 = new Location("Tom", "0501112233", "Ben Gurion St.", 10, District.Central);
        Location origin4 = new Location("Nina", "0502223344", "Herzl St.", 50, District.South);

        Location dest1 = new Location("Luke", "05235567065", "Dizengof St.", 35, District.South);
        Location dest2 = new Location("Mia", "050321445065", "5th Ave.", 3, District.North);
        Location dest3 = new Location("Lea", "0547778899", "Allenby St.", 15, District.Central);
        Location dest4 = new Location("Ben", "0506667788", "Begin St.", 42, District.South);

        // Create shipments and add products
        Shipment shipmentOne = new Shipment(origin1, dest1, date1);
        shipmentOne.addProductToShipment(new Product("Apples", 2), 100);
        shipmentOne.addProductToShipment(new Product("Meat", 10), 12);
        shipmentOne.addProductToShipment(new Product("Bananas", 2), 25);
        shipmentOne.addProductToShipment(new Product("Watermelons", 15), 10);
        shipmentOne.addProductToShipment(new Product("Cheese", 4), 25);

        Shipment shipmentTwo = new Shipment(origin2, dest2, date2);
        shipmentTwo.addProductToShipment(new Product("Lettuce", 1), 15);
        shipmentTwo.addProductToShipment(new Product("Tomatoes", 2), 10);
        shipmentTwo.addProductToShipment(new Product("Milk", 3), 6);
        shipmentTwo.addProductToShipment(new Product("Butter", 1), 8);
        shipmentTwo.addProductToShipment(new Product("Bread", 2), 12);

        Shipment shipmentThree = new Shipment(origin3, dest3, date3);
        shipmentThree.addProductToShipment(new Product("Potatoes", 4), 20);
        shipmentThree.addProductToShipment(new Product("Rice", 2), 5);
        shipmentThree.addProductToShipment(new Product("Beans", 1), 10);
        shipmentThree.addProductToShipment(new Product("Eggs", 3), 6);
        shipmentThree.addProductToShipment(new Product("Yogurt", 2), 10);

        Shipment shipmentFour = new Shipment(origin4, dest4, date4);
        shipmentFour.addProductToShipment(new Product("Oranges", 2), 10);
        shipmentFour.addProductToShipment(new Product("Chicken", 8), 4);
        shipmentFour.addProductToShipment(new Product("Onions", 1), 8);
        shipmentFour.addProductToShipment(new Product("Cucumbers", 2), 10);
        shipmentFour.addProductToShipment(new Product("Juice", 5), 6);

        // Add shipments to the booking system to trigger full business logic and persistence
        bookingSystem.addNewShipment(shipmentOne);
        bookingSystem.addNewShipment(shipmentTwo);
        bookingSystem.addNewShipment(shipmentThree);
        bookingSystem.addNewShipment(shipmentFour);

        System.out.println("Data loaded using full service-layer functionality.");
    }
}