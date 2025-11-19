package Domain.DeliveriesDomain;

public class Van extends Vehicle{
    public Van(int licenseNum){
        super(licenseNum,100, 250);
    }

    @Override
    public String getVehicleType(){
        return "Van";
    }
}
