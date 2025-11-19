package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.shipmentProductDTO;
import DTO.DeliveriesDTO.ShipmentDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Shipment {
    private int id;
    private s_status shipmentStatus;
    private Location originLocation;
    private Location destinationLocation;
    private LocalDate shipmentDate;
    private LocalTime departureTime;
    private ArrayList<Product> productsInShipment;

    /**
     * Constructor to initialize a new shipment instance with specified origin, destination, and shipment date.
     *
     * @param originLocation the origin location of the shipment
     * @param destinationLocation the destination location of the shipment
     * @param shipmentDate the date on which the shipment is created
     */
    public Shipment(Location originLocation, Location destinationLocation,LocalDate shipmentDate) {
        this.originLocation = originLocation;
        this.destinationLocation = destinationLocation;
        this.shipmentStatus = s_status.PENDING;
        this.shipmentDate = shipmentDate;
        this.productsInShipment = new ArrayList<>();

    }

    /**
     * Constructor that initializes a Shipment instance from a ShipmentDTO object.
     *
     * @param fromDB the ShipmentDTO object containing shipment data including ID, origin,
     *               destination, status, shipment date, and associated products.
     */
    public Shipment(ShipmentDTO fromDB){
        this.id = fromDB.getShipmentID();
        this.originLocation = new Location(fromDB.getOrigin());
        this.destinationLocation = new Location(fromDB.getDestination());
        this.shipmentStatus = s_status.valueOf(fromDB.getShipmentStatus());
        this.shipmentDate = LocalDate.of(fromDB.getShipmentDate().getYear(), fromDB.getShipmentDate().getMonth(), fromDB.getShipmentDate().getDayOfMonth());
        if (fromDB.getShipmentTime() != null) {
            this.departureTime = LocalTime.of(fromDB.getShipmentTime().getHour(), fromDB.getShipmentTime().getMinute());
        } else {
            this.departureTime = null;
        }

        this.productsInShipment = new ArrayList<>();
        for(shipmentProductDTO shipmentProductDTO : fromDB.getProductsInShipment()){
            this.productsInShipment.add(new Product(shipmentProductDTO));
        }
    }

    /**
     * a getter function for a shipment's ID
     *
     * @return shipment's ID
     */
    public int getId() {
        return id;
    }

    /**
     * a function that updates the shipment's status
     *
     * @param status current status of shipment
     */
    public void updateStatus(s_status status) {
        this.shipmentStatus = status;
    }

    /**
     * a getter function for the shipment's status
     *
     * @return an instance of s_status
     */
    public s_status getStatus() {
        return this.shipmentStatus;
    }

    /**
     * a getter function for a shipment's origin location
     *
     * @return an instance of Location
     */
    public Location getOriginLocation() {
        return this.originLocation;
    }

    /**
     * a getter function for a shipment's origin location
     *
     * @return an instance of Location
     */
    public Location getDestinationLocation() {
        return this.destinationLocation;
    }

    /**
     * a setter function for a shipment's departure time
     * the time is set for once the shipment is shipped
     */

    public void setDepartureTime() {
        departureTime = LocalTime.now();
    }

    /**
     * a gettter function for a shipment's date
     *
     * @return a LocalDate
     */
    public LocalDate getShipmentDate() {
        return this.shipmentDate;
    }

    /**
     * a function that adds a new product to a shipment
     * @param product a product to add
     * @param quantity amount of the added product
     */
    public void addProductToShipment(Product product, int quantity) {
        product.setAmount(quantity);
        this.productsInShipment.add(product);
    }

    /**
     * a function that removes a product from a shipment
     * @param product the product to remove
     */
    public void removeProductFromShipment(Product product) {
        this.productsInShipment.remove(product);
    }

    /**
     * @return a list of all products in a shipment
     */
    public ArrayList<Product> getProductsInShipment() {
        return this.productsInShipment;
    }

    /**
     * a function that takes off the amount of an amount of product from the shipment
     *
     * @param product the product to reduce from
     * @param amount the amount to take out
     */
    public void updateProductsInShipment(Product product, int amount) {
        this.productsInShipment.remove(product);
        product.takePortionFromProduct(amount);
        this.productsInShipment.add(product);
    }

    /**
     * a print function for all products in a shipment including index
     */
    public void printShipmentProducts(){
        for (int i = 0; i < productsInShipment.size(); i++) {
            System.out.println("[" + i + "]");
            productsInShipment.get(i).printProduct();
            System.out.println();
        }
    }
    /**
     * Sets the shipment ID.
     *
     * @param id the unique identifier for the shipment
     */
    public void setShipmentId(int id){
        this.id = id;
    }


    /**
     * a getter function for a shipment's total weight
     * @return the total weight of the shipment
     */
    public int getShipmentWeight(){
        int totalWeight = 0;
        for(Product product : this.productsInShipment){
            totalWeight += product.calculateFullWeight();
        }
        return totalWeight;
    }
    /**
     * A getter function for the shipment's departure time.
     *
     * @return the departure time of the shipment as a LocalTime instance
     */
    public LocalTime getDepartureTime(){
        return this.departureTime;
    }

    /**
     * Sets the departure time for the shipment.
     *
     * @param departureTime the time at which the shipment is scheduled to depart, represented as a LocalTime instance
     */
    public void setDepartureTime(LocalTime departureTime){
        this.departureTime = departureTime;
    }
}
