package Domain.InventoryDomain;

import DTO.InventoryDTO.ProductDiscountDTO;
import DTO.InventoryDTO.CategoryDiscountDTO;
import DataAccess.InventoryDAO.SaleDiscountDao;
import Domain.InventoryDomain.ProductDiscount;

import java.sql.SQLException;
import java.util.*;

// Repository responsible for managing discounts for products and categories
public class DiscountRepository {
    private final Map<Integer, List<ProductDiscount>> productDiscounts;
    private final Map<String, List<CategoryDiscount>> categoryDiscounts;
    private final SaleDiscountDao dao;
    private int discountIDCounter;

    // constructs the repository and initializes the counter from DB
    public DiscountRepository() throws SQLException {
        this.dao = new SaleDiscountDao();
        this.productDiscounts = new HashMap<>();
        this.categoryDiscounts = new HashMap<>();
        this.discountIDCounter = dao.getMaxDiscountID() + 1;
    }

    // adds a new product discount to both memory and DB
    public void addProductDiscount(ProductDiscountDTO dto) throws SQLException {
        dto.setDiscountID(discountIDCounter++);
        dao.insertProductDiscount(dto);

        ProductDiscount discount = new ProductDiscount(dto);
        if (!productDiscounts.containsKey(dto.getProductID())) {
            productDiscounts.put(dto.getProductID(), new ArrayList<>());
        }
        productDiscounts.get(dto.getProductID()).add(discount);
    }

    // adds a new category discount to both memory and DB
    public void addCategoryDiscount(CategoryDiscountDTO dto) throws SQLException {
        dto.setDiscountID(discountIDCounter++);
        dao.insertCategoryDiscount(dto);

        CategoryDiscount discount = new CategoryDiscount(dto);
        if (!categoryDiscounts.containsKey(dto.getCategoryName())) {
            categoryDiscounts.put(dto.getCategoryName(), new ArrayList<>());
        }
        categoryDiscounts.get(dto.getCategoryName()).add(discount);
    }

    // returns all product discounts (loaded from DB if not already cached)
    public List<ProductDiscount> getProductDiscounts(int productID) throws SQLException {
        List<ProductDiscountDTO> dtos = dao.getDiscountsByProductID(productID);
        List<ProductDiscount> discounts = new ArrayList<>();

        for (ProductDiscountDTO dto : dtos) {
            discounts.add(new ProductDiscount(dto));
        }

        productDiscounts.put(productID, discounts);
        return discounts;
    }

    // returns all category discounts (loaded from DB if not already cached)
    public List<CategoryDiscount> getCategoryDiscounts(String categoryName) throws SQLException {
        List<CategoryDiscountDTO> dtos = dao.getDiscountsByCategoryName(categoryName);
        List<CategoryDiscount> discounts = new ArrayList<>();

        for (CategoryDiscountDTO dto : dtos) {
            discounts.add(new CategoryDiscount(dto));
        }

        categoryDiscounts.put(categoryName, discounts);
        return discounts;
    }
}
