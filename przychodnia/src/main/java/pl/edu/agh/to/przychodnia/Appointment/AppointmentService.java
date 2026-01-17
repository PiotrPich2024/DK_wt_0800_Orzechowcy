package pl.edu.agh.to.przychodnia.Appointment;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Doctor.DoctorService;
import pl.edu.agh.to.przychodnia.Patient.Patient;
import pl.edu.agh.to.przychodnia.Patient.PatientRepository;
import pl.edu.agh.to.przychodnia.Patient.PatientService;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private ScheduleRepository scheduleRepository;
    private PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              ScheduleRepository scheduleRepository,
                              PatientRepository patientRepository
                              ) {
        this.appointmentRepository = appointmentRepository;
        this.scheduleRepository = scheduleRepository;
        this.patientRepository = patientRepository;
    }

    private GetAppointmentDTO maptoDTO(Appointment appointment) {
        return new GetAppointmentDTO(
                appointment.getId(),
                appointment.getPatient().getFirstName(),
                appointment.getPatient().getLastName(),
                appointment.getSchedule().getDoctor().getFirstName(),
                appointment.getSchedule().getDoctor().getLastName(),
                appointment.getSchedule().getDoctor().getSpecialization().toString(),
                appointment.getSchedule().getRoom().getRoomNumber(),
                appointment.getAppointmentStart(),
                appointment.getAppointmentEnd()
        );
    }

    public GetAppointmentDTO createAppointment(CreateAppointmentDTO appointmentDTO) {
        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Schedule schedule = scheduleRepository.findById(appointmentDTO.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        for (Appointment appointment : appointmentRepository.findAll()) {
            if (appointment.getSchedule().getId() == schedule.getId()
                    && appointment.getAppointmentStart().isEqual(appointmentDTO.getStartDate())) {
                throw new IllegalStateException("This appointment slot is already taken");
            }
        }

        Appointment appointment = new Appointment(
                patient,
                schedule,
                appointmentDTO.getStartDate(),
                appointmentDTO.getStartDate().plusMinutes(15)
        );


        return maptoDTO(appointmentRepository.save(appointment));
    }

    public List<String> getAllAppointments() {
        ArrayList<String> appointments = new ArrayList<>();
        for(Appointment appointment : appointmentRepository.findAll()){
            appointments.add(appointment.toString());
        }
        return appointments;

    }

    public Boolean deleteAppointment(int id) {
        if(appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<DoctorsAppointmentsDTO> getFreeDoctorAppointments(int doctorID, LocalDateTime date) {

        List<DoctorsAppointmentsDTO> doctorsFreeAppointments = new ArrayList<>();
        List<Appointment> doctorsTakenAppointments = new ArrayList<>();
        for (Appointment appointment : appointmentRepository.findAll()) {
            if (appointment.getSchedule().getDoctorId() == doctorID) {
                doctorsTakenAppointments.add(appointment);
            }
        }

        for (Schedule schedule : scheduleRepository.findAll()) {
            // pomijamy dyżury które są tydzień
            if(schedule.getStartdate().isAfter(date.plusDays(7))){
                continue;
            }
            if (schedule.getDoctor().getId() == doctorID) {
                LocalDateTime startDate =  schedule.getStartdate();
                LocalDateTime endDate =  schedule.getEnddate();
                while (startDate.isBefore(endDate)) {
                    Boolean flag = true;
                    for (Appointment appointment : doctorsTakenAppointments) {
                        if (appointment.getAppointmentStart().isEqual(startDate)){
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        doctorsFreeAppointments.add(new DoctorsAppointmentsDTO(
                                schedule.getDoctor().getFirstName(),
                                schedule.getDoctor().getLastName(),
                                schedule.getDoctor().getSpecialization().toString(),
                                schedule.getRoom().getRoomNumber(),
                                startDate
                        ));
                    }
                    startDate = startDate.plusMinutes(15);
                }
            }
        }

        return doctorsFreeAppointments;
    }

}
