package Domain.InventoryDomain;

import DTO.InventoryDTO.CategoryDTO;

// Represents a product category in the domain layer
public class Category {
    // name of the category
    private final String categoryName;

    // rank of the category (1=primary, 2=secondary, 3=size)
    private final int rank;

    // constructs a domain Category from a DTO
    public Category(CategoryDTO dto) {
        this.categoryName = dto.getCategoryName();
        this.rank = dto.getRank();
    }

    // returns the category name
    public String getCategoryName() {
        return categoryName;
    }

    // returns the category rank
    public int getRank() {
        return rank;
    }
}
