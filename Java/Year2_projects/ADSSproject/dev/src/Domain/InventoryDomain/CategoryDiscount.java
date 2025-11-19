package Domain.InventoryDomain;

import DTO.InventoryDTO.CategoryDiscountDTO;
import Domain.InventoryDomain.SaleDiscount;


// Represents a category-specific discount
public class CategoryDiscount extends SaleDiscount {
    // the category this discount applies to
    private final String categoryName;

    // constructs a category discount from its DTO
    public CategoryDiscount(CategoryDiscountDTO dto) {
        super(dto);
        this.categoryName = dto.getCategoryName();
    }

    // returns the category name
    public String getCategoryName() {
        return categoryName;
    }
}
