package DataAccess.DeliveriesDAO;

import DTO.DeliveriesDTO.LocationDTO;
import DTO.DeliveriesDTO.shipmentProductDTO;
import DTO.DeliveriesDTO.ShipmentDTO;
import Util.Database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShipmentDAO implements IShipmentDAO {
    private final LocationDAO locationDAO;

    public ShipmentDAO() {
        this.locationDAO = new LocationDAO();
    }

    @Override
    public int addShippedShipment(ShipmentDTO shipment) throws SQLException {
        return addShipment(shipment, "PENDING");
    }

    @Override
    public int addSelfDeliveredShipment(ShipmentDTO shipment) throws SQLException {
        return addShipment(shipment, "SELF_DELIVERED");
    }

    private int addShipment(ShipmentDTO shipment, String status) throws SQLException {
        int idToReturn = -1;
        String shipmentSql = "INSERT INTO Shipments (origin, destination, day, month, year, hour, mins, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String productSql = "INSERT INTO ShipmentsProducts (product_name, product_weight, product_amount, shipment_id) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);
            try {

                // Insert shipment
                try (PreparedStatement stmt = conn.prepareStatement(shipmentSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, shipment.getOrigin().getID());
                    stmt.setInt(2, shipment.getDestination().getID());
                    LocalDate date = shipment.getShipmentDate();
                    LocalTime time = shipment.getShipmentTime();

                    stmt.setInt(3, date.getDayOfMonth());
                    stmt.setInt(4, date.getMonthValue());
                    stmt.setInt(5, date.getYear());

                    if (time != null) {
                        stmt.setInt(6, time.getHour());
                        stmt.setInt(7, time.getMinute());
                    } else {
                        stmt.setNull(6, Types.INTEGER);
                        stmt.setNull(7, Types.INTEGER);
                    }

                    stmt.setString(8, status);
                    stmt.executeUpdate();

                    // Get generated shipment ID
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            shipment.setShipmentID(rs.getInt(1));
                            idToReturn = shipment.getShipmentID();
                        } else {
                            throw new SQLException("Failed to get shipment ID");
                        }
                    }
                }

                // Insert products
                try (PreparedStatement stmt = conn.prepareStatement(productSql)) {
                    for (shipmentProductDTO product : shipment.getProductsInShipment()) {
                        stmt.setString(1, product.getProductName());
                        stmt.setInt(2, product.getWeightPerUnit());
                        stmt.setInt(3, product.getAmount());
                        stmt.setInt(4, shipment.getShipmentID());
                        stmt.executeUpdate();
                    }
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
        return idToReturn;
    }

    @Override
    public ShipmentDTO getShipmentById(int id) throws SQLException {
        String shipmentSql = "SELECT * FROM Shipments WHERE id = ?";
        String productsSql = "SELECT * FROM ShipmentsProducts WHERE shipment_id = ?";
        ShipmentDTO shipment = null;

        try (Connection conn = Database.getConnection();
             PreparedStatement shipmentStmt = conn.prepareStatement(shipmentSql)) {
            shipmentStmt.setInt(1, id);
            try (ResultSet rs = shipmentStmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                int shipmentId = rs.getInt("id");
                int originId = rs.getInt("origin");
                int destinationId = rs.getInt("destination");
                int day = rs.getInt("day");
                int month = rs.getInt("month");
                int year = rs.getInt("year");
                LocalDate date = LocalDate.of(year, month, day);
                int rawHour = rs.getInt("hour");
                Integer hour = rs.wasNull() ? null : rawHour;
                int rawMins = rs.getInt("mins");
                Integer mins = rs.wasNull() ? null : rawMins;
                LocalTime time = (hour != null && mins != null) ? LocalTime.of(hour, mins) : null;
                String status = rs.getString("status");

                shipment = new ShipmentDTO(shipmentId, null, null, date, time, status);


                LocationDTO origin = locationDAO.getById(originId);
                LocationDTO destination = locationDAO.getById(destinationId);
                shipment.setOrigin(origin);
                shipment.setDestination(destination);
            }
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement productsStmt = conn.prepareStatement(productsSql)) {
            productsStmt.setInt(1, id);
            try (ResultSet productsRs = productsStmt.executeQuery()) {
                while (productsRs.next()) {
                    shipmentProductDTO product = new shipmentProductDTO(
                            productsRs.getString("product_name"),
                            productsRs.getInt("product_weight"),
                            productsRs.getInt("product_amount")
                    );
                    shipment.addProductToShipment(product);
                }
            }
        }

        return shipment;

    }

    @Override
    public void updateShipmentTime(int shipmentId, LocalTime time) throws SQLException {
        String sql = "UPDATE Shipments SET hour = ?, mins = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, time.getHour());
            stmt.setInt(2, time.getMinute());
            stmt.setInt(3, shipmentId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Shipment update failed, no rows affected. ID: " + shipmentId);
            }
        }
    }

    @Override
    public void save(ShipmentDTO shipment) throws SQLException {
        String updateShipmentSql = "UPDATE Shipments SET status = ?, hour = ?, mins = ? WHERE id = ?";
        String deleteProductsSql = "DELETE FROM ShipmentsProducts WHERE shipment_id = ?";
        String insertProductsSql = "INSERT INTO ShipmentsProducts (product_name, product_weight, product_amount, shipment_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Update shipment details
                try (PreparedStatement stmt = conn.prepareStatement(updateShipmentSql)) {
                    stmt.setString(1, shipment.getShipmentStatus());
                    LocalTime time = shipment.getShipmentTime();
                    if (time != null) {
                        stmt.setInt(2, time.getHour());
                        stmt.setInt(3, time.getMinute());
                    } else {
                        stmt.setNull(2, Types.INTEGER);
                        stmt.setNull(3, Types.INTEGER);
                    }
                    stmt.setInt(4, shipment.getShipmentID());
                    stmt.executeUpdate();
                }

                // Delete existing products
                try (PreparedStatement stmt = conn.prepareStatement(deleteProductsSql)) {
                    stmt.setInt(1, shipment.getShipmentID());
                    stmt.executeUpdate();
                }

                // Insert updated products
                if (!shipment.getProductsInShipment().isEmpty()) {
                    try (PreparedStatement stmt = conn.prepareStatement(insertProductsSql)) {
                        for (shipmentProductDTO product : shipment.getProductsInShipment()) {
                            stmt.setString(1, product.getProductName());
                            stmt.setInt(2, product.getWeightPerUnit());
                            stmt.setInt(3, product.getAmount());
                            stmt.setInt(4, shipment.getShipmentID());
                            stmt.executeUpdate();
                        }
                    }
                }

                conn.commit();
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    // Combine both exceptions
                    e.addSuppressed(rollbackEx);
                }
                throw e;
            }
        }
    }
}




