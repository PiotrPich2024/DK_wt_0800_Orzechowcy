package pl.edu.agh.to.przychodnia.Doctor;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<String> listDoctors(){
        ArrayList<String> doctors = new ArrayList<>();
        for(Doctor doctor : doctorRepository.findAll()){
            doctors.add(doctor.toString());
        }
        return doctors;
    }

    // TODO Proszę pamiętać, że nie można usunąć lekarza, ani gabinetu, który aktualnie jest przypisany do gabinetu, lub lekarza. Powinien wyświetlić się odpowiedni komunikat (obsłużyć wyjątek)
    public Boolean deleteDoctor(int id){
        if(doctorRepository.existsById(id)){
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Doctor addDoctor(
            String firstName,
            String lastName,
            String specialty,
            String pesel,
            String address,
            String phone
    ){
        Doctor doctor = new Doctor(
             firstName,
             lastName,
             specialty,
             pesel,
             address,
             phone
        );
        return doctorRepository.save(doctor);
    }

    public void initDoctors(){
        addDoctor("Jan", "Kowalski", "Kardiolog", "11111111111", "Adres 1", "123456789");
        addDoctor("Anna", "Nowak", "Kardiolog", "22222222222", "Adres 2", "987654321");
        addDoctor("Marek", "Zieliński", "Kardiolog", "33333333333", "Adres 3", "555444333");

        addDoctor("Piotr", "Lewandowski", "Pediatra", "44444444444", "Adres 4", "666555444");
        addDoctor("Karolina", "Król", "Pediatra", "55555555555", "Adres 5", "123123123");

        addDoctor("Tomasz", "Adamczyk", "Neurolog", "66666666666", "Adres 6", "321321321");

        addDoctor("Monika", "Lis", "Dermatolog", "77777777777", "Adres 7", "111222333");

        System.out.println(">>> Baza została wypełniona danymi lekarzy");
    }

    public void dropDoctors(){
        doctorRepository.deleteAll();
    }


    public Doctor findDoctorById(int Id) {
        Optional<Doctor> temp = doctorRepository.findById(Id);
        return temp.orElse(null);
    }

    public List<Schedule> findDoctorsScheduleById(int Id) {
        List<Schedule> schedules = doctorRepository.findById(Id).get().getSchedule();
        return schedules;
    }
}
