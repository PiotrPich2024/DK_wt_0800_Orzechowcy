package pl.edu.agh.to.przychodnia.Patient;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RequestMapping(path = "patients")
@RestController
public class PatientController {
    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @GetMapping
    public List<GetPatientDTO> listPatients() {
        return  patientService.listPatients();
    }

    @GetMapping(value = "/{id}")
    public GetPatientDTO getPatient(@PathVariable int id){
        return patientService.findPatientDTOById(id);
    }


    @GetMapping(value = "/delete/{id}")
    public Boolean deletePatient(@PathVariable int id){
        return patientService.deletePatient(id);
    }


    @PostMapping(value = "/add")
    public GetPatientDTO addPatient(@RequestBody CreatePatientDTO dto) {
        return patientService.addPatient(
                dto
        );
    }

    @GetMapping(value = "/drop")
    public void dropPatients(){
        patientService.dropPatients();
    }
}
