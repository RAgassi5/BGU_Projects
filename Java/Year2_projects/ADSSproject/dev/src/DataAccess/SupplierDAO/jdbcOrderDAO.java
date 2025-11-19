package DataAccess.SupplierDAO;

import DTO.SupplierDTO.orderDTO;
import DTO.SupplierDTO.orderItemDTO;
import Util.Database;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class jdbcOrderDAO implements orderDAO {

    @Override
    public int insertOrder(int supplierID, int contactID) throws SQLException {
        String sql = "INSERT INTO orders(orderDate, orderRecDate, supplierID, orderStatus, totalPrice, orderType, contactID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, LocalDate.now().toString());
            ps.setNull(2, java.sql.Types.DATE);
            ps.setInt(3, supplierID);
            ps.setBoolean(4, false);
            ps.setDouble(5, 0);
            ps.setString(6, null);
            ps.setInt(7, contactID);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public void updateOrder(orderDTO dto) throws SQLException {
        String sql = "UPDATE orders SET orderDate = ?, orderRecDate = ?, orderStatus = ?, totalPrice = ?, orderType = ?, address = ?, contactID = ? WHERE orderID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, convertDate(dto.orderDate()).toString());
            ps.setString(2, convertDate(dto.orderRecDate()).toString());
            ps.setBoolean(3, dto.orderStatus());
            ps.setDouble(4, dto.totalPrice());
            ps.setString(5, dto.orderType());
            ps.setString(6, dto.address());
            ps.setInt(7, dto.contactID());
            ps.setInt(8, dto.orderID());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteOrder(int orderID) throws SQLException {
        String deleteItemsSQL = "DELETE FROM order_items WHERE orderID = ?";
        String deleteOrderSQL = "DELETE FROM orders WHERE orderID = ?";
        try (Connection conn = Database.getConnection()) {
            try (PreparedStatement psItems = conn.prepareStatement(deleteItemsSQL);
                 PreparedStatement psOrder = conn.prepareStatement(deleteOrderSQL)) {
                psItems.setInt(1, orderID);
                psItems.executeUpdate();

                psOrder.setInt(1, orderID);
                psOrder.executeUpdate();
            }
        }
    }

    @Override
    public void insertOrderItem(orderItemDTO dto) throws SQLException {
        String sql = "INSERT INTO order_items(orderID, productID, quantity, price, name) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.orderID());
            ps.setInt(2, dto.productID());
            ps.setInt(3, dto.quantity());
            ps.setDouble(4, dto.price());
            ps.setString(5, dto.name());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateOrderItem(orderItemDTO dto) throws SQLException {
        String sql = "UPDATE order_items SET quantity = ?, price = ?, name = ? WHERE orderID = ? AND productID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.quantity());
            ps.setDouble(2, dto.price());
            ps.setString(3, dto.name());
            ps.setInt(4, dto.orderID());
            ps.setInt(5, dto.productID());
            ps.executeUpdate();
        }
    }

    @Override
    public int deleteOrderItem(int orderID, int productID) throws SQLException {
        String selectSql = "SELECT quantity FROM order_items WHERE orderID = ? AND productID = ?";
        String deleteSql = "DELETE FROM order_items WHERE orderID = ? AND productID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement selectPs = conn.prepareStatement(selectSql);
             PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {

            // שליפת הכמות לפני המחיקה
            selectPs.setInt(1, orderID);
            selectPs.setInt(2, productID);

            int quantity = 0;
            try (ResultSet rs = selectPs.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getInt("quantity");
                } else {
                    // לא נמצאה רשומה למחיקה
                    return 0;
                }
            }

            // ביצוע המחיקה
            deletePs.setInt(1, orderID);
            deletePs.setInt(2, productID);
            deletePs.executeUpdate();

            return quantity;
        }
    }

    @Override
    public int getMaxOrderID() throws SQLException {
        String sql = "SELECT MAX(orderID) FROM orders";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public orderDTO getOrderById(int orderID) throws SQLException {
        String sql = "SELECT * FROM orders WHERE orderID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int[] orderDate = parseTimestamp(rs.getLong("orderDate"));
                    int[] recDate = parseTimestamp(rs.getLong("orderRecDate"));
                    return new orderDTO(
                            orderID,
                            orderDate,
                            recDate,
                            rs.getInt("supplierID"),
                            rs.getBoolean("orderStatus"),
                            rs.getDouble("totalPrice"),
                            rs.getString("orderType"),
                            rs.getString("address"),
                            rs.getInt("contactID")
                    );
                }
            }
        }
        return null;
    }

    // מפרש timestamp
    private int[] parseTimestamp(long millis) {
        if (millis == 0) return null;
        LocalDate date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
        return new int[]{date.getYear(), date.getMonthValue(), date.getDayOfMonth()};
    }


    @Override
    public List<orderItemDTO> getOrderItemsByOrderID(int orderID) throws SQLException {
        List<orderItemDTO> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE orderID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new orderItemDTO(
                            rs.getInt("orderID"),
                            rs.getInt("productID"),
                            rs.getInt("quantity"),
                            rs.getInt("price"),
                            rs.getString("name")
                    ));
                }
            }
        }
        return items;
    }


    private Date convertDate(int[] arr) {
        return Date.valueOf(arr[0] + "-" + arr[1] + "-" + arr[2]);
    }

    @Override
    public int getContactIDForOrder(int supplierID) throws SQLException {
        String sql = "SELECT contactID FROM contacts WHERE supplierID = ? LIMIT 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, supplierID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("contactID");
                }
            }
        }

        throw new SQLException("Contact not found for supplierID: " + supplierID);
    }

    public boolean hasFixedDeliveryTomorrow() throws SQLException {
        String sql = """
        SELECT EXISTS (
            SELECT 1
            FROM orders
            WHERE orderType = 'FixedDelivery'
              AND orderDate = ?
        )
    """;

        String today = LocalDate.now().toString(); // תאריך היום בפורמט "YYYY-MM-DD"

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, today);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 1;
            }
        }
    }
}

