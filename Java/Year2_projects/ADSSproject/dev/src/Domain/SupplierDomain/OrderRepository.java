package Domain.SupplierDomain;

import DTO.SupplierDTO.orderDTO;
import DTO.SupplierDTO.productForOrderDTO;
import DTO.SupplierDTO.supplierProductDTO;

import java.util.ArrayList;

public interface OrderRepository {
    public orderDTO createOrder(int supplierID);
    public void addProductsToOrder(orderDTO orderDTO, ArrayList <productForOrderDTO> products);
    public orderDTO isOrderExists(int orderID);
    boolean checkIfProductInOrder(int orderID,int productID);
    double getOrderTotalPrice(int orderID);
    void displayActive();
    void deleteProductFromOrder(orderDTO newOrderDTO, supplierProductDTO spDTO);
    void changeProductQuantity(orderDTO newOrderDTO,supplierProductDTO spDTO, int quantity);

    void changeOrderStatus(orderDTO newOrderDTO);

    void deleteOrder(orderDTO newOrderDTO);

    int getOrderItemQuantity(orderDTO newOrderDTO,supplierProductDTO spDTO);


}
