package pl.edu.agh.to.przychodnia.Room;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "room")
@RestController
public class RoomController {

    private final RoomService roomService;
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomDTO> getRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public RoomDTO getRoom(@PathVariable int id) {
        return roomService.findRoomDTOById(id);
    }

    @PostMapping(value ="/add")
    public RoomDTO addRoom(@RequestBody RoomDTO dto) {
        return roomService.addRoom(
                dto
        );
    }

    @GetMapping(value = "/delete/{id}")
    public Boolean deleteRoom(@PathVariable int id) {
        return roomService.deleteRoom(id);
    }

    @GetMapping(value = "/{id}/schedules")
    public List<RoomsScheduleDTO> showRoomSchedules(@PathVariable int id) {
        return roomService.showRoomSchedules(id);
    }


}

