package DTO.InventoryDTO;

import java.time.LocalDate;

public class UnitDTO {
    private final int unitID;
    private final int productID;
    private String location;
    private LocalDate expiryDate;
    private boolean isDefective;

    public UnitDTO(int unitID, int productID, String location, LocalDate expiryDate, boolean isDefective) {
        this.unitID = unitID;
        this.productID = productID;
        this.location = location;
        this.expiryDate = expiryDate;
        this.isDefective = isDefective;
    }

    //Getters:
    public int getUnitID() { return unitID; }
    public int getProductID() { return productID; }
    public String getLocation() { return location; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public boolean isDefective() { return isDefective; }

    //Setters:
    public void setLocation(String location) { this.location = location; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public void setDefective(boolean defective) { isDefective = defective; }
}
