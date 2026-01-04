package pl.edu.agh.to.przychodnia.Schedule;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Room.Room;

import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {

    private ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule addSchedule(Doctor doctor, Room room, Date startDate, Date endDate) {
        return scheduleRepository.save(new Schedule(doctor, room, startDate, endDate));
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }


}
