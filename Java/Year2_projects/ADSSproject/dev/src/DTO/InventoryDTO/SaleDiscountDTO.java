package DTO.InventoryDTO;


import java.time.LocalDate;

//Abstract base class for all sale discounts
public abstract class SaleDiscountDTO {
    protected int discountID;
    protected double rate;
    protected LocalDate startDate;
    protected LocalDate endDate;

    public SaleDiscountDTO(int discountID, double rate, LocalDate startDate, LocalDate endDate) {
        this.discountID = discountID;
        this.rate = rate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getDiscountID() { return discountID; }
    public double getRate() { return rate; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    public void setDiscountID(int discountID) {
        if (this.discountID == -1) //marked as need change for new discounts
            this.discountID = discountID;
    }
}
