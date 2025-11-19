package DataAccess.DeliveriesDAO;

import DTO.DeliveriesDTO.LocationDTO;

import java.sql.SQLException;
import java.util.List;

public interface ILocationDAO {

    void addLocation(LocationDTO location) throws SQLException;

    LocationDTO getById(int id) throws SQLException;

    List<LocationDTO> getAll() throws SQLException;

    void update(LocationDTO location) throws SQLException;

    void delete(int id) throws SQLException;

    LocationDTO findMatchingLocation(LocationDTO location) throws SQLException;

}
