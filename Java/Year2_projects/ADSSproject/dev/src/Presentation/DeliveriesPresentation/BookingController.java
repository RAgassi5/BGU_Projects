package Presentation.DeliveriesPresentation;

import Domain.DeliveriesDomain.Shipment;
import Service.DeliveriesService.BookingSystem;

public class BookingController {
    private final BookingSystem bookingSystem;

    public BookingController() {
        this.bookingSystem = BookingSystem.getInstance();
    }

    public void bookADeliveredShipment(Shipment shipment) {
        this.bookingSystem.addNewShipment(shipment);
    }
    public void bookASelfShipment(Shipment shipment) {
        this.bookingSystem.addNewSelfShipment(shipment);
    }
    public void trackShipment(int shipmentID) {
        this.bookingSystem.displayShipment(shipmentID);
    }
}
