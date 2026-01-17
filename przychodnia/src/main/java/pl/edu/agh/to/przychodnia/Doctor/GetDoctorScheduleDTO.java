package pl.edu.agh.to.przychodnia.Doctor;

import java.time.LocalDateTime;
import java.util.Date;

public class GetDoctorScheduleDTO {
    private LocalDateTime  startDate;
    private LocalDateTime endDate;
    private int roomNumber;

    public GetDoctorScheduleDTO(LocalDateTime  startDate, LocalDateTime  endDate, int roomNumber) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomNumber = roomNumber;
    }

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
    public int getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
