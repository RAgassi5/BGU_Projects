package Domain.DeliveriesDomain;

public enum vehicle_status {
    AVAILABLE("The vehicle is Available"),
    WORKING("The vehicle is currently working on a different shipment");

    private final String vehicle_status;

    private vehicle_status(String driver_status) {
        this.vehicle_status = driver_status;
    }
    public String getDriver_status() {
        return vehicle_status;
    }
}
