package Domain.DeliveriesDomain;

import java.sql.SQLException;
import java.util.Collection;

public interface IDriverRepo {
    Driver getDriverByID(int driverID) throws SQLException;
    void addDriver(String driverName) throws SQLException;
    void removeDriver(int driverID) throws SQLException;
    void updateDriverStatus(int DriverID, driver_status status) throws SQLException;
    void updateDriverLicense(int DriverID, String license) throws SQLException;
    Collection<Driver> getAllDrivers() throws SQLException;
    boolean hasLicense(int driverId, String licenseType) throws SQLException;
}
