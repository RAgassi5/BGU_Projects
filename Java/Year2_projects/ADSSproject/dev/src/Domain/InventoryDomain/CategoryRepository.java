package Domain.InventoryDomain;

import DTO.InventoryDTO.CategoryDTO;
import DTO.InventoryDTO.InventoryStatus;
import DataAccess.InventoryDAO.CategoryDao;

import java.sql.SQLException;
import java.util.*;

public class CategoryRepository {
    private final Map<String, Category> categories;
    private final CategoryDao dao;

    // constructs the repository and initializes DAO
    public CategoryRepository() {
        this.dao = new CategoryDao();
        this.categories = new HashMap<>();
    }

    // returns a category by name (loads from DB if not in memory)
    public Category getCategoryByName(String name) throws SQLException {
        if (categories.containsKey(name)) {
            return categories.get(name);
        }

        CategoryDTO dto = dao.getCatByName(name);
        if (dto == null) {
            return null;
        }

        Category category = new Category(dto);
        categories.put(name, category);
        return category;
    }

    // adds a category only if it does not already exist with a different rank
    public InventoryStatus addCategory(CategoryDTO dto) throws SQLException {
        Category existing = getCategoryByName(dto.getCategoryName());

        if (existing != null) {
            if (existing.getRank() == dto.getRank()) {
                return InventoryStatus.Success;
            } else {
                return InventoryStatus.NotValid;
            }
        }

        dao.insertCat(dto);
        Category category = new Category(dto);
        categories.put(dto.getCategoryName(), category);
        return InventoryStatus.Success;
    }

    // returns all categories that match a given rank (loads from DB as needed)
    public List<Category> getAllByRank(int rank) throws SQLException {
        List<CategoryDTO> allFromDb = dao.getAllCats();
        List<Category> result = new ArrayList<>();

        for (CategoryDTO dto : allFromDb) {
            if (!categories.containsKey(dto.getCategoryName())) {
                Category cat = new Category(dto);
                categories.put(dto.getCategoryName(), cat);
                if (cat.getRank() == rank) {
                    result.add(cat);
                }
                continue;
            }
            Category loaded = categories.get(dto.getCategoryName());
            if (loaded.getRank() == rank) {
                result.add(loaded);
            }
        }

        return result;
    }

    // checks if a category exists with a different rank than the one provided
    public boolean existsWithDifferentRank(String name, int rank) throws SQLException {
        Category category = getCategoryByName(name);
        return category != null && category.getRank() != rank;
    }
}
