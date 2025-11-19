package Domain.DeliveriesDomain;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testVanPropertiesAndStatus() {
        Van van = new Van(1234);
        assertEquals(1234, van.getLicenseNumber(), "License number should be set by constructor");
        assertEquals(100, van.getVehicleWeight(), "Van default weight should be 100");
        assertEquals(250, van.getVehicleMaxCapacity(), "Van max capacity should be 250");
        assertEquals(vehicle_status.AVAILABLE, van.getCurrentStatus(), "Initial status should be AVAILABLE");
        assertEquals("Van", van.getVehicleType(), "Vehicle type should be 'Van'");
    }

    @Test
    void testTruckPropertiesAndStatus() {
        Truck truck = new Truck(5678);
        assertEquals(5678, truck.getLicenseNumber(), "License number should be set by constructor");
        assertEquals(200, truck.getVehicleWeight(), "Truck default weight should be 200");
        assertEquals(500, truck.getVehicleMaxCapacity(), "Truck max capacity should be 500");
        assertEquals(vehicle_status.AVAILABLE, truck.getCurrentStatus(), "Initial status should be AVAILABLE");
        assertEquals("Truck", truck.getVehicleType(), "Vehicle type should be 'Truck'");
    }

    @Test
    void testNewVehicleTypeCapitalization() {
        NewVehicle newVeh = new NewVehicle("bus", 1111, 150, 300);
        assertEquals("Bus", newVeh.getVehicleType(), "First letter of type should be capitalized");
        assertEquals(1111, newVeh.getLicenseNumber(), "License number should be set by constructor");
        assertEquals(150, newVeh.getVehicleWeight(), "Weight should be set by constructor");
        assertEquals(300, newVeh.getVehicleMaxCapacity(), "Max capacity should be set by constructor");
        assertEquals(vehicle_status.AVAILABLE, newVeh.getCurrentStatus(), "Initial status should be AVAILABLE");
    }

    @Test
    void testNewVehicleNullOrEmptyType() {
        NewVehicle nullType = new NewVehicle(null, 2222, 100, 200);
        assertNull(nullType.getVehicleType(), "Type should remain null when constructed with null");

        NewVehicle emptyType = new NewVehicle("", 3333, 100, 200);
        assertEquals("", emptyType.getVehicleType(), "Type should remain empty string when constructed with empty");
    }

    @Test
    void testCommonVehicleMethods() {
        Van van = new Van(7777);
        // Test setter and getter for status
        van.setCurrentStatus(vehicle_status.AVAILABLE);
        assertEquals(vehicle_status.AVAILABLE, van.getCurrentStatus(), "Status should be updatable via setter");

        // Test printVehicle output
        van.printVehicle();
        String output = outContent.toString().trim();
        assertTrue(output.contains("Van #7777 is currently: AVAILABLE"),
                "printVehicle should output type, license number and current status");
    }
}