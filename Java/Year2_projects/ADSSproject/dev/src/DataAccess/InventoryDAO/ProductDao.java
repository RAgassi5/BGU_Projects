package DataAccess.InventoryDAO;

import DTO.InventoryDTO.ProductDTO;
import Util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    //Get all products from the database
    public List<ProductDTO> getAllProducts() throws SQLException {
        List<ProductDTO> products = new ArrayList<>();
        String query = "SELECT * FROM Products";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        }
        return products;
    }

    //Get a single product by ID
    public ProductDTO getProductByID(int productID) throws SQLException {
        String query = "SELECT * FROM Products WHERE productID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, productID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractProductFromResultSet(rs);
            }
        }
        return null;
    }

    //Inserts a new product into the database
    public void insertProduct(ProductDTO product) throws SQLException {
        String query = """
            INSERT INTO Products(productID, productName, producerName, salePrice, minAmount,
                                 shelfAmount, storageAmount, weightPerUnit,
                                 primaryCategory, secondaryCategory, sizeCategory)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            setProductToStatement(ps, product);
            ps.executeUpdate();
        }
    }

    //Updates an existing product in the database (updates all fields)
    public void updateProduct(ProductDTO product) throws SQLException {
        String query = """
            UPDATE Products SET
                productName = ?, producerName = ?, salePrice = ?, minAmount = ?,
                shelfAmount = ?, storageAmount = ?, weightPerUnit = ?,
                primaryCategory = ?, secondaryCategory = ?, sizeCategory = ?
            WHERE productID = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, product.getProductName());
            ps.setString(2, product.getProducerName());
            ps.setDouble(3, product.getSalePrice());
            ps.setInt(4, product.getMinAmount());
            ps.setInt(5, product.getShelfAmount());
            ps.setInt(6, product.getStorageAmount());
            ps.setDouble(7, product.getWeightPerUnit());
            ps.setString(8, product.getPrimaryCategory());
            ps.setString(9, product.getSecondaryCategory());
            ps.setString(10, product.getSizeCategory());
            ps.setInt(11, product.getProductID());

            ps.executeUpdate();
        }
    }

    //returns the highest product ID in the database
    public int getMaxProductID() throws SQLException {
        String query = "SELECT MAX(productID) FROM Products";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                return rs.getInt(1);
            else
                return 0;
        }
    }

    //returns a productID by its name
    public int getProductByDetails(String productName, String producerName, String sizeCategory) throws SQLException {
        String sql = "SELECT productID FROM Products WHERE productName = ? AND producerName = ? AND sizeCategory = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productName);
            stmt.setString(2, producerName);
            stmt.setString(3, sizeCategory);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("productID");
                } else {
                    return -1;
                }
            }
        }
    }


    //Helper: creates a ProductDTO from result set row
    private ProductDTO extractProductFromResultSet(ResultSet rs) throws SQLException {
        return new ProductDTO(
                rs.getInt("productID"),
                rs.getString("productName"),
                rs.getString("producerName"),
                rs.getDouble("salePrice"),
                rs.getInt("minAmount"),
                rs.getInt("shelfAmount"),
                rs.getInt("storageAmount"),
                rs.getDouble("weightPerUnit"),
                rs.getString("primaryCategory"),
                rs.getString("secondaryCategory"),
                rs.getString("sizeCategory")
        );
    }

    //Helper: sets parameters in a PreparedStatement in order to insert from a ProductDTO
    private void setProductToStatement(PreparedStatement ps, ProductDTO product) throws SQLException {
        ps.setInt(1, product.getProductID());
        ps.setString(2, product.getProductName());
        ps.setString(3, product.getProducerName());
        ps.setDouble(4, product.getSalePrice());
        ps.setInt(5, product.getMinAmount());
        ps.setInt(6, product.getShelfAmount());
        ps.setInt(7, product.getStorageAmount());
        ps.setDouble(8, product.getWeightPerUnit());
        ps.setString(9, product.getPrimaryCategory());
        ps.setString(10, product.getSecondaryCategory());
        ps.setString(11, product.getSizeCategory());
    }
}
