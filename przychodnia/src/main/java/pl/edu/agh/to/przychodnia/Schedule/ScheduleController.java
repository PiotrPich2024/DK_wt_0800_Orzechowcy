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
    public List<GetScheduleDTO> getSchedules() {
        return scheduleService.getAllSchedules();
    }

    @PostMapping(value = "/add")
    public GetScheduleDTO addSchedule(@RequestBody CreateScheduleDTO dto) {
        return scheduleService.addSchedule(
                dto
        );
    }

    @GetMapping(value = "delete/{id}")
    public Boolean deleteSchedule(@PathVariable int id) {
        return scheduleService.deleteSchedule(id);
    }

    @PostMapping(value = "/available")
    public List<AvailableSlotDTO> showAvailable(@RequestBody AvailableScheduleDTO dto) {
        return scheduleService.showAvailable(dto);
    }


}
