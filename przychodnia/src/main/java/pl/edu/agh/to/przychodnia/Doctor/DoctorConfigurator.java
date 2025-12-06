package pl.edu.agh.to.przychodnia.Doctor;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
public class DoctorConfigurator {

    private final DoctorRepository doctorRepository;

    public DoctorConfigurator(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }




}
