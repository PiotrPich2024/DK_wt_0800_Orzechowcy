package pl.edu.agh.to.przychodnia.Doctor;

import org.springframework.stereotype.Component;

@Component
public class GetDoctorDTO {
    private int id; // chwilowo dla nas aby móc kasować, pokazywać itp poprzez API
    private String firstName;
    private String lastName;
    private String specialty;
    private String phone;

    public GetDoctorDTO(int id, String firstName, String lastName, String specialty, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.phone = phone;
    }

    public GetDoctorDTO() {

    }

    public int getId(){
        return id;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getSpecialty(){
        return specialty;
    }
    public String getPhone(){
        return phone;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setSpecialty(String specialty){
        this.specialty = specialty;
    }
}
