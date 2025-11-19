package Service.DeliveriesService;


import Domain.DeliveriesDomain.*;

import java.sql.SQLException;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RouteManager {
    private final BookingSystem bookingSystem;
    private Route[] readyRoutesByDistricts;
    private Stack<Route> overWeightRoutes;
    private static RouteManager currentRouteManager;
    private final DriversManager driversManager;
    private final VehiclesManager vehiclesManager;
    private ScheduledExecutorService scheduler;

    private RouteManager() {
        this.bookingSystem = BookingSystem.getInstance();
        this.readyRoutesByDistricts = new Route[0];
        this.overWeightRoutes = new Stack<>();
        this.driversManager = DriversManager.getInstance();
        this.vehiclesManager = VehiclesManager.getInstance();
    }

    /**
     * Returns the singleton instance of the RouteManager.
     * If the instance does not exist, it initializes and returns a new one.
     *
     * @return the singleton instance of the RouteManager
     */
    public static RouteManager getInstance() {
        if (currentRouteManager == null) {
            currentRouteManager = new RouteManager();
        }
        return currentRouteManager;
    }

    /**
     * a time based function that is activated every set amount of time
     * the function "pulls" all incoming shipment bookings
     */
    public void startAutoRunning() {
        if (scheduler != null && !scheduler.isShutdown()) {
            return;
        }
        scheduler = Executors.newScheduledThreadPool(1);


        Runnable task = () -> {
            if (!this.bookingSystem.hasBookings()) {
                return;
            }
            Stack<Shipment> incomingShipments = bookingSystem.getPendingShipments();
            while (!incomingShipments.isEmpty()) {
                Shipment shipment = incomingShipments.pop();
                shipment.updateStatus(s_status.SHIPPED);

                boolean foundRoute = false;
                for (int i = 0; i < this.readyRoutesByDistricts.length; i++) {
                    if (this.readyRoutesByDistricts[i].getCurrentDistrict().equals(shipment.getDestinationLocation().getDistrict()) && this.readyRoutesByDistricts[i].getRouteDate().isEqual(shipment.getShipmentDate())) {
                        this.readyRoutesByDistricts[i].addShipment(shipment);
                        foundRoute = true;
                        break;
                    }
                }

                if (!foundRoute) {
                    Route[] tempRoutes = new Route[this.readyRoutesByDistricts.length + 1];
                    System.arraycopy(this.readyRoutesByDistricts, 0, tempRoutes, 0, this.readyRoutesByDistricts.length);
                    Route newRoute = new Route(this.bookingSystem.getCurrentShipmentsRepo());
                    newRoute.addShipment(shipment);
                    tempRoutes[this.readyRoutesByDistricts.length] = newRoute;
                    this.readyRoutesByDistricts = tempRoutes;
                }

            }

        };
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }

    /**
     * a function that shutdown the time-based function
     */
    public void stopRouting() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }


    /**
     * @return the earliest route possible
     */
    private Route getEarliestRoute() {
        Route earliestRoute = this.readyRoutesByDistricts[0];
        for (int i = 1; i < this.readyRoutesByDistricts.length; i++) {
            if (this.readyRoutesByDistricts[i].getRouteDate().isBefore(earliestRoute.getRouteDate())) {
                earliestRoute = this.readyRoutesByDistricts[i];
            }
        }
        return earliestRoute;
    }

    public Route getCurrentOverWeightRoute() {
        return this.overWeightRoutes.pop();
    }

    /**
     * a function that sends out a shipment if there is one waiting
     *
     * @return returns the instance of the route that was shipped
     */
    public Route sendOutAwaitingShipments() {
        if (this.readyRoutesByDistricts.length == 0) {
            return null;
        } else {
            Route toBeShipped = getEarliestRoute();
            if (this.mergeDriverVehicleToRoute(toBeShipped) != null) {
                toBeShipped.setDepartureTimeForAllShipments();
                removeRouteFromAwaitingList(toBeShipped);
                scheduledDeliveryUpdate(toBeShipped, 45);
                return toBeShipped;
            } else {
                if (toBeShipped.getVehicle() == null) {
                    Vehicle potentialVehicle = this.vehiclesManager.assignVehicle(toBeShipped.getRouteWeight());

                    if (potentialVehicle == null) {
                        // No vehicle can handle this weight
                        toBeShipped.updateRouteStatus(s_status.OVERWEIGHT);
                        this.overWeightRoutes.push(toBeShipped);
                    } else {
                        // Vehicle exists but no driver with a proper license
                        potentialVehicle.setCurrentStatus(vehicle_status.AVAILABLE); // Reset vehicle status
                        toBeShipped.updateRouteStatus(s_status.DELAYED);
                    }

                    removeRouteFromAwaitingList(toBeShipped);
                    return toBeShipped;
                } else {
                    //  vehicle matches but no available driver right now
                    toBeShipped.updateRouteStatus(s_status.DELAYED);
                    this.addRouteBack(toBeShipped);
                    return toBeShipped;
                }
            }

        }
    }

    /**
     * a timed function that updates the status of each shipment after delivery
     *
     * @param route          the route to update
     * @param delayInSeconds how much time to wait until updating the status
     */
    private void scheduledDeliveryUpdate(Route route, long delayInSeconds) {
        Runnable task = () -> {
            try {
                route.updateRouteStatus(s_status.DELIVERED);
                Driver driver = route.getDriver();
                if(driver != null){
                    this.driversManager.getDriverRepo().updateDriverStatus(driver.getDriverId(), driver_status.FREE);
                    //route.getDriver().setDriverStatus(driver_status.FREE);
                }
                Vehicle vehicle = route.getVehicle();
                if(vehicle != null){
                    this.vehiclesManager.getVehicleRepo().updateVehicleStatus(vehicle.getLicenseNumber(), vehicle_status.AVAILABLE);
                    //route.getVehicle().setCurrentStatus(vehicle_status.AVAILABLE);
                }
            }catch (SQLException e){
                System.out.println("There was a problem updating the status of the route!");
            }
        };
        scheduler.schedule(task, delayInSeconds, TimeUnit.SECONDS);
    }

    /**
     * a function that fits a driver and vehicle to a route according to the weight and the driver's license type
     *
     * @param routeInstance an instance of a route that need to be grouped with a vehicle and driver
     * @return the ready route
     */
    private Route mergeDriverVehicleToRoute(Route routeInstance) {
        Vehicle newVehicle = this.vehiclesManager.assignVehicle(routeInstance.getRouteWeight());
        if (newVehicle != null) {
            Driver newDriver = this.driversManager.assignDriver(newVehicle.getVehicleType());
            if (newDriver != null) {
                routeInstance.addDriver(newDriver);
                routeInstance.addVehicle(newVehicle);
                return routeInstance;
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * a function that allows editing to overweight routes
     *
     * @param ShipmentID     the shipment in a route to remove products from
     * @param product        the product to edit
     * @param WeightToRemove the weight to remove
     */
    public void handleOverWeightRoutesProducts(int ShipmentID, Product product, int WeightToRemove) {
        Route routeToEdit = this.overWeightRoutes.pop();
        routeToEdit.updateRouteProducts(ShipmentID, product, WeightToRemove);
        this.addRouteBack(routeToEdit);
    }


    /**
     * a function that checks if there are overweight shipments in the OverWeight stack
     *
     * @return True if empty / False if there are items
     */
    public boolean checkOverWeightIsEmpty() {
        return this.overWeightRoutes.isEmpty();
    }

    /**
     * Pushes an overweight route to the stack of overweight routes for further handling or processing.
     *
     * @param route the route instance that is classified as overweight and needs to be added to the overweight routes stack
     */
    public void pushOverWeightRoute(Route route) {
        this.overWeightRoutes.push(route);
    }

    /**
     * a function that adds back an unsent route
     *
     * @param routeToAdd an instance of a route that needs to be handled again
     */
    private void addRouteBack(Route routeToAdd) {
        routeToAdd.updateRouteStatus(s_status.PENDING);
        Route[] tempRoutes = new Route[this.readyRoutesByDistricts.length + 1];
        System.arraycopy(this.readyRoutesByDistricts, 0, tempRoutes, 0, this.readyRoutesByDistricts.length);
        tempRoutes[this.readyRoutesByDistricts.length] = routeToAdd;
        this.readyRoutesByDistricts = tempRoutes;
    }

    /**
     * Removes a specific route from the list of awaiting routes.
     *
     * @param routeToRemove the route to be removed from the awaiting routes list
     */
    private void removeRouteFromAwaitingList(Route routeToRemove) {
        Route[] tempRoutes = new Route[this.readyRoutesByDistricts.length - 1];
        int index = 0;
        for (int i = 0; i < this.readyRoutesByDistricts.length; i++) {
            if (this.readyRoutesByDistricts[i] != routeToRemove) {
                tempRoutes[index++] = this.readyRoutesByDistricts[i];
            }
        }
        this.readyRoutesByDistricts = tempRoutes;
    }

    /**
     * Reschedules a route that needs to be handled again by adding it back to the system.
     *
     * @param routeToAdd the route instance that needs to be rescheduled and processed again
     */
    public void rescheduleRoute(Route routeToAdd) {
        this.addRouteBack(routeToAdd);
    }

    /**
     * Retrieves the maximum available capacity among all vehicles.
     * <p>
     * This method delegates to the vehicles manager to determine the highest capacity
     * available from the currently managed vehicles.
     *
     * @return the maximum capacity available across all vehicles managed by the system
     */
    public int getMaximumCapacity() {
        return this.vehiclesManager.getMaxAvailableCapacity();
    }
}