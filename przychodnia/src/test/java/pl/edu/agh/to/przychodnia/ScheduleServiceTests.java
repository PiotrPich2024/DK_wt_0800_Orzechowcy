package pl.edu.agh.to.przychodnia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.przychodnia.Appointment.Appointment;
import pl.edu.agh.to.przychodnia.Appointment.AppointmentRepository;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Doctor.DoctorRepository;
import pl.edu.agh.to.przychodnia.Doctor.Specialization;
import pl.edu.agh.to.przychodnia.Room.Room;
import pl.edu.agh.to.przychodnia.Room.RoomRepository;
import pl.edu.agh.to.przychodnia.Schedule.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTests {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    void addScheduleShouldSaveSchedule() {
        Doctor doctor = mock(Doctor.class);
        Room room = mock(Room.class);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        when(doctor.getId()).thenReturn(77);
        when(doctor.getSpecialization()).thenReturn(Specialization.DERMATOLOG);
        when(doctorRepository.findById(77)).thenReturn(Optional.of(doctor));
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));

        Schedule schedule = new Schedule(doctor, room, start, end);

        when(scheduleRepository.save(any(Schedule.class)))
                .thenReturn(schedule);

        CreateScheduleDTO dto =  new CreateScheduleDTO(start, end, doctor.getId(), room.getId());

        GetScheduleDTO result = scheduleService.addSchedule(dto);

        assertNotNull(result);
        assertEquals(doctor.getFullName(), result.getDoctorsFullName());
        assertEquals(room.getRoomNumber(), result.getRoomNumber());

        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    void getAllSchedulesShouldReturnListOfDTOs() {
        Schedule s1 = mock(Schedule.class);
        Doctor d1 = mock(Doctor.class);
        Room r1 = mock(Room.class);
        when(s1.getDoctor()).thenReturn(d1);
        when(d1.getFullName()).thenReturn("Jan Kowalski");
        when(d1.getSpecialization()).thenReturn(Specialization.PEDIATRA);
        when(s1.getRoom()).thenReturn(r1);
        when(r1.getRoomNumber()).thenReturn(101);
        Schedule s2 = mock(Schedule.class);
        Doctor d2 = mock(Doctor.class);
        Room r2 = mock(Room.class);
        when(s2.getDoctor()).thenReturn(d2);
        when(d2.getFullName()).thenReturn("Andrzej Nowak");
        when(d2.getSpecialization()).thenReturn(Specialization.KARDIOLOG);
        when(s2.getRoom()).thenReturn(r2);
        when(r2.getRoomNumber()).thenReturn(102);

        when(scheduleRepository.findAll())
                .thenReturn(List.of(s1, s2));

        List<GetScheduleDTO> result = scheduleService.getAllSchedules();

        assertEquals(2, result.size());
        assertEquals("Jan Kowalski", result.get(0).getDoctorsFullName());
        assertEquals("Andrzej Nowak", result.get(1).getDoctorsFullName());

        verify(scheduleRepository).findAll();
    }

    @Test
    void deleteScheduleShouldReturnTrueWhenExists() {
        Schedule schedule = mock(Schedule.class);
        when(schedule.getId()).thenReturn(1);

        Appointment appointment = mock(Appointment.class);
        when(appointment.getSchedule()).thenReturn(schedule);
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        when(scheduleRepository.existsById(1)).thenReturn(true);

        Boolean result = scheduleService.deleteSchedule(1);

        assertTrue(result);
        verify(appointmentRepository).delete(appointment);
        verify(scheduleRepository).deleteById(1);
    }

    @Test
    void deleteScheduleShouldReturnFalseWhenNotExists() {
        when(scheduleRepository.existsById(2)).thenReturn(false);

        Boolean result = scheduleService.deleteSchedule(2);

        assertFalse(result);
        verify(scheduleRepository, never()).deleteById(anyInt());
    }

    @Test
    void findScheduleByIdShouldReturnSchedule() {
        Schedule s = mock(Schedule.class);
        when(scheduleRepository.findById(5)).thenReturn(Optional.of(s));

        Schedule result = scheduleService.findScheduleById(5);

        assertNotNull(result);
        verify(scheduleRepository).findById(5);
    }

    @Test
    void showAvailableDoctorsShouldReturnOnlyMatchingSpecializationAndAvailable() {
        Doctor doctor = mock(Doctor.class);
        when(doctor.getId()).thenReturn(1);
        when(doctor.getFullName()).thenReturn("dr Jan");
        when(doctor.getSpecialization()).thenReturn(Specialization.fromString("Kardiolog"));
        when(doctor.getSchedule()).thenReturn(List.of());

        when(doctorRepository.findAll()).thenReturn(List.of(doctor));

        Schedule s = mock(Schedule.class);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        Room room = mock(Room.class);
        when(room.getId()).thenReturn(1);
        when(room.getRoomNumber()).thenReturn(101);
        when(room.getSchedule()).thenReturn(List.of());
        when(roomRepository.findAll()).thenReturn(List.of(room));

        AvailableScheduleDTO dto = new AvailableScheduleDTO(
                start,
                end,
                "Kardiolog"
        );

        List<AvailableSlotDTO> result = scheduleService.showAvailable(dto);

        assertEquals(1, result.size());
        assertEquals("dr Jan", result.get(0).getDoctorName());

        verify(doctorRepository).findAll();

//        lenient().when(s.getStartdate()).thenReturn(new LocalDateTime(start.getTime() - 7200000));
//        lenient().when(s.getEnddate()).thenReturn(new LocalDateTime(end.getTime() + 7200000));
//
//        lenient().when(doctor.getSchedule()).thenReturn(List.of());
//
//        lenient().when(doctorRepository.findAll()).thenReturn(List.of(doctor));
//
//        List<String> result = scheduleService.showAvailableDoctors("Kardiolog", start, end);
//
//        assertEquals(1, result.size());
//        assertEquals("Dr. Jan", result.get(0));
//
//        verify(doctorRepository).findAll();
    }

    @Test
    void showAvailableRoomsShouldReturnAvailableRoomNumbers() {
        Room room = mock(Room.class);
        when(room.getId()).thenReturn(1);
        when(room.getRoomNumber()).thenReturn(101);
        when(room.getSchedule()).thenReturn(List.of());
        when(roomRepository.findAll()).thenReturn(List.of(room));

        Doctor doctor = mock(Doctor.class);
        when(doctor.getId()).thenReturn(1);
        when(doctor.getFullName()).thenReturn("dr Jan");
        when(doctor.getSpecialization()).thenReturn(Specialization.fromString("Kardiolog"));
        when(doctor.getSchedule()).thenReturn(List.of());
        when(doctorRepository.findAll()).thenReturn(List.of(doctor));

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        AvailableScheduleDTO dto = new AvailableScheduleDTO(
                start,
                end,
                "Kardiolog"
        );

        List<AvailableSlotDTO> result = scheduleService.showAvailable(dto);

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getRoomNumber());

        verify(roomRepository).findAll();
        verify(doctorRepository).findAll();
    }



}
