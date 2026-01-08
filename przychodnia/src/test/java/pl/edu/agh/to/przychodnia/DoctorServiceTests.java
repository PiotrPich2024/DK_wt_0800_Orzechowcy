package pl.edu.agh.to.przychodnia;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Doctor.DoctorRepository;
import pl.edu.agh.to.przychodnia.Doctor.DoctorService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTests {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void listDoctorsShouldReturnFormattedStrings() {

        Doctor doctor1 = new Doctor("Jan", "Kowalski", "Kardiolog",
                "123", "Adres", "111");

        Doctor doctor2 = new Doctor("Anna", "Nowak", "Neurolog",
                "456", "Adres2", "222");

        when(doctorRepository.findAll())
                .thenReturn(List.of(doctor1, doctor2));

        List<String> result = doctorService.listDoctors();

        assertEquals(2, result.size());
        assertTrue(result.get(0).contains("Jan"));
        assertTrue(result.get(1).contains("Anna"));

        verify(doctorRepository).findAll();
    }

    @Test
    void addDoctorShouldSaveAndReturnDoctor() {

        Doctor savedDoctor = new Doctor("Jan", "Kowalski",
                "Pediatra", "111", "Adres", "123");

        when(doctorRepository.save(any(Doctor.class)))
                .thenReturn(savedDoctor);

        Doctor result = doctorService.addDoctor(
                "Jan", "Kowalski", "Pediatra",
                "111", "Adres", "123"
        );

        assertNotNull(result);
        assertEquals("Jan Kowalski", result.getFullName());

        verify(doctorRepository)
                .save(any(Doctor.class));
    }

    @Test
    void deleteDoctorShouldReturnTrueWhenNoSchedules() {

        when(doctorRepository.existsById(1))
                .thenReturn(true);

        when(scheduleRepository.findAll())
                .thenReturn(List.of());

        Boolean result = doctorService.deleteDoctor(1);

        assertTrue(result);

        verify(doctorRepository)
                .deleteById(1);
    }

    @Test
    void deleteDoctorShouldReturnFalseWhenHasSchedules() {

        Schedule schedule = mock(Schedule.class);
        when(schedule.getDoctorId()).thenReturn(2);

        when(doctorRepository.existsById(2))
                .thenReturn(true);

        when(scheduleRepository.findAll())
                .thenReturn(List.of(schedule));

        Boolean result = doctorService.deleteDoctor(2);

        assertFalse(result);

        verify(doctorRepository, never())
                .deleteById(anyInt());
    }

    @Test
    void deleteDoctorShouldReturnFalseWhenDoctorNotExists() {

        when(doctorRepository.existsById(3))
                .thenReturn(false);

        Boolean result = doctorService.deleteDoctor(3);

        assertFalse(result);

        verify(doctorRepository, never())
                .deleteById(anyInt());
    }

    @Test
    void findDoctorByIdShouldReturnDoctor() {

        Doctor doctor = new Doctor("Monika", "Lis",
                "Dermatolog", "999", "Adres", "333");

        when(doctorRepository.findById(5))
                .thenReturn(Optional.of(doctor));

        Doctor result = doctorService.findDoctorById(5);

        assertNotNull(result);
        assertEquals("Monika Lis",
                result.getFullName());
    }

    @Test
    void showDoctorSchedulesShouldFilterByDoctorId() {

        Schedule s1 = mock(Schedule.class);
        when(s1.getDoctorId()).thenReturn(1);
        when(s1.toString()).thenReturn("schedule1");

        Schedule s2 = mock(Schedule.class);
        when(s2.getDoctorId()).thenReturn(2);

        when(scheduleRepository.findAll())
                .thenReturn(List.of(s1, s2));

        List<String> result =
                doctorService.showDoctorSchedules(1);

        assertEquals(1, result.size());
        assertEquals("schedule1", result.get(0));

        verify(scheduleRepository).findAll();
    }



}
