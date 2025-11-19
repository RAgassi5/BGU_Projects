package Domain.SupplierDomain;

public class Contact {
    private String name;
    private String phone;
    private int supplierID;

    // Constructor - Initializes a new Contact with a name and a phone number.
    public Contact(String name, String phone,int supplierID) {
        this.name = name;
        this.phone = phone;
        this.supplierID = supplierID;
    }
    // Returns the name of the contact.
    public String getName() {return name;}

    // Sets a new name for the contact.
    public void setName(String name) {this.name = name;}

    // Returns the phone number of the contact.
    public String getPhone() {return phone;}

    // Sets a new phone number for the contact.
    public void setPhone(String phone) {this.phone = phone;}

    public int getSupplierID() {return supplierID;}

}
