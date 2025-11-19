package Domain.DeliveriesDomain;

public class NewVehicle extends Vehicle {
    private final String vehicleType;

    public NewVehicle(String vehicleType,int licenseNum, int weight, int maxWeight) {
        super(licenseNum, weight, maxWeight);
        this.vehicleType = makeFirstCapital(vehicleType);
    }
    @Override
    public String getVehicleType() {
        return this.vehicleType;
    }

    /**
     * Converts the first character of the input string to uppercase while keeping the rest of the string unchanged.
     * If the input string is null or empty, the original string is returned.
     *
     * @param str the input string to be processed
     * @return a string with the first character capitalized, or the original string if it is null or empty
     */
    private static  String makeFirstCapital(String str){
        if(str == null || str.isEmpty()){
            return str;
        }else{
        return str.substring(0,1).toUpperCase() + str.substring(1);
        }
    }
}
