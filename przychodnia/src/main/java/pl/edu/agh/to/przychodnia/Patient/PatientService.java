package pl.edu.agh.to.przychodnia.Patient;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Appointment.Appointment;
import pl.edu.agh.to.przychodnia.Appointment.AppointmentRepository;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serwis zarządzający logiką biznesową dotyczącą pacjentów.
 * Umożliwia tworzenie, pobieranie, usuwanie pacjentów oraz konwersję do DTO.
 */
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    /**
     * Konstruktor serwisu wstrzykujący repozytorium pacjentów.
     *
     * @param patientRepository repozytorium pacjentów
     */
    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository
    ) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Mapuje obiekt {@link Patient} na {@link GetPatientDTO}.
     *
     * @param patient obiekt pacjenta
     * @return obiekt DTO reprezentujący pacjenta
     */
    private GetPatientDTO mapToDTO(Patient patient) {
        return new GetPatientDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPhone()
        );
    }

    /**
     * Zwraca listę wszystkich pacjentów w formie DTO.
     *
     * @return lista obiektów {@link GetPatientDTO}
     */
    public List<GetPatientDTO> listPatients(){
        ArrayList<GetPatientDTO> patients = new ArrayList<>();
        for(Patient patient : patientRepository.findAll()){
            patients.add(mapToDTO(patient));
        }
        return patients;
    }

    /**
     * Usuwa pacjenta o podanym ID.
     *
     * @param id identyfikator pacjenta
     * @return true jeśli pacjent został usunięty, false w przeciwnym wypadku
     */
    public Boolean deletePatient(int id){
        if(patientRepository.existsById(id)){
            for (Appointment appointment : appointmentRepository.findAll()){
                if(appointment.getPatient().getId() == id){
                    appointmentRepository.delete(appointment);
                }
            }
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Dodaje nowego pacjenta na podstawie przesłanych danych.
     *
     * @param dto obiekt {@link CreatePatientDTO} zawierający dane nowego pacjenta
     * @return utworzony obiekt {@link GetPatientDTO}
     */
    public GetPatientDTO addPatient(
            CreatePatientDTO dto
    ){
        Patient patient = new Patient(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPesel(),
                dto.getAddress(),
                dto.getPhone()
        );
        return mapToDTO(patientRepository.save(patient));
    }

    /**
     * Usuwa wszystkich pacjentów z bazy danych.
     */
    public void dropPatients(){
        patientRepository.deleteAll();
    }

    /**
     * Zwraca obiekt pacjenta o podanym ID.
     *
     * @param Id identyfikator pacjenta
     * @return obiekt {@link Patient} lub null, jeśli pacjent nie istnieje
     */
    public Patient findPatientById(int Id) {
        Optional<Patient> temp = patientRepository.findById(Id);
        return temp.orElse(null);
    }

    /**
     * Zwraca DTO pacjenta o podanym ID.
     *
     * @param Id identyfikator pacjenta
     * @return obiekt {@link GetPatientDTO} lub null, jeśli pacjent nie istnieje
     */
    public GetPatientDTO findPatientDTOById(int Id) {
        Optional<Patient> temp = patientRepository.findById(Id);
        return mapToDTO(temp.get());
    }


}
