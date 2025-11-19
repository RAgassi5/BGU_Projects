package Service.DeliveriesService;

import Domain.DeliveriesDomain.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehiclesManagerTest {

    // Helper method to remove all vehicles from the repo using existing methods
    private void clearAllVehicles(IVehicleRepo repo, VehiclesManager vehiclesManager) {
        try {
            // Remove all vehicles present in the repo.
            Collection<Vehicle> allVehicles = repo.getAllVehicles();
            for (Vehicle veh : allVehicles) {
                vehiclesManager.removeVehicle(veh.getLicenseNumber());
            }
        } catch (SQLException e) {
            fail("Exception clearing vehicles: " + e.getMessage());
        }
    }

    // Helper method to find a vehicle by license number from the repo
    private Vehicle findVehicle(IVehicleRepo repo, int licenseNumber) {
        try {
            for(Vehicle veh : repo.getAllVehicles()){
                if(veh.getLicenseNumber() == licenseNumber) {
                    return veh;
                }
            }
        } catch (SQLException e) {
            fail("Exception finding vehicle: " + e.getMessage());
        }
        return null;
    }

    @Test
    void testAddTruck_Successful() {
        VehiclesManager vehiclesManager = VehiclesManager.getInstance();
        IVehicleRepo repo = vehiclesManager.getVehicleRepo();

        clearAllVehicles(repo, vehiclesManager);

        vehiclesManager.addVehicle("Truck", 1, 3000, 8000);
        Vehicle addedVehicle = findVehicle(repo, 1);

        assertNotNull(addedVehicle);
        assertTrue(addedVehicle instanceof Truck);
        assertEquals(1, addedVehicle.getLicenseNumber());
    }

    @Test
    void testAddVan_Successful() {
        VehiclesManager vehiclesManager = VehiclesManager.getInstance();
        IVehicleRepo repo = vehiclesManager.getVehicleRepo();

        clearAllVehicles(repo, vehiclesManager);

        vehiclesManager.addVehicle("Van", 2, 1500, 6000);
        Vehicle addedVehicle = findVehicle(repo, 2);

        assertNotNull(addedVehicle);
        assertTrue(addedVehicle instanceof Van);
        assertEquals(2, addedVehicle.getLicenseNumber());
    }

    @Test
    void testAddNewVehicle_Successful() {
        VehiclesManager vehiclesManager = VehiclesManager.getInstance();
        IVehicleRepo repo = vehiclesManager.getVehicleRepo();

        clearAllVehicles(repo, vehiclesManager);

        vehiclesManager.addVehicle("Bike", 3, 50, 100);
        Vehicle addedVehicle = findVehicle(repo, 3);

        assertNotNull(addedVehicle);
        assertTrue(addedVehicle instanceof NewVehicle);
        assertEquals("Bike", ((NewVehicle) addedVehicle).getVehicleType());
        assertEquals(3, ((NewVehicle) addedVehicle).getLicenseNumber());
    }

    @Test
    void testAddVehicle_AlreadyExists() {
        VehiclesManager vehiclesManager = VehiclesManager.getInstance();
        IVehicleRepo repo = vehiclesManager.getVehicleRepo();

        clearAllVehicles(repo, vehiclesManager);

        vehiclesManager.addVehicle("Truck", 4, 3000, 8000);
        vehiclesManager.addVehicle("Truck", 4, 3000, 8000);

        Collection<Vehicle> allVehicles = null;
        try {
            allVehicles = repo.getAllVehicles();
        } catch (SQLException e) {
            fail("Exception retrieving vehicles: " + e.getMessage());
        }
        assertEquals(1, allVehicles.size()); // Only one vehicle should exist in the repository
    }

    @Test
    void testRemoveVehicle_Successful() {
        VehiclesManager vehiclesManager = VehiclesManager.getInstance();
        IVehicleRepo repo = vehiclesManager.getVehicleRepo();

        clearAllVehicles(repo, vehiclesManager);

        vehiclesManager.addVehicle("Van", 5, 2000, 5000);
        vehiclesManager.removeVehicle(5);

        Vehicle removedVehicle = findVehicle(repo, 5);

        assertNull(removedVehicle);
    }

    @Test
    void testRemoveVehicle_NotFound() {
        VehiclesManager vehiclesManager = VehiclesManager.getInstance();
        IVehicleRepo repo = vehiclesManager.getVehicleRepo();

        clearAllVehicles(repo, vehiclesManager);

        vehiclesManager.removeVehicle(10);

        assertNull(findVehicle(repo, 10)); // Ensure no vehicle was removed
    }

    @Test
    void testAssignVehicle_Successful() {
        VehiclesManager vehiclesManager = VehiclesManager.getInstance();
        IVehicleRepo repo = vehiclesManager.getVehicleRepo();

        clearAllVehicles(repo, vehiclesManager);

        // Instead of "Truck", use a non-default type so that the passed capacity values are used.
        vehiclesManager.addVehicle("Lorry", 6, 4000, 10000);

        Vehicle assignedVehicle = vehiclesManager.assignVehicle(3000);
        assertNotNull(assignedVehicle);
        assertEquals(6, assignedVehicle.getLicenseNumber());

        // Re-read the vehicle from the repository to get the updated status.
        Vehicle updatedVehicle = findVehicle(repo, assignedVehicle.getLicenseNumber());
        assertEquals(vehicle_status.WORKING, updatedVehicle.getCurrentStatus());
    }



    @Test
    void testAssignVehicle_NoMatchingVehicle() {
        VehiclesManager vehiclesManager = VehiclesManager.getInstance();
        IVehicleRepo repo = vehiclesManager.getVehicleRepo();

        clearAllVehicles(repo, vehiclesManager);

        vehiclesManager.addVehicle("Van", 7, 1500, 5000);

        Vehicle assignedVehicle = vehiclesManager.assignVehicle(6000);

        assertNull(assignedVehicle); // No vehicle matches the weight requirement
    }

    @Test
    void testGetAmountOfVehicles() {
        VehiclesManager vehiclesManager = VehiclesManager.getInstance();
        IVehicleRepo repo = vehiclesManager.getVehicleRepo();

        clearAllVehicles(repo, vehiclesManager);

        vehiclesManager.addVehicle("Truck", 8, 3000, 8000);
        vehiclesManager.addVehicle("Van", 9, 1500, 5000);

        int vehicleCount = vehiclesManager.getAmountOfVehicles();

        assertEquals(2, vehicleCount);
    }

    @Test
    void testGetMaxAvailableCapacity() {
        VehiclesManager vehiclesManager = VehiclesManager.getInstance();
        IVehicleRepo repo = vehiclesManager.getVehicleRepo();

        clearAllVehicles(repo, vehiclesManager);

        vehiclesManager.addVehicle("Truck", 10, 4000, 9000);
        vehiclesManager.addVehicle("Van", 11, 2000, 7000);

        int maxCapacity = vehiclesManager.getMaxAvailableCapacity();

        // Since truck uses its default max capacity of 500 (and van uses 250),
        // the maximum available capacity is 500.
        assertEquals(500, maxCapacity);

    }
}