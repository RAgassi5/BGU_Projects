package Domain.InventoryDomain;

import DTO.InventoryDTO.ProductCostPriceDTO;

import java.time.LocalDate;


public class ProductCostPrice {
    // product ID this price is associated with
    private final int productID;

    // timestamp of when this price starts
    private final LocalDate startDate;

    // the cost price of the product
    private final double costPrice;

    // constructs a domain ProductCostPrice from a DTO
    public ProductCostPrice(ProductCostPriceDTO dto) {
        this.productID = dto.getProductID();
        this.startDate = dto.getStartDate();
        this.costPrice = dto.getCostPrice();
    }

    // returns the product ID
    public int getProductID() {
        return productID;
    }

    // returns the start date (with time precision)
    public LocalDate getStartDate() {
        return startDate;
    }

    // returns the cost price
    public double getCostPrice() {
        return costPrice;
    }
}
