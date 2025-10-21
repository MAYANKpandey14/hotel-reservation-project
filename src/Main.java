import api.AdminResource;
import api.HotelResource;
import ui.MainMenu;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.lang.System; // Required for System.in and System.out


public class Main {
    public static void main(String[] args) {
        HotelResource hotelResource = new HotelResource();
        AdminResource adminResource = new AdminResource();
        Scanner scanner = new Scanner(System.in);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date today = new Date();
        MainMenu mainMenu = new MainMenu(hotelResource, scanner, today, dateFormat);
        System.out.println("Starting Hotel Reservation Application...");

        mainMenu.startMenu(adminResource);
    }
}