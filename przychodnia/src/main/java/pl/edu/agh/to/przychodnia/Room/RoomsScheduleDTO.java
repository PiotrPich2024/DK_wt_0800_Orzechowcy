package pl.edu.agh.to.przychodnia.Room;

import java.time.LocalDateTime;
import java.util.Date;

public class RoomsScheduleDTO {
    private LocalDateTime  startDate;
    private LocalDateTime  endDate;
    private String doctorsFullName;

    public RoomsScheduleDTO(LocalDateTime  startDate, LocalDateTime  endDate, String doctorsFullName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.doctorsFullName = doctorsFullName;
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
    public String getDoctorsFullName() {
        return doctorsFullName;
    }
    public void setDoctorsFullName(String doctorsFullName) {
        this.doctorsFullName = doctorsFullName;
    }

}
