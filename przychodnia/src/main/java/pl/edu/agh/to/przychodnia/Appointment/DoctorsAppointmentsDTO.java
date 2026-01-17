package pl.edu.agh.to.przychodnia.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DoctorsAppointmentsDTO {
    private String doctorFirstName;
    private String doctorLastName;
    private String specialty;

    private int roomNumber;

    // Wybraliśmy że wizyty będą po 15 minut
    private LocalDateTime appointmentStartDate;
    private LocalDateTime appointmentEndDate;


    public DoctorsAppointmentsDTO() {}
    public DoctorsAppointmentsDTO(
            String doctorFirstName,
            String doctorLastName,
            String specialty,
            int roomNumber,
            LocalDateTime appointmentDate
    ){
        this.doctorFirstName = doctorFirstName;
        this.doctorLastName = doctorLastName;
        this.specialty = specialty;
        this.roomNumber = roomNumber;
        this.appointmentStartDate = appointmentDate;
        this.appointmentEndDate = appointmentDate.plusMinutes(15);
    }

    public String getDoctorFirstName() {
        return doctorFirstName;
    }
    public void setDoctorFirstName(String doctorFirstName) {
        this.doctorFirstName = doctorFirstName;
    }
    public String getDoctorLastName() {
        return doctorLastName;
    }
    public void setDoctorLastName(String doctorLastName) {
        this.doctorLastName = doctorLastName;
    }
    public String getSpecialty() {
        return specialty;
    }
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    public int getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public LocalDateTime getAppointmentDate() {
        return appointmentStartDate;
    }
    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentStartDate = appointmentDate;
    }
    public LocalDateTime getAppointmentEndDate() {
        return appointmentEndDate;
    }
    public void setAppointmentEndDate(LocalDateTime appointmentEndDate) {
        this.appointmentEndDate = appointmentEndDate;
    }
}

