package pl.edu.agh.to.przychodnia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.przychodnia.Appointment.*;

import pl.edu.agh.to.przychodnia.Doctor.Specialization;
import pl.edu.agh.to.przychodnia.Patient.Patient;
import pl.edu.agh.to.przychodnia.Patient.PatientRepository;
import pl.edu.agh.to.przychodnia.Room.Room;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void createAppointmentShouldSaveAppointment() {
        Patient patient = mock(Patient.class);
        Schedule schedule = mock(Schedule.class);
        Doctor doctor = mock(Doctor.class);
        Room room = mock(Room.class);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(scheduleRepository.findById(2)).thenReturn(Optional.of(schedule));
        // when(schedule.getId()).thenReturn(2);
        when(schedule.getDoctor()).thenReturn(doctor);
        when(doctor.getFirstName()).thenReturn("Jan");
        when(doctor.getLastName()).thenReturn("Kowalski");
        when(doctor.getSpecialization()).thenReturn(pl.edu.agh.to.przychodnia.Doctor.Specialization.KARDIOLOG);
        when(schedule.getRoom()).thenReturn(room);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(15);

        Appointment appointment = new Appointment(patient, schedule, start, end);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        CreateAppointmentDTO dto = new CreateAppointmentDTO(1, 2, start, end);
        GetAppointmentDTO result = appointmentService.createAppointment(dto);

        assertNotNull(result);
        assertEquals("Jan", result.getDoctorFirstName());
        assertEquals("Kowalski", result.getDoctorLastName());

        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void createAppointmentShouldThrowIfPatientNotFound() {
        when(patientRepository.findById(1)).thenReturn(Optional.empty());
        CreateAppointmentDTO dto = new CreateAppointmentDTO(1, 2, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15));

        assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(dto));
    }

    @Test
    void deleteAppointmentShouldReturnTrueWhenExists() {
        when(appointmentRepository.existsById(1)).thenReturn(true);

        Boolean result = appointmentService.deleteAppointment(1);

        assertTrue(result);
        verify(appointmentRepository).deleteById(1);
    }

    @Test
    void deleteAppointmentShouldReturnFalseWhenNotExists() {
        when(appointmentRepository.existsById(1)).thenReturn(false);

        Boolean result = appointmentService.deleteAppointment(1);

        assertFalse(result);
        verify(appointmentRepository, never()).deleteById(anyInt());
    }

    @Test
    void getAllAppointmentsShouldReturnList() {
        Appointment a1 = mock(Appointment.class);
        Appointment a2 = mock(Appointment.class);

        when(a1.toString()).thenReturn("Appointment1");
        when(a2.toString()).thenReturn("Appointment2");

        when(appointmentRepository.findAll()).thenReturn(List.of(a1, a2));

        List<String> result = appointmentService.getAllAppointments();

        assertEquals(2, result.size());
        assertEquals("Appointment1", result.get(0));
        assertEquals("Appointment2", result.get(1));

        verify(appointmentRepository).findAll();
    }

    @Test
    void getFreeDoctorAppointmentsShouldReturnSlots() {
        Doctor doctor = mock(Doctor.class);
        Schedule schedule = mock(Schedule.class);
        Room room = mock(Room.class);

        when(doctor.getId()).thenReturn(1);
        when(doctor.getSpecialization()).thenReturn(Specialization.DERMATOLOG);
        when(doctor.getFirstName()).thenReturn("Jan");
        when(doctor.getLastName()).thenReturn("Kowalski");
        when(schedule.getId()).thenReturn(2);
        when(schedule.getDoctor()).thenReturn(doctor);
        // when(schedule.getDoctorId()).thenReturn(1);
        when(schedule.getStartdate()).thenReturn(LocalDateTime.now());
        when(schedule.getEnddate()).thenReturn(LocalDateTime.now().plusHours(1));
        when(schedule.getRoom()).thenReturn(room);
        when(room.getRoomNumber()).thenReturn(101);

        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));
        when(appointmentRepository.findAll()).thenReturn(List.of());

        List<DoctorsAppointmentsDTO> slots =
                appointmentService.getFreeDoctorAppointments(1, LocalDateTime.now().minusHours(1));

        assertFalse(slots.isEmpty());
        assertEquals(2, slots.get(0).getScheduleId());
        assertEquals("Jan", slots.get(0).getDoctorFirstName());
        assertEquals("Kowalski", slots.get(0).getDoctorLastName());
        assertEquals(101, slots.get(0).getRoomNumber());
    }
}
