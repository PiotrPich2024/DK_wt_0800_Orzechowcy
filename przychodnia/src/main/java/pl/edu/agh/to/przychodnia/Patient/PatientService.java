package pl.edu.agh.to.przychodnia.Patient;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<String> listPatients(){
        ArrayList<String> patients = new ArrayList<>();
        for(Patient patient : patientRepository.findAll()){
            patients.add(patient.toString());
        }
        return patients;
    }

    // TODO Proszę pamiętać, że nie można usunąć lekarza, ani gabinetu, który aktualnie jest przypisany do gabinetu, lub lekarza. Powinien wyświetlić się odpowiedni komunikat (obsłużyć wyjątek)
    public Boolean deletePatient(int id){
        if(patientRepository.existsById(id)){
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Patient addPatient(
            String firstName,
            String lastName,
            String pesel,
            String address,
            String phone
    ){
        Patient patient = new Patient(
                firstName,
                lastName,
                pesel,
                address,
                phone
        );
        return patientRepository.save(patient);
    }

    public void dropPatients(){
        patientRepository.deleteAll();
    }


    public Patient findPatientById(int Id) {
        Optional<Patient> temp = patientRepository.findById(Id);
        return temp.orElse(null);
    }


}
