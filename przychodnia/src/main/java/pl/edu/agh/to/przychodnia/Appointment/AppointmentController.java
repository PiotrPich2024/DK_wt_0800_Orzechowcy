package pl.edu.agh.to.przychodnia.Appointment;

import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.przychodnia.Doctor.DoctorService;
import pl.edu.agh.to.przychodnia.Patient.PatientService;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

@Tag(
        name = "Appointment",
        description = "Operacje związane z wizytami lekarskimi"
)
@RequestMapping(path = "appointment")
@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final ScheduleService scheduleService;


    public AppointmentController(AppointmentService appointmentService,
                                 PatientService patientService,
                                 ScheduleService scheduleService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.scheduleService = scheduleService;
    }

    @Operation(
            summary = "Pobierz wszystkie wizyty",
            description = "Zwraca listę wszystkich wizyt w systemie"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista wizyt"
    )
    @GetMapping
    public List<String> getAppointments() {
        return appointmentService.getAllAppointments();
    }

    @Operation(
            summary = "Pobierz wolne terminy lekarza",
            description = "Zwraca listę wolnych wizyt dla danego lekarza w podanym dniu do maksymalnie tygodnia"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista wolnych terminów lekarza"
    )
    @GetMapping("/{doctorID}/{date}")
    public List<DoctorsAppointmentsDTO> getFreeDoctorAppointments(
            @Parameter(
                    description = "specjalizacja lekarza",
                    example = "Kardiolog"
            )
            @PathVariable("doctorID") String doctorSpecialization,

            @Parameter(
                    description = "Data wizyty (ISO-8601)",
                    example = "2026-01-24T10:00"
            )
            @PathVariable("date") LocalDateTime date
    ) {
        return appointmentService.getFreeDoctorAppointments(doctorSpecialization, date);
    }

    @Operation(
            summary = "Dodaj nową wizytę",
            description = "Tworzy nową wizytę na podstawie przekazanych danych (jeśli termin nie jest zajęty)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Utworzona wizyta",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetAppointmentDTO.class)
            )
    )
    @PostMapping(value = "/add")
    public GetAppointmentDTO addAppointment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dane nowej wizyty",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Przykładowa wizyta",
                                    value = """
                    {
                      "patientId": 1,
                      "scheduleId": 1,
                      "startDate": "2026-01-24T11:30",
                      "endDate": "2026-01-24T11:45"
                    }
                    """
                            )
                    )
            )
            @RequestBody CreateAppointmentDTO dto
    ) {
        return appointmentService.createAppointment(dto);
    }

    @Operation(
            summary = "Usuń wizytę",
            description = "Usuwa wizytę o podanym ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Wizyta została usunięta"
    )
    @GetMapping("/{id}/delete")
    public Boolean deleteAppointment(
            @Parameter(
                    description = "ID wizyty",
                    example = "10"
            )
            @PathVariable("id") int id
    ) {
        return appointmentService.deleteAppointment(id);
    }
}
