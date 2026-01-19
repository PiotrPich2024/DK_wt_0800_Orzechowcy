package pl.edu.agh.to.przychodnia.Appointment;

import java.time.LocalDateTime;

public class CreateAppointmentDTO {
    private int patientId;
    private int scheduleId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public CreateAppointmentDTO(int patientId, int scheduleId, LocalDateTime startDate, LocalDateTime endDate) {
        this.patientId = patientId;
        this.scheduleId = scheduleId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public int getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

}
