package pl.edu.agh.to.przychodnia.Schedule;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Doctor.DoctorRepository;
import pl.edu.agh.to.przychodnia.Doctor.GetDoctorDTO;
import pl.edu.agh.to.przychodnia.Doctor.Specialization;
import pl.edu.agh.to.przychodnia.Room.Room;
import pl.edu.agh.to.przychodnia.Room.RoomRepository;

import javax.print.Doc;
import java.time.LocalDateTime;
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

    public GetScheduleDTO addSchedule(CreateScheduleDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if(dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        Schedule schedule = new Schedule(
                doctor,
                room,
                dto.getStartDate(),
                dto.getStartDate());
        return mapDTO(scheduleRepository.save(schedule));
    }

    private GetScheduleDTO mapDTO(Schedule schedule) {
        return new GetScheduleDTO(
                schedule.getStartdate(),
                schedule.getEnddate(),
                schedule.getDoctor().getFullName(),
                schedule.getDoctor().getSpecialization().toString(),
                schedule.getRoom().getRoomNumber()
        );
    }

    public List<GetScheduleDTO> getAllSchedules() {
        ArrayList<GetScheduleDTO> schedules = new ArrayList<>();
        for(Schedule schedule : scheduleRepository.findAll()){
            schedules.add(mapDTO(schedule));
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


    public List<GetScheduleDTO> showAvailable(AvailableScheduleDTO dto) {
        LocalDateTime  startDate = dto.getStartDate();
        LocalDateTime  endDate = dto.getEndDate();
        Specialization specialization = Specialization.fromString(dto.getSpecialization());

        List<GetScheduleDTO> schedules = new ArrayList<>();
        List<Doctor> availableDoctors = new ArrayList<>();
        List<Room> availableRooms = new ArrayList<>();
        for (Doctor doctor : doctorRepository.findAll()) {
            if (doctor.getSpecialization() != specialization) {
                continue;
            }
            Boolean flag = true;
            if(doctor.getSchedule() != null && !doctor.getSchedule().isEmpty()) {
                for (Schedule schedule : doctor.getSchedule()) {
                    LocalDateTime  scheduleStartDate = schedule.getStartdate();
                    LocalDateTime  scheduleEndDate = schedule.getEnddate();
                    if (startDate.compareTo(scheduleEndDate) < 0 && endDate.compareTo(scheduleStartDate) > 0) {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                availableDoctors.add(doctor);
            }
        }

        for (Room room : roomRepository.findAll()) {
            Boolean flag = true;
            if(room.getSchedule() != null && !room.getSchedule().isEmpty()) {
                for (Schedule schedule : room.getSchedule()) {
                    LocalDateTime scheduleStartDate = schedule.getStartdate();
                    LocalDateTime  scheduleEndDate = schedule.getEnddate();
                    if (startDate.compareTo(scheduleEndDate) < 0 && endDate.compareTo(scheduleStartDate) > 0) {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                availableRooms.add(room);
            }
        }

        for (Doctor doctor : availableDoctors){
            for (Room room : availableRooms){
                GetScheduleDTO scheduleDTO = new GetScheduleDTO(
                        dto.getStartDate(),
                        dto.getEndDate(),
                        doctor.getFullName(),
                        doctor.getSpecialization().toString(),
                        room.getRoomNumber()
                );
                schedules.add(scheduleDTO);
            }
        }

        return schedules;
    }

}
