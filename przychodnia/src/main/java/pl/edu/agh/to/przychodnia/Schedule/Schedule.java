package pl.edu.agh.to.przychodnia.Schedule;

import jakarta.persistence.*;
import pl.edu.agh.to.przychodnia.Appointment.Appointment;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;
import pl.edu.agh.to.przychodnia.Room.Room;

import javax.print.Doc;
import java.util.Date;
import java.util.List;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column
    private Date startdate;

    @Column
    private Date enddate;

    @OneToMany
    private List<Appointment> appointments;

    public Schedule(Doctor doctor, Room room, Date startdate, Date enddate) {
        this.doctor = doctor;
        this.room = room;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public Schedule(){

    }

    @Override
    public String toString() {
        return id + " " + doctor.getFullName() + " " + room.getRoomNumber() + " " + startdate.toString() + " " + enddate.toString();
    }

    public int getRoomId() {
        return room.getId();
    }

    public int getDoctorId() {
        return doctor.getId();
    }

    public Date getStartdate() {
        return startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Room getRoom() {
        return room;
    }
}
