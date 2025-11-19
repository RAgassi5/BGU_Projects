package DataAccess.InventoryDAO;

import DTO.InventoryDTO.CategoryDTO;
import Util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    //returns a category by name from the database
    public CategoryDTO getCatByName(String name) throws SQLException {
        String query = "SELECT * FROM Categories WHERE categoryName = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new CategoryDTO(
                        rs.getString("categoryName"),
                        rs.getInt("rank")
                );
            }
        }
        return null;
    }

    //inserts a new category into the database
    public void insertCat(CategoryDTO category) throws SQLException {
        String query = """
            INSERT INTO Categories(categoryName, rank)
            VALUES (?, ?)
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, category.getCategoryName());
            ps.setInt(2, category.getRank());

            ps.executeUpdate();
        }
    }


    // returns all categories from the database as a list of DTOs
    public List<CategoryDTO> getAllCats() throws SQLException {
        String query = "SELECT * FROM Categories";
        List<CategoryDTO> result = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new CategoryDTO(
                        rs.getString("categoryName"),
                        rs.getInt("rank")
                ));
            }
        }
        return result;
    }
}
