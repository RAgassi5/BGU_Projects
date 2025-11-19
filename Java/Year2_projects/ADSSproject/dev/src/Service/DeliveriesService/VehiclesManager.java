package Service.DeliveriesService;

import Domain.DeliveriesDomain.*;

import java.sql.SQLException;

public class VehiclesManager {

    private static VehiclesManager currentVehiclesManager;
    private IVehicleRepo currentVehicleRepo;

    private VehiclesManager() {
        this.currentVehicleRepo = new VehiclesRepo();
    }

    /**
     * Retrieves the singleton instance of the VehiclesManager class.
     * <p>
     * This method ensures that only one instance of VehiclesManager is created and shared
     * across the application. If the instance does not already exist, it initializes and
     * returns a new instance.
     *
     * @return the singleton instance of VehiclesManager
     */
    public static VehiclesManager getInstance() {
        if (currentVehiclesManager == null) {
            currentVehiclesManager = new VehiclesManager();
        }
        return currentVehiclesManager;
    }

    /**
     * Adds a vehicle to the system based on the specified vehicle type and unique vehicle number.
     * Only "Truck" and "Van" types are supported. If the type is invalid or the vehicle number
     * is lower than 1, an IllegalArgumentException is thrown.
     *
     * @param vehicleType      the type of the vehicle to be added (must be "Truck" or "Van")
     * @param vehicleNum       the unique identifier for the vehicle (must be greater than 0)
     * @param vehicleWeight
     * @param maxVehicleWeight
     * @throws IllegalArgumentException if an invalid vehicle type is provided, or the vehicle number is less than 1
     */
    public void addVehicle(String vehicleType, int vehicleNum, int vehicleWeight, int maxVehicleWeight) {
        try {
            if (currentVehicleRepo.vehicleExists(vehicleNum)) {
                System.out.println("Vehicle with the provided license number already exists in the system!");
                return;
            }
            if (vehicleType == null) {
                throw new IllegalArgumentException("Please enter a valid vehicle type!");
            } else if (vehicleNum < 1) {
                throw new IllegalArgumentException("Invalid vehicle number");
            } else {
                if (vehicleType.equalsIgnoreCase("Truck")) {
                    Truck truck = new Truck(vehicleNum);
                    this.currentVehicleRepo.addVehicle(truck);
                    System.out.println("Truck number: " + vehicleNum + " was added to the system successfully!");
                } else if (vehicleType.equalsIgnoreCase("Van")) {
                    Van van = new Van(vehicleNum);
                    this.currentVehicleRepo.addVehicle(van);
                    System.out.println("Van number: " + vehicleNum + " was added to the system successfully!");
                } else {
                    NewVehicle createdVehicle = new NewVehicle(vehicleType, vehicleNum, vehicleWeight, maxVehicleWeight);
                    this.currentVehicleRepo.addVehicle(createdVehicle);
                    System.out.println(vehicleType + " number: " + vehicleNum + " was added to the system successfully!");
                }

            }

        } catch (SQLException e) {
            System.out.println("There was a problem adding the vehicle to the system!");
        }

    }

    /**
     * Removes a vehicle from the system based on its unique vehicle number.
     * If the specified vehicle does not exist, a message indicating the absence is displayed.
     * Otherwise, the vehicle is removed successfully and a confirmation message is shown.
     *
     * @param vehicleNum the unique identifier of the vehicle to be removed
     */
    public void removeVehicle(int vehicleNum) {
        try {
            if (!this.currentVehicleRepo.vehicleExists(vehicleNum)) {
                System.out.println("Vehicle with the provided license number does not exist in the system!");
                return;
            }
            this.currentVehicleRepo.removeVehicle(vehicleNum);

        } catch (SQLException e) {
            System.out.println("There was a problem removing the vehicle from the system!");
        }
    }

    /**
     * Assigns an available vehicle that can handle the specified total weight for transfer.
     * This method iterates through the list of available vehicles to find one with sufficient
     * capacity and in an AVAILABLE status. If such a vehicle is found, its status is updated
     * to WORKING, and the vehicle is returned. If no suitable vehicle is found, the method
     * returns null.
     *
     * @param totalWeightNeededToTransfer the total
     */
    public Vehicle assignVehicle(int totalWeightNeededToTransfer) {
        try {
            for (Vehicle vehicle : this.currentVehicleRepo.getAllVehicles()) {
                if (vehicle.getVehicleMaxCapacity() >= totalWeightNeededToTransfer &&
                        vehicle.getCurrentStatus() == vehicle_status.AVAILABLE) {

                        //vehicle.setCurrentStatus(vehicle_status.WORKING);
                        this.currentVehicleRepo.updateVehicleStatus(vehicle.getLicenseNumber(), vehicle_status.WORKING);
                        return vehicle;

                }
            }


        } catch (SQLException e) {
            System.out.println("There was a problem retrieving the vehicles from the database!");
        }
        return null;
    }

    /**
     * Iterates through all available vehicles and prints their details.
     * <p>
     * This method accesses a collection of vehicles maintained within the `VehiclesManager`
     * class and calls the `printVehicle()` method on each vehicle to display its information.
     * Useful for providing a comprehensive overview of all vehicles currently stored in the system.
     */
    public void printAllVehicles() {
        try {
            this.currentVehicleRepo.getAllVehicles().forEach(Vehicle::printVehicle);
        } catch (SQLException e) {
            System.out.println("There was a problem retrieving the vehicles from the database!");
        }
    }

    /**
     * Retrieves the total number of vehicles currently available in the system.
     * <p>
     * This method accesses the internal collection of available vehicles and returns
     * its size, representing the count of vehicles managed by the system.
     *
     * @return the number of available vehicles
     */
    public int getAmountOfVehicles() {
        try {
            return this.currentVehicleRepo.getAllVehicles().size();
        } catch (Exception e) {
            System.out.println("There was a problem retrieving the vehicles from the database!");
        }
        return 0;
    }

    /**
     * Determines and returns the maximum available capacity among all vehicles.
     * <p>
     * This method iterates over all available vehicles and compares their maximum
     * capacities to find and return the highest capacity.
     *
     * @return the maximum capacity of all available vehicles, or 0 if no vehicles are available
     */
    public int getMaxAvailableCapacity() {
        try {
            int maxCapacity = 0;
            for (Vehicle vehicle : this.currentVehicleRepo.getAllVehicles()) {
                if (vehicle.getVehicleMaxCapacity() > maxCapacity) {
                    maxCapacity = vehicle.getVehicleMaxCapacity();
                }
            }
            return maxCapacity;

        } catch (SQLException e) {
            System.out.println("There was a problem retrieving the vehicles from the database!");
        }
        return 0;
    }

    public IVehicleRepo getVehicleRepo() {
        return this.currentVehicleRepo;
    }
}
