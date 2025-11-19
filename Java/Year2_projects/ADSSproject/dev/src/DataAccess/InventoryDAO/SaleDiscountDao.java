package DataAccess.InventoryDAO;

import DTO.InventoryDTO.ProductDiscountDTO;
import DTO.InventoryDTO.CategoryDiscountDTO;
import DTO.InventoryDTO.SaleDiscountDTO;
import Util.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class SaleDiscountDao {

    // inserts a product discount into both SaleDiscounts and ProductDiscounts tables
    public void insertProductDiscount(ProductDiscountDTO dto) throws SQLException {
        insertSaleDiscount(dto);
        String query = "INSERT INTO ProductDiscounts(discountID, productID) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, dto.getDiscountID());
            ps.setInt(2, dto.getProductID());
            ps.executeUpdate();
        }
    }

    // inserts a category discount into both SaleDiscounts and CategoryDiscounts tables
    public void insertCategoryDiscount(CategoryDiscountDTO dto) throws SQLException {
        insertSaleDiscount(dto);
        String query = "INSERT INTO CategoryDiscounts(discountID, categoryName) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, dto.getDiscountID());
            ps.setString(2, dto.getCategoryName());
            ps.executeUpdate();
        }
    }

    // returns all discounts for a given product ID
    public List<ProductDiscountDTO> getDiscountsByProductID(int productID) throws SQLException {
        List<ProductDiscountDTO> discounts = new ArrayList<>();
        String query = "SELECT sd.*, pd.productID FROM SaleDiscounts sd " +
                       "JOIN ProductDiscounts pd ON sd.discountID = pd.discountID " +
                       "WHERE pd.productID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                discounts.add(extractProductDiscount(rs));
            }
        }
        return discounts;
    }

    // returns all discounts for a given category name
    public List<CategoryDiscountDTO> getDiscountsByCategoryName(String categoryName) throws SQLException {
        List<CategoryDiscountDTO> discounts = new ArrayList<>();
        String query = "SELECT sd.*, cd.categoryName FROM SaleDiscounts sd " +
                       "JOIN CategoryDiscounts cd ON sd.discountID = cd.discountID " +
                       "WHERE cd.categoryName = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, categoryName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                discounts.add(extractCategoryDiscount(rs));
            }
        }
        return discounts;
    }

    // returns the highest discount ID for counter initialization
    public int getMaxDiscountID() throws SQLException {
        String query = "SELECT MAX(discountID) FROM SaleDiscounts";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Helper: inserts into SaleDiscounts table, used internally by both insert methods
    private void insertSaleDiscount(SaleDiscountDTO dto) throws SQLException {
        String query = "INSERT INTO SaleDiscounts(discountID, rate, startDate, endDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, dto.getDiscountID());
            ps.setDouble(2, dto.getRate());
            ps.setString(3, dto.getStartDate().toString());
            ps.setString(4, dto.getEndDate().toString());
            ps.executeUpdate();
        }
    }

    // Helper: creates ProductDiscountDTO from rs
    private ProductDiscountDTO extractProductDiscount(ResultSet rs) throws SQLException {
        return new ProductDiscountDTO(
                rs.getInt("discountID"),
                rs.getDouble("rate"),
                LocalDate.parse(rs.getString("startDate")),
                LocalDate.parse(rs.getString("endDate")),
                rs.getInt("productID")
        );
    }

    // Helper: creates CategoryDiscountDTO from rs
    private CategoryDiscountDTO extractCategoryDiscount(ResultSet rs) throws SQLException {
        return new CategoryDiscountDTO(
                rs.getInt("discountID"),
                rs.getDouble("rate"),
                LocalDate.parse(rs.getString("startDate")),
                LocalDate.parse(rs.getString("endDate")),
                rs.getString("categoryName")
        );
    }
}
