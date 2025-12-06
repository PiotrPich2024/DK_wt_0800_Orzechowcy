package pl.edu.agh.to.przychodnia.Doctor;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("init")
public class DoctorInitializer {

    private final DoctorService doctorService;

    public DoctorInitializer(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostConstruct
    public void init() {

        doctorService.addDoctor("Jan", "Kowalski", "Kardiolog", "11111111111", "Adres 1", "123456789");
        doctorService.addDoctor("Anna", "Nowak", "Kardiolog", "22222222222", "Adres 2", "987654321");
        doctorService.addDoctor("Marek", "Zieliński", "Kardiolog", "33333333333", "Adres 3", "555444333");

        doctorService.addDoctor("Piotr", "Lewandowski", "Pediatra", "44444444444", "Adres 4", "666555444");
        doctorService.addDoctor("Karolina", "Król", "Pediatra", "55555555555", "Adres 5", "123123123");

        doctorService.addDoctor("Tomasz", "Adamczyk", "Neurolog", "66666666666", "Adres 6", "321321321");

        doctorService.addDoctor("Monika", "Lis", "Dermatolog", "77777777777", "Adres 7", "111222333");

        System.out.println(">>> Baza została wypełniona danymi lekarzy");
    }
}
