package Domain.DeliveriesDomain;

/**
 *enum class that represents the status of the shipment
 */
public enum s_status {
    PENDING("Pending"),
    SHIPPED("Shipped"),
    OVERWEIGHT("Overweight"),
    DELAYED("Delayed"),
    IN_PROGRESS("In Progress"),
    DELIVERED("Delivered"),
    SELF_DELIVERED("Self Delivered");
    ;

    private final String displayName;

    private s_status(String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }





}
