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
    public List<String> getRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable int id) {
        return roomService.findRoomById(id);
    }

    @PostMapping(value ="/add")
    public Room addRoom(@RequestBody Map<String, String> map) {
        return roomService.addRoom(
                Integer.parseInt(map.get("roomNumber")),
                map.get("description")
        );
    }

    @GetMapping(value = "/delete/{id}")
    public Boolean deleteRoom(@PathVariable int id) {
        return roomService.deleteRoom(id);
    }

    @GetMapping(value = "/{id}/schedules")
    public List<String> showRoomSchedules(@PathVariable int id) {
        return roomService.showRoomSchedules(id);
    }


}

