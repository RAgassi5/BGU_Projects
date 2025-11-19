package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.DriverDTO;

public class Driver {
    private  int driverId;
    private final String driverName;
    private String[] availableLicenses;
    private driver_status driverStatus;

    public Driver(String driverName) {
        this.driverName = driverName;
        this.availableLicenses = new String[0];
        this.driverStatus = driver_status.FREE;
    }

    /**
     * Constructs a Driver object using the provided DriverDTO instance.
     *
     * @param driverDTO an instance of DriverDTO containing the initial data for the driver,
     *                  including the driver's name, available licenses, status, and ID.
     */
    public Driver(DriverDTO driverDTO){
        this.driverName = driverDTO.getDriverName();
        this.availableLicenses = driverDTO.getLicenses();
        this.driverStatus = driver_status.valueOf(driverDTO.getStatus());
        this.driverId = driverDTO.getDriverID();
    }

    /**
     * adds a license to a driver
     *
     * @param license a string representing the license to be added
     */
    public void addLicense(String license) {
        String[] temp = new String[this.availableLicenses.length + 1];
        System.arraycopy(this.availableLicenses, 0, temp, 0, this.availableLicenses.length);
        temp[this.availableLicenses.length] = license;
        this.availableLicenses = temp;
    }

    /**
     * checks if the driver has the required license
     *
     * @param license a string representing the required license
     * @return bool True/False
     */
    public boolean getLicense(String license) {
        for (String availableLicense : this.availableLicenses) {
            if (availableLicense.equalsIgnoreCase(license)) {
                return true;
            }
        }
        return false;
    }

    public String[] getAvailableLicenses() {
        return this.availableLicenses;
    }

    /**
     * a getter function for the driver's name
     *
     * @return the driver's name
     */
    public String getDriverName() {
        return this.driverName;
    }

    /**
     * a getter function for the driver's status
     */
    public driver_status getDriverStatus() {
        return this.driverStatus;
    }

    /**
     * a setter function for the driver's status
     *
     * @param driverStatus driver's status
     */
    public void setDriverStatus(driver_status driverStatus) {
        this.driverStatus = driverStatus;
    }

    /**
     * print function for Driver
     */
    public void printDriver() {
        System.out.println("\n=== " + this.driverName +" (Driver ID: " + this.driverId + ") ===");
        System.out.println("Current Status: " + this.driverStatus.getDriver_status());
        System.out.println("Available Licenses: ");
        for (String license : this.availableLicenses) {
            if (license != null && !license.isEmpty()) {
                String formatted = license.substring(0, 1).toUpperCase() + license.substring(1).toLowerCase();
                System.out.println("-> " + formatted);
            } else {
                System.out.println("-> " + license);  // handle null or empty strings safely
            }
        }

    }
    public int getDriverId() {
        return this.driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }
}

