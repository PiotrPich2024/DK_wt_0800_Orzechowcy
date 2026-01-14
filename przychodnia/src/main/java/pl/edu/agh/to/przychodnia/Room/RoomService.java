package pl.edu.agh.to.przychodnia.Room;


import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Serwis zarządzający logiką biznesową dotyczącą gabinetów.
 * Umożliwia tworzenie, pobieranie, usuwanie gabinetów oraz przeglądanie ich grafików.
 */
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * Konstruktor serwisu wstrzykujący potrzebne repozytoria.
     *
     * @param roomRepository     repozytorium gabinetów
     * @param scheduleRepository repozytorium grafików
     */
    public RoomService(RoomRepository roomRepository, ScheduleRepository scheduleRepository) {
        this.roomRepository = roomRepository;
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * Mapuje obiekt {@link Room} na {@link RoomDTO}.
     *
     * @param room obiekt gabinetu
     * @return obiekt DTO reprezentujący gabinet
     */
    public RoomDTO mapToDTO(Room room) {
        return new RoomDTO(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomDescription()
        );
    }

    /**
     * Zwraca listę wszystkich gabinetów w formie DTO.
     *
     * @return lista obiektów {@link RoomDTO}
     */
    public List<RoomDTO> getAllRooms() {
        List<RoomDTO> rooms = new ArrayList<>();
        for (Room room : roomRepository.findAll()) {
            rooms.add(mapToDTO(room));
        }
        return rooms;
    }

    /**
     * Dodaje nowy gabinet na podstawie przesłanych danych.
     *
     * @param dto obiekt {@link RoomDTO} zawierający dane nowego gabinetu
     * @return zapisany obiekt {@link RoomDTO}
     */
    public RoomDTO addRoom(RoomDTO dto) {
        Room room = new Room(dto.getRoomNumber(), dto.getRoomDescription());
        return mapToDTO(roomRepository.save(room));
    }

    /**
     * Usuwa gabinet o podanym ID, jeśli nie ma przypisanych grafików.
     *
     * @param roomId identyfikator gabinetu
     * @return true jeśli gabinet został usunięty, false w przeciwnym wypadku
     */
    public Boolean deleteRoom(int roomId) {
        if(roomRepository.existsById(roomId)){
            if(showRoomSchedules(roomId).isEmpty()) {
                roomRepository.deleteById(roomId);
                return true;
            }
            System.out.println("Lista dyżurów dla tego gabinetu nie jest pusta.");
            return false;
        }
        System.out.println("Nie znaleziono gabinetu.");
        return false;
    }


    /**
     * Zwraca obiekt gabinetu o podanym ID.
     *
     * @param roomId identyfikator gabinetu
     * @return obiekt {@link Room} lub null, jeśli gabinet nie istnieje
     */
    public Room findRoomById(int roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

    /**
     * Zwraca DTO gabinetu o podanym ID.
     *
     * @param roomId identyfikator gabinetu
     * @return obiekt {@link RoomDTO} lub null, jeśli gabinet nie istnieje
     */
    public RoomDTO findRoomDTOById(int roomId) {
        return mapToDTO(findRoomById(roomId));
    }

    /**
     * Zwraca listę grafików przypisanych do gabinetu o podanym ID.
     *
     * @param roomId identyfikator gabinetu
     * @return lista obiektów {@link RoomsScheduleDTO} reprezentujących grafiki gabinetu
     */
    public List<RoomsScheduleDTO> showRoomSchedules(int roomId) {
        List<RoomsScheduleDTO> schedules = new ArrayList<>();
        for(Schedule schedule: scheduleRepository.findAll()){
            if (schedule.getRoomId() == roomId){
                schedules.add(new RoomsScheduleDTO(
                        schedule.getStartdate(),
                        schedule.getEnddate(),
                        schedule.getDoctor().getFullName()
                ));
            }
        }
        return schedules;
    }


}
