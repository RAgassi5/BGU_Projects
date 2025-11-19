package Domain.SupplierDomain;
import java.time.LocalDate;

public class Order {

    private int orderID;
    private LocalDate orderDate;
    private LocalDate orderReceivingDate;
    private int supplierID;
    private boolean orderStatus;
    private String contactPhoneNumber;
    private double totalPrice ;
    private String orderType = "";
    private String address;
    private int contactID;

    // Constructor for creating a new order
    public Order(int supplierID){
        this.supplierID = supplierID;
        this.orderStatus = false;
        this.totalPrice = 0;
    }
    public Order(int orderID, LocalDate orderDate, LocalDate orderReceivingDate, int supplierID, boolean orderStatus, double totalPrice, String orderType, String address, int contactID){
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.orderReceivingDate = orderReceivingDate;
        this.supplierID = supplierID;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.orderType = orderType;
        this.address = address;
        this.contactID = contactID;
    }
    // Getters and Setters
    public int getOrderID() {return orderID;}
    public boolean getOrderStatus() {return orderStatus;}
    public String getOrderType(){return orderType;}
    public void setOrderType(String orderType){this.orderType = orderType;}
    public void setOrderStatus() {
        this.orderReceivingDate =  LocalDate.now();
        this.orderStatus = true;}
    public void setOrderRecDate(LocalDate orderReceivingDate){
        this.orderReceivingDate = orderReceivingDate;
    }
    public String getOrderAddress() {return address;}
    public int getContactID() {return contactID;}
    public void setOrderAddress(String address){this.address = address;}
    public void setContactID(int contactID){this.contactID = contactID;}



    // Internal method to edit total price
    public void editTotalPrice(double amount){
        this.totalPrice += amount;
    }

    // Update the contact phone number
    public void editContactPhoneNumber(String  PhoneNumber){
        contactPhoneNumber = PhoneNumber;
    }

    // Set and get order dates
    public void setOrderDate(LocalDate orderDate){
        this.orderDate = orderDate;
    }
    public LocalDate getOrderDate(){return orderDate;}
    public LocalDate getOrderReceivingDate(){return orderReceivingDate;}
    public void setOrderReceivingDate(LocalDate orderReceivingDate){
        this.orderReceivingDate = orderReceivingDate;
    }

    // Nicely format the order details for printing
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("----- Order Details -----\n");
        sb.append("Order ID: ").append(orderID).append("\n");
        sb.append("Supplier ID: ").append(supplierID).append("\n");
        sb.append("Contact Phone Number: ").append(contactPhoneNumber).append("\n");
        sb.append("Order Status: ").append(orderStatus ? "Completed" : "In Progress").append("\n");
        sb.append("Order Date: ").append(orderDate != null ? orderDate : "Not Set").append("\n");
        sb.append("Receiving Date: ").append(orderReceivingDate != null ? orderReceivingDate : "Not Set").append("\n");
        sb.append("Total Price: ").append(totalPrice).append(" ₪\n");
        sb.append("Products in Order:\n");
        sb.append("--------------------------\n");

        return sb.toString();
    }

    // Get the total price of the order
    public double getOrderPrice(){return totalPrice;}

    // Get the supplier ID of this order
    public int getOrderSupplierID(){
        return supplierID;
    }





}


