package Domain.DeliveriesDomain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Route {
    private static class DrivingRoute {
        /**
         * a class representing a Node in a linked list
         */
        private class Node {
            private Shipment shipmentData;
            private Node nextNode;
            private Node prevNode;

            /**
             * a constructor for the Node class
             *
             * @param shipmentData the shipment to be added
             */
            public Node(Shipment shipmentData) {
                this.shipmentData = shipmentData;
            }

            /**
             * a getter function for the shipment data held inside the Node
             *
             * @return an instance of a Shipment
             */
            public Shipment getShipmentData() {
                return shipmentData;
            }

            /**
             * a setter function for the next Node
             *
             * @param nextNode the Node that will be placed next in order
             */
            public void setNextNode(Node nextNode) {
                this.nextNode = nextNode;
            }

            /**
             * a getter function for the next node
             *
             * @return an instance of a Node
             */
            public Node getNextNode() {
                return nextNode;
            }

            /**
             * a setter function for the previous Node
             *
             * @param prevNode the Node to be placed before the Node
             */
            public void setPrevNode(Node prevNode) {
                this.prevNode = prevNode;
            }

            /**
             * a getter function for the previous Node
             *
             * @return an instance of a Node
             */
            public Node getPrevNode() {
                return prevNode;
            }
        }

        private Node start;
        private Node end;
        private Node currentShipment;
        private int amountOfStops;
        private IShipmentRepo shipmentRepo;

        public DrivingRoute(IShipmentRepo shipmentRepo) {
            this.shipmentRepo = shipmentRepo;
            this.start = null;
            this.end = null;
            this.currentShipment = null;
            this.amountOfStops = 0;
        }

        /**
         * a function that adds a stop along the route of the shipment vehicle
         *
         * @param shipmentData the Shipment to be added to the route
         */
        public void addStop(Shipment shipmentData) {
            Node stopToAdd = new Node(shipmentData);
            if (this.amountOfStops == 0) {
                this.start = stopToAdd;
                this.end = this.start;
                this.currentShipment = this.start;
            } else {
                Node currentNode = this.start;
                while (currentNode.getNextNode() != null) {
                    currentNode = currentNode.getNextNode();
                }
                currentNode.setNextNode(stopToAdd);
                stopToAdd.setPrevNode(currentNode);
                this.end = stopToAdd;
            }
            this.amountOfStops++;
        }

        /**
         * a getter function for the shipment that is being delivered right now
         *
         * @return an instance of a Shipment that is being delivered right now
         */
        public Shipment getCurrentShipment() {
            return this.currentShipment.getShipmentData();
        }


        /**
         * a function that searches for a specific shipment in a route
         *
         * @param shipmentId the shipment's ID
         * @return boolean
         */

        public boolean searchForShipmentById(int shipmentId) {
            if (shipmentId <= 0) {
                return false;
            }
            Node currentNode = this.start;

            while (currentNode != null) {
                if (currentNode.shipmentData.getId() == shipmentId) {
                    return true;
                } else {
                    currentNode = currentNode.getNextNode();
                }
            }
            return false;
        }

        /**
         * a getter function for a specific function
         *
         * @param shipmentId the id of the wanted shipment
         * @return the looked for shipment
         */
        public Shipment getShipmentById(int shipmentId) {
            if (shipmentId <= 0) {
                return null;
            }
            Node currentNode = this.start;
            while (currentNode != null) {
                if (currentNode.shipmentData.getId() == shipmentId) {
                    return currentNode.getShipmentData();
                }
                currentNode = currentNode.getNextNode();
            }
            return null;
        }

        /**
         * a setter function for the route status
         *
         * @param status the shipment's status
         */
        public void updateAllShipmentsStatusInRoute(s_status status) {
            Node currentNode = this.start;
            while (currentNode != null) {
                Shipment shipment = currentNode.getShipmentData();
                shipment.updateStatus(status);
                this.shipmentRepo.updateShipment(shipment);
                currentNode = currentNode.getNextNode();
            }
        }

        /**
         * a getter function for the driving route's total weight
         *
         * @return the total weight
         */
        public int getTotalWeight() {
            int routeTotalWeight = 0;
            Node currentNode = this.start;
            while (currentNode != null) {
                routeTotalWeight += currentNode.shipmentData.getShipmentWeight();
                currentNode = currentNode.getNextNode();
            }
            return routeTotalWeight;
        }

        /**
         * a function that returns the route's status
         *
         * @return the route's current status
         */
        public s_status getStatus() {
            return this.start.shipmentData.getStatus();
        }

        /**
         * a function that prints all products in a route
         */
        public void printAllRouteProducts() {
            Node currentNode = this.start;
            while (currentNode != null) {
                System.out.println("\n=== Products in shipment " + currentNode.shipmentData.getId() + " ===");
                System.out.println("Shipment " + currentNode.getShipmentData().getId() + " current weight: " + currentNode.shipmentData.getShipmentWeight()+
                        "\n");
                currentNode.shipmentData.printShipmentProducts();
                currentNode = currentNode.getNextNode();
            }
        }

        /**
         * a function that sets all shipment's departure time
         */
        public void setDepTime() {
            LocalTime departureTime = LocalTime.now();
            Node currentNode = this.start;

            while (currentNode != null) {
                Shipment shipment = currentNode.getShipmentData();
                shipment.setDepartureTime(departureTime);
                shipment.updateStatus(s_status.IN_PROGRESS);
                this.shipmentRepo.updateShipment(shipment);
                currentNode = currentNode.getNextNode();
            }
        }

        /**
         * Retrieves the departure time of the shipment associated with the starting node of the route.
         *
         * @return a LocalTime object representing the departure time of the shipment
         */
        public LocalTime getDepartureTime() {
            return this.start.getShipmentData().getDepartureTime();
        }


        /**
         * a function that prints all shipments ID's of driving route
         */
        public void printAllShipmentIds() {
            Node currentNode = this.start;
            System.out.println("=== Shipment IDs in Route ===");
            while (currentNode != null) {
                System.out.println("Shipment ID: " + currentNode.getShipmentData().getId());
                currentNode = currentNode.getNextNode();
            }
        }

        /**
         * a function that removes a shipment from the route
         *
         * @param shipmentId the shipment's ID to be removed
         * @return boolean
         */
        public boolean removeShipmentById(int shipmentId) {
            if (!this.searchForShipmentById(shipmentId)) {
                return false;
            }

            // If removing the first node
            if (this.start.shipmentData.getId() == shipmentId) {
                if (this.start == this.end) {
                    // Only one node in the list
                    this.start = null;
                    this.end = null;
                } else {
                    // More than one node, removing first
                    this.start = this.start.getNextNode();
                    this.start.setPrevNode(null);
                }
                this.amountOfStops--;
                return true;
            }

            // For other nodes
            Node currentShipment = this.start;
            while (currentShipment != null && currentShipment.getNextNode() != null) {
                if (currentShipment.getNextNode().getShipmentData().getId() == shipmentId) {
                    Node toRemove = currentShipment.getNextNode();
                    Node toConnect = toRemove.getNextNode();

                    if (toConnect == null) {
                        // Removing last node
                        this.end = currentShipment;
                        currentShipment.setNextNode(null);
                    } else {
                        // Removing middle node
                        currentShipment.setNextNode(toConnect);
                        toConnect.setPrevNode(currentShipment);
                    }
                    this.amountOfStops--;
                    return true;
                }
                currentShipment = currentShipment.getNextNode();
            }

            return false;
        }

    }

    private DrivingRoute CurrentdrivingRoute;
    private Driver currentDriver;
    private Vehicle currentVehicle;
    private District currentShipmentDistrict;
    private LocalDate routeDate;
    private IShipmentRepo shipmentRepo;

    public Route(IShipmentRepo shipmentRepo) {
        this.CurrentdrivingRoute = new DrivingRoute(shipmentRepo);
        this.currentDriver = null;
        this.currentVehicle = null;
        this.shipmentRepo = shipmentRepo;
    }

    /**
     * a fucntion that adds a shipment to the current Route
     *
     * @param shipmentData the shipment to be added
     */
    public void addShipment(Shipment shipmentData) {
        if (this.CurrentdrivingRoute.amountOfStops == 0) {
            this.currentShipmentDistrict = shipmentData.getOriginLocation().getDistrict();
            this.routeDate = shipmentData.getShipmentDate();
        }
        this.CurrentdrivingRoute.addStop(shipmentData);
    }

    /**
     * a function that adds a new Driver to a Route
     *
     * @param driver an instance of the Driver to be added
     */
    public void addDriver(Driver driver) {
        this.currentDriver = driver;
    }

    /**
     * a getter function for a route's Driver
     *
     * @return the route's driver
     */
    public Driver getDriver() {
        return this.currentDriver;
    }


    /**
     * a function that adds a new Vehicle to a Route
     *
     * @param vehicleInstance an instance of shipment vehicle
     */
    public void addVehicle(Vehicle vehicleInstance) {
        this.currentVehicle = vehicleInstance;

    }

    /**
     * a getter function for the route's vehicle
     *
     * @return the route's vehicle
     */
    public Vehicle getVehicle() {
        return this.currentVehicle;
    }

    /**
     * a getter function for a route's vehicle license number
     *
     * @return the vehicle's license number
     */
    public int getRouteVehicleLicense() {
        return this.currentVehicle.getLicenseNumber();
    }

    /**
     * a getter function for the Route's district
     *
     * @return District
     */
    public District getCurrentDistrict() {
        return this.currentShipmentDistrict;
    }

    /**
     * a function that returns the length of the current route
     *
     * @return the length of the route
     */
    public int getRouteLength() {
        return this.CurrentdrivingRoute.amountOfStops;
    }

    /**
     * a getter function for the route's date
     *
     * @return LocalDate
     */
    public LocalDate getRouteDate() {
        return this.routeDate;
    }

    /**
     * a getter function for a specific shipment by its ID
     *
     * @param shipmentId the shipment's ID to be searched for
     * @return looked for Shipment
     */
    public Shipment getShipmentInRoute(int shipmentId) {
        if (!this.CurrentdrivingRoute.searchForShipmentById(shipmentId)) {
            return null;
        }
        return this.CurrentdrivingRoute.getShipmentById(shipmentId);
    }

    /**
     * a setter function for each shipment in the current Route
     *
     * @param status the shipment's status
     */
    public void updateRouteStatus(s_status status) {
        this.CurrentdrivingRoute.updateAllShipmentsStatusInRoute(status);

    }

    /**
     * a getter function for the route's status
     *
     * @return the route's status
     */
    public s_status getStatus() {
        return this.CurrentdrivingRoute.getStatus();
    }


    /**
     * a getter function for the route's weight
     *
     * @return the total weight of the current route
     */
    public int getRouteWeight() {
        return (this.CurrentdrivingRoute.getTotalWeight());
    }

    /**
     * a function that updates a shipment's products in a route
     *
     * @param ShipmentId the shipment to be updated
     * @param product    the product to update
     * @param weight     the weigh to take out
     */
    public void updateRouteProducts(int ShipmentId, Product product, int weight) {
        Shipment toUpdate = this.CurrentdrivingRoute.getShipmentById(ShipmentId);
        if (toUpdate != null) {
            toUpdate.updateProductsInShipment(product, weight);
        }

    }

    /**
     * a print function for all products in a route
     */
    public void printRouteProducts() {
        this.CurrentdrivingRoute.printAllRouteProducts();
    }

    /**
     * Sets the departure time for all shipments in the current driving route.
     *
     * This method calls the `setDepTime` function on the `CurrentdrivingRoute` object,
     * which iterates through all shipments in the route and updates their departure time.
     * Each shipment's departure time is set as part of the route's initialization or update process.
     */
    public void setDepartureTimeForAllShipments() {
        this.CurrentdrivingRoute.setDepTime();
    }

    /**
     * a getter function for the departure time of the current driving route
     * @return LocalTime
     */
    public LocalTime getDepartureTimeForAllShipments() {
        return this.CurrentdrivingRoute.getDepartureTime();
    }

    /**
     * Prints all shipment IDs present in the current driving route.
     *
     * This method invokes the `printAllShipmentIds` function of the
     * `CurrentdrivingRoute` object to print the IDs of all shipments in the route to the console.
     * Each shipment ID is printed on a new line, preceded by a descriptive message.
     */
    public void printAllShipmentIdsInRoute() {
        this.CurrentdrivingRoute.printAllShipmentIds();
    }

    /**
     * Removes a shipment from the current route based on its ID.
     * If the shipment cannot be found, a message is printed to the console.
     *
     * @param shipmentId the ID of the shipment to be removed
     */
    public void removeShipmentById(int shipmentId) {
        if (!this.CurrentdrivingRoute.removeShipmentById(shipmentId)) {
            throw new IllegalArgumentException("Shipment with ID: " + shipmentId + " not found in route");
        }
    }

}

