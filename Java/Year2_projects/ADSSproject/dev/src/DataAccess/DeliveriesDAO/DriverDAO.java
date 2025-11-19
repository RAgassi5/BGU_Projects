package DataAccess.DeliveriesDAO;

import DTO.DeliveriesDTO.DriverDTO;
import Util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class DriverDAO implements IDriverDAO {


    /**
     * Adds a new driver and their licenses to the database.
     *
     * @param toAdd the DriverDTO object containing driver details such as
     *              name, status, and associated licenses
     * @return the unique ID of the newly added driver
     * @throws SQLException if a database access error occurs or the operation
     *                      cannot be completed
     */
    @Override
    public int addDriver(DriverDTO toAdd) throws SQLException {
        String insertDriverQuery = "INSERT INTO drivers (name, status) VALUES (?, ?)";
        String insertLicenseQuery = "INSERT INTO licenses (license_type, driver_id) VALUES (UPPER(?), ?)";

        int driverID = -1;
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            // Add the driver
            try (PreparedStatement psDriver = conn.prepareStatement(insertDriverQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psDriver.setString(1, toAdd.getDriverName());
                psDriver.setString(2, toAdd.getStatus());
                psDriver.executeUpdate();

                // Get the generated driver ID
                ResultSet rs = psDriver.getGeneratedKeys();
                if (rs.next()) {
                    driverID = rs.getInt(1);

                    // Add licenses for the driver
                    try (PreparedStatement psLicense = conn.prepareStatement(insertLicenseQuery)) {
                        for (String license : toAdd.getLicenses()) {
                            psLicense.setString(1, license);
                            psLicense.setInt(2, driverID);
                            psLicense.executeUpdate();
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
        return driverID;
    }

    /**
     * Removes a driver from the database given their unique identifier.
     *
     * @param driverID the unique identifier of the driver to be removed
     * @throws SQLException if a database access error occurs or the operation cannot be completed
     */
    @Override
    public void removeDriver(int driverID) throws SQLException {
        String deleteDriverQuery = "DELETE FROM drivers WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(deleteDriverQuery)) {
            ps.setInt(1, driverID);
            ps.executeUpdate();
        }


    }

    /**
     * Updates the details of an existing driver in the database, including their
     * information and associated licenses. This method ensures transactional
     * consistency and rolls back changes in case of any failure.
     *
     * @param driverID the unique identifier of the driver to be updated
     * @param updatedDriver a DriverDTO object containing the updated driver details,
     *                      such as name, status, and licenses
     * @throws SQLException if a database access error occurs or the operation cannot be completed
     */
    @Override
    public void saveDriver(int driverID, DriverDTO updatedDriver) throws SQLException {
        String updateDriverQuery = "UPDATE drivers SET name = ?, status = ? WHERE id = ?";
        String deleteLicensesQuery = "DELETE FROM licenses WHERE driver_id = ?";
        String insertLicensesQuery = "INSERT INTO licenses (license_type, driver_id) VALUES (UPPER(?), ?)";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            // Update driver information
            try (PreparedStatement psUpdateDriver = conn.prepareStatement(updateDriverQuery)) {
                psUpdateDriver.setString(1, updatedDriver.getDriverName());
                psUpdateDriver.setString(2, updatedDriver.getStatus());
                psUpdateDriver.setInt(3, driverID);
                psUpdateDriver.executeUpdate();
            }

            // Update the licenses
            try {
                // delete all existing licenses for the driver
                try (PreparedStatement psDeleteLicenses = conn.prepareStatement(deleteLicensesQuery)) {
                    psDeleteLicenses.setInt(1, driverID);
                    psDeleteLicenses.executeUpdate();
                }

                // add the updated licenses
                try (PreparedStatement psInsertLicenses = conn.prepareStatement(insertLicensesQuery)) {
                    Set<String> uniqueLicenses = new HashSet<>(List.of(updatedDriver.getLicenses()));
                    for (String license : uniqueLicenses) {
                        psInsertLicenses.setString(1, license);
                        psInsertLicenses.setInt(2, driverID);
                        psInsertLicenses.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }


    /**
     * Retrieves the details of a specific driver from the database, including
     * their name, status, and associated licenses.
     *
     * @param driverID the unique identifier of the driver to be retrieved
     * @return a DriverDTO object containing the driver's details, or null if no
     *         driver with the specified ID is found
     * @throws SQLException if a database access error occurs or the operation
     *                      cannot be completed
     */
    @Override
    public DriverDTO findDriver(int driverID) throws SQLException {
        String selectDriverQuery = "SELECT id AS driver_id,name AS driver_name, status FROM drivers WHERE driver_id = ?";
        String selectLicensesQuery = "SELECT license_type FROM licenses WHERE driver_id = ?";

        try (Connection conn = Database.getConnection()) {
            String driverName = null;
            String status = null;

            try (PreparedStatement psDriver = conn.prepareStatement(selectDriverQuery)) {
                psDriver.setInt(1, driverID);
                ResultSet rs = psDriver.executeQuery();
                if (rs.next()) {
                    driverName = rs.getString("driver_name");
                    status = rs.getString("status");
                }
            }

            List<String> licenses = new ArrayList<>();
            try (PreparedStatement psLicenses = conn.prepareStatement(selectLicensesQuery)) {
                psLicenses.setInt(1, driverID);
                ResultSet rsLicenses = psLicenses.executeQuery();
                while (rsLicenses.next()) {
                    licenses.add(rsLicenses.getString("license_type"));
                }
            }

            if (driverName != null) {
                return new DriverDTO(driverName, licenses.toArray(new String[0]), status, driverID);
            }
        }
        return null;

    }

    /**
     * Retrieves a collection of all drivers from the database, including their
     * details such as name, status, and associated licenses. This method fetches
     * data by executing a SQL query and maps the results into a collection of
     * DriverDTO objects.
     *
     * @return a collection of DriverDTO objects representing all drivers in the
     *         database, with each object containing the driver's details such as
     *         name, licenses, status, and ID
     * @throws SQLException if a database access error occurs or the SQL operation
     *                      cannot be completed
     */
    @Override
    public Collection<DriverDTO> getAllDrivers() throws SQLException {
        String query = "SELECT d.id AS driver_id, d.name AS driver_name, d.status, l.license_type " +
                "FROM drivers d " +
                "LEFT JOIN licenses l ON d.id = l.driver_id " +
                "ORDER BY d.id";

        Map<Integer, DriverDTO> driversMap = new LinkedHashMap<>();

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int driverId = rs.getInt("driver_id");
                String driverName = rs.getString("driver_name");
                String status = rs.getString("status");
                String license = rs.getString("license_type");

                driversMap.putIfAbsent(driverId, new DriverDTO(driverName, new String[0], status, driverId));

                if (license != null) {
                    DriverDTO driver = driversMap.get(driverId);
                    List<String> licenses = new ArrayList<>(Arrays.asList(driver.getLicenses()));
                    licenses.add(license);
                    driver.setLicenses(licenses.toArray(new String[0]));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return driversMap.values();

    }
    /**
     * Checks if a driver has a specific type of license.
     *
     * @param driverId the unique identifier of the driver
     * @param licenseType the type of license to check for
     * @return true if the driver has the specified license, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean hasLicense(int driverId, String licenseType) throws SQLException {
        String sql = "SELECT 1 FROM Licenses WHERE driver_id = ? AND license_type = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, driverId);
            pstmt.setString(2, licenseType);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // true if a match exists
        }
    }

}