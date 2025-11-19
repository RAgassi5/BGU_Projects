package DTO.DeliveriesDTO;

import Domain.DeliveriesDomain.Address;

public class LocationDTO {
    private String contactName;
    private String contactNUM;
    private Address address;
    private  int ID;

    public LocationDTO(int ID, String contactName, String contactNUM, Address address) {
        this.ID = ID;
        this.contactName = contactName;
        this.contactNUM = contactNUM;
        this.address = address;
    }
    public String getContactName() {
        return this.contactName;
    }
    public String getContactNUM() {
        return this.contactNUM;
    }
    public Address getAddress() {
        return this.address;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public void setContactNUM(String contactNUM) {
        this.contactNUM = contactNUM;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public int getID() {
        return this.ID;
    }
    public void setID(int ID){
        this.ID = ID;
    }
}
