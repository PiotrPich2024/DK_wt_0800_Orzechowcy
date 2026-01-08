package pl.edu.agh.to.przychodnia;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.przychodnia.Patient.Patient;
import pl.edu.agh.to.przychodnia.Patient.PatientRepository;
import pl.edu.agh.to.przychodnia.Patient.PatientService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTests {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void listPatientsShouldReturnListOfStrings() {
        Patient p1 = mock(Patient.class);
        when(p1.toString()).thenReturn("Jan Kowalski");

        Patient p2 = mock(Patient.class);
        when(p2.toString()).thenReturn("Anna Nowak");

        when(patientRepository.findAll())
                .thenReturn(List.of(p1, p2));

        List<String> result = patientService.listPatients();

        assertEquals(2, result.size());
        assertEquals("Jan Kowalski", result.get(0));
        assertEquals("Anna Nowak", result.get(1));

        verify(patientRepository).findAll();
    }

    @Test
    void addPatientShouldSaveAndReturnPatient() {
        Patient patient = new Patient("Jan", "Kowalski", "12345678901", "Adres", "111222333");

        when(patientRepository.save(any(Patient.class)))
                .thenReturn(patient);

        Patient result = patientService.addPatient(
                "Jan", "Kowalski", "12345678901", "Adres", "111222333"
        );

        assertNotNull(result);
        assertEquals("Jan Kowalski", result.getFullName());

        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void deletePatientShouldReturnTrueWhenExists() {
        when(patientRepository.existsById(1)).thenReturn(true);

        Boolean result = patientService.deletePatient(1);

        assertTrue(result);
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
