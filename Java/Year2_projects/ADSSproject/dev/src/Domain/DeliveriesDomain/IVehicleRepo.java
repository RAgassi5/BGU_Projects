package Domain.DeliveriesDomain;

import java.sql.SQLException;
import java.util.Collection;

public interface IVehicleRepo {
    void addVehicle(Vehicle currentVehicle) throws SQLException;
    void removeVehicle(int licenseNum) throws SQLException;
    Collection<Vehicle> getAllVehicles() throws SQLException;
    boolean vehicleExists(int licenseNum) throws SQLException;
    void updateVehicleStatus(int vehicleId, vehicle_status status) throws SQLException;
    Vehicle getVehicleByID(int licenseNum) throws SQLException;


}
