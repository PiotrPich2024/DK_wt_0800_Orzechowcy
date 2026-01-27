package pl.edu.agh.to.przychodnia.Appointment;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Doctor.DoctorService;
import pl.edu.agh.to.przychodnia.Doctor.Specialization;
import pl.edu.agh.to.przychodnia.Patient.Patient;
import pl.edu.agh.to.przychodnia.Patient.PatientRepository;
import pl.edu.agh.to.przychodnia.Patient.PatientService;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Service responsible for handling business logic related to medical appointments.
 * <p>
 * This service allows creating, deleting and retrieving appointments,
 * as well as finding available appointment slots for a given doctor.
 */
@Service
public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private ScheduleRepository scheduleRepository;
    private PatientRepository patientRepository;

    /**
     * Constructs an AppointmentService with required repositories.
     *
     * @param appointmentRepository repository for appointments
     * @param scheduleRepository repository for schedules
     * @param patientRepository repository for patients
     */
    public AppointmentService(AppointmentRepository appointmentRepository,
                              ScheduleRepository scheduleRepository,
                              PatientRepository patientRepository
                              ) {
        this.appointmentRepository = appointmentRepository;
        this.scheduleRepository = scheduleRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Maps an {@link Appointment} entity to a {@link GetAppointmentDTO}.
     *
     * @param appointment appointment entity to be mapped
     * @return mapped appointment DTO
     */
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

    /**
     * Creates a new appointment for a given patient and schedule.
     * <p>
     * The appointment lasts 15 minutes. If the selected time slot
     * is already taken, an exception is thrown.
     *
     * @param appointmentDTO DTO containing appointment creation data
     * @return created appointment as DTO
     * @throws IllegalArgumentException if patient or schedule does not exist
     * @throws IllegalStateException if the appointment slot is already taken
     */
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

    /**
     * Retrieves all appointments as string representations.
     *
     * @return list of appointment strings
     */
    public List<String> getAllAppointments() {
        ArrayList<String> appointments = new ArrayList<>();
        for(Appointment appointment : appointmentRepository.findAll()){
            appointments.add(appointment.toString());
        }
        return appointments;

    }

    /**
     * Deletes an appointment with the given ID.
     *
     * @param id appointment identifier
     * @return {@code true} if appointment existed and was deleted,
     *         {@code false} otherwise
     */
    public Boolean deleteAppointment(int id) {
        if(appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Returns a list of free appointment slots for a given doctor
     * within the next 7 days starting from the provided date.
     * <p>
     * Appointments are divided into 15-minute slots.
     *
     * @param doctorSpecialization specialization of doctor
     * @param date starting date for searching available slots
     * @return list of available doctor appointment slots
     */
    public List<DoctorsAppointmentsDTO> getFreeDoctorAppointments(
            String doctorSpecialization,
            LocalDateTime date
    ) {

        Specialization specialization = Specialization.fromString(doctorSpecialization);

        List<DoctorsAppointmentsDTO> freeAppointments = new ArrayList<>();

        List<Appointment> allAppointments = appointmentRepository.findAll();
        List<Schedule> allSchedules = scheduleRepository.findAll();

        for (Schedule schedule : allSchedules) {

            if (schedule.getDoctor().getSpecialization() != specialization) {
                continue;
            }

            if (schedule.getStartdate().isAfter(date.plusDays(7))) {
                continue;
            }

            LocalDateTime slotStart = schedule.getStartdate();
            LocalDateTime slotEnd = schedule.getEnddate();

            while (slotStart.isBefore(slotEnd)) {

                boolean isFree = true;

                for (Appointment appointment : allAppointments) {
                    if (
                            appointment.getSchedule().getId() == schedule.getId() &&
                                    appointment.getAppointmentStart().isEqual(slotStart)
                    ) {
                        isFree = false;
                        break;
                    }
                }

                if (isFree) {
                    freeAppointments.add(new DoctorsAppointmentsDTO(
                            schedule.getId(),
                            schedule.getDoctor().getFirstName(),
                            schedule.getDoctor().getLastName(),
                            schedule.getDoctor().getSpecialization().toString(),
                            schedule.getRoom().getRoomNumber(),
                            slotStart
                    ));
                }

                slotStart = slotStart.plusMinutes(15);
            }
        }

        return freeAppointments;
    }

}
