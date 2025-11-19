package DataAccess.InventoryDAO;

import DTO.InventoryDTO.UnitDTO;
import Util.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UnitDao {
    //returns all units in the db as unitDTO list.
    public List<UnitDTO> getAllUnits() throws SQLException {
        List<UnitDTO> units = new ArrayList<>();
        String query = "SELECT * FROM Units";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                units.add(extractUnitFromResultSet(rs));
            }
        }
        return units;
    }

    //returns all units of a specific product in the db as unitDTO list.
    public List<UnitDTO> getUnitsByProductID(int productID) throws SQLException {
        List<UnitDTO> units = new ArrayList<>();
        String query = "SELECT * FROM Units WHERE productID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, productID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    units.add(extractUnitFromResultSet(rs));
                }
            }
        }
        return units;
    }

    //returns a specific unit by UnitID
    public UnitDTO getUnitByID(int unitID) throws SQLException {
        String query = "SELECT * FROM Units WHERE unitID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, unitID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractUnitFromResultSet(rs);
            }
        }
        return null;
    }

    //insert new unit to the db
    public void insertUnit(UnitDTO unit) throws SQLException {
        String query = """
            INSERT INTO Units(unitID, productID, location, expiryDate, isDefective)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, unit.getUnitID());
            ps.setInt(2, unit.getProductID());
            ps.setString(3, unit.getLocation());
            ps.setString(4, unit.getExpiryDate().toString());
            ps.setBoolean(5, unit.isDefective());

            ps.executeUpdate();
        }
    }

    //updates a unit in the DB
    public void updateUnit(UnitDTO unit) throws SQLException {
        String query = """
            UPDATE Units SET
                productID = ?, location = ?, expiryDate = ?, isDefective = ?
            WHERE unitID = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, unit.getProductID());
            ps.setString(2, unit.getLocation());
            ps.setString(3, unit.getExpiryDate().toString());
            ps.setBoolean(4, unit.isDefective());
            ps.setInt(5, unit.getUnitID());

            ps.executeUpdate();
        }
    }

    public void removeUnit(int unitID) throws SQLException {
        String query = "DELETE FROM Units WHERE unitID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, unitID);
            ps.executeUpdate();
        }
    }

    public int getMaxUnitID() throws SQLException {
        String query = "SELECT MAX(unitID) FROM Units";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                return rs.getInt(1);
            else
                return 0;
        }
    }

    // Helper: creates unitDTO from rs.
    private UnitDTO extractUnitFromResultSet(ResultSet rs) throws SQLException {
        return new UnitDTO(
                rs.getInt("unitID"),
                rs.getInt("productID"),
                rs.getString("location"),
                LocalDate.parse(rs.getString("expiryDate")),
                rs.getBoolean("isDefective")
        );
    }
}
