package pl.edu.agh.to.przychodnia.Schedule;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.przychodnia.Doctor.DoctorService;
import pl.edu.agh.to.przychodnia.Room.Room;
import pl.edu.agh.to.przychodnia.Room.RoomService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * REST Controller zarządzający operacjami na dyżurach lekarzy i pokoi.
 * Umożliwia pobieranie, dodawanie, usuwanie dyżuru oraz sprawdzanie dostępnych terminów.
 */
@RequestMapping(path = "schedule")
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final RoomService roomService;
    private final DoctorService doctorService;
    /**
     * Konstruktor kontrolera, wstrzykujący serwisy potrzebne do zarządzania dyżurem.
     *
     * @param scheduleService serwis odpowiedzialny za logikę biznesową dotyczącą dyżuru
     * @param roomService     serwis odpowiedzialny za pokoje
     * @param doctorService   serwis odpowiedzialny za lekarzy
     */
    public ScheduleController(ScheduleService scheduleService, RoomService roomService, DoctorService doctorService) {
        this.scheduleService = scheduleService;
        this.roomService = roomService;
        this.doctorService = doctorService;
    }

    /**
     * Zwraca listę wszystkich grafików.
     *
     * @return lista obiektów {@link GetScheduleDTO} reprezentujących grafiki
     */
    @GetMapping()
    public List<GetScheduleDTO> getSchedules() {
        return scheduleService.getAllSchedules();
    }

    /**
     * Dodaje nowy grafik na podstawie przesłanych danych.
     *
     * @param dto obiekt {@link CreateScheduleDTO} zawierający dane nowego grafiku
     * @return utworzony obiekt {@link GetScheduleDTO}
     */
    @PostMapping(value = "/add")
    public GetScheduleDTO addSchedule(@RequestBody CreateScheduleDTO dto) {
        return scheduleService.addSchedule(
                dto
        );
    }

    /**
     * Usuwa grafik o podanym ID.
     *
     * @param id identyfikator grafiku do usunięcia
     * @return true jeśli grafik został usunięty, false w przeciwnym wypadku
     */
    @GetMapping(value = "delete/{id}")
    public Boolean deleteSchedule(@PathVariable int id) {
        return scheduleService.deleteSchedule(id);
    }

    /**
     * Zwraca listę dostępnych terminów dla podanych kryteriów.
     *
     * @param dto obiekt {@link AvailableScheduleDTO} zawierający kryteria wyszukiwania dostępnych terminów
     * @return lista obiektów {@link AvailableSlotDTO} reprezentujących dostępne sloty czasowe
     */
    @PostMapping(value = "/available")
    public List<AvailableSlotDTO> showAvailable(@RequestBody AvailableScheduleDTO dto) {
        return scheduleService.showAvailable(dto);
    }


}
