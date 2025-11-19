package Domain.SupplierDomain;

public class orderItem {
    private int quantity;
    private double price;
    private String name;
    private int productID;
    private int orderID;

    // Constructor for orderItem
    public orderItem(int quantity, double price,String name,int productID,int orderID){
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.productID = productID;
        this.orderID = orderID;
    }
    // Getters
    public int getOrderID(){return orderID;}
    public int getProductId() {return productID;}
    public int getQuantity() {return quantity;}
    public double getPrice() {return price;}
    public String getName() {return name;}

    // Update the quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Calculate total price for this item
    public double getCalcPrice() {
        return price*quantity;
    }
    @Override
    public String toString() {
        return "OrderItem {" +
                "Order ID = " + orderID +
                ", Product ID = " + productID +
                ", Name = '" + name + '\'' +
                ", Quantity = " + quantity +
                ", Price = " + price +
                '}';
    }
}
