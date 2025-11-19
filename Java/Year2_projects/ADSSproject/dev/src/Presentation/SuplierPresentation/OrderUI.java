package Presentation.SuplierPresentation;

import DTO.SupplierDTO.orderDTO;
import DTO.SupplierDTO.supplierProductDTO;

import Service.SupplierService.OrderService;
import Service.SupplierService.SupplierService;
import java.util.Scanner;

public class OrderUI {
    private OrderService orderService;
    private SupplierService supplierService;
    static Scanner scanner = new Scanner(System.in);

    public OrderUI(OrderService orderService, SupplierService supplierService) {
        this.orderService = orderService;
        this.supplierService = supplierService;
    }

    public void manageOrder() {
        boolean run = true;
        while (run) {
            System.out.println("\n========= Order Management =========");
            System.out.println("1. Create a new order");
            System.out.println("2. Edit products in an order");
            System.out.println("3. Change order status");
            System.out.println("4. Delete open order");
            System.out.println("5. View order details by ID");
            System.out.println("6. View all active orders");
            System.out.println("7. View price offers on a product");
            System.out.println("8. Back to main menu");
            System.out.println("====================================");

            int choice = SuppliersMain.promptMenuChoice(8);
            switch (choice) {
                case 1 -> createNewOrder();
                case 2 -> editProductsInOrder(null);
                case 3 -> changeOrderStatusFlow();
                case 4 -> deleteOrderFlow();
                case 5 -> viewOrderDetails();
                case 6 -> viewAllActiveOrders();
                case 7 -> viewProductOffers();
                case 8 -> run = false;
            }
        }
    }

    private void createNewOrder() {
        int supplierID = SuppliersMain.promptForInt("Enter supplier ID:");
        if (!supplierService.doesSupplierExist(supplierID)) {
            System.out.println("Supplier does not exist.");
            return;
        }
        if(!orderService.supplierHasProducts(supplierID)){
            System.out.println("⚠️ Supplier with ID " + supplierID + " has no products in the system.");
            return;
        }
        System.out.println("Enter Contact Phone Number:");
        String contactPhoneNumber = scanner.nextLine();
        while (contactPhoneNumber.length() != 10) {
            System.out.println("Contact phone number must be 10 digits. Try again:");
            contactPhoneNumber = scanner.nextLine();
        }
        orderDTO newOrderDTO = orderService.createNewOrder(supplierID, contactPhoneNumber);
        System.out.println("Order " + newOrderDTO.orderID() + " was successfully added.");
        System.out.print("Would you like to add products to the order? (yes/no): ");
        String answer = scanner.nextLine().trim();
        while (!answer.equalsIgnoreCase("yes") && !answer.equalsIgnoreCase("no")) {
            System.out.print("Invalid input. Please type 'yes' or 'no': ");
            answer = scanner.nextLine().trim();
        }
        if (answer.equalsIgnoreCase("yes")) {
            editProductsInOrder(newOrderDTO);
        } else {
            System.out.println("The order is saved and waiting.");
        }
    }

    private void changeOrderStatusFlow() {
        int orderID = SuppliersMain.promptForInt("Enter Order ID:");
        orderDTO oDTO = orderService.checkIfOrderExists(orderID);
        if (oDTO == null) {
            System.out.println("There is no such order in the system.");
            return;
        }
        if (oDTO.orderStatus()) {
            System.out.println("Order: " + orderID + " is completed.");
            return;
        }
        System.out.println("Order: " + orderID + " is waiting.");
        System.out.print("Would you like to change the order status? (yes/no): ");
        String answer = scanner.nextLine().trim();
        if (answer.equalsIgnoreCase("yes")) {
            orderService.changeOrderStatus(oDTO);
            System.out.println("Order status changed.");
        }
    }

    private void deleteOrderFlow() {
        int orderID = SuppliersMain.promptForInt("Enter Order ID:");
        orderDTO oDTO = orderService.checkIfOrderExists(orderID);
        if (oDTO == null) {
            System.out.println("There is no such order in the system.");
            return;
        }
        if (oDTO.orderStatus()) {
            System.out.println("You can't delete a completed order.");
            return;
        }
        orderService.deleteOrder(oDTO);
        System.out.println("Order deleted successfully.");
    }

    private void viewOrderDetails() {
        int orderID = SuppliersMain.promptForInt("Enter Order ID:");
        orderDTO oDTO = orderService.checkIfOrderExists(orderID);
        if (oDTO == null) {
            System.out.println("There is no such order in the system.");
            return;
        }
        System.out.println(oDTO);
    }

