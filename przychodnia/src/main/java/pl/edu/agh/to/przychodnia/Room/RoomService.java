package pl.edu.agh.to.przychodnia.Room;


import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
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


    // TODO Proszę pamiętać, że nie można usunąć lekarza, ani gabinetu, który aktualnie jest przypisany do gabinetu, lub lekarza. Powinien wyświetlić się odpowiedni komunikat (obsłużyć wyjątek)
    public Boolean deleteRoom(int roomId) {
        if(roomRepository.existsById(roomId)){
            roomRepository.deleteById(roomId);
            return true;
        }
        return false;
    }

    // TODO Dodac dyżury
    public Room findRoomById(int roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }



}
