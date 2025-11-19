package Domain.SupplierDomain;

public abstract class DeliverableAgreement extends Agreement {

    // Constructor - Initializes a DeliverableAgreement with supplier ID and payment method.
    public DeliverableAgreement(int supplierID, String paymentMethod) {
        super(supplierID, paymentMethod);
    }

    // Returns the type of the agreement - here it is always "Deliverable".
    @Override
    public String getAgreementType(){
        return "Deliverable";
    }

}
