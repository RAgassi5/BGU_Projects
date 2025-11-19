package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.LocationDTO;
import DTO.DeliveriesDTO.shipmentProductDTO;
import DTO.DeliveriesDTO.ShipmentDTO;
import DataAccess.DeliveriesDAO.IShipmentDAO;
import DataAccess.DeliveriesDAO.ShipmentDAO;

import java.sql.SQLException;
import java.util.Hashtable;

public class ShipmentsRepo implements IShipmentRepo {
    private ILocationRepo locationRepo;
    private Hashtable<Integer, Shipment> shipmentsHistory;
    private IShipmentDAO shipmentDAO;


    public ShipmentsRepo() {
        this.locationRepo =  new LocationsRepo();
        this.shipmentsHistory = new Hashtable<>();
        this.shipmentDAO = new ShipmentDAO();
    }

    /**
     * Adds a new shipped shipment entry to the repository. This process involves
     * converting the shipment's data into corresponding DTO objects, storing
     * relevant location data, adding products associated with the shipment,
     * and persisting the shipment into the data source. The new shipment ID
     * is then updated in the original shipment object, and the shipment
     * is recorded in the local shipment history.
     *
     * @param currentShipment the shipment object containing the shipment's
     *                        details, including origin and destination
     *                        locations, products, shipment date, and status
     */
    @Override
    public void addShippedShipment(Shipment currentShipment) {
        try {
            LocationDTO originDTO = new LocationDTO(
                    currentShipment.getOriginLocation().getLocationId(),
                    currentShipment.getOriginLocation().getContactName(),
                    currentShipment.getOriginLocation().getContactNUM(),
                    currentShipment.getOriginLocation().getAddress()
            );

            LocationDTO destinationDTO = new LocationDTO(
                    currentShipment.getDestinationLocation().getLocationId(),
                    currentShipment.getDestinationLocation().getContactName(),
                    currentShipment.getDestinationLocation().getContactNUM(),
                    currentShipment.getDestinationLocation().getAddress()
            );

            this.locationRepo.addLocation(originDTO);
            this.locationRepo.addLocation(destinationDTO);

            ShipmentDTO shipmentToDB = new ShipmentDTO(
                    0,
                    originDTO,
                    destinationDTO,
                    currentShipment.getShipmentDate(),
                    currentShipment.getDepartureTime(),
                    currentShipment.getStatus().toString()
            );

            // Add products to ShipmentDTO
            for (Product product : currentShipment.getProductsInShipment()) {
                shipmentProductDTO shipmentProductDTO = new shipmentProductDTO(
                        product.getName(),
                        product.getWeightPerUnit(),
                        product.getAmount()

                );
                shipmentToDB.addProductToShipment(shipmentProductDTO);
            }

            int newID = this.shipmentDAO.addShippedShipment(shipmentToDB);
            currentShipment.setShipmentId(newID);
            this.shipmentsHistory.put(currentShipment.getId(), currentShipment);

        } catch (SQLException e) {
            System.out.println("Failed to add shipment");
        }
    }


