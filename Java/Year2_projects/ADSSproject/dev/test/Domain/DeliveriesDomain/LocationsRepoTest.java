package Domain.DeliveriesDomain;

import DTO.DeliveriesDTO.LocationDTO;
import DataAccess.DeliveriesDAO.ILocationDAO;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocationsRepoTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));  // swallow any printlns
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }


    // helper to inject our mock into the private field
    private void injectDAO(LocationsRepo repo, ILocationDAO dao) throws Exception {
        Field f = LocationsRepo.class.getDeclaredField("currentlocationDAO");
        f.setAccessible(true);
        f.set(repo, dao);
    }

    /**
     * Verifies the behavior of the `getLocationByID` method in the `LocationsRepo` class
     * when the requested location exists in the in-memory cache.
     *
     * The test ensures that:
     * - If the location with the given ID is already present in the in-memory cache,
     *   it is returned directly without making a call to the database layer.
     * - The correct data is retrieved from the cache and matches the expected results.
     *
     * Test setup includes:
     * - Creating a mock implementation of the `ILocationDAO` interface.
     * - Injecting the mock DAO into a `LocationsRepo` instance using the `injectDAO` helper method.
     * - Adding a `LocationDTO` to the in-memory cache to simulate an existing location.
     *
     * Verifications performed:
     * - The returned `Location` object is not null.
     * - The `locationId` of the returned object matches the expected ID.
     * - The DAO's `getById` method is never called, confirming that the data is retrieved from
     *   the cache instead of querying the database.
     *
     * @throws Exception if any unexpected error occurs during the test execution
     */
    @Test
    public void testGetLocationByID_LocationExistsInCache() throws Exception {
        ILocationDAO mockDAO = Mockito.mock(ILocationDAO.class);
        LocationsRepo locationsRepo = new LocationsRepo();
        injectDAO(locationsRepo, mockDAO);

        LocationDTO dto = new LocationDTO(1, "John Doe", "123456789",
                new Address(District.South, "Main St", 5));
        locationsRepo.addLocation(dto);

        Location result = locationsRepo.getLocationByID(1);
        assertNotNull(result);
        assertEquals(1, result.getLocationId());
        verify(mockDAO, never()).getById(1);
    }

    /**
     * Tests the `getLocationByID` method of the `LocationsRepo` class to verify its behavior
     * when a location with the specified ID exists in the database.
     *
     * This test ensures that:
     * - The method retrieves the location from the database if it is not present in the in-memory cache.
     * - The retrieved location is correctly populated with the expected data.
     * - A subsequent request for the same location retrieves it from the in-memory cache.
     *
     * Test setup includes:
     * - Creating a mock implementation of the `ILocationDAO` interface.
     * - Injecting the mock DAO into a `LocationsRepo` instance using the `injectDAO` helper method.
     * - Configuring the mock DAO to return a predefined `LocationDTO` for the requested ID.
     *
     * Verifications performed:
     * - The returned `Location` object is not null.
     * - The `locationId` of the returned object matches the expected ID.
     * - The DAO's `getById` method is called exactly once to fetch the location from the database.
     * - Subsequent retrieval of the same location returns the same object from the cache.
     *
     * @throws Exception if any unexpected error occurs during test execution
     */
    @Test
    public void testGetLocationByID_LocationExistsInDatabase() throws Exception {
        ILocationDAO mockDAO = Mockito.mock(ILocationDAO.class);
        Address addressTest = new Address(District.North, "High Ave.", 10);

        LocationDTO mockLocationDTO = new LocationDTO(2, "Jane Doe", "987654321",
                addressTest);
        when(mockDAO.getById(2)).thenReturn(mockLocationDTO);

        LocationsRepo locationsRepo = new LocationsRepo();
        injectDAO(locationsRepo, mockDAO);

        Location result = locationsRepo.getLocationByID(2);
        assertNotNull(result);
        assertEquals(2, result.getLocationId());
        Location cached = locationsRepo.getLocationByID(2);
        assertSame(result, cached);

        verify(mockDAO, times(1)).getById(2);
    }

    /**
     * Tests the `getLocationByID` method of the `LocationsRepo` class to verify its behavior
     * when a location with the specified ID does not exist in the database.
     *
     * This test ensures the following:
     * - The method returns `null` if the DAO indicates that no location exists with the given ID.
     * - The DAO's `getById` method is called exactly once with the specified ID.
     *
     * Test setup includes:
     * - Creating a mock implementation of the `ILocationDAO` interface.
     * - Configuring the mock DAO to return `null` when the `getById` method is called with the
     *   specified ID.
     * - Injecting the mock DAO into a `LocationsRepo` instance using the `injectDAO` helper method.
     *
     * Verifications performed:
     * - The returned `Location` object is null.
     * - The DAO's `getById` method is invoked exactly once with the correct ID.
     *
     * @throws Exception if any unexpected error occurs during test execution.
     */
    @Test
    public void testGetLocationByID_LocationDoesNotExist() throws Exception {
        ILocationDAO mockDAO = Mockito.mock(ILocationDAO.class);
        when(mockDAO.getById(3)).thenReturn(null);

        LocationsRepo locationsRepo = new LocationsRepo();
        injectDAO(locationsRepo, mockDAO);

        Location result = locationsRepo.getLocationByID(3);
        assertNull(result);
        verify(mockDAO, times(1)).getById(3);
    }

    /**
     * Tests the `getLocationByID` method of the `LocationsRepo` class to verify its behavior
     * when an `SQLException` is thrown by the DAO layer.
     *
     * This test ensures the following:
     * - The exception thrown by the DAO's `getById` method is correctly propagated
     *   to the caller of the `getLocationByID` method.
     * - The DAO's `getById` method is invoked exactly once with the specified ID.
     *
     * Test setup includes:
     * - Creating a mock implementation of the `ILocationDAO` that simulates throwing an
     *   `SQLException` when the `getById` method is called.
     * - Injecting the mock DAO instance into a `LocationsRepo` instance using a helper method.
     *
     * Verifications performed:
     * - The `getById` method of the DAO is called once with the expected ID.
     * - An `SQLException` is thrown when the `getLocationByID` method is executed.
     *
     * @throws Exception if any unexpected error occurs during the test execution
     */
    @Test
    public void testGetLocationByID_SQLExceptionThrown() throws Exception {
        ILocationDAO mockDAO = Mockito.mock(ILocationDAO.class);
        when(mockDAO.getById(4)).thenThrow(new SQLException("Database Error"));

        LocationsRepo locationsRepo = new LocationsRepo();
        injectDAO(locationsRepo, mockDAO);

        assertThrows(SQLException.class, () -> locationsRepo.getLocationByID(4));
        verify(mockDAO, times(1)).getById(4);
    }

    /**
     * Tests the `addLocation` method of the `LocationsRepo` class to verify its behavior
     * when adding a new location is successful.
     *
     * This test ensures the following:
     * - The `addLocation` method successfully adds a given `LocationDTO` object to the repository.
     * - The newly added location can be retrieved using the `getLocationByID` method.
     * - The DAO's `addLocation` method is invoked exactly once with the expected `LocationDTO`.
     *
     * Test setup includes:
     * - A mock implementation of the `ILocationDAO` interface.
     * - A sample `LocationDTO` object representing the location to be added.
     * - Injecting the mock DAO into a `LocationsRepo` instance using a helper method.
     *
     * Verifications performed:
     * - The location returned by `getLocationByID` is not null.
     * - The `addLocation` method of the DAO is invoked exactly once with the provided `LocationDTO`.
     *
     * @throws Exception if any unexpected error occurs during the test execution
     */
    @Test
    public void testAddLocation_Success() throws Exception {
        ILocationDAO mockDAO = Mockito.mock(ILocationDAO.class);
        Address adrressTest = new Address(District.South, "Green St", 15);

        LocationDTO dto = new LocationDTO(5, "Alice Johnson", "111222333",
                adrressTest);

        LocationsRepo locationsRepo = new LocationsRepo();
        injectDAO(locationsRepo, mockDAO);

        locationsRepo.addLocation(dto);
        assertNotNull(locationsRepo.getLocationByID(5));
        verify(mockDAO, times(1)).addLocation(dto);
    }

    /**
     * Tests the `addLocation` method of the `LocationsRepo` class to verify its behavior
     * when an `SQLException` is thrown during the add operation in the DAO layer. This test
     * ensures that the exception does not propagate beyond the method and that the repository
     * handles the error gracefully without adding the location to the in-memory cache.
     *
     * The test sets up the following:
     * - A mock implementation of the `ILocationDAO` interface that simulates throwing an
     *   `SQLException` when the `addLocation` method is called.
     * - A `LocationDTO` object representing the location to be added.
     * - An instance of the `LocationsRepo` class with the mocked DAO injected.
     *
     * The test verifies the following:
     * - No exception is thrown by the `addLocation` method.
     * - The location is not present in the repository after the operation.
     * - The DAO's `addLocation` method is called exactly once with the expected `LocationDTO`.
     *
     * @throws Exception if any unexpected error occurs during the test execution
     */
    @Test
    public void testAddLocation_SQLException() throws Exception {
        ILocationDAO mockDAO = Mockito.mock(ILocationDAO.class);
        Address adrressTest = new Address(District.Central, "Blue St", 25);
        LocationDTO dto = new LocationDTO(6, "Tom Cruise", "444555666",
                adrressTest);
        doThrow(new SQLException("Database Error"))
                .when(mockDAO).addLocation(dto);

        LocationsRepo locationsRepo = new LocationsRepo();
        injectDAO(locationsRepo, mockDAO);

        assertDoesNotThrow(() -> locationsRepo.addLocation(dto));
        assertNull(locationsRepo.getLocationByID(6));
        verify(mockDAO, times(1)).addLocation(dto);
    }

    /**
     * Tests the `removeLocation` method of the `LocationsRepo` class to ensure that
     * a location is successfully removed both from the in-memory cache and the
     * database when a valid location ID is provided.
     *
     * The test sets up a mocked instance of `ILocationDAO`, initializes the
     * `LocationsRepo`, and injects the mock DAO implementation. A sample `LocationDTO`
     * is added to the repository to simulate existing data. The test then removes the
     * location using the `removeLocation` method and verifies the following:
     *
     * - The location is no longer present in the repository (i.e., the cache and DAO).
     * - The DAO's `delete` method is invoked exactly once with the correct location ID.
     *
     * @throws Exception if any unexpected error occurs during the test execution
     */
    @Test
    public void testRemoveLocation_Success() throws Exception {
        ILocationDAO mockDAO = Mockito.mock(ILocationDAO.class);
        Address adrressTest = new Address(District.North, "Spider St", 30);

        LocationDTO dto = new LocationDTO(7, "Peter Parker", "777888999",
                adrressTest);

        LocationsRepo locationsRepo = new LocationsRepo();
        injectDAO(locationsRepo, mockDAO);
        locationsRepo.addLocation(dto);

        locationsRepo.removeLocation(7);
        assertNull(locationsRepo.getLocationByID(7));
        verify(mockDAO, times(1)).delete(7);
    }

    /**
     * Tests the `removeLocation` method of the `LocationsRepo` class when a `SQLException`
     * is thrown during the delete operation in the DAO layer. This test ensures that the
     * exception does not propagate beyond the method and that the DAO's `delete` method
     * is invoked exactly once with the specified location ID.
     *
     * The test utilizes a mock implementation of `ILocationDAO` to simulate a
     * `SQLException` when the `delete` method is called. It verifies that no exception
     * is thrown by the `removeLocation` method and asserts that the DAO's `delete` method
     * was called the expected number of times.
     *
     * @throws Exception if any unexpected error occurs during the test execution
     */
    @Test
    public void testRemoveLocation_SQLException() throws Exception {
        ILocationDAO mockDAO = Mockito.mock(ILocationDAO.class);
        doThrow(new SQLException("Database Error"))
                .when(mockDAO).delete(8);

        LocationsRepo locationsRepo = new LocationsRepo();
        injectDAO(locationsRepo, mockDAO);

        assertDoesNotThrow(() -> locationsRepo.removeLocation(8));
        verify(mockDAO, times(1)).delete(8);
    }
}