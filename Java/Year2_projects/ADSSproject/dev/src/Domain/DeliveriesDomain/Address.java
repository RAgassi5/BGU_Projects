package Domain.DeliveriesDomain;

public record Address(District district, String street, int houseNum) {
    public Address {
        if (street == null || street.isEmpty()) {
            throw new IllegalArgumentException("Please enter a street name");
        }

        if (houseNum <= 0) {
            throw new IllegalArgumentException("Please enter a valid house number");
        }

        if (district == null) {
            throw new IllegalArgumentException("Please provide a valid district");
        }
    }

    @Override
    public String toString() {
        return "Region: " + district + ", Street: " + street + ", House: " + houseNum;
    }

    public District getDistrict() {
        return district;
    }
    public int getHouseNum() {
        return houseNum;
    }

    public String getStreet() {
        return street;
    }
}
