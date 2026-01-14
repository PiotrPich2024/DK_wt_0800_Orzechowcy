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

    private GetPatientDTO mapToDTO(Patient patient) {
        return new GetPatientDTO(
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPhone()
        );
    }

    public List<GetPatientDTO> listPatients(){
        ArrayList<GetPatientDTO> patients = new ArrayList<>();
        for(Patient patient : patientRepository.findAll()){
            patients.add(mapToDTO(patient));
        }
        return patients;
    }


    public Boolean deletePatient(int id){
        if(patientRepository.existsById(id)){
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public GetPatientDTO addPatient(
            CreatePatientDTO dto
    ){
        Patient patient = new Patient(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPesel(),
                dto.getAddress(),
                dto.getPhone()
        );
        return mapToDTO(patientRepository.save(patient));
    }

    public void dropPatients(){
        patientRepository.deleteAll();
    }


    public Patient findPatientById(int Id) {
        Optional<Patient> temp = patientRepository.findById(Id);
        return temp.orElse(null);
    }

    public GetPatientDTO findPatientDTOById(int Id) {
        Optional<Patient> temp = patientRepository.findById(Id);
        return mapToDTO(temp.get());
    }


}
