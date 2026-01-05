package pl.edu.agh.to.przychodnia.Doctor;

import jakarta.persistence.*;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;

import java.util.List;


@Entity
public class Doctor {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String specialty;

    @Column(nullable = false)
    private String pesel;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @OneToMany
    private List<Schedule> schedule;


    public Doctor(
            String firstName,
            String lastName,
            String specialty,
            String pesel,
            String address,
            String phone
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.pesel = pesel;
        this.address = address;
        this.phone = phone;
    }

    public Doctor() {

    }

    @Override
    public String toString() {
        String s = "Doktor ID: " + id + ", Imię: " + firstName + ", Nazwisko: " + lastName
                + ", Specjalizacja: " + specialty + ", Pesel: " + pesel +
                ", Adres: " + address + ", Telefon: " + phone;

        s += ", Dyżury: ";
        for (Schedule schedule : schedule) {
            s += schedule + ", ";
        }
        String result = s.substring(0, s.length() - 2);

        return result;
    }

    public List<Schedule> getSchedule(){
        return schedule;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
