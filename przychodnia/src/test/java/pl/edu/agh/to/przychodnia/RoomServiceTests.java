package pl.edu.agh.to.przychodnia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.przychodnia.Room.Room;
import pl.edu.agh.to.przychodnia.Room.RoomRepository;
import pl.edu.agh.to.przychodnia.Room.RoomService;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTests {
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void getAllRoomsShouldReturnListOfStrings() {

        Room room1 = new Room(101, "Gabinet kardiologiczny");
        Room room2 = new Room(102, "Gabinet pediatryczny");

        when(roomRepository.findAll())
                .thenReturn(List.of(room1, room2));

        List<String> result = roomService.getAllRooms();

        assertEquals(2, result.size());
        assertTrue(result.get(0).contains("101"));
        assertTrue(result.get(1).contains("102"));

        verify(roomRepository).findAll();
    }

    @Test
    void addRoomShouldSaveRoom() {

        Room savedRoom = new Room(201, "Sala zabiegowa");

        when(roomRepository.save(any(Room.class)))
                .thenReturn(savedRoom);

        Room result = roomService.addRoom(201, "Sala zabiegowa");

        assertNotNull(result);
        assertEquals(201, result.getRoomNumber());

        verify(roomRepository)
                .save(any(Room.class));
    }

    @Test
    void deleteRoomShouldReturnTrueWhenNoSchedules() {

        when(roomRepository.existsById(1))
                .thenReturn(true);

        when(scheduleRepository.findAll())
                .thenReturn(List.of());

        Boolean result = roomService.deleteRoom(1);

        assertTrue(result);

        verify(roomRepository).deleteById(1);
    }

    @Test
    void deleteRoomShouldReturnFalseWhenSchedulesExist() {

        Schedule schedule = mock(Schedule.class);
        when(schedule.getRoomId()).thenReturn(2);

        when(roomRepository.existsById(2))
                .thenReturn(true);

        when(scheduleRepository.findAll())
                .thenReturn(List.of(schedule));

        Boolean result = roomService.deleteRoom(2);

        assertFalse(result);

        verify(roomRepository, never())
                .deleteById(anyInt());
    }

    @Test
    void deleteRoomShouldReturnFalseWhenRoomNotExists() {

        when(roomRepository.existsById(5))
                .thenReturn(false);

        Boolean result = roomService.deleteRoom(5);

        assertFalse(result);

        verify(roomRepository, never())
                .deleteById(anyInt());
    }

    @Test
    void findRoomByIdShouldReturnRoom() {

        Room room = new Room(301, "Gabinet neurologiczny");

        when(roomRepository.findById(3))
                .thenReturn(Optional.of(room));

        Room result = roomService.findRoomById(3);

        assertNotNull(result);
        assertTrue(result.toString().contains("301"));

        verify(roomRepository).findById(3);
    }


    @Test
    void showRoomSchedulesShouldFilterSchedules() {

        Schedule s1 = mock(Schedule.class);
        when(s1.getRoomId()).thenReturn(10);
        when(s1.toString()).thenReturn("schedule10");

        Schedule s2 = mock(Schedule.class);
        when(s2.getRoomId()).thenReturn(20);

        when(scheduleRepository.findAll())
                .thenReturn(List.of(s1, s2));

        List<String> result =
                roomService.showRoomSchedules(10);

        assertEquals(1, result.size());
        assertEquals("schedule10", result.get(0));

        verify(scheduleRepository).findAll();
    }



}
