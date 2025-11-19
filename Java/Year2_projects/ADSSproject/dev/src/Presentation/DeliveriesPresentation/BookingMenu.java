package Presentation.DeliveriesPresentation;

import Domain.DeliveriesDomain.District;
import Domain.DeliveriesDomain.Location;
import Domain.DeliveriesDomain.Product;
import Domain.DeliveriesDomain.Shipment;

import java.time.LocalDate;
import java.util.Scanner;

public class BookingMenu {
    private final BookingController bookingController;

    public BookingMenu() {
        this.bookingController = new BookingController();

    }

    public void run(Scanner scanner) {
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.println("\n===Enter Origin Information ===");
                Location origin = locationInformation(scanner);
                System.out.println("\n===Enter Destination Information ===");
                Location destination = locationInformation(scanner);
                System.out.println("\n===Enter Shipment Date ===");
                LocalDate date = checkDate(scanner);
                Shipment newShipment = new Shipment(origin, destination, date);
                int productSwitch = 0;
                while (productSwitch != 1) {
                    System.out.println("\n=== Add A Product === ");
                    productSwitch = ProductsToShipment(scanner, newShipment);
                }
                bookMadeShipment(scanner, newShipment);
                validInput = true;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Invalid input was given!");
                System.out.println("Would you like to try again? (y/n): ");
                String answer = scanner.next().toLowerCase();
                if (!answer.equals("y")) {
                    System.out.println("Thank you for using our service!");
                    System.out.println("Returning to main menu...");
                    return;
                }

            }
        }
    }

    public Location locationInformation(Scanner scanner) {
        System.out.print("Contact Name: ");
        String contactName = scanner.nextLine();
        System.out.print("Contact Phone Number: ");
        String contactPhoneNumber = scanner.nextLine();
        System.out.print("Street Name: ");
        String streetName = scanner.nextLine();
        System.out.print("House Number: ");
        int houseNumber;
        try {
            houseNumber = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            throw new IllegalArgumentException("Please enter a valid house number!");
        }
        System.out.print("District(South, North, Central): ");
        String district = scanner.nextLine();
        if (district.equalsIgnoreCase("South")) {
            return new Location(contactName, contactPhoneNumber, streetName, houseNumber, District.South);
        } else if (district.equalsIgnoreCase("North")) {
            return new Location(contactName, contactPhoneNumber, streetName, houseNumber, District.North);
        } else if (district.equalsIgnoreCase("Central")) {
            return new Location(contactName, contactPhoneNumber, streetName, houseNumber, District.Central);
        } else {
            throw new IllegalArgumentException("Please select from the following options: South, North, Central");
        }
    }

    public LocalDate checkDate(Scanner scanner) {
        System.out.print("Enter shipment day: ");
        int shipmentDay = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter shipment month: ");
        int shipmentMonth = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter shipment year: ");
        int shipmentYear = Integer.parseInt(scanner.nextLine());
        LocalDate givenDate = LocalDate.of(shipmentYear, shipmentMonth, shipmentDay);
        if (givenDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date entered has already passed!");
        } else {
            return givenDate;
        }
    }

    public int ProductsToShipment(Scanner scanner, Shipment shipment) {
        System.out.print("Enter Product Name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter Product Weight Per Unit(kg): ");
        int productWeight = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Product Amount: ");
        int productAmount = Integer.parseInt(scanner.nextLine());
        Product newProduct = new Product(productName, productWeight);
        shipment.addProductToShipment(newProduct, productAmount);
        System.out.println("Product " + productName + " added successfully!");
        System.out.print("Do you want to add another product to the shipment? (y/n): ");
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            return 0;
        } else {
            return 1;
        }
    }

    public void bookMadeShipment(Scanner scanner, Shipment shipment) {
        System.out.print("Do you want your shipment delivered? (y/n): ");
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            this.bookingController.bookADeliveredShipment(shipment);
            System.out.println("\nYour delivery has been received and will be handled shortly! ");
        } else {
            this.bookingController.bookASelfShipment(shipment);
            System.out.println("\nYour self-booking has been confirmed! ");
        }
        System.out.println("Thank you for booking a shipment! ");
    }
}

