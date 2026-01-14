package pl.edu.agh.to.przychodnia.Doctor;

import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;
import java.util.Map;

/**
 * REST Controller zarządzający operacjami na lekarzach.
 * Umożliwia pobieranie, dodawanie, usuwanie lekarzy oraz przeglądanie ich dyżurów.
 */
@RequestMapping(path = "doctors")
@RestController
public class DoctorController {

    private DoctorService doctorService;

    /**
     * Konstruktor kontrolera, wstrzykujący serwis zarządzający lekarzami.
     *
     * @param doctorService serwis odpowiedzialny za logikę biznesową dotyczącą lekarzy
     */
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    /**
     * Zwraca listę wszystkich lekarzy.
     *
     * @return lista obiektów {@link GetDoctorDTO} reprezentujących lekarzy
     */
    @GetMapping
    public List<GetDoctorDTO> listDoctors(){
        return  doctorService.listDoctors();
    }

    /**
     * Zwraca szczegóły lekarza o podanym ID.
     *
     * @param id identyfikator lekarza
     * @return obiekt {@link GetDoctorDTO} reprezentujący lekarza
     */
    @GetMapping(value = "/{id}")
    public GetDoctorDTO getDoctor(@PathVariable int id){
        return doctorService.findDoctorDTOById(id);

    }

    /**
     * Usuwa lekarza o podanym ID.
     *
     * @param id identyfikator lekarza do usunięcia
     * @return true jeśli lekarz został usunięty, false w przeciwnym wypadku
     */
    @GetMapping(value = "/delete/{id}")
    public Boolean DeleteDoctor(@PathVariable int id){
        return doctorService.deleteDoctor(id);
    }

    /**
     * Dodaje nowego lekarza na podstawie przesłanych danych.
     *
     * @param dto obiekt {@link CreateDoctorDTO} zawierający dane nowego lekarza
     * @return utworzony obiekt {@link Doctor}
     */
    @PostMapping(value = "/add")
    public Doctor addDoctor(@RequestBody CreateDoctorDTO  dto) {
        return doctorService.addDoctor(
            dto
        );
    }

    /**
     * Inicjalizuje bazę danych przykładowymi lekarzami.
     */
    @GetMapping(value = "/init")
    public void initDoctor(){
        doctorService.initDoctors();
    }

    /**
     * Usuwa wszystkich lekarzy z bazy danych.
     */
    @GetMapping(value = "/drop")
    public void dropDoctors(){
        doctorService.dropDoctors();
    }

    /**
     * Zwraca listę grafików lekarza o podanym ID.
     *
     * @param id identyfikator lekarza
     * @return lista obiektów {@link GetDoctorScheduleDTO} reprezentujących grafiki lekarza
     */
    @GetMapping(value = "/{id}/schedules")
    public List<GetDoctorScheduleDTO> schowDoctorSchedules(@PathVariable int id){
        return doctorService.showDoctorSchedules(id);
    }


}
