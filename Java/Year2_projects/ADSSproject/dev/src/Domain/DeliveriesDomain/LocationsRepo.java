package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.LocationDTO;
import DataAccess.DeliveriesDAO.ILocationDAO;
import DataAccess.DeliveriesDAO.LocationDAO;

import java.sql.SQLException;
import java.util.Hashtable;

public class LocationsRepo implements ILocationRepo{
    private ILocationDAO  currentlocationDAO;
    private Hashtable<Integer, Location> locations;

    public LocationsRepo(){
        locations = new Hashtable<>();
        currentlocationDAO = new LocationDAO();
    }

    /**
     * Compares the fields of a given Location instance and a LocationDTO instance to check if they match.
     *
     * @param loc the Location instance to be compared
     * @param dto the LocationDTO instance to be compared
     * @return true if all corresponding fields match, false otherwise
     */
    private boolean fieldsMatch(Location loc, LocationDTO dto) {
        return loc.getContactName().equals(dto.getContactName()) &&
                loc.getContactNUM().equals(dto.getContactNUM()) &&
                loc.getAddress().getStreet().equals(dto.getAddress().getStreet()) &&
                loc.getAddress().getHouseNum() == dto.getAddress().getHouseNum() &&
                loc.getAddress().getDistrict() == dto.getAddress().getDistrict();
    }

    /**
     * Searches the in-memory cache for a matching location.
     */
    private Location findMatchingLocationInMemory(LocationDTO dto) {
        for (Location loc : locations.values()) {
            if (fieldsMatch(loc, dto)) {
                return loc;
            }
        }
        return null;
    }

    /**
     * Adds a new location to either the in-memory cache or the database if it does not already exist.
     * The method checks for matching locations in memory first, and if no match is found,
     * it checks the database. If a matching location is found, it is updated in the in-memory cache.
     *
     * @param location the location to be added, represented as a LocationDTO instance
     */
    @Override
    public void addLocation(LocationDTO location) {
        try{
            Location matchingInMemory = findMatchingLocationInMemory(location);
            if (matchingInMemory != null) {
                location.setID(matchingInMemory.getLocationId());
                locations.put(matchingInMemory.getLocationId(), matchingInMemory);
                return;
            }

            LocationDTO existingLocation = currentlocationDAO.findMatchingLocation(location);
            if (existingLocation != null) {
                location.setID(existingLocation.getID());
                Location foundLocation = new Location(location);
                locations.put(foundLocation.getLocationId(), foundLocation);

            } else {
                currentlocationDAO.addLocation(location);
                Location newLocation = new Location(location);
                locations.put(newLocation.getLocationId(), newLocation);
            }


        }catch (SQLException e){
            System.out.println("Failed to add location");
        }
    }

    /**
     * Removes a location identified by the given location ID. The method deletes the location
     * from both the database and the in-memory cache. If an exception occurs during the
     * database operation, a message is logged.
     *
     * @param locationID the unique identifier of the location to be removed
     */
    @Override
    public void removeLocation(int locationID) {
        try{
            currentlocationDAO.delete(locationID);
            locations.remove(locationID);
        }catch (SQLException e){
            System.out.println("Failed to remove location");
        }

    }
    /**
     * Retrieves the Location object associated with the given location ID.
     * If the location exists in the in-memory cache, it is returned directly.
     * Otherwise, the method fetches the location from the database using the DAO and updates the cache.
     *
     * @param locationID the unique identifier of the location to be retrieved
     * @return the Location object if found, either from the in-memory cache or the database;
     *         null if the location does not exist in either source
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Location getLocationByID(int locationID) throws SQLException {
        if(locations.containsKey(locationID)){
            return locations.get(locationID);
        }

        LocationDTO locationFromDB = this.currentlocationDAO.getById(locationID);
        if(locationFromDB == null){
            return null;
        }
        Location newLocation = new Location(locationFromDB);
        this.locations.put(newLocation.getLocationId(), newLocation);
        return newLocation;
    }
}
