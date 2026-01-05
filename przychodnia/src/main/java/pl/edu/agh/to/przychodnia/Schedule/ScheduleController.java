package pl.edu.agh.to.przychodnia.Schedule;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.przychodnia.Doctor.DoctorService;
import pl.edu.agh.to.przychodnia.Room.Room;
import pl.edu.agh.to.przychodnia.Room.RoomService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping(path = "schedule")
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final RoomService roomService;
    private final DoctorService doctorService;
    public ScheduleController(ScheduleService scheduleService, RoomService roomService, DoctorService doctorService) {
        this.scheduleService = scheduleService;
        this.roomService = roomService;
        this.doctorService = doctorService;
    }

    @GetMapping()
    public List<Schedule> getSchedules() {
        return scheduleService.getAllSchedules();
    }

    @PostMapping
    public Schedule addSchedule(@RequestBody Map<String, String> map) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        Date endDate;

        try {
            startDate = parser.parse(map.get("startDate"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            endDate = parser.parse(map.get("endDate"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }



        return scheduleService.addSchedule(
                doctorService.findDoctorById(Integer.parseInt(map.get("doctorId"))),
                roomService.findRoomById(Integer.parseInt(map.get("roomId"))),
                startDate,
                endDate
        );
    }

    @GetMapping(value = "delete/{id}")
    public Boolean deleteSchedule(@PathVariable int id) {
        return scheduleService.deleteSchedule(id);


    }


}
