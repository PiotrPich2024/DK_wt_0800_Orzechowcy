package pl.edu.agh.to.przychodnia.Doctor;

import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;

@RequestMapping(path = "doctors")
@RestController
public class DoctorController {

    private DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    @GetMapping
    public List<String> listDoctors(){
        return  doctorService.listDoctors();
    }

    @GetMapping(value = "/{id}")
    public String getDoctor(@PathVariable int id){
        return doctorService.listDoctors().get(id).toString();
    }


    @GetMapping(value = "/delete/{id}")
    public Boolean DeleteDoctor(@PathVariable int id){
        return doctorService.deleteDoctor(id);
    }


    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Doctor addDoctor(
            @RequestBody String firstName,
            @RequestBody String lastName,
            @RequestBody String specialty,
            @RequestBody String pesel,
            @RequestBody String address,
            @RequestBody String phone
    ){
        return doctorService.addDoctor(firstName, lastName, specialty, pesel, address, phone);
    }

    @GetMapping(value = "/init")
    public void initDoctor(){
        doctorService.initDoctors();
    }

    @GetMapping(value = "/drop")
    public void dropDoctors(){
        doctorService.dropDoctors();
    }
}
