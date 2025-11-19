package DataAccess.DeliveriesDAO;

import DTO.DeliveriesDTO.ShipmentDTO;

import java.sql.SQLException;
import java.time.LocalTime;

public interface IShipmentDAO {
    int addShippedShipment(ShipmentDTO shipment) throws SQLException;

    int addSelfDeliveredShipment(ShipmentDTO shipment) throws SQLException;

    ShipmentDTO getShipmentById(int id) throws SQLException;

    void updateShipmentTime(int shipmentId, LocalTime time) throws SQLException;

    void save(ShipmentDTO shipmentDTO) throws SQLException;
}
