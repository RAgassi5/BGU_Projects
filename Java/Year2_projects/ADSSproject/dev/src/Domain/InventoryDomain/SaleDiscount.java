package Domain.InventoryDomain;


import DTO.InventoryDTO.SaleDiscountDTO;

import java.time.LocalDate;

public abstract class SaleDiscount {
    //Fields:
    protected final int discountID;
    protected final double rate;
    protected final LocalDate startDate;
    protected final LocalDate endDate;

    // constructs a discount from a DTO
    public SaleDiscount(SaleDiscountDTO dto) {
        this.discountID = dto.getDiscountID();
        this.rate = dto.getRate();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
    }

    // returns the discount ID
    public int getDiscountID() {return discountID;}

    // returns the discount rate
    public double getRate() {return rate;}

    // returns the start date
    public LocalDate getStartDate() {return startDate;}

    // returns the end date
    public LocalDate getEndDate() {return endDate;}
}
