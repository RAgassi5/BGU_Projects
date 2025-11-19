    package Domain.SupplierDomain;

    public class SupplierProduct {
        // Static counter to assign unique IDs for each product
        private int productID;
        private int catalogID;
        private String manufacturer;
        private int supplierID;
        private int agreementID;
        private double price;
        private String productType;
        private String productName;
        private String AgreementType = null;
        private boolean isActive;

        // Constructor for a new Product (auto-generates productID)
        public SupplierProduct(int catalogID, double price,int supplierID, String productName,String manufacturer,String productType,int agreementID) {
            this.productType=productType;
            this.manufacturer = manufacturer;
            this.catalogID = catalogID;
            this.supplierID = supplierID;
            this.price = price;
            this.productName = productName;
            this.agreementID = agreementID;
            this.isActive = true;
        }
        // Constructor when productID is already known (e.g., from database)
        public SupplierProduct(int productID,int catalogID, double price,int supplierID, String productName,String manufacturer,String productType,int agreementID) {
            this.productType = productType;
            this.manufacturer = manufacturer;
            this.productID =productID;
            this.catalogID = catalogID;
            this.supplierID = supplierID;
            this.price = price;
            this.productName = productName;
            this.agreementID = agreementID;

        }
        // Getters
        public int getProductID() {return productID;}
        public int getCatalogID() {return catalogID;}
        public int getSupplierID() {return supplierID;}
        public double getPrice() {return price;}
        public double getDiscountedPrice(int quantity) {return price;}
        public String getProductName() {return productName;}
        public String getManufacturer() {return manufacturer;}
        public String getProductType() {return productType;}
        public String getAgreementType() {return AgreementType;}
        public int getAgreementID() {return agreementID;}
        // Setters
        public void setPrice(double price) {this.price = price;}
        public void setStatus(boolean status) {this.isActive = status;}
        public void setAgreementType(String AgreementType) {this.AgreementType = AgreementType;}


        // String representation of the Product object
        @Override
        public String toString() {
            return "ProductID: " + productID +
                    ", Name: " + productName +
                    ", CatalogID: " + catalogID +
                    ", Price: " + price + " ₪" +
                    ", Manufacturer: " + manufacturer +
                    ", Product Type: " + productType +
                    ", Supplier ID: " + supplierID;
        }
    }
