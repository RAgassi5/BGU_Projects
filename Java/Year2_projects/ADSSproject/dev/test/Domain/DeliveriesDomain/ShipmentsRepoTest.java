package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.LocationDTO;
import DTO.DeliveriesDTO.ShipmentDTO;
import DataAccess.DeliveriesDAO.IShipmentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentsRepoTest {

    private ShipmentsRepo repo;
    private StubShipmentDAO stubDao;
    private StubLocationRepo stubLoc;

    /**
     * Sets up the necessary components before each test in the test class.
     * This method initializes dependencies and injects them into the system
     * under test to ensure a clean testing environment.
     *
     * @throws Exception if an error occurs during dependency injection.
     */
    @BeforeEach
    void setUp() throws Exception {
        repo     = new ShipmentsRepo();
        stubDao  = new StubShipmentDAO();
        stubLoc  = new StubLocationRepo();
        injectDependencies(repo, stubDao, stubLoc);
    }

    /**
     * Injects dependencies into the specified {@code ShipmentsRepo} instance by
     * setting the provided DAO and location repository fields.
     *
     * @param repo the {@code ShipmentsRepo} instance whose dependencies are to be injected
     * @param dao the {@code IShipmentDAO} implementation to be injected into the {@code repo}
     * @param locRepo the {@code ILocationRepo} implementation to be injected into the {@code repo}
     * @throws Exception if reflection operations like accessing or modifying fields fail
     */
    private void injectDependencies(ShipmentsRepo repo,
                                    IShipmentDAO dao,
                                    ILocationRepo locRepo) throws Exception {
        Field daoField = ShipmentsRepo.class.getDeclaredField("shipmentDAO");
        daoField.setAccessible(true);
        daoField.set(repo, dao);

        Field locField = ShipmentsRepo.class.getDeclaredField("locationRepo");
        locField.setAccessible(true);
        locField.set(repo, locRepo);
    }

    /**
     * Stub implementation of the {@code IShipmentDAO} interface, designed for testing purposes.
     * This class serves as a mock database for managing shipment data, allowing controlled simulation
     * of database operations. It records the last operation performed and provides mechanisms to
     * simulate exceptions for each operation.
     *
     * The supported operations include adding shipped shipments, adding self-delivered shipments,
     * retrieving shipments by ID, updating shipment times, and saving shipment data.
     */
    static class StubShipmentDAO implements IShipmentDAO {
        enum Method { ADD_SHIPPED, ADD_SELF, GET, UPDATE_TIME, SAVE }
        Method            lastMethod;
        ShipmentDTO       lastDto;
        int               lastShipmentId;
        LocalTime         lastTime;

        int                returnShippedId;
        int                returnSelfId;
        Map<Integer,ShipmentDTO> dtoMap = new HashMap<>();

        boolean throwOnAddShipped  = false;
        boolean throwOnAddSelf     = false;
        boolean throwOnGet         = false;
        boolean throwOnUpdateTime  = false;
        boolean throwOnSave        = false;

        /**
         * Adds a shipped shipment to the system.
         * This method records the shipment data provided in the form of a ShipmentDTO object
         * and assigns a unique identifier for the shipment. It simulates storing the shipment
         * in a database and logs the operation for testing purposes.
         *
         * @param dto the {@link ShipmentDTO} object containing the shipment details to be added
         * @return the unique identifier of the added shipment
         * @throws SQLException if an error occurs during the operation, such as a simulated database failure
         */
        @Override
        public int addShippedShipment(ShipmentDTO dto) throws SQLException {
            lastMethod = Method.ADD_SHIPPED;
            lastDto    = dto;
            if (throwOnAddShipped) throw new SQLException();
            return returnShippedId;
        }

        /**
         * Adds a self-delivered shipment to the system.
         * This method records the shipment data provided through a {@link ShipmentDTO} object,
         * simulating the process of storing this data in a database. It assigns and returns
         * a unique identifier for the self-delivered shipment.
         *
         * @param dto the {@link ShipmentDTO} object containing details of the shipment to be added
         * @return the unique identifier of the self-delivered shipment
         * @throws SQLException if an error occurs during the operation, such as a simulated database failure
         */
        @Override
        public int addSelfDeliveredShipment(ShipmentDTO dto) throws SQLException {
            lastMethod = Method.ADD_SELF;
            lastDto    = dto;
            if (throwOnAddSelf) throw new SQLException();
            return returnSelfId;
        }

        /**
         * Retrieves a shipment by its unique identifier.
         * This method searches for and returns the shipment details corresponding to the given shipment ID.
         * It simulates a database operation, enabling testing of shipment retrieval functionality.
         *
         * @param id the unique identifier of the shipment to retrieve
         * @return the {@link ShipmentDTO} object containing the details of the shipment, or null if no such shipment exists
         * @throws SQLException if an error occurs during the operation, such as a simulated database failure
         */
        @Override
        public ShipmentDTO getShipmentById(int id) throws SQLException {
            lastMethod     = Method.GET;
            lastShipmentId = id;
            if (throwOnGet) throw new SQLException();
            return dtoMap.get(id);
        }

        /**
         * Updates the shipment time for a specified shipment.
         * This method records the provided time for the given shipment ID
         * and simulates updating the information in the database.
         *
         * @param shipmentId the unique identifier of the shipment to update
         * @param time the new time to be set for the shipment
         * @throws SQLException if an error occurs during the operation, such as a simulated database failure
         */
        @Override
        public void updateShipmentTime(int shipmentId, LocalTime time) throws SQLException {
            lastMethod     = Method.UPDATE_TIME;
            lastShipmentId = shipmentId;
            lastTime       = time;
            if (throwOnUpdateTime) throw new SQLException();
        }

        /**
         * Saves a shipment record to the system.
         * This method processes the shipment details contained in the provided
         * {@link ShipmentDTO} object and simulates the operation of storing these details.
         * It also manages internal state for testing purposes.
         *
         * @param dto the {@link ShipmentDTO} object containing the details of the shipment to be saved
         * @throws SQLException if an error occurs during the operation, such as a simulated database failure
         */
        @Override
        public void save(ShipmentDTO dto) throws SQLException {
            lastMethod = Method.SAVE;
            lastDto    = dto;
            if (throwOnSave) throw new SQLException();
        }
    }

    /**
     * Stub implementation of the ILocationRepo interface for testing purposes.
     * Provides basic functionality for manipulating and querying location data
     * using an internal in-memory map. This implementation is intended for use
     * in environments where a full-featured repository is not required, such as
     * unit tests.
     */
    static class StubLocationRepo implements ILocationRepo {
        Map<Integer, Location> map = new HashMap<>();

        @Override public void addLocation(LocationDTO location)         { }
        @Override public void removeLocation(int locationID)            { }
        @Override public Location getLocationByID(int locationID) {
            return map.get(locationID);
        }
    }

    /**
     * Validates that adding a self-delivered shipment correctly updates the data access object (DAO)
     * and the shipment history. The test ensures that:
     * <ul>
     * <li>The appropriate method is invoked on the DAO.</li>
     * <li>The shipment details are captured correctly in the DAO.</li>
     * <li>The shipment is assigned a valid ID.</li>
     * <li>The shipment is cached and retrievable by its ID from the repository.</li>
     * </ul>
     * Preconditions:
     * <ul>
     * <li>A self-delivered shipment is created with valid origin, destination, and date information.</li>
     * </ul>
     * Assertions:
     * <ul>
     * <li>The DAO's last invoked method matches the expected 'addSelfDeliveredShipment' method.</li>
     * <li>The DAO contains a non-null shipment DTO with the expected information.</li>
     * <li>The shipment is assigned the expected identifier.</li>
     * <li>The shipment is stored in the repository's cache and is retrievable by its assigned ID.</li>
     * </ul>
     */
    @Test
    void whenAddSelfDeliveredShipment_thenShipmentAddedToDAOAndHistory() {
        stubDao.returnSelfId = 42;

        var orig  = new Location("A","000","St",1, District.North);
        var dest  = new Location("B","111","Rd",2, District.South);
        var date  = LocalDate.now();
        var ship  = new Shipment(orig, dest, date);

        repo.addSelfDeliveredShipment(ship);

        assertEquals(StubShipmentDAO.Method.ADD_SELF, stubDao.lastMethod);
        assertNotNull(stubDao.lastDto);
        assertEquals(42, ship.getId());

        // now in cache
        Shipment fromCache = repo.getShipmentById(42);
        assertSame(ship, fromCache);
    }

    /**
     * Tests the behavior of the repository when attempting to add a self-delivered shipment
     * and an SQL exception is triggered by the underlying data access object (DAO).
     *
     * The test verifies that:
     * 1. The repository gracefully handles the SQLException without throwing it to the caller.
     * 2. The DAO's last invoked method corresponds to the expected operation for adding
     *    a self-delivered shipment.
     *
     * Preconditions:
     * - A shipment object is created with valid origin, destination, and date information.
     * - The DAO is configured to throw an SQL exception during the add operation.
     *
     * Assertions:
     * - The repository does not propagate the exception when the add operation is invoked.
     * - The DAO indicates that the correct method was invoked by checking its `lastMethod` field.
     */
    @Test
    void whenAddSelfDeliveredShipment_thenSQLExceptionHandled() {
        stubDao.throwOnAddSelf = true;
        var ship = new Shipment(
                new Location("A","000","St",1, District.North),
                new Location("B","111","Rd",2, District.South),
                LocalDate.now()
        );

        assertDoesNotThrow(() -> repo.addSelfDeliveredShipment(ship));
        assertEquals(StubShipmentDAO.Method.ADD_SELF, stubDao.lastMethod);
    }

    /**
     * Tests the behavior of the `getShipmentById` method in the repository when given an existing shipment ID.
     *
     * Preconditions:
     * - The shipment with the specified ID exists in the repository's cache.
     * - The internal `shipmentsHistory` field contains the shipment with the expected ID.
     *
     * Verifications:
     * - The `getShipmentById` method retrieves the shipment directly from the cache without invoking external components.
     * - The returned shipment object is the same instance as the cached object.
     *
     * Assertions:
     * - The cached shipment is returned as-is from the repository for the specified ID.
     * - The returned shipment instance is identical (via reference equality) to the cached object.
     *
     * @throws Exception if reflection operations like accessing the private `shipmentsHistory` field fail
     */
    @Test
    void whenGetShipmentById_withExistingId_thenReturnShipmentFromCache() throws Exception {
        var cached = new Shipment(
                new Location("O","0","X",1, District.Central),
                new Location("D","1","Y",2, District.Central),
                LocalDate.now()
        );
        cached.setShipmentId(5);

        Field histField = ShipmentsRepo.class.getDeclaredField("shipmentsHistory");
        histField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var history = (Hashtable<Integer, Shipment>) histField.get(repo);
        history.put(5, cached);

        Shipment result = repo.getShipmentById(5);
        assertSame(cached, result);
    }



    @Test
    void whenGetShipmentById_withSQLException_thenGracefullyHandle() {
        stubDao.throwOnGet = true;
        Shipment result = repo.getShipmentById(123);
        assertNull(result);
        assertEquals(StubShipmentDAO.Method.GET, stubDao.lastMethod);
    }

    @Test
    void whenUpdateShipment_thenDAOAndHistoryUpdated() throws Exception {
        var s = new Shipment(
                new Location("O","0","X",1, District.Central),
                new Location("D","1","Y",2, District.Central),
                LocalDate.now()
        );
        s.setShipmentId(9);

        Field hf = ShipmentsRepo.class.getDeclaredField("shipmentsHistory");
        hf.setAccessible(true);
        @SuppressWarnings("unchecked")
        var history = (Hashtable<Integer, Shipment>) hf.get(repo);
        history.put(9, s);

        repo.updateShipment(s);
        assertEquals(StubShipmentDAO.Method.SAVE, stubDao.lastMethod);
        assertNotNull(stubDao.lastDto);
    }

    @Test
    void whenUpdateShipment_withSQLException_thenHandleGracefully() throws Exception {
        stubDao.throwOnSave = true;
        var s = new Shipment(
                new Location("O","0","X",1, District.Central),
                new Location("D","1","Y",2, District.Central),
                LocalDate.now()
        );
        s.setShipmentId(8);

        Field hf = ShipmentsRepo.class.getDeclaredField("shipmentsHistory");
        hf.setAccessible(true);
        @SuppressWarnings("unchecked")
        var history = (Hashtable<Integer, Shipment>) hf.get(repo);
        history.put(8, s);

        assertDoesNotThrow(() -> repo.updateShipment(s));
        assertEquals(StubShipmentDAO.Method.SAVE, stubDao.lastMethod);
    }

    @Test
    void whenUpdateShipment_withNonExistentShipment_thenSQLExceptionThrown() {
        var s = new Shipment(
                new Location("O", "0", "X", 1, District.Central),
                new Location("D", "1", "Y", 2, District.Central),
                LocalDate.now()
        );
        s.setShipmentId(99);

        // Capture System.out so we can assert that the error message is printed
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            repo.updateShipment(s);
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString();
        assertTrue(output.contains("No shipment data found in database for ID: 99"),
                "Expected error message was not printed.");

    }
}