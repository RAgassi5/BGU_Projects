package Domain.DeliveriesDomain;

public enum driver_status {
    WORKING("currently working on a different shipment"),
    FREE("ready to work!"),
    NOT_IN_SHIFT("currently not available to work"),;

    private final String driver_status;

    private driver_status(String driver_status) {
        this.driver_status = driver_status;
    }
    public String getDriver_status() {
        return driver_status;
    }
}
