package DataAccess.DeliveriesDAO;

import DTO.DeliveriesDTO.shipmentProductDTO;

import java.sql.SQLException;
import java.util.Collection;

public interface IProductDAO {
    shipmentProductDTO getProductByName(String name) throws SQLException;

    Collection<shipmentProductDTO> getAllProducts() throws SQLException;

    boolean productExists(String name) throws SQLException;
}
