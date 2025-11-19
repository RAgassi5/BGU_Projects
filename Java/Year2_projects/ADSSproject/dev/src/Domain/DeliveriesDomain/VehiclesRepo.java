package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.VehicleDTO;
import DataAccess.DeliveriesDAO.IVehicleDAO;
import DataAccess.DeliveriesDAO.VehicleDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class VehiclesRepo implements IVehicleRepo {
    private HashMap<Integer, Vehicle> currentVehicles;
    private IVehicleDAO vehicleDAO;

    public  VehiclesRepo(){
        currentVehicles = new HashMap<>();
        vehicleDAO = new VehicleDAO();
    }


    @Override
    public void addVehicle(Vehicle currentVehicle) throws SQLException {
        VehicleDTO addToDB = new VehicleDTO(currentVehicle.getLicenseNumber(), currentVehicle.getVehicleWeight(), currentVehicle.getVehicleMaxCapacity(), currentVehicle.getCurrentStatus().toString(), currentVehicle.getVehicleType());
        vehicleDAO.addVehicle(addToDB);
        currentVehicles.put(currentVehicle.getLicenseNumber(), currentVehicle);
    }

    @Override
    public void removeVehicle(int licenseNum) throws SQLException {
        this.vehicleDAO.removeVehicle(licenseNum);
        this.currentVehicles.remove(licenseNum);
    }

    @Override
    public Collection<Vehicle> getAllVehicles() throws SQLException{
        Collection<VehicleDTO> allVehicleInDB = this.vehicleDAO.getAllVehicles();
        Collection<Vehicle> toReturn = new ArrayList<>();
        for(VehicleDTO vehicleDTO : allVehicleInDB){
            Vehicle vehicle;
            if(vehicleDTO.getVehicleType().equals("Van")){
                vehicle = new Van(vehicleDTO.getLicenseNum());
            } else if (vehicleDTO.getVehicleType().equals("Truck")) {
                vehicle = new Truck(vehicleDTO.getLicenseNum());
            } else {
                vehicle = new NewVehicle(vehicleDTO.getVehicleType(),
                        vehicleDTO.getLicenseNum(),
                        vehicleDTO.getCapacity(),
                        vehicleDTO.getMaxCapacity());
            }
            vehicle.setCurrentStatus(vehicle_status.valueOf(vehicleDTO.getVStaus()));
            toReturn.add(vehicle);
        }
        return toReturn;

    }

    @Override
    public boolean vehicleExists(int licenseNum) throws SQLException {
        if(currentVehicles.containsKey(licenseNum)){
            return true;
        }
        return vehicleDAO.vehicleExists(licenseNum);
    }

    @Override
    public void updateVehicleStatus(int vehicleId, vehicle_status status) throws SQLException {
        Vehicle vehicleToUpdate = getVehicleByID(vehicleId);
        if (vehicleToUpdate != null) {
            vehicleToUpdate.setCurrentStatus(status);
            VehicleDTO updateDTO = new VehicleDTO(
                    vehicleToUpdate.getLicenseNumber(),
                    vehicleToUpdate.getVehicleWeight(),
                    vehicleToUpdate.getVehicleMaxCapacity(),
                    status.toString(),
                    vehicleToUpdate.getVehicleType()
            );
            vehicleDAO.updateVehicleStatus(vehicleId, updateDTO);
        } else {
            System.out.println("There is no vehicle with the license number: " + vehicleId);
        }


    }

    @Override
    public Vehicle getVehicleByID(int licenseNum) throws SQLException {
        if (currentVehicles.containsKey(licenseNum)) {
            return currentVehicles.get(licenseNum);
        }
        VehicleDTO fetchedVehicle = vehicleDAO.findVehicle(licenseNum);
        if (fetchedVehicle == null) {
            return null;
        }
        Vehicle vehicle;
        if (fetchedVehicle.getVehicleType().equals("Van")) {
            vehicle = new Van(fetchedVehicle.getLicenseNum());
        } else if (fetchedVehicle.getVehicleType().equals("Truck")) {
            vehicle = new Truck(fetchedVehicle.getLicenseNum());
        } else {
            vehicle = new NewVehicle(fetchedVehicle.getVehicleType(),
                    fetchedVehicle.getLicenseNum(),
                    fetchedVehicle.getCapacity(),
                    fetchedVehicle.getMaxCapacity());
        }
        currentVehicles.put(licenseNum, vehicle);
        return vehicle;

    }


}
