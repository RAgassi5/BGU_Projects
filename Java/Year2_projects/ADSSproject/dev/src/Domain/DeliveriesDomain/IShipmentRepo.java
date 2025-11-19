package Domain.DeliveriesDomain;

public interface IShipmentRepo {
    void addShippedShipment(Shipment currentShipment);
    void addSelfDeliveredShipment(Shipment currentShipment);
    Shipment getShipmentById(int id);
    void updateShipment(Shipment shipment);
}
