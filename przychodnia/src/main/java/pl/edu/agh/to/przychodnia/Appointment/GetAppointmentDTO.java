package pl.edu.agh.to.przychodnia.Appointment;

import java.time.LocalDateTime;

public class GetAppointmentDTO {
    private int id;
    private String patientFirstName;
    private String patientLastName;

    private String doctorFirstName;
    private String doctorLastName;
    private String specialty;

    private int roomNumber;

    private LocalDateTime appointmentStartDate;
    private LocalDateTime appointmentEndDate;

    public GetAppointmentDTO() {}
    public GetAppointmentDTO(
            int id,
            String patientFirstName,
            String patientLastName,
            String doctorFirstName,
            String doctorLastName,
            String specialty,
            int roomNumber,
            LocalDateTime appointmentStartDate,
            LocalDateTime appointmentEndDate
    ){
        this.id = id;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.doctorFirstName = doctorFirstName;
        this.doctorLastName = doctorLastName;
        this.specialty = specialty;
        this.roomNumber = roomNumber;
        this.appointmentStartDate = appointmentStartDate;
        this.appointmentEndDate = appointmentEndDate;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPatientFirstName() {
        return patientFirstName;
    }
    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }
    public String getPatientLastName() {
        return patientLastName;
    }
    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
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
    public LocalDateTime getAppointmentStartDate() {
        return appointmentStartDate;
    }
    public void setAppointmentStartDate(LocalDateTime appointmentStartDate) {
        this.appointmentStartDate = appointmentStartDate;
    }
    public LocalDateTime getAppointmentEndDate() {
        return appointmentEndDate;
    }
    public void setAppointmentEndDate(LocalDateTime appointmentEndDate) {
        this.appointmentEndDate = appointmentEndDate;
    }

}
