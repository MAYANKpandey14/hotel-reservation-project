package ui;

import api.AdminResource;
import model.IROOM;

import java.util.Scanner;

/**
 * Manages the administrative menu loop and operations.
 * Consolidates admin interface logic.
 */
public final class AdminMenu {

    private final AdminResource adminResource;
    private final Scanner scanner;

    public AdminMenu(AdminResource adminResource, Scanner scanner) {
        this.adminResource = adminResource;
        this.scanner = scanner;
    }

    /**
     * Starts the administrator menu and handles the loop.
     */
    public void start() {
        String choice;
        boolean active = true;

        try {
            while (active) {
                showMenu();
                choice = scanner.nextLine();
                active = processChoice(choice);
            }
        } catch (Exception ex) {
            System.out.println("\nAdmin Menu Error: Bad input or unexpected issue. Returning to Main Menu.");
        }
    }

    private void showMenu() {
        System.out.println("\n----------------------------------------------------");
        System.out.println("                     Admin Control Panel");
        System.out.println("----------------------------------------------------");
        System.out.println("1. View All Customers");
        System.out.println("2. View All Rooms");
        System.out.println("3. View All Bookings");
        System.out.println("4. Create New Room");
        System.out.println("5. Return to Main Menu");
        System.out.println("----------------------------------------------------");
        System.out.print("Enter your option (1-5): ");
    }

    private boolean processChoice(String choice) {
        switch (choice) {
            case "1":
                System.out.println("View All Customers selected.");
                adminResource.getAllCustomers();
                return true;
            case "2":
                // Logic for viewing all room records
                System.out.println("View All Rooms selected.");
                adminResource.getAllRooms();
                return true;
            case "3":

                System.out.println("View All Bookings selected.");
                adminResource.displayAllReservations();
                return true;
            case "4":

                System.out.println("Create New Room selected.");
                return true;
            case "5":
                return false;
            default:
                System.out.println("Invalid selection. Please enter a number between 1 and 5.");
                return true;
        }
    }
}
