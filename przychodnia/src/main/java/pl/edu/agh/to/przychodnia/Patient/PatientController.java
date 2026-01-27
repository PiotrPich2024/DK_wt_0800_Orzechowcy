package pl.edu.agh.to.przychodnia.Patient;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Patients",
        description = "Operacje związane z pacjentami: pobieranie, dodawanie i usuwanie"
)
@RequestMapping(path = "patients")
@RestController
public class PatientController {

    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(
            summary = "Pobierz listę pacjentów",
            description = "Zwraca listę wszystkich pacjentów w systemie"
    )
    @ApiResponse(responseCode = "200", description = "Lista pacjentów")
    @GetMapping
    public List<GetPatientDTO> listPatients() {
        return patientService.listPatients();
    }

    @Operation(
            summary = "Pobierz pacjenta po ID",
            description = "Zwraca szczegóły pacjenta o podanym identyfikatorze"
    )
    @ApiResponse(responseCode = "200", description = "Dane pacjenta")
    @GetMapping(value = "/{id}")
    public GetPatientDTO getPatient(
            @Parameter(description = "ID pacjenta", example = "4")
            @PathVariable int id
    ){
        return patientService.findPatientDTOById(id);
    }

    @Operation(
            summary = "Usuń pacjenta",
            description = "Usuwa pacjenta o podanym ID z systemu"
    )
    @ApiResponse(responseCode = "200", description = "Status usunięcia pacjenta")
    @GetMapping(value = "/delete/{id}")
    public Boolean deletePatient(
            @Parameter(description = "ID pacjenta", example = "6")
            @PathVariable int id
    ){
        return patientService.deletePatient(id);
    }

    @Operation(
            summary = "Dodaj nowego pacjenta",
            description = "Tworzy nowego pacjenta na podstawie przekazanych danych"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Utworzony pacjent",
            content = @Content(schema = @Schema(implementation = GetPatientDTO.class))
    )
    @PostMapping(value = "/add")
    public GetPatientDTO addPatient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dane nowego pacjenta",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Przykładowy pacjent",
                                    value = """
                                        {
                                        "firstName": "Anna",
                                        "lastName": "Nowak",
                                        "pesel": "90010112345",
                                        "address": "Krakow",
                                        "phone": "789567456",
                                        }
                                    """
                            )
                    )
            )
            @RequestBody CreatePatientDTO dto
    ) {
        return patientService.addPatient(dto);
    }

    @Operation(
            summary = "Usuń wszystkich pacjentów",
            description = "Czyści bazę danych pacjentów"
    )
    @ApiResponse(responseCode = "200", description = "Baza pacjentów wyczyszczona")
    @GetMapping(value = "/drop")
    public void dropPatients(){
        patientService.dropPatients();
    }
}
