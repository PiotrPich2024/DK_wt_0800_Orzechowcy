package pl.edu.agh.to.przychodnia.Doctor;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;
import pl.edu.agh.to.przychodnia.Schedule.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final ScheduleRepository scheduleRepository;

    public DoctorService(DoctorRepository doctorRepository, ScheduleRepository scheduleRepository) {
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public GetDoctorDTO mapToGetDoctorDTO(Doctor doctor) {
        return new GetDoctorDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization().toString(),
                doctor.getPhone()
        );
    }

    public List<GetDoctorDTO> listDoctors() {
        List<GetDoctorDTO> result = new ArrayList<>();
        for (Doctor doctor : doctorRepository.findAll()) {
            result.add(mapToGetDoctorDTO(doctor));
        }
        return result;
    }

    public Boolean deleteDoctor(int doctorId){
        if(doctorRepository.existsById(doctorId)){
            if(showDoctorSchedules(doctorId).isEmpty()) {
                doctorRepository.deleteById(doctorId);
                return true;
            }
            System.out.println("Lista dyżurów dla tego doktora nie jest pusta.");
            return false;
        }
        System.out.println("Nie znaleziono doktora.");
        return false;
    }

    public Doctor addDoctor(
            CreateDoctorDTO dto
    ){
        Doctor doctor = new Doctor(
             dto.getFirstName(),
             dto.getLastName(),
             Specialization.fromString(dto.getSpecialty()),
             dto.getPesel(),
             dto.getAddress(),
             dto.getPhone()
        );
        return doctorRepository.save(doctor);
    }

    private Doctor createDoctor(
            String firstName,
            String lastName,
            Specialization specialization,
            String pesel,
            String address,
            String phone
    ) {
        Doctor doctor = new Doctor(
                firstName,
                lastName,
                specialization,
                pesel,
                address,
                phone
        );
        return doctorRepository.save(doctor);
    }

    public void initDoctors(){
        createDoctor("Jan", "Kowalski", Specialization.fromString("Kardiolog"), "11111111111", "Adres 1", "123456789");
        createDoctor("Anna", "Nowak", Specialization.fromString("Kardiolog"), "22222222222", "Adres 2", "987654321");
        createDoctor("Marek", "Zieliński", Specialization.fromString("Kardiolog"), "33333333333", "Adres 3", "555444333");

        createDoctor("Piotr", "Lewandowski", Specialization.fromString("Pediatra"), "44444444444", "Adres 4", "666555444");
        createDoctor("Karolina", "Król", Specialization.fromString("Pediatra"), "55555555555", "Adres 5", "123123123");

        createDoctor("Tomasz", "Adamczyk", Specialization.fromString("Neurolog"), "66666666666", "Adres 6", "321321321");

        createDoctor("Monika", "Lis", Specialization.fromString("Dermatolog"), "77777777777", "Adres 7", "111222333");

        System.out.println(">>> Baza została wypełniona danymi lekarzy");
    }

    public void dropDoctors(){
        doctorRepository.deleteAll();
    }


    public Doctor findDoctorById(int Id) {
        Optional<Doctor> temp = doctorRepository.findById(Id);
        return temp.orElse(null);
    }

//    public List<Schedule> findDoctorsScheduleById(int Id) {
//        List<Schedule> schedules = doctorRepository.findById(Id).get().getSchedule();
//        return schedules;
//    }

    public List<String> showDoctorSchedules(int doctorId) {
        ArrayList<String> schedules = new ArrayList<>();
        for(Schedule schedule: scheduleRepository.findAll()){
            if (schedule.getDoctorId() == doctorId){
                schedules.add(schedule.toString());
            }
        }
        return schedules;
    }
}
