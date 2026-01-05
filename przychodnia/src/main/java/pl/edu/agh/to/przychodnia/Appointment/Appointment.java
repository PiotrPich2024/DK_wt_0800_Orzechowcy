package pl.edu.agh.to.przychodnia.Appointment;

import jakarta.persistence.*;
import pl.edu.agh.to.przychodnia.Patient.Patient;
import pl.edu.agh.to.przychodnia.Schedule.Schedule;

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
    private Date appointmentStart;

    @Column
    private Date appointmentEnd;

    public Appointment() {}

    public Appointment(Patient patient, Schedule schedule, Date appointmentStart, Date appointmentEnd) {
        this.patient = patient;
        this.schedule = schedule;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
    }

    public Date getAppointmentEnd() {
        return appointmentEnd;
    }

    public Date getAppointmentStart() {
        return appointmentStart;
    }

    @Override
    public String toString() {
        return "ID wizyty: " + id + ", Dyżur: " + schedule + "," +
                " Początek wizyty: " + appointmentStart + ", Koniec wizyty: " + appointmentEnd;

    }
}
