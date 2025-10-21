package api;

import model.Customer;
import model.IROOM;
import service.CustomerService;
import service.ReservationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AdminResource {
    public AdminResource() {

    }

    public Customer getCustomer(String email) {
        return CustomerService.getCustomer(email);
    }

    public void addRoom(List<IROOM> rooms) {
        for (IROOM newRoom : rooms) {
            ReservationService.addRoom(newRoom);
        }
    }

    public Collection<IROOM> getAllRooms() {
        Map<String, IROOM> allRooms = ReservationService.getRooms();
        return new ArrayList<>(allRooms.values());
    }

    public Collection<Customer> getAllCustomers() {
        return CustomerService.getAllCustomers();
    }

    public void displayAllReservations() {
        ReservationService.printAllReservation();
    }

}
