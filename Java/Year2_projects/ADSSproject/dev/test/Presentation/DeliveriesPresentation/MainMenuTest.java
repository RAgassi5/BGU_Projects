package Presentation.DeliveriesPresentation;

import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class MainMenuTest {

    @Test
    void testDisplayMenu_WithValidInputForNonManager() {
        // Arrange
        String input = "3\n";
        Scanner scanner = new Scanner(input);

        // Act
        int result = MainMenu.displayMenu(scanner);

        // Assert
        assertEquals(3, result, "The displayMenu method should return the correct menu choice for a non-manager user.");
    }

    @Test
    void testDisplayMenu_WithValidInputForManager() {
        // Arrange
        String input = "5\n";
        Scanner scanner = new Scanner(input);
        setManagerMode(true);

        // Act
        int result = MainMenu.displayMenu(scanner);

        // Assert
        assertEquals(5, result, "The displayMenu method should return the correct menu choice for a manager user.");
    }

    @Test
    void testDisplayMenu_WithManagerLoggingIn() {
        // Arrange
        String input = "123\nadmin123\n";
        Scanner scanner = new Scanner(input);
        setManagerMode(false);

        // Act
        int result = MainMenu.displayMenu(scanner);

        // Assert
        assertEquals(123, result, "The displayMenu method should correctly detect the manager login input.");
    }

    @Test
    void testDisplayMenu_WithInvalidInput() {
        // Arrange
        String input = "abc\n3\n";
        Scanner scanner = new Scanner(input);

        // Act
        int result = MainMenu.displayMenu(scanner);

        // Assert
        assertEquals(3, result, "The method should ask for valid input when invalid input is given and then return the correct choice.");
    }

    @Test
    void testDisplayMenu_WithExitChoice() {
        // Arrange
        String input = "0\n";
        Scanner scanner = new Scanner(input);

        // Act
        int result = MainMenu.displayMenu(scanner);

        // Assert
        assertEquals(0, result, "The displayMenu method should allow exiting the menu by returning 0.");
    }

    @Test
    void testDisplayMenu_WithOutOfRangeInputForNonManager() {
        // Arrange
        String input = "6\n4\n";
        Scanner scanner = new Scanner(input);

        // Act
        int result = MainMenu.displayMenu(scanner);

        // Assert
        assertEquals(4, result, "The method should reject out-of-range inputs for a non-manager user and ask again.");
    }

    @Test
    void testDisplayMenu_WithOutOfRangeInputForManager() {
        // Arrange
        // The input tokens for this test:
        // First call: "123" (manager login command)
        // Second call: "6" (invalid for manager when in manager mode) then "5" (valid)
        String input = "123\n6\n5\n";
        Scanner scanner = new Scanner(input);
        // Start as non-manager for the login phase.
        setManagerMode(false);

        // Act
        int loginResult = MainMenu.displayMenu(scanner); // Should read 123.
        // Simulate a successful manager login.
        setManagerMode(true);
        int result = MainMenu.displayMenu(scanner);        // Reads invalid "6", then valid "5" on next iteration.

        // Assert
        assertEquals(123, loginResult, "The manager login should succeed.");
        assertEquals(5, result, "The method should reject out-of-range input for a manager and accept valid input.");
    }

    @Test
    void testVerifyManagerPassword_WithCorrectPassword() {
        // Arrange
        String input = "admin123\n";
        Scanner scanner = new Scanner(input);

        // Act
        boolean result = invokeVerifyManagerPassword(scanner);

        // Assert
        assertTrue(result, "The verifyManagerPassword method should return true for the correct password.");
    }

    @Test
    void testVerifyManagerPassword_WithIncorrectPassword() {
        // Arrange
        String input = "wrongpassword\n";
        Scanner scanner = new Scanner(input);

        // Act
        boolean result = invokeVerifyManagerPassword(scanner);

        // Assert
        assertFalse(result, "The verifyManagerPassword method should return false for an incorrect password.");
    }

    // Helper method to call the private verifyManagerPassword method
    private boolean invokeVerifyManagerPassword(Scanner scanner) {
        try {
            var method = MainMenu.class.getDeclaredMethod("verifyManagerPassword", Scanner.class);
            method.setAccessible(true);
            return (boolean) method.invoke(null, scanner);
        } catch (Exception e) {
            fail("Failed to call verifyManagerPassword method: " + e.getMessage());
            return false;
        }
    }

    // Helper method for setting manager mode
    private void setManagerMode(boolean isManager) {
        try {
            var field = MainMenu.class.getDeclaredField("isManager");
            field.setAccessible(true);
            field.set(null, isManager);
        } catch (Exception e) {
            fail("Failed to set manager mode: " + e.getMessage());
        }
    }
}