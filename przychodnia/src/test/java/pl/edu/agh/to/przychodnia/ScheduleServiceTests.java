package pl.edu.agh.to.przychodnia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Doctor.DoctorRepository;
import pl.edu.agh.to.przychodnia.Doctor.Specialization;
import pl.edu.agh.to.przychodnia.Room.Room;
import pl.edu.agh.to.przychodnia.Room.RoomRepository;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleRepository;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleService;

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

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    void addScheduleShouldSaveSchedule() {
        Doctor doctor = mock(Doctor.class);
        Room room = mock(Room.class);
        Date start = new Date();
        Date end = new Date(start.getTime() + 3600000); // +1h

        Schedule schedule = new Schedule(doctor, room, start, end);

        when(scheduleRepository.save(any(Schedule.class)))
                .thenReturn(schedule);

        Schedule result = scheduleService.addSchedule(doctor, room, start, end);

        assertNotNull(result);
        assertEquals(doctor, result.getDoctor());
        assertEquals(room, result.getRoom());

        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    void getAllSchedulesShouldReturnListOfStrings() {
        Schedule s1 = mock(Schedule.class);
        when(s1.toString()).thenReturn("Schedule1");
        Schedule s2 = mock(Schedule.class);
        when(s2.toString()).thenReturn("Schedule2");

        when(scheduleRepository.findAll())
                .thenReturn(List.of(s1, s2));

        List<String> result = scheduleService.getAllSchedules();

        assertEquals(2, result.size());
        assertEquals("Schedule1", result.get(0));
        assertEquals("Schedule2", result.get(1));

        verify(scheduleRepository).findAll();
    }

    @Test
    void deleteScheduleShouldReturnTrueWhenExists() {
        when(scheduleRepository.existsById(1)).thenReturn(true);

        Boolean result = scheduleService.deleteSchedule(1);

        assertTrue(result);
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
        when(doctor.getSpecialization()).thenReturn(Specialization.fromString("Kardiolog"));
        when(doctor.toString()).thenReturn("Dr. Jan");

        Schedule s = mock(Schedule.class);
        Date start = new Date();
        Date end = new Date(start.getTime() + 3600000);

        lenient().when(s.getStartdate()).thenReturn(new Date(start.getTime() - 7200000));
        lenient().when(s.getEnddate()).thenReturn(new Date(end.getTime() + 7200000));

        lenient().when(doctor.getSchedule()).thenReturn(List.of());

        lenient().when(doctorRepository.findAll()).thenReturn(List.of(doctor));

        List<String> result = scheduleService.showAvailableDoctors("Kardiolog", start, end);

        assertEquals(1, result.size());
        assertEquals("Dr. Jan", result.get(0));

        verify(doctorRepository).findAll();
    }

    @Test
    void showAvailableRoomsShouldReturnAvailableRoomNumbers() {
        Room room = mock(Room.class);
        when(room.getRoomNumber()).thenReturn(101);
        when(room.getSchedule()).thenReturn(List.of()); // brak konfliktów
        when(roomRepository.findAll()).thenReturn(List.of(room));

        Date start = new Date();
        Date end = new Date(start.getTime() + 3600000);

        List<String> result = scheduleService.showAvailableRooms(start, end);

        assertEquals(1, result.size());
        assertEquals("101", result.get(0));

        verify(roomRepository).findAll();
    }



}
