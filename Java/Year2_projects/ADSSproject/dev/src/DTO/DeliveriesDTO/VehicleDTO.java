package DTO.DeliveriesDTO;

public class VehicleDTO {
    private int licenseNum;
    private int capacity;
    private int MaxCapacity;
    private String vStatus;
    private String vehicleType;

    public VehicleDTO(int licenseNum, int capacity, int MaxCapacity, String vStatus, String vehicleType) {
        this.licenseNum = licenseNum;
        this.capacity = capacity;
        this.MaxCapacity = MaxCapacity;
        this.vStatus = vStatus;
        this.vehicleType = vehicleType;

    }

    public int getLicenseNum() {
        return this.licenseNum;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getMaxCapacity() {
        return this.MaxCapacity;
    }

    public String getVStaus() {
        return this.vStatus;
    }

    public void setLicenseNum(int licenseNum) {
        this.licenseNum = licenseNum;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMaxCapacity(int MaxCapacity) {
        this.MaxCapacity = MaxCapacity;
    }

    public void setVStaus(String vStatus) {
        this.vStatus = vStatus;
    }

    public String getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }


}

