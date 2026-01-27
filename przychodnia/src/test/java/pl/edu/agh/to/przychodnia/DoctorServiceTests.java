package pl.edu.agh.to.przychodnia;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.edu.agh.to.przychodnia.Doctor.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.przychodnia.Room.Room;
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
    void listDoctorsShouldReturnDoctorDTOs() {

        Doctor doctor1 = new Doctor("Jan", "Kowalski", Specialization.fromString("Kardiolog"),
                "12312312311", "Adres", "111222333");

        Doctor doctor2 = new Doctor("Anna", "Nowak", Specialization.fromString("Neurolog"),
                "45645645644", "Adres2", "222444222");

        when(doctorRepository.findAll())
                .thenReturn(List.of(doctor1, doctor2));

        List<GetDoctorDTO> result = doctorService.listDoctors();

        assertEquals(2, result.size());

        assertEquals("Jan", result.get(0).getFirstName());
        assertEquals("Anna", result.get(1).getFirstName());

        assertEquals("Kowalski", result.get(0).getLastName());
        assertEquals("Nowak", result.get(1).getLastName());

        assertEquals("Kardiolog", result.get(0).getSpecialty());
        assertEquals("Neurolog", result.get(1).getSpecialty());

        assertEquals("111222333", result.get(0).getPhone());
        assertEquals("222444222", result.get(1).getPhone());

        verify(doctorRepository).findAll();
    }

    @Test
    void addDoctorShouldSaveDoctorFromDTO() {

        CreateDoctorDTO dto = new CreateDoctorDTO();
        dto.setFirstName("Jan");
        dto.setLastName("Kowalski");
        dto.setSpecialty("Pediatra");
        dto.setPesel("12312312311");
        dto.setPhone("111222333");
        dto.setAddress("Adres 22");

        when(doctorRepository.save(any(Doctor.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Doctor result = doctorService.addDoctor(dto);

        assertNotNull(result);
        assertEquals("Jan", result.getFirstName());
        assertEquals("Kowalski", result.getLastName());
        assertEquals("Pediatra", result.getSpecialization().toString());
        assertEquals("12312312311", result.getPesel());
        assertEquals("111222333", result.getPhone());
        assertEquals("Adres 22", result.getAddress());

        verify(doctorRepository).save(any(Doctor.class));

    }

    @Test
    void deleteDoctorShouldDeleteWhenNoSchedules() {

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
    void deleteDoctorShouldFailWhenHasSchedules() {

        Schedule schedule = mock(Schedule.class);
        Room room = mock(Room.class);

        when(schedule.getRoom()).thenReturn(room);
        when(room.getRoomNumber()).thenReturn(101);
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
                Specialization.fromString("Dermatolog"), "999", "Adres", "333");

        when(doctorRepository.findById(5))
                .thenReturn(Optional.of(doctor));

        Doctor result = doctorService.findDoctorByID(5);

        assertNotNull(result);
        assertEquals("Monika Lis",
                result.getFullName());
    }

    @Test
    void findDoctorDTOByIdShouldReturnDTO() {
        Doctor doctor = new Doctor("Monika", "Lis",
                Specialization.fromString("Dermatolog"),
                "999", "Adres", "333");

        when(doctorRepository.findById(5))
                .thenReturn(Optional.of(doctor));

        GetDoctorDTO dto = doctorService.findDoctorDTOById(5);

        assertNotNull(dto);
        assertEquals("Monika", dto.getFirstName());
        assertEquals("Lis", dto.getLastName());
    }

    @Test
    void showDoctorSchedulesShouldReturnScheduleDTOs() {
        Schedule schedule = mock(Schedule.class);
        Room room = mock(Room.class);

        when(room.getRoomNumber()).thenReturn(101);
        when(schedule.getDoctorId()).thenReturn(1);
        when(schedule.getRoom()).thenReturn(room);

        when(scheduleRepository.findAll())
                .thenReturn(List.of(schedule));

        List<GetDoctorScheduleDTO> result =
                doctorService.showDoctorSchedules(1);

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getRoomNumber());
    }



}

