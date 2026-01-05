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
    public List<String> listPatients() {
        return  patientService.listPatients();
    }

    @GetMapping(value = "/{id}")
    public String getPatient(@PathVariable int id){
        return patientService.findPatientById(id).toString();

    }


    @GetMapping(value = "/delete/{id}")
    public Boolean DeletePatient(@PathVariable int id){
        return patientService.deletePatient(id);
    }


    @PostMapping(value = "/add")
    public Patient addPatient(@RequestBody Map<String, String> map) {
        return patientService.addPatient(
                map.get("firstName"),
                map.get("lastName"),
                map.get("pesel"),
                map.get("address"),
                map.get("phone")
        );
    }

    @GetMapping(value = "/drop")
    public void dropPatients(){
        patientService.dropPatients();
    }
}
