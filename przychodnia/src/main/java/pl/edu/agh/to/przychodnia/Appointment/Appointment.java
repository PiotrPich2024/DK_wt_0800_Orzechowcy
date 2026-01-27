package pl.edu.agh.to.przychodnia.Appointment;

import jakarta.persistence.*;
import pl.edu.agh.to.przychodnia.Patient.Patient;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Appointment {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Column
    private LocalDateTime appointmentStart;

    @Column
    private LocalDateTime appointmentEnd;

    public Appointment() {}

    public Appointment(Patient patient, Schedule schedule, LocalDateTime appointmentStart, LocalDateTime appointmentEnd) {
        this.patient = patient;
        this.schedule = schedule;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
    }

    public LocalDateTime getAppointmentEnd() {
        return appointmentEnd;
    }

    public LocalDateTime getAppointmentStart() {
        return appointmentStart;
    }

    public Schedule getSchedule() {
        return schedule;
    }
    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    @Override
    public String toString() {
        return "ID wizyty: " + id + ", Dyżur: " + schedule + "," +
                " Początek wizyty: " + appointmentStart + ", Koniec wizyty: " + appointmentEnd  ;

    }
}
