package pl.edu.agh.to.przychodnia.Patient;

import jakarta.persistence.*;
import pl.edu.agh.to.przychodnia.Appointment.Appointment;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;

import java.util.List;


@Entity
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String pesel;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @OneToMany
    private List<Appointment> appointments;


    public Patient(
            String firstName,
            String lastName,
            String pesel,
            String address,
            String phone
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.address = address;
        this.phone = phone;
    }

    public Patient() {

    }

    @Override
    public String toString() {
        return "Pacjent ID: " + id + ", Imię: " + firstName + "," +
                " Nazwisko: " + lastName + ", Pesel: " + pesel +
                ", Adres: " + address + ", Telefon: " + phone;

    }


    public String getFullName() {
        return firstName + " " + lastName;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getPhone(){
        return phone;
    }
}
