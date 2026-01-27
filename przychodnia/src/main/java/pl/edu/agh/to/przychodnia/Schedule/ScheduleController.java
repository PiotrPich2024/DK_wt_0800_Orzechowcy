package pl.edu.agh.to.przychodnia.Schedule;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.przychodnia.Doctor.DoctorService;
import pl.edu.agh.to.przychodnia.Room.Room;
import pl.edu.agh.to.przychodnia.Room.RoomService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
@RequestMapping(path = "schedule")
@RestController
public class ScheduleController {


    private final ScheduleService scheduleService;
    private final RoomService roomService;
    private final DoctorService doctorService;


    public ScheduleController(ScheduleService scheduleService, RoomService roomService, DoctorService doctorService) {
        this.scheduleService = scheduleService;
        this.roomService = roomService;
        this.doctorService = doctorService;
    }


    @Operation(
            summary = "Pobierz wszystkie grafiki",
            description = "Zwraca listę wszystkich grafików w systemie"
    )
    @ApiResponse(responseCode = "200", description = "Lista grafików")
    @GetMapping
    public List<GetScheduleDTO> getSchedules() {
        return scheduleService.getAllSchedules();
    }


    @Operation(
            summary = "Dodaj nowy grafik",
            description = "Tworzy nowy grafik lekarza i pokoju na podstawie przekazanych danych"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Utworzony grafik",
            content = @Content(schema = @Schema(implementation = GetScheduleDTO.class))
    )
    @PostMapping(value = "/add")
    public GetScheduleDTO addSchedule(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dane nowego grafiku",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Przykładowy grafik",
                                    value = """
                                    {
                                    "startDate": "2026-01-24T08:00",
                                    "endDate": "2026-01-24T14:00",
                                    "doctorId": 3,
                                    "roomId": 1
                                    }
                                    """
                            )
                    )
            )
            @RequestBody CreateScheduleDTO dto
    ) {
        return scheduleService.addSchedule(dto);
    }


    @Operation(
            summary = "Usuń grafik",
            description = "Usuwa grafik o podanym ID z systemu"
    )
    @ApiResponse(responseCode = "200", description = "Status usunięcia grafiku")
    @GetMapping(value = "/delete/{id}")
    public Boolean deleteSchedule(
            @Parameter(description = "ID grafiku", example = "12")
            @PathVariable int id
    ) {
        return scheduleService.deleteSchedule(id);
    }


    @Operation(
            summary = "Sprawdź dostępne terminy",
            description = "Zwraca listę dostępnych slotów czasowych na podstawie podanych kryteriów"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista dostępnych terminów",
            content = @Content(schema = @Schema(implementation = AvailableSlotDTO.class))
    )
    @PostMapping(value = "/available")
    public List<AvailableSlotDTO> showAvailable(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Kryteria wyszukiwania dostępnych terminów",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Przykładowe kryteria",
                                    value = """
                                    {
                                    "startDate": "2026-01-24T08:00",
                                    "endDate": "2026-01-24T16:00",
                                    "specialization" : "Kardiolog"
                                    }
                                    """
                            )
                    )
            )
            @RequestBody AvailableScheduleDTO dto
    ) {
        return scheduleService.showAvailable(dto);
    }
}

