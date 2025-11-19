package DataAccess.DeliveriesDAO;

import DTO.DeliveriesDTO.VehicleDTO;

import java.sql.SQLException;
import java.util.Collection;

public interface IVehicleDAO {
    void addVehicle(VehicleDTO vehicle) throws SQLException;

    void removeVehicle(int vehicleID) throws SQLException;

    Collection<VehicleDTO> getAllVehicles() throws SQLException;

    boolean vehicleExists(int licenseNum) throws SQLException;

    void updateVehicleStatus(int licenseNum, VehicleDTO vehicle) throws SQLException;

    VehicleDTO findVehicle(int licenseNum) throws SQLException;
}