    /**
     * Adds a new self-delivered shipment to the repository. This method processes
     * the shipment by converting its data into corresponding DTO objects for both
     * locations and products. It also saves the shipment's data to the database, assigns
     * a new shipment ID, and records the shipment in the local history.
     *
     * @param currentShipment the shipment object containing details such as origin and
     *                        destination locations, products included in the shipment,
     *                        shipment date, and status to be added as a self-delivered shipment
     */
    @Override
    public void addSelfDeliveredShipment(Shipment currentShipment) {
        try {
            LocationDTO originDTO = new LocationDTO(
                    currentShipment.getOriginLocation().getLocationId(),
                    currentShipment.getOriginLocation().getContactName(),
                    currentShipment.getOriginLocation().getContactNUM(),
                    currentShipment.getOriginLocation().getAddress()
            );

            LocationDTO destinationDTO = new LocationDTO(
                    currentShipment.getDestinationLocation().getLocationId(),
                    currentShipment.getDestinationLocation().getContactName(),
                    currentShipment.getDestinationLocation().getContactNUM(),
                    currentShipment.getDestinationLocation().getAddress()
            );

            ShipmentDTO shipmentToDB = new ShipmentDTO(
                    0,
                    originDTO,
                    destinationDTO,
                    currentShipment.getShipmentDate(),
                    currentShipment.getDepartureTime(),
                    currentShipment.getStatus().toString()
            );

            for (Product product : currentShipment.getProductsInShipment()) {
                shipmentProductDTO shipmentProductDTO = new shipmentProductDTO(
                        product.getName(),
                        product.getWeightPerUnit(),
                        product.getAmount()
                );
                shipmentToDB.addProductToShipment(shipmentProductDTO);
            }

            int newID = this.shipmentDAO.addSelfDeliveredShipment(shipmentToDB);
            currentShipment.setShipmentId(newID);
            this.shipmentsHistory.put(currentShipment.getId(), currentShipment);

        } catch (SQLException e) {
            System.out.println("Failed to add shipment");
        }

    }

    /**
     * Retrieves a shipment by its unique identifier. If the shipment is available
     * in the local history, it is returned directly. Otherwise, the shipment data
     * is fetched from the data source, converted to a Shipment object, and stored
     * in the local history for future use.
     *
     * @param id the unique identifier of the shipment to be retrieved
     * @return the Shipment object associated with the given ID, or null if the shipment
     *         does not exist in the local history or database
     */
    @Override
    public Shipment getShipmentById(int id) {
        try{
            if(this.shipmentsHistory.containsKey(id)){
                return this.shipmentsHistory.get(id);
            }
            ShipmentDTO retrievedFromDB = this.shipmentDAO.getShipmentById(id);
            if (retrievedFromDB == null) {
                System.out.println("No shipment data found in database for ID: " + id);
                return null;
            }
            Shipment toReturn = new Shipment(retrievedFromDB);
            this.shipmentsHistory.put(toReturn.getId(), toReturn);
            return toReturn;

        }catch (SQLException e){
            System.out.println("Failed to retrieve shipment");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates an existing shipment with the provided details. This method ensures
     * the shipment exists either in the local history or in the database before
     * updating it. Converts the shipment into a DTO object, updates its data in
     * the database, and records the updated shipment in the local history.
     *
     * @param shipment the shipment object to be updated. Contains updated details
     *                 including origin and destination locations, shipment date,
     *                 departure time, status, and associated products.
     */
    @Override
    public void updateShipment(Shipment shipment) {
        try {
            if (!shipmentsHistory.containsKey(shipment.getId())) {
                Shipment existingShipment = getShipmentById(shipment.getId());
                if (existingShipment == null) {
                    throw new SQLException("No shipment found with ID: " + shipment.getId());
                }
            }

            ShipmentDTO shipmentDTO = new ShipmentDTO(
                    shipment.getId(),
                    new LocationDTO(
                            shipment.getOriginLocation().getLocationId(),
                            shipment.getOriginLocation().getContactName(),
                            shipment.getOriginLocation().getContactNUM(),
                            shipment.getOriginLocation().getAddress()
                    ),
                    new LocationDTO(
                            shipment.getDestinationLocation().getLocationId(),
                            shipment.getDestinationLocation().getContactName(),
                            shipment.getDestinationLocation().getContactNUM(),
                            shipment.getDestinationLocation().getAddress()
                    ),
                    shipment.getShipmentDate(),
                    shipment.getDepartureTime(),
                    shipment.getStatus().toString()
            );

            for (Product p : shipment.getProductsInShipment()) {
                shipmentDTO.addProductToShipment(new shipmentProductDTO(
                        p.getName(),
                        p.getWeightPerUnit(),
                        p.getAmount()
                ));
            }

            shipmentDAO.save(shipmentDTO);

            shipmentsHistory.put(shipment.getId(), shipment);


        } catch (SQLException e) {
            System.out.println("Error updating shipment info in database for ID: " + shipment.getId());
            e.printStackTrace();
        }
    }

}
