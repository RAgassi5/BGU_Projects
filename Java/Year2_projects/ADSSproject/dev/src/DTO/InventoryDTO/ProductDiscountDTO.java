package DTO.InventoryDTO;

import DTO.InventoryDTO.ProductDiscountDTO;

import java.time.LocalDate;

//discount applied to specific products
public class ProductDiscountDTO extends SaleDiscountDTO {
    private final int productID;

    public ProductDiscountDTO(int discountID, double rate, LocalDate startDate, LocalDate endDate, int productID) {
        super(discountID, rate, startDate, endDate);
        this.productID = productID;
    }

    public int getProductID() {return productID;}
}
