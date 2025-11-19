package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.DriverDTO;
import Domain.DeliveriesDomain.Driver;
import Domain.DeliveriesDomain.DriversRepo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class DriversRepoTest {

    /**
     * Tests the behavior of retrieving a driver by ID when the driver does not exist
     * in both the repository and the underlying data access object (DAO).
     *
     * Validates that the `getDriverByID` method in the `DriversRepo` class returns
     * `null` when no driver with the specified ID is found either in the in-memory
     * cache (repository) or in the external data source (DAO).
     *
     * Preconditions:
     * - The repository is initialized and contains no driver with the requested ID.
     * - The DAO does not have a driver with the specified ID.
     *
     * Validations:
     * - The result of `getDriverByID` for a non-existent ID is `null`.
     *
     * @throws SQLException if an error occurs during interaction with the data source.
     */
    @Test
    public void testGetDriverById_DriverNotInRepoAndNotInDAO() throws SQLException {
        // Arrange
        DriversRepo driversRepo = new DriversRepo();

        // Act
        Driver result = driversRepo.getDriverByID(999);

        // Assert
        assertNull(result);
    }

    /**
     * Tests the functionality of adding a driver to the repository and ensures
     * the driver is correctly added with the expected attributes.
     *
     * This method validates that:
     * - A new driver named "Chris" is successfully added to the repository.
     * - The driver's name matches the expected value ("Chris").
     * - The driver's status is initialized to "FREE".
     *
     * Preconditions:
     * - An instance of `DriversRepo` is initialized and ready for use.
     *
     * Validations:
     * - The newly added driver is not null.
     * - The driver's name is "Chris".
     * - The driver's status is "FREE".
     *
     * @throws SQLException if an error occurs while performing database operations.
     */
    @Test
    public void testAddDriver_AddsDriverSuccessfully() throws SQLException {
        // Arrange
        DriversRepo driversRepo = new DriversRepo();

        // Act
        driversRepo.addDriver("Chris");

        // Assert
        Driver addedDriver = driversRepo.getAllDrivers().stream()
                .filter(driver -> "Chris".equals(driver.getDriverName()))
                .findFirst()
                .orElse(null);

        assertNotNull(addedDriver);
        assertEquals("Chris", addedDriver.getDriverName());
        assertEquals("FREE", addedDriver.getDriverStatus().toString());
    }

    /**
     * Tests the removal of a driver from the repository and ensures
     * the driver is successfully removed.
     *
     * This method validates that:
     * - A driver named "Sara" is added to the repository.
     * - The driver's ID is retrieved successfully.
     * - The driver is successfully removed using their ID.
     * - The driver's entry no longer exists in the repository after removal.
     *
     * Preconditions:
     * - An instance of `DriversRepo` is initialized and ready for use.
     * - A driver named "Sara" exists in the repository prior to removal.
     *
     * Validations:
     * - The driver identified by the specified ID is removed successfully.
     * - A call to `getDriverByID` for the removed driver's ID returns `null`.
     *
     * @throws SQLException if an error occurs while performing database operations.
     */
    @Test
    public void testRemoveDriver_DriverRemovedSuccessfully() throws SQLException {
        // Arrange
        DriversRepo driversRepo = new DriversRepo();
        driversRepo.addDriver("Sara");
        int driverIDToRemove = driversRepo.getAllDrivers().stream()
                .filter(driver -> "Sara".equals(driver.getDriverName()))
                .findFirst()
                .orElseThrow()
                .getDriverId();

        // Act
        driversRepo.removeDriver(driverIDToRemove);

        // Assert
        assertNull(driversRepo.getDriverByID(driverIDToRemove));
    }

    /**
     * Tests the functionality of updating a driver's status in the repository
     * and ensures the status is updated successfully and accurately.
     *
     * This method validates that:
     * - A new driver named "Diana" is added to the repository.
     * - The driver's ID is retrieved successfully.
     * - The driver's status is updated to "WORKING" using their ID.
     * - The updated status of the driver matches the expected value.
     *
     * Preconditions:
     * - An instance of `DriversRepo` is initialized and ready for use.
     * - A driver named "Diana" exists in the repository prior to the status update.
     *
     * Validations:
     * - The driver's status is successfully updated to "WORKING".
     * - A call to `getDriverByID` for the updated driver's ID reflects the new status "WORKING".
     *
     * @throws SQLException if an error occurs while accessing the data source.
     */
    @Test
    public void testUpdateDriverStatus_UpdatesStatusSuccessfully() throws SQLException {
        // Arrange
        DriversRepo driversRepo = new DriversRepo();
        driversRepo.addDriver("Diana");
        int driverID = driversRepo.getAllDrivers().stream()
                .filter(driver -> "Diana".equals(driver.getDriverName()))
                .findFirst()
                .orElseThrow()
                .getDriverId();

        // Act
        driversRepo.updateDriverStatus(driverID, driver_status.WORKING);

        // Assert
        Driver updatedDriver = driversRepo.getDriverByID(driverID);
        assertEquals(driver_status.WORKING, updatedDriver.getDriverStatus());
    }

    /**
     * Tests the behavior of the `hasLicense` method when the driver has the specified license.
     *
     * The purpose of this test is to validate that when a driver is added to the repository
     * and assigned a specific license type, the `hasLicense` method correctly identifies
     * that the driver holds the relevant license.
     *
     * Preconditions:
     * - A driver named "Sam" is added to the repository.
     * - The driver is assigned a license of type "D".
     *
     * Validations:
     * - The `hasLicense` method returns `true` when invoked with the driver's ID
     *   and the license type "D".
     *
     * @throws SQLException if an error occurs while performing database operations.
     */
    @Test
    public void testHasLicense_ReturnsTrueIfDriverHasLicense() throws SQLException {
        // Arrange
        DriversRepo driversRepo = new DriversRepo();
        driversRepo.addDriver("Sam");
        int driverID = driversRepo.getAllDrivers().stream()
                .filter(driver -> "Sam".equals(driver.getDriverName()))
                .findFirst()
                .orElseThrow()
                .getDriverId();
        driversRepo.updateDriverLicense(driverID, "D");

        boolean hasLicense = driversRepo.hasLicense(driverID, "D");

        assertTrue(hasLicense);
    }

    /**
     * Tests the behavior of the `hasLicense` method when the driver specified does not hold the
     * relevant license.
     *
     * The purpose of this test is to verify that when a driver is added to the repository without
     * any specific license, the `hasLicense` method correctly returns `false` for the queried
     * license type.
     *
     * Preconditions:
     * - A driver named "Morgan" is added to the repository.
     * - The driver does not hold any licenses upon addition.
     *
     * Validations:
     * - The `hasLicense` method returns `false` when invoked with the driver's ID and license type "A".
     *
     * @throws SQLException if an error occurs while performing database operations.
     */
    @Test
    public void testHasLicense_ReturnsFalseIfDriverDoesNotHaveLicense() throws SQLException {
        // Arrange
        DriversRepo driversRepo = new DriversRepo();
        driversRepo.addDriver("Morgan");
        int driverID = driversRepo.getAllDrivers().stream()
                .filter(driver -> "Morgan".equals(driver.getDriverName()))
                .findFirst()
                .orElseThrow()
                .getDriverId();

        boolean hasLicense = driversRepo.hasLicense(driverID, "A");

        assertFalse(hasLicense);
    }
}