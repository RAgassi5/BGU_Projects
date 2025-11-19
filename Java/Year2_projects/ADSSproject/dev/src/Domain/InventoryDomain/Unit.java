package Domain.InventoryDomain;

import DTO.InventoryDTO.UnitDTO;

import java.time.LocalDate;

public class Unit {
    // immutable fields
    private final int unitID;
    private final int productID;

    // mutable fields
    private String location; // "shelf" or "storage"
    private LocalDate expiryDate;
    private boolean isDefective;

    //constructs a domain Unit from a DTO
    public Unit(UnitDTO dto) {
        this.unitID = dto.getUnitID();
        this.productID = dto.getProductID();
        this.location = dto.getLocation();
        this.expiryDate = dto.getExpiryDate();
        this.isDefective = dto.isDefective();
    }

    //updates the mutable fields of the unit from a new DTO
    public void updateFromDTO(UnitDTO dto) {
        this.location = dto.getLocation();
        this.expiryDate = dto.getExpiryDate();
        this.isDefective = dto.isDefective();
    }

    //converts the domain object back to a DTO
    public UnitDTO toDTO() {
        return new UnitDTO(
            unitID,
            productID,
            location,
            expiryDate,
            isDefective
        );
    }

    //returns the unit ID
    public int getUnitID() {
        return unitID;
    }

    //returns the product ID
    public int getProductID() {
        return productID;
    }

    //returns the location
    public String getLocation() {
        return location;
    }

    //returns the expiry date
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    //returns whether the unit is defective
    public boolean isDefective() {
        return isDefective;
    }

    //updates the location to "shelf"
    public void moveToShelf() {
        this.location = "Shelf";
    }

    //marks the unit as defective
    public void markAsDefective() {
        this.isDefective = true;
    }
}
