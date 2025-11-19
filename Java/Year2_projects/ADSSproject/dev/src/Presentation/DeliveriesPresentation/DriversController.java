package Presentation.DeliveriesPresentation;

import Service.DeliveriesService.DriversManager;

public class DriversController {
    private final DriversManager driversManager;

    public DriversController() {
        this.driversManager = DriversManager.getInstance();
    }

    public void addNewDriverToSystem(String driverName){
        this.driversManager.addDriver(driverName);
    }
    public void deleteDriveFromSystem(int driverId){
        this.driversManager.deleteDriver(driverId);
    }
    public void addLicenseToDriverInSystem(int driverId, String license){
        this.driversManager.setDriverLicense(driverId, license);
    }
    public void showAllDrivers(){
        this.driversManager.printAvailableDrivers();
    }

    public int getNumberOfDrivers(){
       return driversManager.getDriverCount();
    }
}
