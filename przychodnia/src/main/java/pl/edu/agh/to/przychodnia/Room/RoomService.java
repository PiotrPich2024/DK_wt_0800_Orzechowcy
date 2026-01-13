package pl.edu.agh.to.przychodnia.Room;


import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;
    public RoomService(RoomRepository roomRepository, ScheduleRepository scheduleRepository) {
        this.roomRepository = roomRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public RoomDTO mapToDTO(Room room) {
        return new RoomDTO(
                room.getRoomNumber(),
                room.getRoomDescription()
        );
    }

    public List<RoomDTO> getAllRooms() {
        List<RoomDTO> rooms = new ArrayList<>();
        for (Room room : roomRepository.findAll()) {
            rooms.add(mapToDTO(room));
        }
        return rooms;
    }

    public RoomDTO addRoom(RoomDTO dto) {
        Room room = new Room(dto.getRoomNumber(), dto.getRoomDescription());
        return mapToDTO(roomRepository.save(room));
    }


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


    public Room findRoomById(int roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

    public RoomDTO findRoomDTOById(int roomId) {
        return mapToDTO(findRoomById(roomId));
    }

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
