package pl.edu.agh.to.przychodnia.Appointment;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentConfigurator {
    private AppointmentRepository appointmentRepository;
    public AppointmentConfigurator(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
}
