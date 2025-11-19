package Presentation.SuplierPresentation;

import DTO.SupplierDTO.orderDTO;
import DTO.SupplierDTO.supplierProductDTO;
import Service.SupplierService.AgreementService;
import Service.SupplierService.OrderService;
import Service.SupplierService.SupplierService;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class initializationSuppliers {

    private final SupplierService supplierService;
    private final AgreementService agreementService;
    private final OrderService orderService;

    public initializationSuppliers(SupplierService supplierService, AgreementService agreementService, OrderService orderService) {
        this.supplierService = supplierService;
        this.agreementService = agreementService;
        this.orderService = orderService;
    }

    public void initializeSystemData() {
        // --- Suppliers Initialization ---
        int cocaColaId = supplierService.createSupplier("Coca Cola", "123-456");
        int tempoId = supplierService.createSupplier("Tempo", "456-789");
        int tnuvaId = supplierService.createSupplier("Tnuva", "111-222");
        int nestleId = supplierService.createSupplier("Nestle", "333-444");
        int pepsiId = supplierService.createSupplier("Pepsi", "555-666");
        int heinzId = supplierService.createSupplier("Heinz", "777-888");
        int osemId = supplierService.createSupplier("Osem", "999-000");

        int[] supplierIds = {cocaColaId, tempoId, tnuvaId, nestleId, pepsiId, heinzId, osemId};
        String[] supplierNames = {"Coca Cola", "Tempo", "Tnuva", "Nestle", "Pepsi", "Heinz", "Osem"};

        for (int id : supplierIds) {
            supplierService.addContactToSupplier(id, "John Doe", "050-1111111");
            supplierService.addContactToSupplier(id, "Jane Smith", "050-2222222");
        }

// --- Products Data ---
        String[][] products = {
                {"Bissli Grill", "Osem", "Snacks", "Salty", "50 gr"},
                {"Natural Potato Chips", "Elite", "Snacks", "Potato Chips", "100 gr"},
                {"Para Chocolate", "Elite", "Sweets", "Chocolate", "200 gr"},
                {"Tnuva Milk 3%", "Tnuva", "Dairy Products", "Milk", "1 l"},
                {"Natural Yogurt", "Tnuva", "Dairy Products", "Yogurts", "150 gr"},
                {"Neviot Mineral Water", "Neviot", "Drinks", "Water", "500 ml"},
                {"Goldstar Beer", "Tempo", "Alcohol", "Beer", "500 ml"},
                {"Angel Sliced Bread", "Angel", "Bakery", "Bread", "600 gr"},
                {"Extra Virgin Olive Oil", "Yad Mordechai", "Basic Food", "Oils", "750 ml"}
        };

        DayOfWeek[] fixedDays = {DayOfWeek.SUNDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY};
        int catalogID = 1;

        for (int i = 0; i < 2; i++) {
            int supplierId = supplierIds[i];
            String supplierName = supplierNames[i];

            int flexibleId = supplierService.CreateFlexibleAgreement("Cash", supplierId, (i % 4) + 2);
            int fixedId = supplierService.CreateFixedAgreement("Credit", supplierId, new ArrayList<>(List.of(fixedDays)));

            for (int j = 0; j < products.length; j++) {
                String[] product = products[j];
                String productName = product[0];
                String manufacturer = product[1];
                String productType = product[2];
                String sizeCategory = product[4];

                int price = 5 + (i + j) % 30;
                boolean toFlexible = j < products.length / 2;
                int agreementID = toFlexible ? flexibleId : fixedId;

                boolean added = agreementService.addNewProductToAgreement(
                        catalogID,
                        price,
                        supplierId,
                        productName,
                        agreementID,
                        manufacturer,
                        productType,
                        sizeCategory
                );

                if (added && catalogID % 2 == 0) {
                    int discountQty = 10 + (catalogID % 5);
                    int discountPercent = 5 + (catalogID % 20);
                    agreementService.createDiscountForProduct(agreementID, catalogID, supplierId, discountQty, discountPercent);
                }

                catalogID++;
            }
        }
    }
}
