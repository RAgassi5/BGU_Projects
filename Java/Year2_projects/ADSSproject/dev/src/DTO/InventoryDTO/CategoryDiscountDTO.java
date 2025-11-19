package DTO.InventoryDTO;

import DTO.InventoryDTO.CategoryDiscountDTO;

import java.time.LocalDate;

//discount applied to specific category
public class CategoryDiscountDTO extends SaleDiscountDTO {
    private final String categoryName;

    public CategoryDiscountDTO(int discountID, double rate, LocalDate startDate, LocalDate endDate, String categoryName) {
        super(discountID, rate, startDate, endDate);
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
