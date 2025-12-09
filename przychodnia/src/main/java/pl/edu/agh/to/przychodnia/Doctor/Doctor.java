package pl.edu.agh.to.przychodnia.Doctor;

import jakarta.persistence.*;


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
        return "Doktor id: " + id + ", Imię: " + firstName + ", Nazwisko: " + lastName
                + ", specjalizacja: " + specialty + ", pesel: " + pesel +
                ", address: " + address + ", phone: " + phone;
    }


}
