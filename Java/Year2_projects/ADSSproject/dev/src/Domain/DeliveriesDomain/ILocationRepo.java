package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.LocationDTO;

import java.sql.SQLException;

public interface ILocationRepo {
    public void addLocation(LocationDTO location);
    public void removeLocation(int locationID);
    public Location getLocationByID(int locationID) throws SQLException;

}
