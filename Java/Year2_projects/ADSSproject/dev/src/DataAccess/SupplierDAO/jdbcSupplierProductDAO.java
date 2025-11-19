package DataAccess.SupplierDAO;

import DTO.SupplierDTO.supplierProductDTO;
import Util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class jdbcSupplierProductDAO implements supplierProductDAO {

    @Override
    public void insertSupplierProduct(supplierProductDTO dto) throws SQLException {
        String sql = """
        INSERT INTO supplier_products(
            catalogID,
            productID,
            manufacturer,
            supplierID,
            agreementID,
            price,
            productType,
            productName,
            AgreementType,
            isActive
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.catalogID());
            ps.setInt(2, dto.productID());
            ps.setString(3, dto.manufacturer());
            ps.setInt(4, dto.supplierID());
            ps.setInt(5, dto.agreementID());
            ps.setDouble(6, dto.price());
            ps.setString(7, dto.productType());
            ps.setString(8, dto.productName());
            ps.setString(9, dto.agreementType());
            ps.setBoolean(10, true);
            ps.executeUpdate();
        }
    }

    @Override
    public List<supplierProductDTO> getProductsForSupplier(int supplierID) throws SQLException {
        List<supplierProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM supplier_products WHERE supplierID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    supplierProductDTO dto = new supplierProductDTO(
                            rs.getInt("catalogID"),
                            rs.getInt("productID"),
                            rs.getString("manufacturer"),
                            rs.getInt("supplierID"),
                            rs.getInt("agreementID"),
                            rs.getDouble("price"),
                            rs.getString("agreementType"),
                            rs.getString("productName"),
                            rs.getString("productType")
                    );
                    list.add(dto);
                }
            }
        }
        return list;
    }

    @Override
    public void updateSupplierProduct(supplierProductDTO dto) throws SQLException {
        String sql = "UPDATE supplier_products SET catalogID = ?, price = ?, productName = ?, manufacturer = ?, productType = ?, agreementID = ?, agreementType = ? WHERE supplierID = ? AND productID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.catalogID());
            ps.setDouble(2, dto.price());
            ps.setString(3, dto.productName());
            ps.setString(4, dto.manufacturer());
            ps.setString(5, dto.productType());
            ps.setInt(6, dto.agreementID());
            ps.setString(7, dto.agreementType());
            ps.setInt(8, dto.supplierID());
            ps.setInt(9, dto.productID());
            ps.executeUpdate();
        }
    }
    @Override
    public int getProductIdByName(String productName) {
        String sql = "SELECT productID FROM supplier_products WHERE productName = ? LIMIT 1";

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("productID");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch productID by name: " + e.getMessage());
        }

        return -1;
    }
    @Override
    public List<supplierProductDTO> getProductsByAgreementID(int agreementID) throws SQLException {
        List<supplierProductDTO> products = new ArrayList<>();

        String sql = """
            SELECT catalogID, productID, manufacturer, supplierID, agreementID,
                   price, AgreementType, productName, productType
            FROM supplier_products
            WHERE agreementID = ?
            """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, agreementID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    supplierProductDTO dto = new supplierProductDTO(
                            rs.getInt("catalogID"),
                            rs.getInt("productID"),
                            rs.getString("manufacturer"),
                            rs.getInt("supplierID"),
                            rs.getInt("agreementID"),
                            rs.getDouble("price"),
                            rs.getString("AgreementType"),
                            rs.getString("productName"),
                            rs.getString("productType")
                    );
                    products.add(dto);
                }
            }
        }

        return products;
    }


}
