package ui;

import api.AdminResource;
import api.HotelResource;
import model.IROOM;
import model.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainMenu {

    private final HotelResource hotelResource; // adding API
    private final Scanner scanner; // for taking user input
    private final DateFormat dateFormat;
    private final Date today;

    public MainMenu(HotelResource hotelResource, Scanner scanner, Date today, DateFormat dateFormat) {
        this.hotelResource = hotelResource;
        this.scanner = new Scanner(System.in);
        this.today = today;
        this.dateFormat = dateFormat;
    }

    public boolean startMenu(AdminResource adminResource) {
        boolean active = true;
        try {
            while (active) {
                activateMainMenu();
                int input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1:
                        makeAReservation();
                        return true;
                    case 2:
                        showCustomerReservations();
                        return true;
                    case 3:
                        createAccount();
                        return true;
                    case 4:
                        AdminMenu adminMenu= new AdminMenu(adminResource,scanner);
                        adminMenu.start();
                        return true;
                    case 5:
                        System.out.println("Exiting application. Goodbye!");
                        return true;
                    default:
                        System.out.println("Invalid selection. Please enter a number between 1 and 5.");
                        return true;
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println("Please enter a number between 1-5;");
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unknown Error Occurred: " + ex.getMessage());

        }
        return active;
    }

    private void createAccount() {
        System.out.print("Enter Email (name@domain.com): ");
        String email = getEmail();

        // Check that customer with this email does not already exist
        if (alreadyRegistered(email)) {
            System.out.println("Customer with this email already registered. Returning to Main Menu.");
            return;
        }

        System.out.print("Enter First Name: ");
        String firstName = getName(true);

        System.out.print("Enter Last Name: ");
        String lastName = getName(false);

        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("\nAccount created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("\nError creating account: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred: " + e.getMessage());
        }
    }

    private String getName(boolean isFirstName) {
        String nameType = isFirstName ? "first" : "last";
        String name = "";
        boolean read = true;
        while (read) {
            String input = scanner.nextLine();
            if (!notEmpty(input)) {
                System.out.println("Your " + nameType + " name should have at least one letter.");
                continue;
            }
            name = input;
            read = false;
        }
        return name;
    }
    private boolean notEmpty(String input) {
        return input.matches(".*[a-zA-Z]+.*");
    }

    private void activateMainMenu() {
        System.out.println("\n----------------------------------------------------");
        System.out.println("Welcome to Mayank's Hotel Reservation App");
        System.out.println("----------------------------------------------------");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("----------------------------------------------------");
        System.out.print("Please enter a selection (1-5): ");
    }

    public void showCustomerReservations() {
        System.out.println("Please enter your email address");
        boolean read = true;
        String email = "";
        while (read) {
            String input = scanner.nextLine();
            if (checkEmaiL(input)) {
                System.out.println("Please enter valid email only, for ex- Johndoe@gmail.com");
            }
            email = input;
            read = false;
        }
        if (hotelResource.getCustomer(email) == null) {
            System.out.println("You are not registered with this email, Please make an account with us.");
        }
        Collection<Reservation> customerReservations = hotelResource.getCustomerReservations(email);

        if (customerReservations.size() < 0) {
            System.out.println("You have currently have no reservations with us.");
        } else {
            System.out.println("You have " + customerReservations.size() + " reservations");
            for (Reservation currentReservation : customerReservations) {
                System.out.println(currentReservation);
            }
        }
    }

    public boolean checkEmaiL(String email) {
        String Email_regex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(Email_regex);
        return !pattern.matcher(Email_regex).matches(); //returns true or false;
    }


    public void makeAReservation() {
        boolean check = true;
        while (check) {
            System.out.println("Enter check-in date in format mm/dd/yyyy " +
                    "Example: 05/30/2022");
            Date checkInDate = getDate();
            System.out.println("Enter check-out date in format mm/dd/yyyy " +
                    "Example: 06/03/2022");
            Date checkOutDate = getDate();
            if (checkInDate.after(checkOutDate)) {
                System.out.println("Make sure check out date is later than check in date. Please re-enter dates.");
                continue;
            }
            Collection<IROOM> roomsAvailable = AvailableRooms(checkInDate, checkOutDate);
            if (roomsAvailable.isEmpty()) {
                check = false;
                continue;
            }
            System.out.println("Following rooms are available: ");
            for (IROOM room : roomsAvailable) {
                System.out.println(room);
            }
            // Ask if customer wants to book a room
            if (stopBooking()) {
                check = false; // Go to main menu
                continue;
            }

            // Ask if customer has an account
            if (HasNoAccount()) {
                check = false; // Go to main menu
                continue;
            }

            System.out.println("Please enter your email");
            String email = getEmail();

            if (!alreadyRegistered(email)) {
                System.out.println("You are not registered with this email. Please make an account before bookig a room");
                check = false;
                continue;
            }

            String roomNo = getRoomNo(roomsAvailable);

            try {
                IROOM room = hotelResource.getRoom(roomNo);
                Reservation newReservation = hotelResource.bookARoom(email, room, checkInDate, checkOutDate);
                System.out.println("\n Room booked successfully!");
                System.out.println(newReservation);
            } catch (IllegalArgumentException ex) {
                System.out.println("\nError during booking: " + ex.getMessage());
            }

            check = false;
        }
    }

    private String getRoomNo(Collection<IROOM> roomsAvailable) {
        System.out.println("Please enter the room number to book:");
        String roomNo = "";
        boolean read = true;
        while (read) {
            String input = scanner.nextLine();
            boolean isAvailableRoom = roomsAvailable.stream()
                    .anyMatch(room -> room.getRoomNumber().equals(input));

            if (isAvailableRoom) {
                read = false;
                roomNo = input;
            } else {
                System.out.println("The room number you entered is not available. Please enter a room number from the list above.");
            }
        }
        return roomNo;
    }

    private boolean alreadyRegistered(String email) {
        return hotelResource.getCustomer(email) != null;
    }


    private Collection<IROOM> AvailableRooms(Date checkIn, Date checkOut) {
        Collection<IROOM> availableRooms = hotelResource.findARoom(checkIn, checkOut);

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms found for selected dates. Trying to find a room in the next 7 days...");

            // Shift dates
            Date shiftedCheckIn = changeDate(checkIn);
            Date shiftedCheckOut = changeDate(checkOut);

            // Find rooms available for shifted dates
            availableRooms = hotelResource.findARoom(shiftedCheckIn, shiftedCheckOut);

            if (availableRooms.isEmpty()) {
                System.out.println("No free rooms found, even after checking the next 7 days. Try different dates.");
            } else {
                // Print shifted dates and available rooms
                System.out.println("You can book following rooms from " + dateFormat.format(shiftedCheckIn) +
                        " till " + dateFormat.format(shiftedCheckOut) + ":");
            }
        }
        return availableRooms;
    }

    private boolean HasNoAccount() {
        System.out.println("Do you have an account? (y/n)");
        boolean customerHasNoAccount = false;
        boolean keepReadingAnswer = true;
        while (keepReadingAnswer) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "y" -> keepReadingAnswer = false; // Proceed with booking
                case "n" -> {
                    System.out.println("Please create an account using option 3 in the main menu.");
                    customerHasNoAccount = true; // Stops the booking flow
                    keepReadingAnswer = false;
                }
                default -> System.out.println("Enter \"y\" for yes or \"n\" for no");
            }
        }
        return customerHasNoAccount;
    }

    private Date getDate() {
        boolean keepReadingDate = true;
        Date date = null;
        while (keepReadingDate) {
            String input = scanner.nextLine();
            if (checkDate(input)) {
                try {
                    date = dateFormat.parse(input);
                } catch (ParseException ex) {
                    System.out.println("Try entering the date again.");
                    continue;
                }

                // Check if date is in the future
                if (!date.before(today)) {
                    keepReadingDate = false;
                } else {
                    System.out.println("This date is in the past. Please re-enter the date.");
                }
            } else {
                System.out.println("Re-enter the date in format " + dateFormat + ".");
            }
        }
        return date;
    }

    private String getEmail() {
        boolean keepReadingEmail = true;
        String email = "";
        while (keepReadingEmail) {
            String input = scanner.nextLine();
            // Validate email format
            if (checkEmaiL(input)) {
                System.out.println("It is not a valid email format. Please enter like example@mail.com");
                continue;
            }
            email = input;
            keepReadingEmail = false;
        }
        return email;
    }


    private boolean checkDate(String input) {
        try {
            dateFormat.parse(input);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    private Date changeDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    private boolean stopBooking() {
        System.out.println("Would you like to book one of the rooms above? (y/n)");
        boolean stopBooking = false;
        boolean read = true;
        while (read) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "y" -> read = false;
                case "n" -> {
                    read = false;
                    stopBooking = true;
                }
                default -> System.out.println("Enter \"y\" for yes or \"n\" for no");
            }
        }
        return stopBooking;
    }

}
