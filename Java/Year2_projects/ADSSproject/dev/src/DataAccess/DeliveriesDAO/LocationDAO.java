package DataAccess.DeliveriesDAO;


import java.sql.*;
import java.util.*;
import DTO.DeliveriesDTO.LocationDTO;
import Domain.DeliveriesDomain.Address;
import Domain.DeliveriesDomain.District;
import Util.Database;

public class LocationDAO implements ILocationDAO {

    /**
     * Adds a new location to the database based on the provided {@code LocationDTO} object.
     * The method inserts information such as the location's street, house number, district,
     * contact name, and contact number. If the operation is successful, the generated
     * unique identifier for the new location is set in the provided {@code LocationDTO} object.
     *
     * @param location the {@code LocationDTO} object containing the details of the location
     *                 to be added. This includes the location's address details (street, house
     *                 number, district), contact name, and contact number.
     * @throws SQLException if a database access error occurs or the insertion fails.
     */
    @Override
    public void addLocation(LocationDTO location) throws SQLException {
        String sql = "INSERT INTO Locations (street, number, district, contact_name, contact_num) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Address address = location.getAddress();
            stmt.setString(1, address.getStreet());
            stmt.setInt(2, address.getHouseNum());
            stmt.setString(3, address.getDistrict().toString());
            stmt.setString(4, location.getContactName());
            stmt.setString(5, location.getContactNUM());
            stmt.executeUpdate();

            // Retrieve the generated ID and assign it back to the LocationDTO object
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    location.setID(generatedKeys.getInt(1));
                }
            }
        }
    }

    /**
     * Retrieves a location record from the database based on the provided ID and
     * returns it as a {@code LocationDTO} object. The method fetches details
     * including the location's ID, contact name, contact number, and address
     * information.
     *
     * @param id the unique identifier of the location to be retrieved
     * @return a {@code LocationDTO} object containing the location details
     *         if found, or {@code null} if no matching record exists
     * @throws SQLException if a database access error occurs or the query execution fails
     */
    @Override
    public LocationDTO getById(int id) throws SQLException {
        String sql = "SELECT * FROM Locations WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Address address = new Address(
                            District.valueOf(rs.getString("district")) ,
                            rs.getString("street"),
                            rs.getInt("number")
                    );
                    return new LocationDTO(
                            rs.getInt("id"),
                            rs.getString("contact_name"),
                            rs.getString("contact_num"),
                            address
                    );
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all location records from the database and returns them as a list of
     * {@code LocationDTO} objects. Each {@code LocationDTO} object includes the location's
     * unique identifier, contact name, contact number, and address details.
     *
     * @return a {@code List<LocationDTO>} containing all location records. Each location
     *         includes its ID, contact name, contact number, and address details.
     * @throws SQLException if a database access error occurs or the query execution fails.
     */
    @Override
    public List<LocationDTO> getAll() throws SQLException {
        List<LocationDTO> locations = new ArrayList<>();
        String sql = "SELECT * FROM Locations";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Address address = new Address(
                        District.valueOf(rs.getString("district")) ,
                        rs.getString("street"),
                        rs.getInt("number")
                );
                locations.add(new LocationDTO(
                        rs.getInt("id"),
                        rs.getString("contact_name"),
                        rs.getString("contact_num"),
                        address
                ));
            }
        }
        return locations;
    }

    /**
     * Updates an existing location record in the database with the details provided in the
     * given {@code LocationDTO} object. The method updates fields such as street, house number,
     * district, contact name, and contact number based on the object's data, using the location's ID
     * as the unique identifier for the database record.
     *
     * @param location the {@code LocationDTO} object containing the updated location data to
     *                 write to the database. This includes the location's address details
     *                 (street, house number, and district), contact name, contact number, and
     *                 ID, which is used to locate the record to be updated.
     * @throws SQLException if a database access error occurs or the update operation fails.
     */
    @Override
    public void update(LocationDTO location) throws SQLException {
        String sql = "UPDATE Locations SET street = ?, number = ?, district = ?, contact_name = ?, contact_num = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            Address address = location.getAddress();
            stmt.setString(1, address.getStreet());
            stmt.setInt(2, address.getHouseNum());
            stmt.setString(3, address.getDistrict().toString());
            stmt.setString(4, location.getContactName());
            stmt.setString(5, location.getContactNUM());
            stmt.setInt(6, location.getID());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a location from the database based on the provided ID.
     *
     * @param id the unique identifier of the location to be deleted
     * @throws SQLException if a database access error occurs or the SQL execution fails
     */
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Locations WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Finds a matching location in the database that corresponds to the details provided in the
     * input {@code LocationDTO} object. If a match is found, a LocationDTO object representing
     * the matching record is returned. If no match is found, {@code null} is returned.
     *
     * @param location the {@code LocationDTO} object containing the location details to match
     *                 against the database. The object must include details such as street,
     *                 house number, district, contact name, and contact number.
     * @return a {@code LocationDTO} object representing the matching location from the database,
     *         or {@code null} if no match is found.
     * @throws SQLException if a database access error occurs or the query execution fails.
     */
    @Override
    public LocationDTO findMatchingLocation(LocationDTO location) throws SQLException {
        String sql = "SELECT id, street, number, district, contact_name, contact_num " +
                "FROM Locations " +
                "WHERE street = ? AND number = ? AND district = ? AND contact_name = ? AND contact_num = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, location.getAddress().getStreet());
            stmt.setInt(2, location.getAddress().getHouseNum());
            stmt.setString(3, location.getAddress().getDistrict().toString());
            stmt.setString(4, location.getContactName());
            stmt.setString(5, location.getContactNUM());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Address toADD = new Address(District.valueOf(rs.getString("district")),rs.getString("street"),rs.getInt("number"));
                    return new LocationDTO(
                            rs.getInt("id"),
                            rs.getString("contact_name"),
                            rs.getString("contact_num"),
                            toADD
                    );
                }
                return null;
            }
        }
    }
}