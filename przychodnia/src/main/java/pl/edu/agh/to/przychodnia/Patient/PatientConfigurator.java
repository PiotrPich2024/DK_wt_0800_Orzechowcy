package pl.edu.agh.to.przychodnia.Patient;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
public class PatientConfigurator {

    private final PatientRepository patientRepository;

    public PatientConfigurator(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }




}
