package Presentation.DeliveriesPresentation;

import Service.DeliveriesService.VehiclesManager;

public class VehicleController {
    private final VehiclesManager vehiclesManager;

    public VehicleController() {
        this.vehiclesManager = VehiclesManager.getInstance();
    }

    public void addVehicle(String vehicleType, int licenseNUM, int vehicleWeight, int maxVehicleWeight) {
        try {
            vehiclesManager.addVehicle(vehicleType, licenseNUM, vehicleWeight, maxVehicleWeight);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void deleteVehicle(int licenseNum) {
        vehiclesManager.removeVehicle(licenseNum);
    }

    public void printAllVehiclesInSystem() {
        vehiclesManager.printAllVehicles();
    }

    public int getNumberOfVehiclesInSystem() {
        return this.vehiclesManager.getAmountOfVehicles();

    }


}
