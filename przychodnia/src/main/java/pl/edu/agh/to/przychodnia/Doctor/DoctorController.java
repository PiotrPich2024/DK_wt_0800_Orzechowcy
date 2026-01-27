package pl.edu.agh.to.przychodnia.Doctor;

import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Doctors",
        description = "Operacje związane z lekarzami: pobieranie, dodawanie, usuwanie oraz grafiki"
)
@RequestMapping(path = "doctors")
@RestController
public class DoctorController {

    private DoctorService doctorService;


    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    @Operation(
            summary = "Pobierz listę lekarzy",
            description = "Zwraca listę wszystkich lekarzy w systemie"
    )
    @ApiResponse(responseCode = "200", description = "Lista lekarzy")
    @GetMapping
    public List<GetDoctorDTO> listDoctors(){
        return  doctorService.listDoctors();
    }

    @Operation(
            summary = "Pobierz lekarza po ID",
            description = "Zwraca szczegóły lekarza o podanym identyfikatorze"
    )
    @ApiResponse(responseCode = "200", description = "Dane lekarza")
    @GetMapping(value = "/{id}")
    public GetDoctorDTO getDoctor(
            @Parameter(description = "ID lekarza", example = "1")
            @PathVariable int id
    ){
        return doctorService.findDoctorDTOById(id);

    }

    @Operation(
            summary = "Usuń lekarza",
            description = "Usuwa lekarza o podanym ID z systemu jęsli nie ma przypisanego dyżuru inaczej nie usuwamy doktora i zwracamy false"
    )
    @ApiResponse(responseCode = "200", description = "Status usunięcia lekarza")
    @GetMapping(value = "/delete/{id}")
    public Boolean DeleteDoctor(
            @Parameter(description = "ID lekarza", example = "1")
            @PathVariable int id
    ){
        return doctorService.deleteDoctor(id);
    }

    @Operation(
            summary = "Dodaj nowego lekarza",
            description = "Tworzy nowego lekarza na podstawie przekazanych danych"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Utworzony lekarz",
            content = @Content(schema = @Schema(implementation = Doctor.class))
    )
    @PostMapping(value = "/add")
    public Doctor addDoctor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dane nowego lekarza",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Przykładowy lekarz",
                                    value = """
                                        {
                                        "firstName": "Jan",
                                        "lastName": "Kowalski",
                                        "specialty": "Kardiolog",
                                        "pesel": "0293809238",
                                        "address": "Krakow",
                                        "phone": "890567489",
                                        }
                                        """
                            )
                    )
            )
            @RequestBody CreateDoctorDTO dto
    ) {
        return doctorService.addDoctor(
            dto
        );
    }

    @Operation(
            summary = "Inicjalizacja lekarzy",
            description = "Tworzy przykładowych lekarzy w bazie danych"
    )
    @ApiResponse(responseCode = "200", description = "Zainicjalizowano dane")
    @GetMapping(value = "/init")
    public void initDoctor(){
        doctorService.initDoctors();
    }

    @Operation(
            summary = "Usuń wszystkich lekarzy",
            description = "Czyści bazę danych lekarzy"
    )
    @ApiResponse(responseCode = "200", description = "Baza lekarzy wyczyszczona")
    @GetMapping(value = "/drop")
    public void dropDoctors(){
        doctorService.dropDoctors();
    }

    @Operation(
            summary = "Pobierz grafiki lekarza",
            description = "Zwraca listę grafików/dyżurów lekarza o podanym ID"
    )
    @GetMapping(value = "/{id}/schedules")
    public List<GetDoctorScheduleDTO> schowDoctorSchedules(
            @Parameter(description = "ID lekarza", example = "1")
            @PathVariable int id
    ){
        return doctorService.showDoctorSchedules(id);
    }


}
