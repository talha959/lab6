package com.example.demo.service;

import com.example.demo.entity.Appointment;
import com.example.demo.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + id));
    }

    public Appointment createAppointment(Appointment appointment) {
        validateStatus(appointment.getStatus());
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointmentStatus(Long id, String status) {
        validateStatus(status);
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        if (Appointment.CANCELED.equals(appointment.getStatus())) {
            appointmentRepository.delete(appointment);
        } else {
            throw new IllegalStateException("Only canceled appointments can be deleted");
        }
    }

    private void validateStatus(String status) {
        if (!Appointment.SCHEDULED.equals(status) &&
                !Appointment.COMPLETED.equals(status) &&
                !Appointment.CANCELED.equals(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
