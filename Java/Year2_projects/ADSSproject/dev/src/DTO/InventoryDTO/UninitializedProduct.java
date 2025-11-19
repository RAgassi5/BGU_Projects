package DTO.InventoryDTO;


public class UninitializedProduct {
    private final String productName;
    private final String producerName;
    private final double salePrice;
    private final int minAmount;
    private final int shelfAmount;
    private final int storageAmount;
    private final double weightPerUnit;
    private final String primaryCategory;
    private final String secondaryCategory;
    private final String sizeCategory;

    public UninitializedProduct(String productName, String producerName,
                                double salePrice, int minAmount, int shelfAmount, int storageAmount,
                                double weightPerUnit, String primaryCategory, String secondaryCategory, String sizeCategory) {
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

    //Getters:
    public String getUProductName() {
        return productName;
    }

    public String getUProducerName() {
        return producerName;
    }

    public double getUSalePrice() {
        return salePrice;
    }

    public int getUMinAmount() {
        return minAmount;
    }

    public int getUShelfAmount() {
        return shelfAmount;
    }

    public int getUStorageAmount() {
        return storageAmount;
    }

    public double getUWeightPerUnit() {
        return weightPerUnit;
    }

    public String getUPrimaryCategory() {
        return primaryCategory;
    }

    public String getUSecondaryCategory() {
        return secondaryCategory;
    }

    public String getUSizeCategory() {
        return sizeCategory;
    }
}


