package pl.edu.agh.to.przychodnia.Schedule;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Room.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {

    private ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule addSchedule(Doctor doctor, Room room, Date startDate, Date endDate) {
        Schedule schedule = new Schedule(doctor, room, startDate, endDate);
        return scheduleRepository.save(schedule);
    }

    public List<String> getAllSchedules() {
        ArrayList<String> schedules = new ArrayList<>();
        for(Schedule schedule : scheduleRepository.findAll()){
            schedules.add(schedule.toString());
        }
        return schedules;
    }

    public Boolean deleteSchedule(int id) {
        if(scheduleRepository.existsById(id)) {
            scheduleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Schedule findScheduleById(int id) {
        return scheduleRepository.findById(id).orElse(null);
    }


}
