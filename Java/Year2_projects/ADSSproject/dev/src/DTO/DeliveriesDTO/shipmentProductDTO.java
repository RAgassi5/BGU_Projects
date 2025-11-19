package DTO.DeliveriesDTO;

public class shipmentProductDTO {
    private String productName;
    private int weightPerUnit;
    private int amount;

    public shipmentProductDTO(String productName, int weightPerUnit, int amount) {
        this.productName = productName;
        this.weightPerUnit = weightPerUnit;
        this.amount = amount;
    }
    public String getProductName() {
        return this.productName;
    }
    public int getWeightPerUnit() {
        return this.weightPerUnit;
    }
    public int getAmount() {
        return this.amount;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setWeightPerUnit(int weightPerUnit) {
        this.weightPerUnit = weightPerUnit;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
