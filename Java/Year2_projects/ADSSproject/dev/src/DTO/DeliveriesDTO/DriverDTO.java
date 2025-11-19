package DTO.DeliveriesDTO;

public class DriverDTO {
    private String driverName;
    private String[] licenses;
    private String status;
    private int driverID;

    public DriverDTO(String driverName, String[] licenses, String status, int driverID) {
        this.driverName = driverName;
        this.licenses = licenses;
        this.status = status;
        this.driverID = driverID;
    }
    public String getDriverName() {
        return this.driverName;
    }
    public String[] getLicenses() {
        return this.licenses;
    }
    public String getStatus() {
        return this.status;
    }
    public int getDriverID() {
        return this.driverID;
    }
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public void setLicenses(String[] licenses) {
        this.licenses = licenses;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }
}
