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

    @GetMapping
    public List<String> getAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{doctorID}/{date}")
    public List<DoctorsAppointmentsDTO> getFreeDoctorAppointments(@PathVariable("doctorID") int doctorID, @PathVariable("date") LocalDateTime date) {
        return appointmentService.getFreeDoctorAppointments(doctorID, date);
    }

    @PostMapping(value = "/add")
    public GetAppointmentDTO addAppointment(@RequestBody CreateAppointmentDTO dto) {
        return appointmentService.createAppointment(dto);
    }

    @GetMapping("/{id}/delete")
    public Boolean deleteAppointment(@PathVariable("id") int id) {
        return appointmentService.deleteAppointment(id);
    }
}
