package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.DriverDTO;
import DataAccess.DeliveriesDAO.DriverDAO;
import DataAccess.DeliveriesDAO.IDriverDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

public class DriversRepo implements IDriverRepo {
    private Hashtable<Integer, Driver> currentDriversRepo;
    private IDriverDAO driverDAO;

    public DriversRepo() {
        currentDriversRepo = new Hashtable<>();
        driverDAO = new DriverDAO();
    }

    /**
     * Retrieves a driver by their unique identifier.
     * If the driver is already cached in the repository, it is returned directly.
     * If not, the method queries the data access object (DAO) to fetch the driver
     * from the database, creates a new Driver object, caches it in the repository,
     * and returns it.
     *
     * @param driverID The unique identifier of the driver to be retrieved.
     * @return The Driver object corresponding to the given ID, or {@code null} if
     *         no driver with the specified ID exists in the database.
     * @throws SQLException If a database access error occurs during the operation.
     */
    @Override
    public Driver getDriverByID(int driverID) throws SQLException {
        if (currentDriversRepo.containsKey(driverID)) {
            return currentDriversRepo.get(driverID);
        }
        DriverDTO fetchedDriver = driverDAO.findDriver(driverID);
        if (fetchedDriver == null) {
            return null;
        }
        Driver newInstance = new Driver(fetchedDriver);
        currentDriversRepo.put(driverID, newInstance);
        return newInstance;
    }

    /**
     * Adds a new driver to the repository and database.
     * This method creates a new DriverDTO object, adds it to the database through the DAO,
     * assigns the generated ID to the driver, and stores the driver in the repository.
     *
     * @param driverName The name of the driver to be added.
     * @throws SQLException If a database access error occurs during the operation.
     */
    @Override
    public void addDriver(String driverName) throws SQLException {
        DriverDTO newDriver = new DriverDTO(driverName, new String[0], "FREE", 0);
        int newID = this.driverDAO.addDriver(newDriver);
        newDriver.setDriverID(newID);
        Driver driverInstance = new Driver(newDriver);
        currentDriversRepo.put(driverInstance.getDriverId(), driverInstance);
        System.out.println(driverName + "'s assigned ID : " + newID);
    }

    /**
     * Removes a driver from the repository and database using the provided driver ID.
     *
     * @param driverID The unique identifier of the driver to be removed.
     * @throws SQLException If a database access error occurs during the removal.
     */
    @Override
    public void removeDriver(int driverID) throws SQLException {
        currentDriversRepo.remove(driverID);
        driverDAO.removeDriver(driverID);
    }

    /**
     * Updates the status of a driver identified by the provided driver ID.
     * If the driver with the specified ID exists, their status is updated
     * and the changes are saved using the data access object (DAO). If the
     * driver does not exist, no update is performed.
     *
     * @param DriverID The unique identifier of the driver whose status is to be updated.
     * @param status The new status to be set for the driver.
     * @throws SQLException If a database access error occurs during the operation.
     */
    @Override
    public void updateDriverStatus(int DriverID, driver_status status) throws SQLException {
        Driver driverToUpdate = getDriverByID(DriverID);
        if (driverToUpdate != null) {
            driverToUpdate.setDriverStatus(status);
            driverDAO.saveDriver(DriverID, new DriverDTO(driverToUpdate.getDriverName(), driverToUpdate.getAvailableLicenses(), driverToUpdate.getDriverStatus().toString(), driverToUpdate.getDriverId()));
        } else {
            System.out.println("There is no driver with the ID: " + DriverID + "");
        }

    }

    /**
     * Updates the driver's license by adding a new license to an existing driver.
     * If the driver with the provided ID does not exist, no update is performed.
     * The updated driver information is then saved using the data access object (DAO).
     *
     * @param driverID The unique identifier of the driver whose license is being updated.
     * @param license The new license to be added for the driver.
     * @throws SQLException If a database access error occurs during the operation.
     */
    @Override
    public void updateDriverLicense(int driverID, String license) throws SQLException {
        Driver driverToUpdate = getDriverByID(driverID);
        if (driverToUpdate != null) {
            driverToUpdate.addLicense(license);
            driverDAO.saveDriver(driverID, new DriverDTO(driverToUpdate.getDriverName(), driverToUpdate.getAvailableLicenses(), driverToUpdate.getDriverStatus().toString(), driverToUpdate.getDriverId()));

        } else {
            System.out.println("There is no driver with the ID: " + driverID);
        }
    }

    /**
     * Retrieves all drivers saved in the database and maps them to Driver objects.
     *
     * @return a collection of Driver objects representing the drivers stored in the database.
     * @throws SQLException if a database access error occurs during the retrieval of drivers.
     */
    @Override
    public Collection<Driver> getAllDrivers() throws SQLException {
        Collection<DriverDTO> allDriversInDB = this.driverDAO.getAllDrivers();
        Collection<Driver> allDriversToReturn = new ArrayList<>();
        for (DriverDTO driverDTO : allDriversInDB) {
            Driver newInstance = new Driver(driverDTO);
            allDriversToReturn.add(newInstance);
        }
        return allDriversToReturn;
    }

    /**
     * Determines if a driver possesses a specified type of license.
     *
     * @param driverId The unique identifier of the driver.
     * @param licenseType The type of the license to be checked.
     * @return {@code true} if the driver has the specified license, {@code false} otherwise.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public boolean hasLicense(int driverId, String licenseType) throws SQLException {
        return driverDAO.hasLicense(driverId, licenseType);
    }
}
