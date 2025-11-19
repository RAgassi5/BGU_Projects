package DataAccess.SupplierDAO;

import DTO.SupplierDTO.contactDTO;
import DTO.SupplierDTO.orderDTO;
import DTO.SupplierDTO.orderItemDTO;
import Domain.SupplierDomain.Contact;

import java.sql.SQLException;
import java.util.List;

public interface orderDAO {
    public int insertOrder(int supplierID,int contactID) throws SQLException;
    void updateOrder(orderDTO dto) throws SQLException;
    void deleteOrder(int orderID) throws SQLException;

    void insertOrderItem(orderItemDTO dto) throws SQLException;
    void updateOrderItem(orderItemDTO dto) throws SQLException;
    int deleteOrderItem(int orderID, int productID) throws SQLException;
    int getMaxOrderID() throws SQLException;
    orderDTO getOrderById(int orderID) throws SQLException;
    List<orderItemDTO> getOrderItemsByOrderID(int orderID) throws SQLException;
    int getContactIDForOrder(int supplierID) throws SQLException ;
    boolean hasFixedDeliveryTomorrow() throws SQLException;

    }