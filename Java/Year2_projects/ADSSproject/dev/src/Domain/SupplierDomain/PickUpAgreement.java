package Domain.SupplierDomain;

import java.time.LocalDate;

public class PickUpAgreement extends Agreement {
    private String supplierAddress;

    // Constructor - Initializes a PickUpAgreement and saves the supplier address.
    public PickUpAgreement(int supplierID, String paymentMethod,String supplierAddress) {
        super(supplierID, paymentMethod);
        this.supplierAddress = supplierAddress;
    }
    public PickUpAgreement(int agreementID, int supplierID, String paymentMethod, LocalDate paymentDate,
                           boolean agreementStatus, String supplierAddress) {
        super(agreementID, supplierID, paymentMethod, paymentDate, agreementStatus);
        this.supplierAddress = supplierAddress;
    }
    public String getSupplierAddress() {
        return supplierAddress;
    }
}
