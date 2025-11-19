package Domain.SupplierDomain;

import DTO.SupplierDTO.*;
import DataAccess.SupplierDAO.orderDAO;


import java.time.LocalDate;
import java.util.*;

public class OrderRepositoryIMP implements OrderRepository {
    private final Map<Integer, Order> orders = new HashMap<>();
    private final Map<Integer, Map<Integer, orderItem>> orderItems = new HashMap<>();

    private final orderDAO orderDAO;
    private agreementRepository agreementRepo;


    public OrderRepositoryIMP(orderDAO orderDAO,agreementRepository agreementRepo) {
        this.agreementRepo = agreementRepo;
        this.orderDAO = orderDAO;
    }

    @Override
    public orderDTO createOrder(int supplierID) {
        try {
            int contactID = orderDAO.getContactIDForOrder(supplierID);
            int generatedID = orderDAO.insertOrder(supplierID,contactID);
            Order newOrder = new Order(generatedID, LocalDate.now(), null, supplierID, false, 0, null,null,contactID);
            orders.put(generatedID, newOrder);

            int[] orderDate = dateToArray(newOrder.getOrderDate());
            int[] recDate = null;
            return new orderDTO(generatedID, orderDate, recDate, supplierID, false, 0, null,null,contactID);

        } catch (Exception e) {
            System.err.println("❌ Failed to create order: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void addProductsToOrder(orderDTO orderDTO, ArrayList<productForOrderDTO> products) {
        // remind or to set order recDATE
        try {
            Order order = orders.get(orderDTO.orderID());
            if (order == null) return;

            Map<Integer, orderItem> items = orderItems.computeIfAbsent(orderDTO.orderID(), k -> new HashMap<>());

            for (productForOrderDTO p : products) {
                if(orderDTO.orderType() == null){
                    LocalDate date = LocalDate.of(orderDTO.orderDate()[0], orderDTO.orderDate()[1], orderDTO.orderDate()[2]);
                    agreementDetailsDTO newDetails  = agreementRepo.getAgreementDetails(p.spDTO().agreementID(),orderDTO.supplierID(),date);
                    order.setOrderType(newDetails.type());
                    order.setOrderRecDate(newDetails.recDate());
                    order.setOrderAddress(newDetails.address());


                }
                supplierProductDTO sp = p.spDTO();
                orderItem item = new orderItem(p.amount(), p.price(), sp.productName(), sp.productID(), order.getOrderID());
                items.put(sp.productID(), item);
                order.editTotalPrice(p.price());

                orderItemDTO itemDTO = toOrderItemDTO(item);
                orderDAO.insertOrderItem(itemDTO);
            }

            orderItems.put(order.getOrderID(), items);
            orderDAO.updateOrder(toOrderDTO(order));
        } catch (Exception e) {
            System.err.println("❌ Failed to add products to order: " + e.getMessage());
        }
    }

    @Override
    public orderDTO isOrderExists(int orderID) {
        if (orders.containsKey(orderID)) {
            return toOrderDTO(orders.get(orderID));
        }
        try {
            orderDTO dto = orderDAO.getOrderById(orderID);
            if (dto == null) return null;
            LocalDate recDate = null;
            LocalDate orderDate = null;
            if (dto.orderRecDate() != null && dto.orderRecDate().length == 3) {
                recDate = LocalDate.of(dto.orderRecDate()[0], dto.orderRecDate()[1], dto.orderRecDate()[2]);
            }
            if (dto.orderDate() != null && dto.orderDate().length == 3) {
                orderDate = LocalDate.of(dto.orderDate()[0], dto.orderDate()[1], dto.orderDate()[2]);
            }
            Order order = new Order(
                    dto.orderID(),
                    orderDate,
                    recDate,
                    dto.supplierID(),
                    dto.orderStatus(),
                    dto.totalPrice(),
                    dto.orderType(),
                    dto.address(),
                    dto.contactID()
            );
            orders.put(orderID, order);

            List<orderItemDTO> items = orderDAO.getOrderItemsByOrderID(orderID);
            for (orderItemDTO itemDTO : items) {
                orderItem item = new orderItem(
                        itemDTO.quantity(),
                        itemDTO.price(),
                        itemDTO.name(),
                        itemDTO.productID(),
                        itemDTO.orderID()
                );
                orderItems.computeIfAbsent(orderID, k -> new HashMap<>()).put(itemDTO.productID(), item);
            }

            return dto;
        } catch (Exception e) {
            System.err.println("❌ Failed to check order existence: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean checkIfProductInOrder(int orderID, int productID) {
        return orderItems.getOrDefault(orderID, new HashMap<>()).containsKey(productID);
    }

    @Override
    public double getOrderTotalPrice(int orderID) {
        return orders.get(orderID).getOrderPrice();
    }

    @Override
    public void displayActive() {
        for (Order order : orders.values()) {
            if (!order.getOrderStatus()) {
                System.out.println(order);
                orderItems.getOrDefault(order.getOrderID(), Map.of()).values().forEach(System.out::println);
            }
        }
    }

    @Override
    public void deleteProductFromOrder(orderDTO orderDTO, supplierProductDTO spDTO) {
        try {
            int orderId = orderDTO.orderID();
            int productId = spDTO.productID();
            Map<Integer, orderItem> items = orderItems.get(orderId);
            if (items != null && items.containsKey(productId)) {
                items.remove(productId);
                int quantity = orderDAO.deleteOrderItem(orderId, productId);
                Order order = orders.get(orderId);
                order.editTotalPrice(-quantity*spDTO.price());
                orderDAO.updateOrder(toOrderDTO(order));
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to delete product from order: " + e.getMessage());
        }
    }

    @Override
    public void changeProductQuantity(orderDTO orderDTO, supplierProductDTO spDTO, int quantity) {
        try {
            int orderId = orderDTO.orderID();
            int productId = spDTO.productID();
            orderItem item = orderItems.getOrDefault(orderId, Map.of()).get(productId);
            int prevQuantity = item.getQuantity();
            if (item != null) {
                item.setQuantity(quantity);
                Order order = orders.get(orderId);
                order.editTotalPrice((quantity - prevQuantity)*spDTO.price());
                orderDAO.updateOrder(toOrderDTO(order));
                orderDAO.updateOrderItem(toOrderItemDTO(item));
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to change product quantity: " + e.getMessage());
        }
    }

    @Override
    public void changeOrderStatus(orderDTO orderDTO) {
        try {
            Order order = orders.get(orderDTO.orderID());
            if (order != null) {
                order.setOrderStatus();
                orderDAO.updateOrder(toOrderDTO(order));
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to change order status: " + e.getMessage());
        }
    }

    @Override
    public void deleteOrder(orderDTO orderDTO) {
        int orderId = orderDTO.orderID();

        try {
            Map<Integer, orderItem> items = orderItems.getOrDefault(orderId, Map.of());
            for (Integer productId : items.keySet()) {
                try {
                    orderDAO.deleteOrderItem(orderId, productId);
                } catch (Exception e) {
                    System.err.println("❌ Failed to delete order item (orderID: " + orderId + ", productID: " + productId + "): " + e.getMessage());
                }
            }

            orderDAO.deleteOrder(orderId);
            orderItems.remove(orderId);
            orders.remove(orderId);

        } catch (Exception e) {
            System.err.println("❌ Failed to delete order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getOrderItemQuantity(orderDTO orderDTO, supplierProductDTO spDTO) {
        return orderItems.getOrDefault(orderDTO.orderID(), Map.of())
                .getOrDefault(spDTO.productID(), new orderItem(0, 0, "", 0, 0))
                .getQuantity();
    }

    private int[] dateToArray(LocalDate date) {
        if(date == null)return null;
        return new int[]{date.getYear(), date.getMonthValue(), date.getDayOfMonth()};
    }

    private orderDTO toOrderDTO(Order order) {
        return new orderDTO(
                order.getOrderID(),
                dateToArray(order.getOrderDate()),
                dateToArray(order.getOrderReceivingDate()),
                order.getOrderSupplierID(),
                order.getOrderStatus(),
                order.getOrderPrice(),
                order.getOrderType(),
                order.getOrderAddress(),
                order.getContactID()
        );
    }

    private orderItemDTO toOrderItemDTO(orderItem item) {
        return new orderItemDTO(
                item.getOrderID(),
                item.getProductId(),
                item.getQuantity(),
                (int)item.getPrice(),
                item.getName()
        );
    }
}
