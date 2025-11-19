package DataAccess.SupplierDAO;

import DTO.SupplierDTO.supplierDTO;
import Util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class jdbcSupplierDAO implements supplierDAO {

    @Override
    public supplierDTO getSupplier(int supplierID) throws SQLException {
        String sql = "SELECT * FROM suppliers WHERE supplierID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new supplierDTO(
                            rs.getInt("supplierID"),
                            rs.getString("supplierName"),
                            rs.getString("bankAccountNumber"),
                            rs.getBoolean("isActive")
                    );
                }
            }
        }
        return null;
    }
    @Override
    public int insertSupplier(supplierDTO supplier) throws SQLException {
        String sql = "INSERT INTO suppliers(supplierName, bankAccountNumber, isActive) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, supplier.supplierName());
            ps.setString(2, supplier.bankAccountNumber());
            ps.setBoolean(3, supplier.isActive());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // מחזיר את ה-supplierID שנוצר
                } else {
                    throw new SQLException("Inserting supplier failed, no ID obtained.");
                }
            }
        }
    }

    public void updateSupplier(supplierDTO supplier) throws SQLException {
        String sql = "UPDATE suppliers SET supplierName = ?, bankAccountNumber = ? WHERE supplierID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplier.supplierName());
            ps.setString(2, supplier.bankAccountNumber());
            ps.setInt(3, supplier.supplierID());
            ps.executeUpdate();
        }
    }
    @Override
    public int insertContact(int supplierID, String name, String phone) throws SQLException {
        String sql = "INSERT INTO contacts(supplierID, name, phone) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, supplierID);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // מחזיר את ה-id שנוצר
                } else {
                    throw new SQLException("No ID returned after insertContact");
                }
            }
        }
    }

    @Override
    public void insertProductType(int supplierID, String productType) throws SQLException {
        if (!productTypeExists(supplierID, productType)) {
            String sql = "INSERT INTO supplier_product_types(supplierID, productType) VALUES (?, ?)";
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, supplierID);
                ps.setString(2, productType);
                ps.executeUpdate();
            }

        }
    }

    @Override
    public void insertManufacturer(int supplierID, String manufacturer) throws SQLException {
        if (!manufacturerExists(supplierID, manufacturer)) {
            String sql = "INSERT INTO supplier_manufacturers(supplierID, manufacturerName) VALUES (?, ?)";
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, supplierID);
                ps.setString(2, manufacturer);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public List<String> getSupplierProductTypes(int supplierID) throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT productType FROM supplier_product_types WHERE supplierID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("productType"));
                }
            }
        }
        return list;
    }

    @Override
    public List<String> getmanufacturersBySupplier(int supplierID) throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT manufacturerName FROM supplier_manufacturers WHERE supplierID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("manufacturerName"));
                }
            }
        }
        return list;
    }

    private boolean productTypeExists(int supplierID, String productType) throws SQLException {
        String sql = "SELECT 1 FROM supplier_product_types WHERE supplierID = ? AND productType = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierID);
            ps.setString(2, productType);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean manufacturerExists(int supplierID, String manufacturer) throws SQLException {
        String sql = "SELECT 1 FROM supplier_manufacturers WHERE supplierID = ? AND manufacturerName = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierID);
            ps.setString(2, manufacturer);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<supplierDTO> getActiveSuppliers() throws SQLException {
        List<supplierDTO> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers WHERE isActive = 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                suppliers.add(new supplierDTO(
                        rs.getInt("supplierID"),
                        rs.getString("supplierName"),
                        rs.getString("bankAccountNumber"),
                        rs.getBoolean("isActive")
                ));
            }
        }

        return suppliers;
    }



}
