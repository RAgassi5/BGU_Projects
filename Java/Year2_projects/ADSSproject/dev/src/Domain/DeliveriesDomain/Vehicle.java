package Domain.DeliveriesDomain;

public abstract class Vehicle {
    private int licenseNumber;
    private int weight;
    private final int maxWeight;
    private vehicle_status current_status;

    Vehicle(int licenseNumber, int weight, int maxCapacity) {
        this.licenseNumber = licenseNumber;
        this.weight = weight;
        this.maxWeight = maxCapacity;
        this.current_status = vehicle_status.AVAILABLE;
    }


    /**
     * a getter function for the vehicle's license number
     *
     * @return vehicle's license number
     */
    public int getLicenseNumber() {
        return licenseNumber;
    }


    /**
     * a getter function for the vehicle's weight
     *
     * @return vehicle's weight
     */
    public int getVehicleWeight() {
        return this.weight;
    }

    /**
     * a getter function for the vehicle's MaxCapacity
     *
     * @return vehicle's max capacity
     */
    public int getVehicleMaxCapacity() {
        return this.maxWeight;
    }

    /**
     * a getter function for the vehicle's status
     * @return the vehicle's current status
     */
    public vehicle_status getCurrentStatus() {
        return this.current_status;
    }

    /**
     * a setter function for a vehicle's current status
     * @param current_status the vehicle's status to update
     */
    public void setCurrentStatus(vehicle_status current_status) {
        this.current_status = current_status;
    }

    /**
     * a getter function for the vehicle's type
     *
     * @return String representing the type of vehicle
     */
    public abstract String getVehicleType();

    public void printVehicle() {
        System.out.println(getVehicleType() + " #" + getLicenseNumber() + " is currently: " + getCurrentStatus());
    }
}

