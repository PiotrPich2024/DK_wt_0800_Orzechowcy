package pl.edu.agh.to.przychodnia.Doctor;

public class CreateDoctorDTO {
    private String firstName;
    private String lastName;
    private String specialty;
    private String pesel;
    private String address;
    private String phone;

    public CreateDoctorDTO() {}

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getSpecialty(){
        return specialty;
    }
    public String getPesel(){
        return pesel;
    }
    public String getAddress(){
        return address;
    }
    public String getPhone(){
        return phone;
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
    public void setPesel(String pesel){
        this.pesel = pesel;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
}
