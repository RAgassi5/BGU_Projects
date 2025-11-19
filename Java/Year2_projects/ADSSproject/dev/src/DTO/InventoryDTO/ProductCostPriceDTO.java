package DTO.InventoryDTO;


import java.time.LocalDate;

public class ProductCostPriceDTO {
    private final int productID;
    private final LocalDate startDate;
    private final double costPrice;

    public ProductCostPriceDTO(int productID, LocalDate startDate, double costPrice) {
        this.productID = productID;
        this.startDate = startDate;
        this.costPrice = costPrice;
    }

    //Getters:
    public int getProductID() { return productID; }
    public LocalDate getStartDate() { return startDate; }
    public double getCostPrice() { return costPrice; }
}
