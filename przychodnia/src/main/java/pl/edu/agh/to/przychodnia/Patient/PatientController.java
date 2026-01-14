package pl.edu.agh.to.przychodnia.Patient;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

/**
 * REST Controller zarządzający operacjami na pacjentach.
 * Umożliwia pobieranie, dodawanie, usuwanie pacjentów oraz przeglądanie ich danych.
 */
@RequestMapping(path = "patients")
@RestController
public class PatientController {
    private PatientService patientService;

    /**
     * Konstruktor kontrolera, wstrzykujący serwis zarządzający pacjentami.
     *
     * @param patientService serwis odpowiedzialny za logikę biznesową dotyczącą pacjentów
     */
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Zwraca listę wszystkich pacjentów.
     *
     * @return lista obiektów {@link GetPatientDTO} reprezentujących pacjentów
     */
    @GetMapping
    public List<GetPatientDTO> listPatients() {
        return  patientService.listPatients();
    }

    /**
     * Zwraca szczegóły pacjenta o podanym ID.
     *
     * @param id identyfikator pacjenta
     * @return obiekt {@link GetPatientDTO} reprezentujący pacjenta
     */
    @GetMapping(value = "/{id}")
    public GetPatientDTO getPatient(@PathVariable int id){
        return patientService.findPatientDTOById(id);
    }

    /**
     * Usuwa pacjenta o podanym ID.
     *
     * @param id identyfikator pacjenta do usunięcia
     * @return true jeśli pacjent został usunięty, false w przeciwnym wypadku
     */
    @GetMapping(value = "/delete/{id}")
    public Boolean deletePatient(@PathVariable int id){
        return patientService.deletePatient(id);
    }

    /**
     * Dodaje nowego pacjenta na podstawie przesłanych danych.
     *
     * @param dto obiekt {@link CreatePatientDTO} zawierający dane nowego pacjenta
     * @return utworzony obiekt {@link GetPatientDTO}
     */
    @PostMapping(value = "/add")
    public GetPatientDTO addPatient(@RequestBody CreatePatientDTO dto) {
        return patientService.addPatient(
                dto
        );
    }

    /**
     * Usuwa wszystkich pacjentów z bazy danych.
     */
    @GetMapping(value = "/drop")
    public void dropPatients(){
        patientService.dropPatients();
    }
}
