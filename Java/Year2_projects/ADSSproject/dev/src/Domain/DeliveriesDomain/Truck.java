package Domain.DeliveriesDomain;

public class Truck extends Vehicle {


    public Truck(int licenseNum) {
        super(licenseNum, 200, 500);
    }


    @Override
    public String getVehicleType() {
        return "Truck";
    }
}
