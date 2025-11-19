package Service.DeliveriesService;

import Domain.DeliveriesDomain.IShipmentRepo;
import Domain.DeliveriesDomain.Shipment;
import Domain.DeliveriesDomain.ShipmentsRepo;
import Domain.DeliveriesDomain.s_status;

import java.util.*;

public class BookingSystem {
    //the stack that holds all pending shipments
    private final Stack<Shipment> pendingShipments;

    //an instance of the class BookingSystem
    private static BookingSystem currentBookingSystem;

    private IShipmentRepo currentShipmentsRepo;


    //private constructor
    private BookingSystem() {
        this.pendingShipments = new Stack<>();
        this.currentShipmentsRepo = new ShipmentsRepo();

    }

    /**
     * a function that returns the Singleton instance of booking system
     *
     * @return the booking system
     */
    public static BookingSystem getInstance() {
        if (currentBookingSystem == null) {
            currentBookingSystem = new BookingSystem();
        }
        return currentBookingSystem;
    }

    /**
     * a function that receives a new shipment booking
     *
     * @param shipment a new shipment
     */
    public void addNewShipment(Shipment shipment) {
        //shipment.updateStatus(s_status.PENDING);
        currentShipmentsRepo.addShippedShipment(shipment);
        pendingShipments.push(shipment);

    }

    /**
     * a function that takes all pending bookings and "dumps" them
     *
     * @return a copy of-pending-shipments Stack
     */
    public Stack<Shipment> getPendingShipments() {
        Stack<Shipment> temp = (Stack<Shipment>) pendingShipments.clone();
        pendingShipments.clear();
        return temp;
    }

    /**
     * checks if there are any pending bookings
     *
     * @return True/False
     */
    public boolean hasBookings() {
        return !pendingShipments.isEmpty();
    }

    /**
     * a function that adds a new self-delivered shipment
     * @param shipment a new self-delivered shipment
     */
    public void addNewSelfShipment(Shipment shipment) {
        shipment.updateStatus(s_status.SELF_DELIVERED);
        this.currentShipmentsRepo.addSelfDeliveredShipment(shipment);
    }

    /**
     * a function that displays the shipment Form as needed
     * @param shipmentId represent a shipment ID
     */

    public void displayShipment(int shipmentId) {
        Shipment found = this.currentShipmentsRepo.getShipmentById(shipmentId);
        if (found == null) {
            System.out.println("\nNo shipment found with ID: " + shipmentId);
        }
        else {
            System.out.println("\n=== Your Shipment form ===");
            System.out.println("Shipment number: " + found.getId());
            System.out.println("Shipment status: " + found.getStatus().getDisplayName());
            System.out.println("Shipment date: " + found.getShipmentDate());
            System.out.println("Shipment departure time: " + found.getDepartureTime());
            System.out.println("Shipment origin address: " + found.getOriginLocation().getAddress());
            System.out.println("Shipment origin contact name: " + found.getOriginLocation().getContactName());
            System.out.println("Shipment origin contact phone number: " + found.getOriginLocation().getContactNUM());
            System.out.println("Shipment destination address: " + found.getDestinationLocation().getAddress());
            System.out.println("Shipment destination contact name: " + found.getDestinationLocation().getContactName());
            System.out.println("Shipment destination contact phone number: " + found.getDestinationLocation().getContactNUM());
            System.out.println("=== Products in Shipment " + found.getId() + " ===");
            found.printShipmentProducts();
            System.out.println("==============================");
        }
    }

    public IShipmentRepo getCurrentShipmentsRepo() {
        return this.currentShipmentsRepo;
    }
}
