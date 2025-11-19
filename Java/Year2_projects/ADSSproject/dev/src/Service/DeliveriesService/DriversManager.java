package Service.DeliveriesService;

import Domain.DeliveriesDomain.Driver;
import Domain.DeliveriesDomain.DriversRepo;
import Domain.DeliveriesDomain.IDriverRepo;
import Domain.DeliveriesDomain.driver_status;

import java.sql.SQLException;
import java.util.Collection;


public class DriversManager {
    private static DriversManager currentDriversManager;
    private IDriverRepo currentDRepo;

    private DriversManager() {
        this.currentDRepo =  new DriversRepo();
    }

    /**
     * a function that returns the instance of the driversManager
     * makes sure it stays a Singleton
     *
     * @return current instance of DriversManager
     */
    public static DriversManager getInstance() {
        if (currentDriversManager == null) {
            currentDriversManager = new DriversManager();
        }
        return currentDriversManager;
    }

    /**
     * a function that adds a driver to the available drivers
     *
     * @param driverName the name of the new driver
     */
    public void addDriver(String driverName) {
        try{
            currentDRepo.addDriver(driverName);
            System.out.println("Driver " + driverName + " has been added to the system successfully!");
        }catch (SQLException e) {
            System.out.println("There was a problem adding the driver to the system!");
        }

    }

    /**
     * a function that removes a driver from the available drivers
     *
     * @param driverId the ID of the driver to be removed
     */
    public void deleteDriver(int driverId) {
        try{
            Driver driver = currentDRepo.getDriverByID(driverId);
            if (driver == null) {
                System.out.println("There is no driver with the ID: " + driverId + " in the system!");
                return;
            }

            currentDRepo.removeDriver(driverId);
            System.out.println("Driver ID: " +driverId + " was deleted successfully from the system!");
        }catch (SQLException e){
            System.out.println("There was a problem removing the driver from the system!"
);
        }

    }

    /**
     * @param driverId the driver's ID to be updated
     * @param status     the status to set
     */
    public void setDriverStatus(int driverId, driver_status status) {
        try{
            currentDRepo.updateDriverStatus(driverId, status);
        } catch (SQLException e) {
            System.out.println("There was a problem updating the driver status!");
        }
    }

    /**
     * a function that adds a license type to a specific driver
     *
     * @param driverId the ID of the driver
     * @param license    the type of license
     */
    public void setDriverLicense(int driverId, String license) {
        Driver currentDriver;
        try {
            currentDriver = currentDRepo.getDriverByID(driverId);
            if(currentDriver == null){
                System.out.println("There is no driver with the ID: " + driverId + " in the system!");
                return;
            }
            if (currentDRepo.hasLicense(driverId, license)) {
                System.out.println("This driver already has the license: " + license + "!");
                return;
            }
            currentDriver.addLicense(license);
            currentDRepo.updateDriverLicense(driverId, license);
            System.out.println("The licenses have been updated!");

        }catch (SQLException e){
            System.out.println("There was a problem updating the driver license!");
        }
    }

    /**
     * a function that retrieves a driver according to the license type
     *
     * @param license the license type to search for
     * @return an instance of Driver that has the wanted license type
     */
    public Driver assignDriver(String license) {
        try {
            Collection<Driver> allDrivers = currentDRepo.getAllDrivers();
            for(Driver driver: allDrivers){
                if (driver.getLicense(license) && driver.getDriverStatus() == driver_status.FREE) {
                    //driver.setDriverStatus(driver_status.WORKING);
                    this.currentDRepo.updateDriverStatus(driver.getDriverId(), driver_status.WORKING);
                    return driver;
                }
            }
        }catch (SQLException e){
            System.out.println("There was a problem retrieving the drivers from the database!");
        }

        return null;
    }

    /**
     * Prints the details of all currently working drivers.
     *
     * For each driver in the map of currently working drivers, this method
     * invokes the driver's printDriver method to display their details,
     * including their name, status, and available licenses.
     */
    public void printAvailableDrivers() {
        try {
            Collection<Driver> allDrivers = currentDRepo.getAllDrivers();
            for(Driver driver: allDrivers){
                driver.printDriver();
            }
        }catch (SQLException e){
            System.out.println("There was a problem retrieving the drivers from the database!");
        }

}

    /**
     * Retrieves the count of currently working drivers in the system.
     *
     * @return the number of drivers currently working.
     */
    public int getDriverCount() {
        Collection<Driver> allDrivers = null;
        try {
            allDrivers = currentDRepo.getAllDrivers();
            return allDrivers.size();
        } catch (SQLException e) {
            System.out.println("There was a problem retrieving the drivers from the database!");
        }

        return 0;
    }

    public IDriverRepo getDriverRepo() {
        return this.currentDRepo;
    }

}
