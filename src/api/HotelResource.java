package api;

import model.Customer;
import model.IROOM;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {

    public HotelResource() {
    }

    public Customer getCustomer(String email) {
        return CustomerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        CustomerService.addCustomer(email, firstName, lastName);
    }

    public IROOM getRoom(String roomNO) {
        return ReservationService.getARoom(roomNO);
    }

    public Reservation bookARoom(String customerEmail, IROOM room, Date checkInDate, Date checkOutDate) {
        Customer presentCustomer = getCustomer(customerEmail);
        return ReservationService.reserveARoom(presentCustomer, room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomerReservations(String customerEmail) {
        Customer newCustomer = getCustomer(customerEmail);
        return ReservationService.getCustomerReservation(newCustomer);
    }

    public Collection<IROOM> findARoom(Date checkIn, Date checkOut) {
        return ReservationService.findRooms(checkIn, checkOut);
    }
}