    private void viewAllActiveOrders() {
        orderService.displayActiveOrders();
    }

    private void viewProductOffers() {
        int productID = SuppliersMain.promptForInt("Enter product ID:");
        showProductsPrices(productID);
    }

    public void editProductsInOrder(orderDTO oDTO) {
        if (oDTO == null) {
            int orderID = SuppliersMain.promptForInt("Enter Order ID:");
            oDTO = orderService.checkIfOrderExists(orderID);
        }
        if (oDTO == null) {
            System.out.println("❌ There is no such order in the system.");
            return;
        }

        while (true) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1. View price offers on a product");
            System.out.println("2. Add product to order");
            System.out.println("3. Delete product from order");
            System.out.println("4. Exit");
            System.out.println("5. Edit existing product in order");

            int choice = SuppliersMain.promptMenuChoice(5);

            if (choice == 4) {
                System.out.println("Exiting product editing.");
                break;
            }

            int productID = SuppliersMain.promptForInt("Enter product ID:");

            if (choice == 1 || choice == 2 || choice == 5) {
                supplierProductDTO spDTO = supplierService.checkIfProductExist(oDTO.supplierID(), productID);
                if (spDTO == null) {
                    System.out.println("❌ The supplier does not provide this product.");
                    continue;
                }

                if (!(oDTO.orderType() == null || oDTO.orderType().equals(spDTO.agreementType()))) {
                    System.out.println("❌ The product belongs to an agreement type that doesn't match the order type: " + oDTO.orderType());
                    continue;
                }

                if (choice == 1) {
                    showProductsPrices(productID);
                } else if (choice == 2) {
                    if (orderService.checkIfProductInOrder(oDTO.orderID(), productID)) {
                        editExistingProductsInOrder(oDTO, spDTO);
                    } else {
                        int quantity = SuppliersMain.promptForInt("Enter quantity:");
                        orderService.addProductToOrder(oDTO, spDTO, quantity);
                        System.out.println("✅ Product " + productID + " with quantity " + quantity + " was added.");
                        System.out.println("💰 Updated order price: " + orderService.getOrderTotalPrice(oDTO.orderID()) + "₪");
                    }
                } else if (choice == 5) {
                    if (orderService.checkIfProductInOrder(oDTO.orderID(), productID)) {
                        editExistingProductsInOrder(oDTO, spDTO);
                    } else {
                        System.out.println("❌ Product is not part of this order, so it cannot be edited.");
                    }
                }

            } else if (choice == 3) {
                if (!orderService.checkIfProductInOrder(oDTO.orderID(), productID)) {
                    System.out.println("❌ Product is not part of this order.");
                    continue;
                }

                supplierProductDTO spDTO = supplierService.checkIfProductExist(oDTO.supplierID(), productID);
                if (spDTO == null) {
                    System.out.println("❌ Could not retrieve product details from supplier.");
                    continue;
                }

                orderService.deleteProductFromOrder(oDTO, spDTO);
                System.out.println("✅ Product " + productID + " was removed from the order.");
                System.out.println("💰 Updated order price: " + orderService.getOrderTotalPrice(oDTO.orderID()) + "₪");
            }
        }
    }


    public void editExistingProductsInOrder(orderDTO OrderDTO, supplierProductDTO spDTO) {
        System.out.println("The item is in the order, please choose an option:");
        System.out.println("1. Delete product from order");
        System.out.println("2. Change quantity");
        System.out.println("3. Get item quantity");
        System.out.println("4. Exit");
        int choice = SuppliersMain.promptMenuChoice(4);
        switch (choice) {
            case 1 -> {
                orderService.deleteProductFromOrder(OrderDTO, spDTO);
                System.out.println("The product: " + spDTO.productID() + " removed.");
                System.out.println("The updated order price is: " + orderService.getOrderTotalPrice(OrderDTO.orderID()) + "₪");
            }
            case 2 -> {
                int quantity = SuppliersMain.promptForInt("Insert quantity:");
                orderService.changeProductQuantity(OrderDTO, spDTO, quantity);
                System.out.println("The product: " + spDTO.productID() + " quantity has been changed to: " + quantity + ".");
                System.out.println("The updated order price is: " + orderService.getOrderTotalPrice(OrderDTO.orderID()) + "₪");
            }
            case 3 -> System.out.println("The product: " + spDTO.productID() + " quantity is: " + orderService.getProductQuantity(OrderDTO, spDTO));
            case 4 -> {
            }
        }
    }

    private void showProductsPrices(int productID) {
        int quantity = SuppliersMain.promptForInt("Insert quantity:");
        orderService.showProductsPrices(productID, quantity);
    }
}
