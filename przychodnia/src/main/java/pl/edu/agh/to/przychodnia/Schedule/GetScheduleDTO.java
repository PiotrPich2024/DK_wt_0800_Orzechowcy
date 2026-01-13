package pl.edu.agh.to.przychodnia.Schedule;

import java.time.LocalDateTime;
import java.util.Date;

public class GetScheduleDTO {
    private LocalDateTime  startDate;
    private LocalDateTime  endDate;
    private String doctorsFullName;
    private String specialization;
    private int roomNumber;

    public GetScheduleDTO(LocalDateTime  startDate, LocalDateTime  endDate, String doctorsFullName,String specialization, int roomNumber) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.doctorsFullName = doctorsFullName;
        this.specialization = specialization;
        this.roomNumber = roomNumber;
    }

    public LocalDateTime  getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime  getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime  endDate) {
        this.endDate = endDate;
    }
    public String getDoctorsFullName() {
        return doctorsFullName;
    }
    public void setDoctorsFullName(String doctorsFullName) {
        this.doctorsFullName = doctorsFullName;
    }

    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
