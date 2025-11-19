package Service.SupplierService;

import DataAccess.SupplierDAO.*;
import Domain.SupplierDomain.*;
import DTO.SupplierDTO.orderDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SupplierTest {

    static SupplierService supplierService;
    static AgreementService agreementService;
    static OrderService orderService;

    static supplierRepository supplierRepo;
    static supplierProductRepository supplierProductRepo;
    static agreementRepository agreementRepo;
    static OrderRepository orderRepo;

    static int supplierId;
    static int agreementId;

    @BeforeAll
    public static void setup() {
        // DAO
        AgreementDAO agreementDAO = new jdbcAgreementDAO();
        supplierDAO supplierDAO = new jdbcSupplierDAO();
        supplierProductDAO supplierProductDAO = new jdbcSupplierProductDAO();
        orderDAO orderDAO = new jdbcOrderDAO();

        // Repository
        agreementRepo = new AgreementRepositoryIMP(agreementDAO);
        supplierProductRepo = new SupplierProductRepositoryIMP(supplierProductDAO, agreementRepo, null, orderDAO);
        supplierRepo = new SupplierRepositoryIMP(supplierDAO, supplierProductDAO, agreementRepo, supplierProductRepo);
        orderRepo = new OrderRepositoryIMP(orderDAO, agreementRepo);
        supplierProductRepo.setSupplierRepo(supplierRepo);

        // Services
        agreementService = new AgreementService(supplierRepo, supplierProductRepo, agreementRepo);
        orderService = new OrderService(supplierProductRepo, orderRepo, supplierRepo);
        supplierService = new SupplierService(supplierRepo, supplierProductRepo, agreementRepo);
    }

    @Test
    @Order(1)
    public void testCreateSupplier() {
        supplierId = supplierService.createSupplier("JUnitSupplier", "IL1234567890");
        assertTrue(supplierId > 0);
    }
    @Test
    @Order(2)
    public void testCreatePickupAgreement() {
        agreementId = supplierService.CreatePickUpAgreement("Cash", supplierId, "Hertzel 99");
        assertTrue(agreementId > 0);
    }

    @Test
    @Order(3)
    public void testCreateFixedAgreement() {
        ArrayList<DayOfWeek> days = new ArrayList<>(List.of(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY));
        int fixedAgreementId = supplierService.CreateFixedAgreement("Card", supplierId, days);
        assertTrue(fixedAgreementId > 0);
    }

    @Test
    @Order(4)
    public void testCreateFlexibleAgreement() {
        int flexibleAgreementId = supplierService.CreateFlexibleAgreement("Transfer", supplierId, 5);
        assertTrue(flexibleAgreementId > 0);
    }
    @Test
    @Order(5)
    public void testAddNewProductToAgreement() {

        boolean success = agreementService.addNewProductToAgreement(
                1010,                // catalogID
                9.99,                // price
                supplierId,          // supplierID
                "Water",             // productName
                agreementId,                   // agreementID
                "Nestle",            // manufacturer
                "Drink",             // productType
                "1.5L"               // weight
        );
        assertTrue(!success);
    }



    @Test
    @Order(6)
    public void testSupplierExists() {
        assertTrue(supplierService.doesSupplierExist(supplierId));
    }

    @Test
    @Order(7)
    public void testAddContactToSupplier() {
        assertDoesNotThrow(() -> supplierService.addContactToSupplier(supplierId, "Test Person", "0500000000"));
    }


    @Test
    @Order(8)
    public void testCreateNewOrder() {
        orderDTO newOrder = orderService.createNewOrder(supplierId, "0504641416");
        assertNotNull(newOrder);
        assertEquals(supplierId, newOrder.supplierID());
    }


    @Test
    @Order(9)
    public void testChangeBankAccount() {
        assertDoesNotThrow(() -> supplierService.changeBankAccountNumber(supplierId, "IL0000000001"));
    }

    @Test
    @Order(10)
    public void testChangeStatus() {
        boolean result = supplierService.changeStatusService(supplierId);
        assertTrue(result);
    }


}
