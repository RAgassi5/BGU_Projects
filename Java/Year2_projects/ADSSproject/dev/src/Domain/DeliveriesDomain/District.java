package Domain.DeliveriesDomain;

public enum District {
    North("North"),
    South("South"),
    Central("Central");

    private final String districtName;

    private District(String name) {
        this.districtName = name;
    }

    public String getDisplayName() {
        return districtName;
    }
}
