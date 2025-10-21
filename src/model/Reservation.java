package model;

import java.util.Date;



public class Reservation {
    private final Customer customer;
    private final IROOM room;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Reservation(Customer customer, IROOM room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public IROOM getRoom() {
        return room;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        return "Reservation for " + customer + "\n in Room " + room + "\n Check In: " + checkInDate + "\n Check Out: " + checkOutDate;

    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Reservation other)) {
            return false;
        }
        // Compare rooms
        boolean roomsEquals = (room == null && other.getRoom() == null) ||
                (room != null && room.equals(other.getRoom()));
        // Compare check-in
        boolean checkInEquals = (checkInDate == null && other.getCheckInDate() == null)
                || (checkInDate != null && checkInDate.equals(other.getCheckInDate()));
        // Compare check-out
        boolean checkOutEquals = (checkOutDate == null && other.getCheckOutDate() == null)
                || (checkOutDate != null && checkOutDate.equals(other.getCheckOutDate()));

        return roomsEquals && checkInEquals && checkOutEquals;
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (room != null) {
            result = 31 * result + room.hashCode();
        }
        if (checkInDate != null) {
            result = 31 * result + checkInDate.hashCode();
        }
        if (checkOutDate != null) {
            result = 31 * result + checkOutDate.hashCode();
        }
        return result;
    }
}



