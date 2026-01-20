package pl.edu.agh.to.przychodnia;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.przychodnia.Appointment.Appointment;
import pl.edu.agh.to.przychodnia.Appointment.AppointmentRepository;
import pl.edu.agh.to.przychodnia.Patient.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTests {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void listPatientsShouldReturnPatientDTOs() {
        Patient p1 = new Patient("Jan", "Kowalski", "123", "Adres", "111");
        Patient p2 = new Patient("Anna", "Nowak", "456", "Adres2", "222");

        when(patientRepository.findAll()).thenReturn(List.of(p1, p2));

        List<GetPatientDTO> result = patientService.listPatients();

        assertEquals(2, result.size());
        assertEquals("Jan", result.get(0).getFirstName());
        assertEquals("Anna", result.get(1).getFirstName());

        verify(patientRepository).findAll();
    }

    @Test
    void addPatientShouldSaveAndReturnDTO() {
        CreatePatientDTO dto = new CreatePatientDTO(
                 "Kowalski", "Jan", "123",
                "Adres", "111"
        );

        when(patientRepository.save(any(Patient.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        GetPatientDTO result = patientService.addPatient(dto);

        assertNotNull(result);
        assertEquals("Jan", result.getFirstName());
        assertEquals("Kowalski", result.getLastName());

        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void deletePatientShouldDeleteAppointmentsAndPatient() {
        // Patient patient = new Patient("Jan", "Kowalski", "123", "Adres", "111");
        Patient patient = mock(Patient.class);
        Appointment appointment = mock(Appointment.class);

        when(patient.getId()).thenReturn(1);
        when(appointment.getPatient()).thenReturn(patient);

        when(patientRepository.existsById(1)).thenReturn(true);
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        Boolean result = patientService.deletePatient(1);

        assertTrue(result);
        verify(appointmentRepository).delete(appointment);
        verify(patientRepository).deleteById(1);
    }

    @Test
    void deletePatientShouldReturnFalseWhenNotExists() {
        when(patientRepository.existsById(2)).thenReturn(false);

        Boolean result = patientService.deletePatient(2);

        assertFalse(result);
        verify(patientRepository, never()).deleteById(anyInt());
    }

    @Test
    void findPatientByIdShouldReturnPatient() {
        Patient patient = new Patient("Anna", "Nowak", "98765432109", "Adres", "123123123");

        when(patientRepository.findById(5)).thenReturn(Optional.of(patient));

        Patient result = patientService.findPatientById(5);

        assertNotNull(result);
        assertEquals("Anna Nowak", result.getFullName());


        verify(patientRepository).findById(5);
    }

    @Test
    void dropPatientsShouldCallDeleteAll() {
        patientService.dropPatients();

        verify(patientRepository).deleteAll();
    }


}
