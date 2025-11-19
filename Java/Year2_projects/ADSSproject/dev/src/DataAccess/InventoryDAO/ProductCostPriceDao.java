package DataAccess.InventoryDAO;


import DTO.InventoryDTO.ProductCostPriceDTO;
import Util.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductCostPriceDao {
    //returns all cost prices for a specific product
    public List<ProductCostPriceDTO> getCPbyPID(int productID) throws SQLException {
        List<ProductCostPriceDTO> prices = new ArrayList<>();
        String query = "SELECT * FROM ProductCostPrices WHERE productID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, productID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                prices.add(extractCPFromResultSet(rs));
            }
        }
        return prices;
    }

    //inserts a new cost price record into the database
    public void insertCP(ProductCostPriceDTO costPrice) throws SQLException {
        String query = """
            INSERT INTO ProductCostPrices(productID, startDate, costPrice)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, costPrice.getProductID());
            ps.setString(2, costPrice.getStartDate().toString()); // LocalDateTime → TEXT (ISO format)
            ps.setDouble(3, costPrice.getCostPrice());

            ps.executeUpdate();
        }
    }

    //Helper: creates ProductCPDTO from rs.
    private ProductCostPriceDTO extractCPFromResultSet(ResultSet rs) throws SQLException {
        return new ProductCostPriceDTO(
                rs.getInt("productID"),
                LocalDate.parse(rs.getString("startDate")),
                rs.getDouble("costPrice")
        );
    }
}
