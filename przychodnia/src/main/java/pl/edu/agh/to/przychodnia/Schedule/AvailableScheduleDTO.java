package pl.edu.agh.to.przychodnia.Schedule;

import java.time.LocalDateTime;
import java.util.Date;

public class AvailableScheduleDTO {
    private LocalDateTime  startDate;
    private LocalDateTime endDate;
    private String specialization;

    public AvailableScheduleDTO(LocalDateTime  startDate, LocalDateTime  endDate, String specialization) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.specialization = specialization;
    }

    public AvailableScheduleDTO() {}

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

    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
