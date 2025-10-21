import api.AdminResource;
import api.HotelResource;
import ui.MainMenu;

void main() {
    HotelResource hotelResource = new HotelResource();
    AdminResource adminResource = new AdminResource();
    Scanner scanner = new Scanner(System.in);
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date today = new Date(); // Represents the current date for validation
    MainMenu mainMenu = new MainMenu(hotelResource, scanner, today, dateFormat);
    IO.println("Starting Hotel Reservation Application...");
    mainMenu.startMenu(adminResource);
}
