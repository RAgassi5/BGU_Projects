package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.LocationDTO;

public class Location {
    private static int locationID = 1;
    private int locationId;
    private final String contactName;
    private String contactNUM;
    private Address address;

    public Location(String contactName, String contactNUM, String street, int houseNum, District district) {
        this.locationId = locationID++;
        this.contactName = contactName;
        this.contactNUM = contactNUM;
        this.address = new Address(district, street, houseNum);
    }
    public Location(LocationDTO locationDTO) {
        this.locationId = locationDTO.getID();
        this.contactName = locationDTO.getContactName();
        this.contactNUM = locationDTO.getContactNUM();
        this.address = locationDTO.getAddress();
    }

    /**
     * a getter function for the contact's name
     *
     * @return the contact's name
     */
    public String getContactName() {
        return this.contactName;
    }

    /**
     * a getter function for the contact's phone number
     *
     * @return the contact's phone number
     */
    public String getContactNUM() {
        return this.contactNUM;
    }

    /**
     * a getter function for the location's address
     *
     * @return the location's address
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * a print function for the location's address
     */
    public void printAddress() {
        System.out.println(this.address.toString());
    }

    /**
     * a getter function for the location's district
     * @return District
     */
    public District getDistrict() {
        return this.address.getDistrict();
    }

    public int getLocationId() {
        return this.locationId;
    }
}


