package pl.edu.agh.to.przychodnia.Appointment;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.przychodnia.Doctor.Doctor;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {
    private AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<String> getAllAppointments() {
        ArrayList<String> appointments = new ArrayList<>();
        for(Appointment appointment : appointmentRepository.findAll()){
            appointments.add(appointment.toString());
        }
        return appointments;

    }

    public Boolean deleteAppointment(int id) {
        if(appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
