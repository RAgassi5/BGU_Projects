package Domain.SupplierDomain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.DayOfWeek;

public class FixedDeliveryAgreement extends Agreement {
    private ArrayList<DayOfWeek> supplyDays;

    // Constructor - Initializes a FixedDeliveryAgreement but missing setting supplyDays field.
    public FixedDeliveryAgreement(int supplierID, String paymentMethod,ArrayList<DayOfWeek> supplyDays) {
        super(supplierID, paymentMethod);
        this.supplyDays = supplyDays;
    }
    public FixedDeliveryAgreement(int agreementID, int supplierID, String paymentMethod, LocalDate paymentDate, boolean agreementStatus, ArrayList<DayOfWeek> supplyDays) {
        super(agreementID, supplierID, paymentMethod, paymentDate, agreementStatus);
        this.supplyDays = supplyDays;
    }

    public ArrayList<DayOfWeek> getSupplyDays() {
        return supplyDays;
    }
    // Overrides the agreement type to "FixedDelivery".
    @Override
    public String getAgreementType(){
        return "FixedDelivery";
    }

}
