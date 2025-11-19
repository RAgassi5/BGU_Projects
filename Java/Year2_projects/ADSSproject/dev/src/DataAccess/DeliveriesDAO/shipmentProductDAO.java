package DataAccess.DeliveriesDAO;

import DTO.DeliveriesDTO.shipmentProductDTO;
import Util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class shipmentProductDAO implements IProductDAO {

    @Override
    public shipmentProductDTO getProductByName(String name) throws SQLException {
        String sql = "SELECT order_items.name, order_items.quantity, Products.weightPerUnit " +
                "FROM order_items " +
                "JOIN Products ON order_items.productID = Products.productID " +
                "WHERE order_items.name = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new shipmentProductDTO(
                            rs.getString("name"),
                            rs.getInt("weightPerUnit"),
                            rs.getInt("quantity")
                    );
                }
            }
        }
        return null;
    }


    @Override
    public Collection<shipmentProductDTO> getAllProducts() throws SQLException {
        String sql = "SELECT order_items.name, order_items.quantity, Products.weightPerUnit " +
                "FROM order_items " +
                "JOIN Products ON order_items.productID = Products.productID";

        List<shipmentProductDTO> products = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                shipmentProductDTO product = new shipmentProductDTO(
                        rs.getString("name"),
                        rs.getInt("weightPerUnit"),
                        rs.getInt("quantity")
                );
                products.add(product);
            }
        }

        return products;
    }


    @Override
    public boolean productExists(String name) throws SQLException {
        String sql = "SELECT 1 FROM order_items WHERE name = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

}

