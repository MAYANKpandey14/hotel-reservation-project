package ui;

import api.AdminResource;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;


public final class AdminMenu {

    private final AdminResource adminResource;
    private final Scanner scanner;

    public AdminMenu(AdminResource adminResource, Scanner scanner) {
        this.adminResource = adminResource;
        this.scanner = scanner;
    }

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
            System.out.println("\nAdmin Menu Error:" + ex.getMessage());
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
                displayAllCustomers();
                return true;
            case "2":
                displayAllRooms();
                return true;
            case "3":
                adminResource.displayAllReservations();
                return true;
            case "4":
                addNewRoom();
                return true;
            case "5":
                return false;
            default:
                System.out.println("Invalid selection. Please enter a number between 1 and 5.");
                return true;
        }
    }

    private void addNewRoom() {
        List<IROOM> newRooms = new ArrayList<>();
        boolean addAnother = true;
        while (addAnother) {
            try {
                String roomNo = getRoomNumber();
                double price = getRoomPrice();
                RoomType roomType = getRoomType();
                IROOM newRoom;
                if (price == 0.0) {
                    newRoom = new FreeRoom(roomNo, roomType);
                } else {
                    newRoom = new Room(roomNo, price, roomType);
                }

                newRooms.add(newRoom);
                System.out.println("New Room added successfully: " + newRoom);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            addAnother = ifAddAnotherRoom();
        }

        if (!newRooms.isEmpty()) {
            try {
                adminResource.addRoom(newRooms);
                System.out.println("\nSuccessfully added " + newRooms.size() + " new room(s).");
            } catch (IllegalArgumentException ex) {
                System.out.println("\nError saving rooms: " + ex.getMessage());
            }
        }
    }

    private RoomType getRoomType() {
        while (true) {
            System.out.println("Enter Room type 's' for SINGLE and 'd' for DOUBLE: ");
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "s":
                    return RoomType.SINGLE;
                case "d":
                    return RoomType.DOUBLE;
                default:
                    System.out.println("Invalid Selection: Please enter 's' or 'd' ");
            }
        }
    }

    private double getRoomPrice() {
        while (true) {
            System.out.println("Enter Room price in $");
            try {
                double price = Double.parseDouble(scanner.nextLine().trim());
                if (price < 0) {
                    System.out.println("Room's price cannot be less than 0");
                } else {
                    return price;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number: " + ex.getMessage());
            }
        }
    }

    private String getRoomNumber() {
        while (true) {
            System.out.println("Enter Room number: ");
            String roomNo = scanner.nextLine().trim();
            if (roomNo.isEmpty()) {
                System.out.println("Please enter a number, Room number cannot be empty");
            } else {
                return roomNo;
            }
        }
    }

    private boolean ifAddAnotherRoom() {
        while (true) {
            System.out.println("Would like to add another room ? Y or N: ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid Input. Please enter either Y or N");
            }
        }
    }

    private void displayAllRooms() {
        Collection<IROOM> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("\n There are no rooms available");
        } else {
            System.out.println("\n----All Rooms----");
            for (IROOM room : rooms) {
                System.out.println(room);
            }
        }
    }


    private void displayAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("\n There are no registered Customers");
        } else {
            System.out.println("\n----All Customers----");
            for (Customer customer : customers) {
                System.out.println(customer);
            }
            System.out.println("-----------------------");
        }
    }
}
