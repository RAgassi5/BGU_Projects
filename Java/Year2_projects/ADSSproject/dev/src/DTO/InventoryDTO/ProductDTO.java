package DTO.InventoryDTO;

public class ProductDTO {
    private final int productID;
    private final String productName;
    private final String producerName;
    private double salePrice;
    private int minAmount;
    private int shelfAmount;
    private int storageAmount;
    private final double weightPerUnit;
    private final String primaryCategory;
    private final String secondaryCategory;
    private final String sizeCategory;

    public ProductDTO(int productID, String productName, String producerName,
                      double salePrice, int minAmount, int shelfAmount, int storageAmount,
                      double weightPerUnit, String primaryCategory, String secondaryCategory, String sizeCategory) {
        this.productID = productID;
        this.productName = productName;
        this.producerName = producerName;
        this.salePrice = salePrice;
        this.minAmount = minAmount;
        this.shelfAmount = shelfAmount;
        this.storageAmount = storageAmount;
        this.weightPerUnit = weightPerUnit;
        this.primaryCategory = primaryCategory;
        this.secondaryCategory = secondaryCategory;
        this.sizeCategory = sizeCategory;
    }

    public ProductDTO(int productID, UninitializedProduct up) {
        this.productID = productID;
        this.productName = up.getUProductName();
        this.producerName = up.getUProducerName();
        this.salePrice = up.getUSalePrice();
        this.minAmount = up.getUMinAmount();
        this.shelfAmount = up.getUShelfAmount();
        this.storageAmount = up.getUStorageAmount();
        this.weightPerUnit = up.getUWeightPerUnit();
        this.primaryCategory = up.getUPrimaryCategory();
        this.secondaryCategory = up.getUSecondaryCategory();
        this.sizeCategory = up.getUSizeCategory();
    }

    //Getters:
    public int getProductID() { return productID; }
    public String getProductName() { return productName; }
    public String getProducerName() { return producerName; }
    public double getSalePrice() { return salePrice; }
    public int getMinAmount() { return minAmount; }
    public int getShelfAmount() { return shelfAmount; }
    public int getStorageAmount() { return storageAmount; }
    public double getWeightPerUnit() { return weightPerUnit; }
    public String getPrimaryCategory() { return primaryCategory; }
    public String getSecondaryCategory() { return secondaryCategory; }
    public String getSizeCategory() { return sizeCategory; }

    //Setters:
    public void setSalePrice(double salePrice) { this.salePrice = salePrice; }
    public void setMinAmount(int minAmount) { this.minAmount = minAmount; }
    public void setShelfAmount(int shelfAmount) { this.shelfAmount = shelfAmount; }
    public void setStorageAmount(int storageAmount) { this.storageAmount = storageAmount; }
}
