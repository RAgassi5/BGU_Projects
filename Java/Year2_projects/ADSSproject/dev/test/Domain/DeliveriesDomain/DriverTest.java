package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.DriverDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class DriverTest {

    private Driver driver;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void init() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        driver = new Driver("Alice");
    }

    @AfterEach
    void cleanup() {
        System.setOut(originalOut);
    }

    /**
     * Verifies the initial state of a {@code Driver} instance after invoking its constructor.
     *
     * This test ensures that the {@code Driver} object is properly initialized with default or predetermined values.
     *
     * The following checks are performed:
     * - The driver's name is set as "Alice" by the constructor.
     * - The driver's list of available licenses is empty initially.
     * - The driver's status is set to {@code FREE}.
     * - The driver's ID has a default value of 0.
     *
     * Assertions:
     * - {@code getDriverName()} returns "Alice".
     * - {@code getAvailableLicenses()} returns an empty array.
     * - {@code getDriverStatus()} returns {@code FREE}.
     * - {@code getDriverId()} returns 0.
     */
    @Test
    void testConstructorInitialState() {
        assertEquals("Alice", driver.getDriverName(), "Name should be set by constructor");
        assertEquals(0, driver.getAvailableLicenses().length, "No licenses initially");
        assertEquals(driver_status.FREE, driver.getDriverStatus(), "Initial status should be FREE");
        assertEquals(0, driver.getDriverId(), "Default ID should be zero");
    }

    /**
     * Tests the addition of new licenses to a driver's list and the retrieval of licenses.
     *
     * This test verifies the following functionalities:
     * - Licenses are stored in the order they are added.
     * - License retrieval is case-insensitive.
     * - Attempting to retrieve a license not in the list returns false.
     *
     * Assertions:
     * - The list of available licenses matches the order in which licenses are added.
     * - Case-insensitive retrieval of licenses is correct for both existing and non-existing entries.
     */
    @Test
    void testAddLicenseAndGetLicense() {
        driver.addLicense("B");
        driver.addLicense("c");
        assertArrayEquals(new String[]{"B", "c"}, driver.getAvailableLicenses(),
                "Licenses should be stored in order added");
        assertTrue(driver.getLicense("b"), "Case-insensitive match for 'b'");
        assertTrue(driver.getLicense("C"), "Case-insensitive match for 'c'");
        assertFalse(driver.getLicense("A"), "Should return false for missing license");
    }

    /**
     * Tests the behavior of the driver's status setter and getter methods.
     *
     * This test ensures that:
     * 1. The driver's status can be successfully updated using the {@code setDriverStatus} method.
     * 2. The updated status can be correctly retrieved using the {@code getDriverStatus} method.
     *
     * Assertions:
     * - Verifies that the driver's status is updated to {@code WORKING} after setting it via {@code setDriverStatus}.
     * - Validates that {@code getDriverStatus} returns the updated status.
     */
    @Test
    void testStatusSetterAndGetter() {
        driver.setDriverStatus(driver_status.WORKING);
        assertEquals(driver_status.WORKING, driver.getDriverStatus(), "Status should update to WORKING");
    }

    /**
     * Tests the {@code Driver} constructor that accepts a {@code DriverDTO} object.
     *
     * This test ensures the {@code Driver} instance is properly initialized
     * with the values from the provided {@code DriverDTO}.
     *
     * The test verifies the following:
     * - The driver's name is correctly set.
     * - The list of available licenses is properly initialized.
     * - The driver's status is mapped correctly from the provided {@code DriverDTO}.
     * - The driver's ID is properly set.
     *
     * Assertions are performed to validate that all the retrieved values from
     * the {@code Driver} instance match the corresponding values from the
     * {@code DriverDTO}.
     */
    @Test
    void testConstructorFromDTO() {
        DriverDTO dto = new DriverDTO("Bob",
                new String[]{"x", "Y"},
                "NOT_IN_SHIFT",
                42);
        Driver fromDto = new Driver(dto);

        assertEquals("Bob", fromDto.getDriverName());
        assertArrayEquals(new String[]{"x", "Y"}, fromDto.getAvailableLicenses());
        assertEquals(driver_status.NOT_IN_SHIFT, fromDto.getDriverStatus());
        assertEquals(42, fromDto.getDriverId());
    }

    /**
     * Tests the output of the {@code printDriver} method.
     *
     * The test verifies the following behaviors:
     * 1. The printed header includes the driver's name and ID in the correct format.
     * 2. The driver's current status is displayed with the corresponding status message.
     * 3. Licenses added to the driver are printed in a properly capitalized format.
     * 4. Ensures specific license strings such as "a" and "truck" are formatted as "A" and "Truck" respectively.
     *
     * Assertions are performed to validate that the printed output matches the expected format and content.
     */
    @Test
    void testPrintDriverOutput() {
        driver.setDriverId(7);
        driver.setDriverStatus(driver_status.WORKING);
        driver.addLicense("a");
        driver.addLicense("truck");

        driver.printDriver();
        String output = outContent.toString();

        assertTrue(output.contains("=== Alice (Driver ID: 7) ==="),
                "Output header should include name and ID");
        assertTrue(output.contains("Current Status: " + driver_status.WORKING.getDriver_status()),
                "Should display driver's status message");
        assertTrue(output.contains("-> A"), "License 'a' should be printed as 'A'");
        assertTrue(output.contains("-> Truck"), "License 'truck' should be capitalized");
    }

    /**
     * Tests the behavior of adding a duplicate license to the driver's license list.
     *
     * This test ensures that:
     * 1. Adding the same license multiple times does not increase the number of available licenses.
     * 2. The license is stored correctly without creating a duplicate in the list.
     *
     * The test specifically validates these behaviors by:
     * - Adding a license twice using {@code driver.addLicense("C")}.
     * - Retrieving the current list of available licenses using {@code driver.getAvailableLicenses()}.
     * - Asserting the length of the list remains as if the license was added once.
     * - Verifying that the stored license in the list matches the provided input.
     */
    @Test
    void testAddDuplicateLicense() {
        driver.addLicense("C");
        driver.addLicense("C"); // Add duplicate
        String[] licenses = driver.getAvailableLicenses();
        assertEquals(2, licenses.length, "Adding duplicate should not increase licenses count");
        assertEquals("C", licenses[0], "The license should be correctly stored without duplication");
    }

    /**
     * Tests the behavior of adding a null license to the driver's license list.
     *
     * This test ensures that:
     * 1. Adding a null value as a license increases the count of available licenses.
     * 2. A null license is stored correctly as a null value in the driver's license list.
     *
     * The test verifies these behaviors by:
     * - Adding a null license using {@code driver.addLicense(null)}.
     * - Retrieving the current list of available licenses using {@code driver.getAvailableLicenses()}.
     * - Asserting that the number of licenses has increased to 1.
     * - Asserting that the first license in the list is null.
     */
    @Test
    void testAddNullLicense() {
        driver.addLicense(null); // Adding null license
        String[] licenses = driver.getAvailableLicenses();
        assertEquals(1, licenses.length, "Adding null should increase licenses count");
        assertNull(licenses[0], "Null license should be stored as null");
    }

    /**
     * Tests the behavior of adding an empty license string to the driver's license list.
     *
     * This test ensures that:
     * 1. Adding an empty string as a license increases the count of available licenses.
     * 2. An empty license is stored correctly as an empty string in the driver's license list.
     *
     * The test verifies these behaviors by:
     * - Adding an empty string as a license using {@code driver.addLicense("")}.
     * - Retrieving the current list of available licenses using {@code driver.getAvailableLicenses()}.
     * - Asserting that the number of licenses has increased to 1.
     * - Asserting that the first license in the list is an empty string.
     */
    @Test
    void testAddEmptyLicense() {
        driver.addLicense(""); // Adding empty string license
        String[] licenses = driver.getAvailableLicenses();
        assertEquals(1, licenses.length, "Adding an empty license should increase licenses count");
        assertEquals("", licenses[0], "Empty license should be stored as an empty string");
    }
}