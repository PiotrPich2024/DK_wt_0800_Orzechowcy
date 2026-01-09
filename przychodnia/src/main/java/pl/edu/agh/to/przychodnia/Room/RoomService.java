package pl.edu.agh.to.przychodnia.Room;


import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Doctor.DoctorRepository;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleRepository;

import java.sql.SQLOutput;
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

    public List<String> getAllRooms() {
        ArrayList<String> rooms = new ArrayList<>();
        for(Room room: roomRepository.findAll()){
            rooms.add(room.toString());
        }
        return rooms;
    }

    public Room addRoom(int roomNumber, String description) {
        Room room = new Room(roomNumber, description);
        return roomRepository.save(room);
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

    public List<String> showRoomSchedules(int roomId) {
        ArrayList<String> schedules = new ArrayList<>();
        for(Schedule schedule: scheduleRepository.findAll()){
            if (schedule.getRoomId() == roomId){
                schedules.add(schedule.toString());
            }
        }
        return schedules;
    }


}
