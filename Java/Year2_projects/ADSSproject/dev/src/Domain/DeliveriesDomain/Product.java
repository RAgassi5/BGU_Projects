package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.shipmentProductDTO;

public class Product {
    private final String name;
    private int amount;
    private int weightPerUnit;

    public Product(String name, int weightPerUnit) {
        this.name = name;
        this.amount = 1;
        if (weightPerUnit > 0) {
            this.weightPerUnit = weightPerUnit;
        } else {
            throw new IllegalArgumentException("Please enter a positive number for the weight per unit!");
        }

    }

    public Product(shipmentProductDTO shipmentProductDTO) {
        this.name = shipmentProductDTO.getProductName();
        this.amount = shipmentProductDTO.getAmount();
        if (shipmentProductDTO.getWeightPerUnit() < 0) {
            throw new IllegalArgumentException("Please enter a positive number for the weight per unit!");
        }
        this.weightPerUnit = shipmentProductDTO.getWeightPerUnit();
    }

    /**
     * Calculates the total weight of the product based on its amount and weight per unit.
     *
     * @return the total weight of the product as an integer
     */
    public int calculateFullWeight() {
        return this.amount * this.weightPerUnit;
    }

    /**
     * a function that removes a portion from a specific product
     */

    public void takePortionFromProduct(int amount) {
        if (amount > this.amount) {
            throw new IllegalArgumentException("Amount cannot be greater than the amount of the product!");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative!");
        }

        this.amount -= amount;
    }

    /**
     * a getter function for the product's amount
     *
     * @return the product's amount
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Sets the amount of the product.
     *
     * @param amount the new amount of the product
     */
    public void setAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative!");
        }
        this.amount = amount;

    }

    /**
     * Prints detailed information about the product, including its name,
     * amount, weight per unit, and total product weight.
     * <p>
     * The method outputs:
     * - The product name
     * - The amount of the product in units
     * - The weight per unit in kilograms
     * - The total weight of the product calculated based on the amount and weight per unit
     */
    public void printProduct() {
        System.out.println("Product: " + this.name);
        System.out.println(" --> Amount: " + this.amount + " units");
        System.out.println(" --> Weight Per Unit: " + this.weightPerUnit + " kg");
        System.out.println(" --> Total Product Weight: " + this.calculateFullWeight());
    }


    public String getName() {
        return this.name;
    }

    public int getWeightPerUnit() {
        return this.weightPerUnit;
    }

}
