package pl.edu.agh.to.przychodnia.Room;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Rooms",
        description = "Operacje związane z pokojami: pobieranie, dodawanie, usuwanie oraz grafiki"
)
@RequestMapping(path = "room")
@RestController
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(
            summary = "Pobierz listę pokoi",
            description = "Zwraca listę wszystkich pokoi w systemie"
    )
    @ApiResponse(responseCode = "200", description = "Lista pokoi")
    @GetMapping
    public List<RoomDTO> getRooms() {
        return roomService.getAllRooms();
    }

    @Operation(
            summary = "Pobierz pokój po ID",
            description = "Zwraca szczegóły pokoju o podanym identyfikatorze"
    )
    @ApiResponse(responseCode = "200", description = "Dane pokoju")
    @GetMapping("/{id}")
    public RoomDTO getRoom(
            @Parameter(description = "ID pokoju", example = "101")
            @PathVariable int id
    ) {
        return roomService.findRoomDTOById(id);
    }

    @Operation(
            summary = "Dodaj nowy pokój",
            description = "Tworzy nowy pokój na podstawie przekazanych danych"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Utworzony pokój",
            content = @Content(schema = @Schema(implementation = RoomDTO.class))
    )
    @PostMapping(value = "/add")
    public RoomDTO addRoom(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dane nowego pokoju",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Przykładowy pokój",
                                    value = """
                        {
                            "roomNumber": 102,
                            "roomDescription":"Gabinet Kardiologiczny"
                        }
                        """
                            )
                    )
            )
            @RequestBody RoomDTO dto
    ) {
        return roomService.addRoom(dto);
    }

    @Operation(
            summary = "Usuń pokój",
            description = "Usuwa pokój o podanym ID z systemu"
    )
    @ApiResponse(responseCode = "200", description = "Status usunięcia pokoju")
    @GetMapping(value = "/delete/{id}")
    public Boolean deleteRoom(
            @Parameter(description = "ID pokoju", example = "101")
            @PathVariable int id
    ) {
        return roomService.deleteRoom(id);
    }

    @Operation(
            summary = "Pobierz grafiki pokoju",
            description = "Zwraca listę grafików / rezerwacji pokoju o podanym ID"
    )
    @ApiResponse(responseCode = "200", description = "Lista grafików pokoju")
    @GetMapping(value = "/{id}/schedules")
    public List<RoomsScheduleDTO> showRoomSchedules(
            @Parameter(description = "ID pokoju", example = "101")
            @PathVariable int id
    ) {
        return roomService.showRoomSchedules(id);
    }
}
