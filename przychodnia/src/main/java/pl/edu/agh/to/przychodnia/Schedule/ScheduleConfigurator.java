package pl.edu.agh.to.przychodnia.Schedule;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleConfigurator {

    private final ScheduleRepository scheduleRepository;
    public ScheduleConfigurator(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

}
