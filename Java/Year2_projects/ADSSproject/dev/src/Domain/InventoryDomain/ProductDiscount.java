package Domain.InventoryDomain;

import DTO.InventoryDTO.ProductDiscountDTO;
import Domain.InventoryDomain.SaleDiscount;

public class ProductDiscount extends SaleDiscount {
    // the product this discount applies to
    private final int productID;

    // constructs a product discount from its DTO
    public ProductDiscount(ProductDiscountDTO dto) {
        super(dto);
        this.productID = dto.getProductID();
    }

    // returns the product ID
    public int getProductID() {
        return productID;
    }
}
