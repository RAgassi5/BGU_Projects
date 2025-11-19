package Service.DeliveriesService;

import Domain.DeliveriesDomain.IShipmentRepo;
import Domain.DeliveriesDomain.Route;
import Domain.DeliveriesDomain.Shipment;
import Domain.DeliveriesDomain.s_status;
import Domain.DeliveriesDomain.Location;
import Domain.DeliveriesDomain.District;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class RouteManagerTest {

    // Reset the RouteManager singleton before each test.
    @BeforeEach
    public void resetSingletons() throws Exception {
        Field routeManagerField = RouteManager.class.getDeclaredField("currentRouteManager");
        routeManagerField.setAccessible(true);
        routeManagerField.set(null, null);

        // Also reset BookingSystem singleton since RouteManager depends on it.
        Field bookingSystemField = BookingSystem.class.getDeclaredField("currentBookingSystem");
        bookingSystemField.setAccessible(true);
        bookingSystemField.set(null, null);
    }

    @AfterEach
    public void tearDown() {
        // Stop any running scheduler after each test to clean up threads.
        RouteManager.getInstance().stopRouting();
    }

    /**
     * A simple dummy IShipmentRepo implementation to allow creating a Route.
     */
    private static class DummyShipmentRepo implements IShipmentRepo {
        @Override
        public void addShippedShipment(Shipment currentShipment) { }
        @Override
        public void addSelfDeliveredShipment(Shipment currentShipment) { }
        @Override
        public Shipment getShipmentById(int id) { return null; }
        @Override
        public void updateShipment(Shipment shipment) { }
    }

    /**
     * Test that sendOutAwaitingShipments() returns null when there are no awaiting routes.
     */
    @Test
    public void testSendOutAwaitingShipments_NoRoutes() {
        RouteManager routeManager = RouteManager.getInstance();
        Route result = routeManager.sendOutAwaitingShipments();
        assertNull(result, "Expected sendOutAwaitingShipments() to return null if no routes are waiting");
    }

    /**
     * Test that pushing an overweight route and then retrieving it returns the same route.
     */
    @Test
    public void testPushAndGetOverweightRoute() {
        RouteManager routeManager = RouteManager.getInstance();
        DummyShipmentRepo dummyRepo = new DummyShipmentRepo();
        Route dummyRoute = new Route(dummyRepo);
        // Mark the route as overweight.
        dummyRoute.updateRouteStatus(s_status.OVERWEIGHT);
        routeManager.pushOverWeightRoute(dummyRoute);
        Route retrieved = routeManager.getCurrentOverWeightRoute();
        assertNotNull(retrieved, "Expected a route to be retrieved from overweight routes");
        assertEquals(dummyRoute, retrieved, "The pushed overweight route should be returned");
    }

    /**
     * Test that rescheduling a route adds it back into the awaiting routes.
     * We use reflection to inspect the private field 'readyRoutesByDistricts'.
     */
    @Test
    public void testRescheduleRoute() throws Exception {
        RouteManager routeManager = RouteManager.getInstance();
        DummyShipmentRepo dummyRepo = new DummyShipmentRepo();
        Route route = new Route(dummyRepo);
        route.updateRouteStatus(s_status.DELAYED);

        // First, use reflection to capture the count of awaiting routes.
        Field readyRoutesField = RouteManager.class.getDeclaredField("readyRoutesByDistricts");
        readyRoutesField.setAccessible(true);
        Route[] initialRoutes = (Route[]) readyRoutesField.get(routeManager);
        int initialCount = initialRoutes.length;

        // Reschedule the route.
        routeManager.rescheduleRoute(route);

        // Get the updated routes array.
        Route[] updatedRoutes = (Route[]) readyRoutesField.get(routeManager);
        int updatedCount = updatedRoutes.length;

        assertEquals(initialCount + 1, updatedCount, "Rescheduling should increase the awaiting routes count by one");
        // And check that our route is among the awaiting routes.
        boolean found = false;
        for (Route r : updatedRoutes) {
            if (r.equals(route)) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Rescheduled route should be present in the awaiting routes list");
    }

    /**
     * Test that starting the auto routing processes pending shipments.
     * We create a shipment, add it via BookingSystem, start the scheduler and then verify that
     * a Route was added to the awaiting routes.
     */
    @Test
    public void testStartAutoRunningProcessesPendingShipments() throws Exception {
        // Create a BookingSystem instance and add a shipment.
        BookingSystem bookingSystem = BookingSystem.getInstance();

        // Create locations for the shipment.
        Location origin = new Location("OriginContact", "0500000000", "Origin St", 1, District.North);
        Location destination = new Location("DestContact", "0600000000", "Dest St", 2, District.South);
        Shipment shipment = new Shipment(origin, destination, LocalDate.now());
        bookingSystem.addNewShipment(shipment);

        // Start RouteManager auto-running (which processes pending shipments).
        RouteManager routeManager = RouteManager.getInstance();
        routeManager.startAutoRunning();

        // Wait 200ms to allow the scheduler's task to process the pending shipment.
        Thread.sleep(200);

        // Stop the scheduler to clean up.
        routeManager.stopRouting();

        // Access the internal readyRoutesByDistricts array via reflection.
        Field readyRoutesField = RouteManager.class.getDeclaredField("readyRoutesByDistricts");
        readyRoutesField.setAccessible(true);
        Route[] routes = (Route[]) readyRoutesField.get(routeManager);

        // Expect that at least one route has been created by processing the pending shipment.
        assertTrue(routes.length > 0, "Expected at least one route to have been created "
                + "by processing pending shipments in the auto-running task");

        // Additionally, check that the shipment status has been updated to SHIPPED.
        assertEquals(s_status.SHIPPED, shipment.getStatus(), "Shipment status should be updated to SHIPPED");
    }
}