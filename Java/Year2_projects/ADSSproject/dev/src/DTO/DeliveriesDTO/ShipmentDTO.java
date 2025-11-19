package DTO.DeliveriesDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ShipmentDTO {
    private int shipmentID;
    private LocationDTO origin;
    private LocationDTO destination;
    private LocalDate shipmentDate;
    private LocalTime shipmentTime;
    private String shipmentStatus;
    private ArrayList<shipmentProductDTO> productsInShipment;

    public ShipmentDTO(int shipmentID, LocationDTO origin, LocationDTO destination, LocalDate shipmentDate, LocalTime shipmentTime, String shipmentStatus){
        this.shipmentID = shipmentID;
        this.origin = origin;
        this.destination = destination;
        this.shipmentDate = shipmentDate;
        this.shipmentTime = shipmentTime;
        this.shipmentStatus = shipmentStatus;
        this.productsInShipment = new ArrayList<>();
    }
    public int getShipmentID() {
        return this.shipmentID;
    }
    public LocationDTO getOrigin() {
        return this.origin;
    }
    public LocationDTO getDestination() {
        return this.destination;
    }
    public LocalDate getShipmentDate() {
        return this.shipmentDate;
    }
    public LocalTime getShipmentTime() {
        return this.shipmentTime;
    }
    public String getShipmentStatus() {
        return this.shipmentStatus;
    }
    public void setShipmentID(int shipmentID) {
        this.shipmentID = shipmentID;
    }
    public void setOrigin(LocationDTO origin) {
        this.origin = origin;
    }
    public void setDestination(LocationDTO destination) {
        this.destination = destination;
    }
    public void setShipmentDate(int day, int month, int year) {
        this.shipmentDate = LocalDate.of(year, month, day);
    }
    public void setShipmentTime(int hour, int minute) {
        this.shipmentTime = LocalTime.of(hour, minute);
    }
    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }
    public ArrayList<shipmentProductDTO> getProductsInShipment() {
        return this.productsInShipment;
    }
    public void setProductsInShipment(ArrayList<shipmentProductDTO> productsInShipment) {
        this.productsInShipment = productsInShipment;
    }
    public void addProductToShipment(shipmentProductDTO product) {
        if (productsInShipment == null) {
            productsInShipment = new ArrayList<>();
        }
        productsInShipment.add(product);
    }

    public void clearProducts() {
        if (productsInShipment != null) {
            productsInShipment.clear();
        }
    }


}
