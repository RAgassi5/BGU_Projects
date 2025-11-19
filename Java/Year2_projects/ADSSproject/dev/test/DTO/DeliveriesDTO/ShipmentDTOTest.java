package DTO.DeliveriesDTO;

import Domain.DeliveriesDomain.Address;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShipmentDTOTest {

    /**
     * This class tests the methods of the ShipmentDTO class.
     * The main focus is on ensuring the functionality of methods such as adding products,
     * setting and retrieving shipment details, and clearing product lists.
     */

    @Test
    public void testGetShipmentID() {
        // Arrange
        ShipmentDTO shipment = new ShipmentDTO(
                1,
                new LocationDTO(1, "Origin1", "dummy", null),
                new LocationDTO(2, "Destination1", "dummy", null),
                LocalDate.of(2025, 6, 3),
                LocalTime.of(10, 30),
                "Pending"
        );

        // Act & Assert
        assertEquals(1, shipment.getShipmentID());
    }

    @Test
    public void testSetShipmentID() {
        // Arrange
        ShipmentDTO shipment = new ShipmentDTO(
                1,
                new LocationDTO(1, "Origin1", "dummy", null),
                new LocationDTO(2, "Destination1", "dummy", null),
                LocalDate.of(2025, 6, 3),
                LocalTime.of(10, 30),
                "Pending"
        );

        // Act
        shipment.setShipmentID(2);

        // Assert
        assertEquals(2, shipment.getShipmentID());
    }

    @Test
    public void testGetSetOrigin() {
        // Arrange
        LocationDTO origin = new LocationDTO(1, "Origin1", "Address1", null);
        ShipmentDTO shipment = new ShipmentDTO(
                1,
                origin,
                new LocationDTO(2, "Destination1", "dummy", null),
                LocalDate.of(2025, 6, 3),
                LocalTime.of(10, 30),
                "Pending"
        );
        LocationDTO newOrigin = new LocationDTO(3, "NewOrigin", "NewAddress1", null);

        // Act
        shipment.setOrigin(newOrigin);

        // Assert
        assertEquals(newOrigin, shipment.getOrigin());
    }

    @Test
    public void testGetSetDestination() {
        // Arrange
        LocationDTO destination = new LocationDTO(2, "Destination1", "Address2", null);
        ShipmentDTO shipment = new ShipmentDTO(
                1,
                new LocationDTO(1, "Origin1", "dummy", null),
                destination,
                LocalDate.of(2025, 6, 3),
                LocalTime.of(10, 30),
                "Pending"
        );
        LocationDTO newDestination = new LocationDTO(4, "NewDestination", "NewAddress2", null);

        // Act
        shipment.setDestination(newDestination);

        // Assert
        assertEquals(newDestination, shipment.getDestination());
    }

    @Test
    public void testGetSetShipmentDate() {
        // Arrange
        ShipmentDTO shipment = new ShipmentDTO(
                1,
                new LocationDTO(1, "Origin1", "Address1", null),
                new LocationDTO(2, "Destination1", "Address2", null),
                LocalDate.of(2025, 6, 3),
                LocalTime.of(10, 30),
                "Pending"
        );

        // Act
        shipment.setShipmentDate(1, 7, 2025);

        // Assert
        assertEquals(LocalDate.of(2025, 7, 1), shipment.getShipmentDate());
    }

    @Test
    public void testGetSetShipmentTime() {
        // Arrange
        ShipmentDTO shipment = new ShipmentDTO(
                1,
                new LocationDTO(1, "Origin1", "dummy", null),
                new LocationDTO(2, "Destination1", "dummy", null),
                LocalDate.of(2025, 6, 3),
                LocalTime.of(10, 30),
                "Pending"
        );

        // Act
        shipment.setShipmentTime(12, 45);

        // Assert
        assertEquals(LocalTime.of(12, 45), shipment.getShipmentTime());
    }

    @Test
    public void testGetSetShipmentStatus() {
        // Arrange
        ShipmentDTO shipment = new ShipmentDTO(
                1,
                new LocationDTO(1, "Origin1", "dummy", null),
                new LocationDTO(2, "Destination1", "dummy", null),
                LocalDate.of(2025, 6, 3),
                LocalTime.of(10, 30),
                "Pending"
        );

        // Act
        shipment.setShipmentStatus("Delivered");

        // Assert
        assertEquals("Delivered", shipment.getShipmentStatus());
    }

    @Test
    public void testClearProducts() {
        // Arrange
        ShipmentDTO shipment = new ShipmentDTO(
                1,
                new LocationDTO(1, "Origin1", "dummy", null),
                new LocationDTO(2, "Destination1", "dummy", null),
                LocalDate.of(2025, 6, 3),
                LocalTime.of(10, 30),
                "Pending"
        );
        shipmentProductDTO product1 = new shipmentProductDTO("Product1", 2, 5);
        shipmentProductDTO product2 = new shipmentProductDTO("Product2", 3, 7);

        shipment.addProductToShipment(product1);
        shipment.addProductToShipment(product2);

        // Act
        shipment.clearProducts();

        // Assert
        assertNotNull(shipment.getProductsInShipment());
        assertTrue(shipment.getProductsInShipment().isEmpty());
    }

    @Test
    public void testGetSetProductsInShipment() {
        // Arrange
        ShipmentDTO shipment = new ShipmentDTO(
                1,
                new LocationDTO(1, "Origin1", "dummy", null),
                new LocationDTO(2, "Destination1", "dummy", null),
                LocalDate.of(2025, 6, 3),
                LocalTime.of(10, 30),
                "Pending"
        );
        ArrayList<shipmentProductDTO> productList = new ArrayList<>();
        productList.add(new shipmentProductDTO("Product1", 1, 2));
        productList.add(new shipmentProductDTO("Product2", 2, 3));

        // Act
        shipment.setProductsInShipment(productList);

        // Assert
        assertEquals(productList, shipment.getProductsInShipment());
        assertEquals(2, shipment.getProductsInShipment().size());
    }
}