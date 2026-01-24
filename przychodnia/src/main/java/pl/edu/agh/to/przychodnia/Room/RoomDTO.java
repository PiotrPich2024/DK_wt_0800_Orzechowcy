package pl.edu.agh.to.przychodnia.Room;

import io.swagger.v3.oas.annotations.media.Schema;

public class RoomDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "5")
    private int id;
    private int roomNumber;
    private String roomDescription;

    public RoomDTO(int id, int roomNumber, String roomDescription) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomDescription = roomDescription;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
