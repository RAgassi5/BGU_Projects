package Service.SupplierService;

import DTO.SupplierDTO.orderDTO;
import DTO.SupplierDTO.productForOrderDTO;
import DTO.SupplierDTO.supplierProductDTO;
import Domain.SupplierDomain.*;

import java.util.ArrayList;
import java.util.List;

public class OrderService {


    private final supplierProductRepository supplierProductRepo;
    private final OrderRepository orderRepo;
    private supplierRepository supplierRepo;

    public OrderService(supplierProductRepository supplierProductRepo, OrderRepository orderRepo,supplierRepository supplierRepository) {
        this.supplierProductRepo = supplierProductRepo;
        this.orderRepo = orderRepo;
        supplierRepo = supplierRepository;
    }

    public boolean generateOrderDueToShortage(int productID,int amount){
        if(!supplierProductRepo.isSupplierExist(productID)) return false;
        productForOrderDTO prodToAdd = supplierProductRepo.chooseSProductByCheapestOffer(productID,amount);
        if(prodToAdd == null){
            return false;
        }
        int supplierID = prodToAdd.spDTO().supplierID();
        supplierRepo.supplierExists(supplierID);
        orderDTO newODTO = orderRepo.createOrder(supplierID);
        ArrayList<productForOrderDTO> products = new ArrayList<>();
        products.add(prodToAdd);
        orderRepo.addProductsToOrder(newODTO, products);
        return true;

    }

    public List<supplierProductDTO> getProductsForTomorrow(){
        return supplierProductRepo.getProductsForTomorrowOrder();
    }

    public boolean createOrderOfPFT(List<productForOrderDTO> productList) {
        if (productList.isEmpty()) return false;

        int currAgreementID = productList.get(0).spDTO().agreementID();
        int supplierID = productList.get(0).spDTO().supplierID();
        supplierRepo.supplierExists(supplierID);
        orderDTO newODTO = orderRepo.createOrder(supplierID);
        ArrayList<productForOrderDTO> newProducts = new ArrayList<>();

        for (productForOrderDTO prodToAdd : productList) {
            if (currAgreementID != prodToAdd.spDTO().agreementID()) {
                orderRepo.addProductsToOrder(newODTO, newProducts);
                currAgreementID = prodToAdd.spDTO().agreementID();
                supplierID = prodToAdd.spDTO().supplierID();
                supplierRepo.supplierExists(supplierID);
                newODTO = orderRepo.createOrder(supplierID);
                newProducts.clear();
            }
            productForOrderDTO pfoDTO = supplierProductRepo.createProductForOrder(prodToAdd.spDTO(), prodToAdd.amount());
            newProducts.add(pfoDTO);
        }

        if (!newProducts.isEmpty()) {
            orderRepo.addProductsToOrder(newODTO, newProducts);
        }

        if ("PickUp".equals(newODTO.orderType())) {
            // call to shipment func
        }

        return true;
    }

    public orderDTO createNewOrder(int supplierID,String contactPhoneNumber){
        return orderRepo.createOrder(supplierID);
    }
    public boolean supplierHasProducts(int supplierID){
        return supplierProductRepo.checkIfSupplierHasProducts(supplierID);
    }

    public boolean checkIfProductInOrder(int productID, int orderID){
        return orderRepo.checkIfProductInOrder(orderID,productID);
    }

    public orderDTO checkIfOrderExists(int orderID){
        return orderRepo.isOrderExists(orderID);
    }

    // Adds a product with a specific quantity to a given order.
    public void addProductToOrder(orderDTO newOrderDTO, supplierProductDTO spDTO,int quantity) {
        productForOrderDTO pfoDTO= supplierProductRepo.createProductForOrder(spDTO,quantity);
        ArrayList<productForOrderDTO> products = new ArrayList<>();
        products.add(pfoDTO);
        orderRepo.addProductsToOrder(newOrderDTO,products);
    }

    // Returns the total price of a given order.
    public double getOrderTotalPrice(int orderID) {
        return orderRepo.getOrderTotalPrice(orderID);
    }

    public void showProductsPrices(int productID, int quantity) {
        supplierProductRepo.showProductOffers(productID, quantity);
    }

    public void displayActiveOrders(){
        orderRepo.displayActive();
    }


    // Changes the status of the order with the given ID to 'completed'.
    public void changeOrderStatus(orderDTO oDTO) {
        orderRepo.changeOrderStatus(oDTO);
    }
    // Deletes the order with the given ID from the repository.
    public void deleteOrder(orderDTO oDTO) {
        orderRepo.deleteOrder(oDTO);
    }

    // Changes the quantity of a specific product in a given order.
    public void changeProductQuantity(orderDTO OrderDTO,supplierProductDTO spDTO,int quantity) {
        orderRepo.changeProductQuantity(OrderDTO,spDTO,quantity);
    }


    // Removes a product from a given order.
    public void deleteProductFromOrder(orderDTO OrderDTO,supplierProductDTO spDTO) {
        orderRepo.deleteProductFromOrder(OrderDTO,spDTO);
    }

    // Returns the quantity of a specific product in a given order.
    public int getProductQuantity(orderDTO OrderDTO,supplierProductDTO spDTO) {
        return orderRepo.getOrderItemQuantity(OrderDTO,spDTO);
    }
    public int getProductIdByName(String productName) {
        // remind me to add option to add product by name to order
        return supplierProductRepo.getProductIdByName(productName);
    }





}
