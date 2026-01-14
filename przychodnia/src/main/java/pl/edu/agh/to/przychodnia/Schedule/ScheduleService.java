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

/**
 * Serwis zarządzający logiką biznesową dotyczącą grafików lekarzy i gabinetów.
 * Umożliwia tworzenie, pobieranie, usuwanie grafików oraz sprawdzanie dostępnych terminów.
 */
@Service
public class ScheduleService {

    private ScheduleRepository scheduleRepository;
    private DoctorRepository doctorRepository;
    private RoomRepository roomRepository;

    /**
     * Konstruktor serwisu wstrzykujący potrzebne repozytoria.
     *
     * @param scheduleRepository repozytorium grafików
     * @param doctorRepository   repozytorium lekarzy
     * @param roomRepository     repozytorium gabinetów
     */
    public ScheduleService(ScheduleRepository scheduleRepository, DoctorRepository doctorRepository, RoomRepository roomRepository) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Dodaje nowy grafik na podstawie przesłanych danych.
     *
     * @param dto obiekt {@link CreateScheduleDTO} zawierający dane nowego grafiku
     * @return utworzony obiekt {@link GetScheduleDTO}
     * @throws RuntimeException jeśli lekarz lub gabinet nie istnieje lub data startowa jest po dacie końcowej
     */
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

    /**
     * Mapuje obiekt {@link Schedule} na {@link GetScheduleDTO}.
     *
     * @param schedule obiekt grafiku
     * @return obiekt DTO reprezentujący grafik
     */
    private GetScheduleDTO mapDTO(Schedule schedule) {
        return new GetScheduleDTO(
                schedule.getId(),
                schedule.getStartdate(),
                schedule.getEnddate(),
                schedule.getDoctor().getFullName(),
                schedule.getDoctor().getSpecialization().toString(),
                schedule.getRoom().getRoomNumber()
        );
    }

    /**
     * Zwraca listę wszystkich grafików w formie DTO.
     *
     * @return lista obiektów {@link GetScheduleDTO}
     */
    public List<GetScheduleDTO> getAllSchedules() {
        ArrayList<GetScheduleDTO> schedules = new ArrayList<>();
        for(Schedule schedule : scheduleRepository.findAll()){
            schedules.add(mapDTO(schedule));
        }
        return schedules;
    }

    /**
     * Usuwa grafik o podanym ID.
     *
     * @param id identyfikator grafiku
     * @return true jeśli grafik został usunięty, false w przeciwnym wypadku
     */
    public Boolean deleteSchedule(int id) {
        if(scheduleRepository.existsById(id)) {
            scheduleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Zwraca obiekt grafiku o podanym ID.
     *
     * @param id identyfikator grafiku
     * @return obiekt {@link Schedule} lub null, jeśli grafik nie istnieje
     */
    public Schedule findScheduleById(int id) {
        return scheduleRepository.findById(id).orElse(null);
    }


    /**
     * Zwraca listę dostępnych slotów dla lekarzy i gabinetów w określonym przedziale czasowym i o określonej specjalizacji.
     *
     * @param dto obiekt {@link AvailableScheduleDTO} zawierający kryteria wyszukiwania
     * @return lista obiektów {@link AvailableSlotDTO} reprezentujących dostępne sloty czasowe
     */
    public List<AvailableSlotDTO > showAvailable(AvailableScheduleDTO dto) {
        LocalDateTime  startDate = dto.getStartDate();
        LocalDateTime  endDate = dto.getEndDate();
        Specialization specialization = Specialization.fromString(dto.getSpecialization());

        List<AvailableSlotDTO> schedules = new ArrayList<>();
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
                AvailableSlotDTO scheduleDTO = new AvailableSlotDTO (
                        doctor.getId(),
                        doctor.getFullName(),
                        doctor.getSpecialization().toString(),
                        room.getId(),
                        room.getRoomNumber(),
                        dto.getStartDate(),
                        dto.getEndDate()
                );
                schedules.add(scheduleDTO);
            }
        }

        return schedules;
    }

}
