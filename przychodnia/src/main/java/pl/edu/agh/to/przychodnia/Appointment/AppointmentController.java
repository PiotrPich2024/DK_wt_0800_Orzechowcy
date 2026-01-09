package pl.edu.agh.to.przychodnia.Appointment;

import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.przychodnia.Doctor.DoctorService;
import pl.edu.agh.to.przychodnia.Patient.PatientService;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @PostMapping(value = "/add")
    public Appointment addAppointment(@RequestBody Map<String, String> map) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        Date endDate;

        try {
            startDate = parser.parse(map.get("startDate"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            endDate = parser.parse(map.get("endDate"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Appointment new_appointment = new Appointment(
                patientService.findPatientById(Integer.parseInt(map.get("patientId"))),
                scheduleService.findScheduleById(Integer.parseInt(map.get("scheduleId"))),
                startDate,
                endDate
        );
        return appointmentService.createAppointment(new_appointment);
    }
}
