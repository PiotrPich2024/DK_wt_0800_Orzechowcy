package pl.edu.agh.to.przychodnia.Schedule;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Doctor.DoctorRepository;
import pl.edu.agh.to.przychodnia.Room.Room;
import pl.edu.agh.to.przychodnia.Room.RoomRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {

    private ScheduleRepository scheduleRepository;
    private DoctorRepository doctorRepository;
    private RoomRepository roomRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, DoctorRepository doctorRepository, RoomRepository roomRepository) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
        this.roomRepository = roomRepository;
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

    public List<String> showAvailableDoctors(String specialization, Date startDate, Date endDate) {
        List<String> doctors = new ArrayList<>();
//        List<String> schedules = getAllSchedules();
        for (Doctor doctor : doctorRepository.findAll()) {
            if (specialization.equals(doctor.getSpecialization())) {
                Boolean flag = true;
                for (Schedule schedule : doctor.getSchedule()) {
                    Date scheduleStartDate = schedule.getStartdate();
                    Date scheduleEndDate = schedule.getEnddate();
                    if (!(scheduleStartDate.compareTo(startDate) <= 0 && startDate.compareTo(scheduleEndDate) < 0 &&  scheduleEndDate.compareTo(endDate) < 0 && endDate.compareTo(scheduleEndDate) <= 0)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    doctors.add(doctor.toString());
                }
            }
        }

        return doctors;
    }

    public List<String> showAvailableRooms(Date  startDate, Date endDate) {
        List<String> rooms = new ArrayList<>();
        for (Room room : roomRepository.findAll()) {
            Boolean flag = true;
            for (Schedule schedule : room.getSchedule()) {
                Date scheduleStartDate = schedule.getStartdate();
                Date scheduleEndDate = schedule.getEnddate();
                if (!(scheduleStartDate.compareTo(startDate) <= 0 && startDate.compareTo(scheduleEndDate) < 0 &&  scheduleEndDate.compareTo(endDate) < 0 && endDate.compareTo(scheduleEndDate) <= 0)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                rooms.add(((Integer)room.getRoomNumber()).toString());
            }
        }

        return rooms;
    }


}
