package DataAccess.DeliveriesDAO;

import DTO.DeliveriesDTO.VehicleDTO;
import Util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class VehicleDAO implements IVehicleDAO {

    /**
     * Adds a new vehicle record to the database using the details provided in the VehicleDTO object.
     *
     * @param vehicle the VehicleDTO object containing details of the vehicle to be added.
     *                Must not be null. The object should include license number, capacity,
     *                maximum capacity, vehicle status, and vehicle type.
     * @throws SQLException if a database access error occurs or the insertion fails.
     * @throws IllegalArgumentException if the provided vehicle is null.
     */
    @Override
    public void addVehicle(VehicleDTO vehicle) throws SQLException {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        String sql = "INSERT INTO Vehicles (license_num, vehicle_weight, max_vehicle_weight, vehicle_status, vehicle_type) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicle.getLicenseNum());
            stmt.setInt(2, vehicle.getCapacity());
            stmt.setInt(3, vehicle.getMaxCapacity());
            stmt.setString(4, vehicle.getVStaus());
            stmt.setString(5, vehicle.getVehicleType());



            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to create vehicle record");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage()); // Debug line
            throw e;


        }
    }
    /**
     * Removes a vehicle record from the database based on its unique license number.
     *
     * @param vehicleID the license number of the vehicle to be removed. Must be valid and existing
     *                  in the database.
     * @throws SQLException if a database access error occurs or if no vehicle with the provided
     *                       license number is found.
     */
    @Override
    public void removeVehicle(int vehicleID) throws SQLException {
        String sql = "DELETE FROM Vehicles WHERE license_num = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleID);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Vehicle with license number " + vehicleID + " not found");
            }
        }

    }

    /**
     * Retrieves all vehicle records from the database and returns them as a collection of VehicleDTO objects.
     *
     * @return a collection of VehicleDTO objects representing all vehicles stored in the database.
     * @throws SQLException if a database access error occurs during the retrieval process.
     */
    @Override
    public Collection<VehicleDTO> getAllVehicles() throws SQLException {
        Collection<VehicleDTO> vehicles = new ArrayList<>();
        String sql = "SELECT license_num, vehicle_weight, max_vehicle_weight, vehicle_status, vehicle_type FROM Vehicles";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VehicleDTO vehicle = new VehicleDTO(
                        rs.getInt("license_num"),
                        rs.getInt("vehicle_weight"),
                        rs.getInt("max_vehicle_weight"),
                        rs.getString("vehicle_status"),
                        rs.getString("vehicle_type")
                );
                vehicles.add(vehicle);
            }
        }

        return vehicles;

    }

    /**
     * Checks if a vehicle with the given license number exists in the database.
     *
     * @param licenseNum the license number of the vehicle to check.
     * @return true if the vehicle exists in the database, false otherwise.
     * @throws SQLException if a database access error occurs during the operation.
     */
    public boolean vehicleExists(int licenseNum) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Vehicles WHERE license_num = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, licenseNum);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    /**
     * Updates the status and weight-related attributes of a vehicle in the database based
     * on the provided license number and VehicleDTO object.
     *
     * @param licenseNum the license number of the vehicle to be updated. Must be valid
     *                   and existing in the database.
     * @param vehicle    the VehicleDTO object containing the updated vehicle status, current
     *                   weight, and maximum weight of the vehicle. Must not be null.
     * @throws SQLException if a database access error occurs or if the update fails to affect
     *                      any rows in the database.
     */
    @Override
    public void updateVehicleStatus(int licenseNum, VehicleDTO vehicle) throws SQLException {
        String sql = "UPDATE Vehicles SET vehicle_status = ?, vehicle_weight = ?, max_vehicle_weight = ? WHERE license_num = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getVStaus());
            stmt.setInt(2, vehicle.getCapacity());
            stmt.setInt(3, vehicle.getMaxCapacity());
            stmt.setInt(4, licenseNum);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Vehicle update failed, no rows affected.");
            }
        }

    }

    /**
     * Retrieves a vehicle record from the database based on its unique license number.
     *
     * @param licenseNum the license number of the vehicle to be retrieved. Must correspond to
     *                   a valid record in the database.
     * @return a VehicleDTO object containing the details of the vehicle if found, or null if no
     *         vehicle with the specified license number exists.
     * @throws SQLException if a database access error occurs during the operation.
     */
    @Override
    public VehicleDTO findVehicle(int licenseNum) throws SQLException {
        String sql = "SELECT license_num, vehicle_weight, max_vehicle_weight, vehicle_status, vehicle_type FROM Vehicles WHERE license_num = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, licenseNum);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new VehicleDTO(
                            rs.getInt("license_num"),
                            rs.getInt("vehicle_weight"),
                            rs.getInt("max_vehicle_weight"),
                            rs.getString("vehicle_status"),
                            rs.getString("vehicle_type")
                    );
                }
                return null;
            }
        }
    }


}
