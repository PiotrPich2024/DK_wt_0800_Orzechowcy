package pl.edu.agh.to.przychodnia.Patient;

public class CreatePatientDTO {
    private String lastName;
    private String firstName;
    private String pesel;
    private String address;
    private String phone;

    public CreatePatientDTO(String lastName, String firstName, String pesel, String address, String phone) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.pesel = pesel;
        this.address = address;
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getPesel() {
        return pesel;
    }
    public void setPesel(String pesel) {
        this.pesel = pesel;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
