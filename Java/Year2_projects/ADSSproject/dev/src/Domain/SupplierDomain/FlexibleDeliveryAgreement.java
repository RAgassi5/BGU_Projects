package Domain.SupplierDomain;

import java.time.LocalDate;

public class FlexibleDeliveryAgreement extends Agreement{
    private int supplyDays;

    // Constructor - Initializes a FlexibleDeliveryAgreement and correctly saves supplyDays.
    public FlexibleDeliveryAgreement(int supplierID, String paymentMethod,int supplyDays) {
        super(supplierID, paymentMethod);
        this.supplyDays = supplyDays;
    }
    public FlexibleDeliveryAgreement(int agreementID, int supplierID, String paymentMethod, LocalDate paymentDate,
                                     boolean agreementStatus, int supplyDays) {
        super(agreementID, supplierID, paymentMethod, paymentDate, agreementStatus);
        this.supplyDays = supplyDays;
    }

    public int getSupplyDays() {
        return supplyDays;
    }
    // Overrides the agreement type to "Flexible Delivery".
    @Override
    public String getAgreementType(){
        return "Flexible Delivery";
    }

}
