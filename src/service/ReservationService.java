package service;

import model.Customer;
import model.IROOM;
import model.Reservation;

import java.util.*;

public final class ReservationService {
    private static final Map<String, IROOM> rooms = new HashMap<String, IROOM>();
    private static final Set<Reservation> reservations = new HashSet<Reservation>();

    private ReservationService() {
    }

    public static void addRoom(IROOM room) {
        String roomNO = room.getRoomNumber();
        if (rooms.containsKey(roomNO)) {
            throw new IllegalArgumentException("Room No: " + roomNO + " already present.");
        }
        rooms.put(roomNO, room);
    }

    public static IROOM getARoom(String roomID) {
        return rooms.get(roomID);
    }

    public static Map<String,IROOM> getRooms(){
        return rooms;
    }

    public static Reservation reserveARoom(Customer customer, IROOM room, Date checkInDate, Date checkOutDate) {
        if (checkInDate.equals(checkOutDate) || checkInDate.after(checkOutDate)) {
            throw new IllegalArgumentException("Check In date must be before Check Out date");
        }
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
        for (Reservation presentReservation : reservations) {
            if (Objects.equals(room, presentReservation.getRoom())) {
                if (isConflict(newReservation, presentReservation)) {
                    throw new IllegalArgumentException("This Room is already booked !");
                }
            }
        }
        reservations.add(newReservation);
        return newReservation;
    }

    public static Collection<IROOM> findRooms(Date checkInDate, Date checkOutDate) {
        Map<String, IROOM> availableRooms = new HashMap<>(rooms);

        for (Reservation presentReservation : reservations) {
            if (isConflict(presentReservation.getCheckInDate(), presentReservation.getCheckOutDate(), checkInDate, checkOutDate)) {
                availableRooms.remove(presentReservation.getRoom().getRoomNumber());
            }
        }
        return new ArrayList<>(availableRooms.values());
    }

    public static Collection<Reservation> getCustomerReservation(Customer customer) {
        List<Reservation> customerReservations = new ArrayList<>();
        for (Reservation oneReservation : reservations) {
            if (oneReservation.getCustomer().equals(customer)) {
                customerReservations.add(oneReservation);
            }
        }
        return customerReservations;
    }

    public static void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println("There are no reservations.");
        } else {
            System.out.println("\n----There are " + reservations.size() + " reservations----");
            for (Reservation eachReservation : reservations) {
                System.out.println(eachReservation.toString());
            }
            System.out.println("----------------------");
        }
    }

    public static boolean isConflict(Reservation newReservation, Reservation presentReservation) {
        if (newReservation.getRoom().getRoomNumber().equals(presentReservation.getRoom().getRoomNumber())) {
            return isConflict(presentReservation.getCheckInDate(), presentReservation.getCheckOutDate(), newReservation.getCheckInDate(), newReservation.getCheckOutDate());
        }
        return false;
    }

    private static boolean isConflict(Date bookedCheckIn, Date bookedCheckOut, Date newCheckIn, Date newCheckOut) {
        boolean bookedEndsAfterNewStarts = bookedCheckOut.after(newCheckIn);
        boolean bookedStartsBeforeNewEnds = bookedCheckIn.before(newCheckOut);
        return bookedEndsAfterNewStarts && bookedStartsBeforeNewEnds;
    }
}

