package Service.DeliveriesService;

import Domain.DeliveriesDomain.District;
import Domain.DeliveriesDomain.Location;
import Domain.DeliveriesDomain.Shipment;
import Domain.DeliveriesDomain.s_status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Stack;

class BookingSystemTest {
    private BookingSystem bookingSystem;
    private Shipment testShipment;
    private Location originLocation;
    private Location destinationLocation;

    @BeforeEach
    void setUp() {
        bookingSystem = BookingSystem.getInstance();
        originLocation = new Location("Shalom", "0500000000","Origin Street", 1 , District.North);
        destinationLocation = new Location("Keren","00779098" ,"Dest Street" ,2,District.South);
        testShipment = new Shipment(originLocation, destinationLocation, LocalDate.now());
    }

    @Test
    void testSingletonInstance() {
        BookingSystem instance1 = BookingSystem.getInstance();
        BookingSystem instance2 = BookingSystem.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testAddNewShipment() {
        bookingSystem.addNewShipment(testShipment);
        assertTrue(bookingSystem.hasBookings());
        assertEquals(s_status.PENDING, testShipment.getStatus());
    }

    @Test
    void testGetPendingShipments() {
        bookingSystem.addNewShipment(testShipment);
        Stack<Shipment> pendingShipments = bookingSystem.getPendingShipments();

        assertFalse(pendingShipments.isEmpty());
        assertEquals(testShipment, pendingShipments.pop());
        assertFalse(bookingSystem.hasBookings()); // Should be empty after getting pending shipments
    }

    @Test
    void testHasBookings() {
        assertFalse(bookingSystem.hasBookings());
        bookingSystem.addNewShipment(testShipment);
        assertTrue(bookingSystem.hasBookings());
    }

    @Test
    void testAddNewSelfShipment() {
        bookingSystem.addNewSelfShipment(testShipment);
        assertEquals(s_status.SELF_DELIVERED, testShipment.getStatus());
    }

    @Test
    void testDisplayShipmentWithValidId() {
        bookingSystem.addNewShipment(testShipment);
        bookingSystem.displayShipment(testShipment.getId());
        // Since displayShipment prints to console, we can only verify it doesn't throw exceptions
    }

    @Test
    void testDisplayShipmentWithInvalidId() {
        bookingSystem.displayShipment(-1); // Should handle invalid ID gracefully
    }

    @Test
    void testMultipleShipments() {
        // Stop any running scheduler to prevent interference
        RouteManager.getInstance().stopRouting();

        Shipment shipment1 = new Shipment(originLocation, destinationLocation, LocalDate.now());
        Shipment shipment2 = new Shipment(originLocation, destinationLocation, LocalDate.now());

        bookingSystem.addNewShipment(shipment1);
        bookingSystem.addNewShipment(shipment2);

        // Get the shipments immediately before the scheduler can process them
        Stack<Shipment> pendingShipments = bookingSystem.getPendingShipments();

        // Since the stack returns items in LIFO order, shipment2 should be first
        assertEquals(shipment2, pendingShipments.pop());
        assertEquals(shipment1, pendingShipments.pop());
    }
}