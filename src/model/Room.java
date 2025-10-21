package model;

public class Room implements IROOM{
    private final String roomNumber;
    private final Double Price;
    private final RoomType roomType;

    public Room(String roomNumber,Double Price, RoomType roomType){
        this.roomNumber=roomNumber;
        this.Price=Price;
        this.roomType=roomType;
    }


     @Override
     public String getRoomNumber(){
        return roomNumber;
     }

    @Override
    public Double getRoomPrice() {
        return Price;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return this.Price == null || this.Price == 0;
    }


    @Override
    public String toString(){
        return "Room Number: "+roomNumber+"Price: "+Price+"Room Type:"+roomType;
    }
}
