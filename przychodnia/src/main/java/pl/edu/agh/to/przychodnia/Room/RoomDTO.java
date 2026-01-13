package pl.edu.agh.to.przychodnia.Room;

public class RoomDTO {
    private int roomNumber;
    private String roomDescription;

    public RoomDTO(int roomNumber, String roomDescription) {
        this.roomNumber = roomNumber;
        this.roomDescription = roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }
    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public int getRoomNumber() {
        return roomNumber;
    }
}
