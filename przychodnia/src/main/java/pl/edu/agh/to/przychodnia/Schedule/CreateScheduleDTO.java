package pl.edu.agh.to.przychodnia.Schedule;

import java.time.LocalDateTime;
import java.util.Date;

public class CreateScheduleDTO {
    private LocalDateTime  startDate;
    private LocalDateTime endDate;
    private int doctorId;
    private int roomId;

    public CreateScheduleDTO(LocalDateTime  startDate, LocalDateTime  endDate, int doctorId, int roomId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.doctorId = doctorId;
        this.roomId = roomId;
    }

    public CreateScheduleDTO() {}

    public LocalDateTime  getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime  startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime  getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime  endDate) {
        this.endDate = endDate;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
