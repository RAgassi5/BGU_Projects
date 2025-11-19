package DataAccess.DeliveriesDAO;

import DTO.DeliveriesDTO.DriverDTO;

import java.sql.SQLException;
import java.util.Collection;


public interface IDriverDAO {
    int addDriver(DriverDTO toAdd) throws SQLException;
    void removeDriver(int DriverID) throws SQLException;
    void saveDriver(int DriverID, DriverDTO updateDriver) throws  SQLException;
    DriverDTO findDriver(int DriverID) throws  SQLException;
    Collection<DriverDTO> getAllDrivers() throws SQLException;
    boolean hasLicense(int driverId, String licenseType) throws SQLException;



}
