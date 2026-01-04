package pl.edu.agh.to.przychodnia.Room;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomConfigurator {

    private final RoomRepository roomRepository;
    public RoomConfigurator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

}
