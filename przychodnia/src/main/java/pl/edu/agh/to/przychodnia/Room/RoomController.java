package pl.edu.agh.to.przychodnia.Room;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller zarządzający operacjami na pokojach.
 * Umożliwia pobieranie, dodawanie, usuwanie pokoi oraz przeglądanie ich grafików.
 */
@RequestMapping(path = "room")
@RestController
public class RoomController {

    private final RoomService roomService;
    /**
     * Konstruktor kontrolera, wstrzykujący serwis zarządzający pokojami.
     *
     * @param roomService serwis odpowiedzialny za logikę biznesową dotyczącą pokoi
     */
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Zwraca listę wszystkich pokoi.
     *
     * @return lista obiektów {@link RoomDTO} reprezentujących pokoje
     */
    @GetMapping
    public List<RoomDTO> getRooms() {
        return roomService.getAllRooms();
    }

    /**
     * Zwraca szczegóły pokoju o podanym ID.
     *
     * @param id identyfikator pokoju
     * @return obiekt {@link RoomDTO} reprezentujący pokój
     */
    @GetMapping("/{id}")
    public RoomDTO getRoom(@PathVariable int id) {
        return roomService.findRoomDTOById(id);
    }

    /**
     * Dodaje nowy pokój na podstawie przesłanych danych.
     *
     * @param dto obiekt {@link RoomDTO} zawierający dane nowego pokoju
     * @return utworzony obiekt {@link RoomDTO}
     */
    @PostMapping(value ="/add")
    public RoomDTO addRoom(@RequestBody RoomDTO dto) {
        return roomService.addRoom(
                dto
        );
    }

    /**
     * Usuwa pokój o podanym ID.
     *
     * @param id identyfikator pokoju do usunięcia
     * @return true jeśli pokój został usunięty, false w przeciwnym wypadku
     */
    @GetMapping(value = "/delete/{id}")
    public Boolean deleteRoom(@PathVariable int id) {
        return roomService.deleteRoom(id);
    }

    /**
     * Zwraca listę grafików pokoju o podanym ID.
     *
     * @param id identyfikator pokoju
     * @return lista obiektów {@link RoomsScheduleDTO} reprezentujących grafiki pokoju
     */
    @GetMapping(value = "/{id}/schedules")
    public List<RoomsScheduleDTO> showRoomSchedules(@PathVariable int id) {
        return roomService.showRoomSchedules(id);
    }


}

