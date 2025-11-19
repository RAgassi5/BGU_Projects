package Domain.InventoryDomain;


import DTO.InventoryDTO.ProductDTO;


public class Product {
    // immutable fields
    private final int productID;
    private final String productName;
    private final String producerName;
    private final double weightPerUnit;
    private final String primaryCategory;
    private final String secondaryCategory;
    private final String sizeCategory;

    // mutable fields
    private double salePrice;
    private int minAmount;
    private int shelfAmount;
    private int storageAmount;

    // constructs a domain Product from a DTO
    public Product(ProductDTO dto) {
        this.productID = dto.getProductID();
        this.productName = dto.getProductName();
        this.producerName = dto.getProducerName();
        this.salePrice = dto.getSalePrice();
        this.minAmount = dto.getMinAmount();
        this.shelfAmount = dto.getShelfAmount();
        this.storageAmount = dto.getStorageAmount();
        this.weightPerUnit = dto.getWeightPerUnit();
        this.primaryCategory = dto.getPrimaryCategory();
        this.secondaryCategory = dto.getSecondaryCategory();
        this.sizeCategory = dto.getSizeCategory();
    }

    // updates the mutable fields of the product from a new DTO
    public void updateFromDTO(ProductDTO dto) {
        this.salePrice = dto.getSalePrice();
        this.minAmount = dto.getMinAmount();
        this.shelfAmount = dto.getShelfAmount();
        this.storageAmount = dto.getStorageAmount();
    }

    // converts the domain object back to a DTO
    public ProductDTO toDTO() {
        return new ProductDTO(
            productID,
            productName,
            producerName,
            salePrice,
            minAmount,
            shelfAmount,
            storageAmount,
            weightPerUnit,
            primaryCategory,
            secondaryCategory,
            sizeCategory
        );
    }

    // increases the shelf amount by 1
    public void incShelf() {
        shelfAmount++;
    }

    // decreases the shelf amount by 1
    public void decShelf() {
        shelfAmount--;
    }

    // increases the storage amount by 1
    public void incStorage() {
        storageAmount++;
    }

    // decreases the storage amount by 1
    public void decStorage() {
        storageAmount--;
    }

    // returns the current shelf amount
    public int getShelfAmount() {
        return shelfAmount;
    }

    // returns the current storage amount
    public int getStorageAmount() {
        return storageAmount;
    }

    // returns the total amount (shelf + storage)
    public int getTotalAmount() {
        return shelfAmount + storageAmount;
    }

    // returns the sale price
    public double getSalePrice() {
        return salePrice;
    }

    // returns the minimum required amount
    public int getMinAmount() {
        return minAmount;
    }

    // returns the weight per unit
    public double getWeightPerUnit() {
        return weightPerUnit;
    }

    // returns the primary category
    public String getPrimaryCategory() {
        return primaryCategory;
    }

    // returns the secondary category
    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    // returns the size category
    public String getSizeCategory() {
        return sizeCategory;
    }

    // returns the product ID
    public int getProductID() {
        return productID;
    }

    // returns product name
    public String getProductName() {
        return this.productName;
    }

    // sets minimum amount (validation in UI)
    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    // returns whether the product belongs to the given category name
    public boolean belongsToCategory(String catName) {
        return primaryCategory.equals(catName) ||
               secondaryCategory.equals(catName) ||
               sizeCategory.equals(catName);
    }


}
