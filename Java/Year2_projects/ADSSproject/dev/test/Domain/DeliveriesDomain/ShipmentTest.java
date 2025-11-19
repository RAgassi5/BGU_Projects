package Domain.DeliveriesDomain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentTest {

    @Test
    void testGetShipmentWeight_withNoProducts_shouldReturnZero() {
        Location origin = new Location("OriginContact","ContactNum","Origin Address", 10, District.South );
        Location destination = new Location("DesContact","ContactNum","Des Address", 22, District.South);
        Shipment shipment = new Shipment(origin, destination, null);
        assertEquals(0, shipment.getShipmentWeight());
    }

    @Test
    void testGetShipmentWeight_withSingleProduct_shouldReturnCorrectWeight() {
        Location origin = new Location("OriginContact","ContactNum","Origin Address", 10, District.South );
        Location destination = new Location("DesContact","ContactNum","Des Address", 22, District.South);
        Shipment shipment = new Shipment(origin, destination, null);

        Product product = new Product("Test Product",5);
        shipment.addProductToShipment(product, 10);
        assertEquals(50, shipment.getShipmentWeight());
    }

    @Test
    void testGetShipmentWeight_withMultipleProducts_shouldReturnCorrectTotalWeight() {
        Location origin = new Location("OriginContact","ContactNum","Origin Address", 10, District.South );
        Location destination = new Location("DesContact","ContactNum","Des Address", 22, District.South);
        Shipment shipment = new Shipment(origin, destination, null);

        Product p1 = new Product("Product 1",  2);
        Product p2 = new Product("Product 2",1);

        shipment.addProductToShipment(p1, 20);
        shipment.addProductToShipment(p2, 10);
        assertEquals(50, shipment.getShipmentWeight());
    }

    @Test
    void testGetShipmentWeight_afterRemovingProduct_shouldReturnUpdatedWeight() {
        Location origin = new Location("OriginContact","ContactNum","Origin Address", 10, District.South );
        Location destination = new Location("DesContact","ContactNum","Des Address", 22, District.South);
        Shipment shipment = new Shipment(origin, destination, null);

        Product p1 = new Product("Product 1",  2);
        Product p2 = new Product("Product 2", 1);

        shipment.addProductToShipment(p1, 8);
        shipment.addProductToShipment(p2, 9);
        shipment.removeProductFromShipment(p1);

        assertEquals(9, shipment.getShipmentWeight());
    }

    @Test
    void testUpdateStatus_shouldUpdateShipmentStatus() {
        Location origin = new Location("OriginContact","ContactNum","Origin Address", 10, District.South );
        Location destination = new Location("DesContact","ContactNum","Des Address", 22, District.South);
        Shipment shipment = new Shipment(origin, destination, null);

        shipment.updateStatus(s_status.DELIVERED);
        assertEquals(s_status.DELIVERED, shipment.getStatus());
    }

    @Test
    void testSetDepartureTime_shouldSetCurrentTime() {
        Location origin = new Location("OriginContact","ContactNum","Origin Address", 10, District.South );
        Location destination = new Location("DesContact","ContactNum","Des Address", 22, District.South);
        Shipment shipment = new Shipment(origin, destination, null);

        shipment.setDepartureTime();
        assertNotNull(shipment.getDepartureTime());
    }

    @Test
    void testSetDepartureTime_withSpecifiedTime_shouldUpdateTime() {
        Location origin = new Location("OriginContact","ContactNum","Origin Address", 10, District.South );
        Location destination = new Location("DesContact","ContactNum","Des Address", 22, District.South);
        Shipment shipment = new Shipment(origin, destination, null);

        LocalTime specified = LocalTime.of(10, 30);
        shipment.setDepartureTime(specified);
        assertEquals(specified, shipment.getDepartureTime());
    }

    @Test
    void testAddProductToShipment_shouldAddProductToList() {
        Location origin = new Location("OriginContact","ContactNum","Origin Address", 10, District.South );
        Location destination = new Location("DesContact","ContactNum","Des Address", 22, District.South);
        Shipment shipment = new Shipment(origin, destination, null);

        Product product = new Product("Test Product", 10);
        shipment.addProductToShipment(product, 1);

        assertTrue(shipment.getProductsInShipment().contains(product));
    }

    @Test
    void testRemoveProductFromShipment_shouldRemoveProductFromList() {
        Location origin = new Location("OriginContact","ContactNum","Origin Address", 10, District.South );
        Location destination = new Location("DesContact","ContactNum","Des Address", 22, District.South);
        Shipment shipment = new Shipment(origin, destination, null);

        Product product = new Product("Test Product", 5);
        shipment.addProductToShipment(product, 1);
        shipment.removeProductFromShipment(product);

        assertFalse(shipment.getProductsInShipment().contains(product));
    }


    @Test
    void testGetOriginAndDestinationLocations_shouldReturnCorrectValues() {
        Location origin = new Location("OriginContact","ContactNum","Origin Address", 10, District.South );
        Location destination = new Location("DesContact","ContactNum","Des Address", 22, District.South);
        Shipment shipment = new Shipment(origin, destination, null);

        assertEquals(origin, shipment.getOriginLocation());
        assertEquals(destination, shipment.getDestinationLocation());
    }
}