package pl.edu.agh.to.przychodnia.Room;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "room")
@RestController
public class RoomController {

    private final RoomService roomService;
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable int id) {
        return roomService.findRoomById(id);
    }

    @PostMapping
    public Room addRoom(@RequestBody Map<String, String> map) {
        return roomService.addRoom(
                Integer.parseInt(map.get("roomNumber")),
                map.get("description")
        );
    }

    @GetMapping(value = "/delete/{id}")
    public void deleteRoom(@PathVariable int id) {
        roomService.deleteRoom(id);
    }


}

