package com.project.hms.service;

import com.project.hms.entity.Appointments;
import com.project.hms.entity.Doctors;
import com.project.hms.entity.Patient;
import com.project.hms.entity.Reception;
import com.project.hms.repository.AppointmentsRepository;
import com.project.hms.repository.DoctorRepository;
import com.project.hms.repository.PatientRepository;
import com.project.hms.repository.ReceptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReceptionService {

    private ReceptionRepository receptionRepository;
    private final PatientRepository patientRepository;
    private final AppointmentsRepository appointmentRepository;
    private final DoctorRepository doctorRepository;


    private static final int DEFAULT_APPOINTMENT_MINUTES = 30;

    public ReceptionService(ReceptionRepository receptionRepository, PatientRepository patientRepository,
                            AppointmentsRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.receptionRepository = receptionRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    public Reception findByUsername(String username) {

        return receptionRepository.findByUsername(username);
    }

    public Patient registerPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElse(null);
    }

    public Patient updatePatient(Long patientId, Patient updated) {
        Patient existing = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setGender(updated.getGender());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setEmail(updated.getEmail());
        return patientRepository.save(existing);
    }

    public List<Appointments> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
/*
    public List<Appointments> getAppointmentsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByAppointmentDateTimeBetween(startOfDay, endOfDay);
    }

    public List<Doctors> checkAvailableDoctors(LocalDateTime desiredStart, int durationMinutes, String specialty) {
        if (durationMinutes <= 0) {
            durationMinutes = DEFAULT_APPOINTMENT_MINUTES;
        }
        LocalDateTime desiredEnd = desiredStart.plusMinutes(durationMinutes);

        List<Doctors> doctors;
        if (specialty == null || specialty.trim().isEmpty()) {
            doctors = doctorRepository.findAll();
        } else {
            // If you have a repository method findBySpecialty, it will be more efficient.
            // Fallback to findAll and filter if not present.
            try {
                doctors = doctorRepository.findBySpecialty(specialty);
            } catch (Exception ex) {
                doctors = doctorRepository.findAll();
            }
        }

        List<Doctors> available = new ArrayList<>();
        for (Doctors doc : doctors) {
            if (isDoctorAvailable(doc.getDoctorId(), desiredStart, durationMinutes)) {
                available.add(doc);
            }
        }
        return available;
    }*/

/*
    public void notifyDoctorAppointments(Long doctorId, LocalDate date) {
        Doctors doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Appointments> appointments = appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetweenOrderByAppointmentDateTimeAsc(
                doctorId, startOfDay, endOfDay);

        StringBuilder sb = new StringBuilder();
        sb.append("Appointments for ").append(date.toString()).append(":\n");
        if (appointments.isEmpty()) {
            sb.append("No appointments scheduled.\n");
        } else {
            for (Appointments a : appointments) {
                sb.append("- ").append(a.getAppointmentDate()).append(" : ")
                        .append(a.getPatient().getFirstName()).append(" ").append(a.getPatient().getLastName())
                        .append(" (").append(a.getStatus() == null ? "no reason" : a.getStatus()).append(")\n");
            }
        }

        notificationService.notifyDoctor(doctor, sb.toString());
    }

    public boolean isDoctorAvailable(Long doctorId, LocalDateTime desiredStart, int durationMinutes) {
        LocalDateTime desiredEnd = desiredStart.plusMinutes(durationMinutes);
        // look for any appointment that starts in the requested interval
        List<Appointments> conflicts = appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctorId, desiredStart, desiredEnd);
        return conflicts.isEmpty();
    }*/

    public List<Doctors> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Patient> searchPatients(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword);
        } else {
            return List.of();
        }
    }

    public List<Appointments> getTodaysAppointments() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return appointmentRepository.findAppointmentsForToday(startOfDay, endOfDay);
    }

    public void cancelAppointment(Long appointmentId) {
        Appointments appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        appointmentRepository.delete(appt);

        try {
            Doctors doctor = appt.getDoctor();
            String message = String.format("Appointment canceled: patient %s %s at %s",
                    appt.getPatient().getFirstName(), appt.getPatient().getLastName(), appt.getAppointmentDate().toString());
        } catch (Exception ignored) { }
    }

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public void deletePatients(Long id) {
        patientRepository.deleteById(id);
    }

    public Appointments createAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDate, String status, String notes) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        Doctors doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        Appointments appointments = new Appointments();
        appointments.setPatient(patient);
        appointments.setDoctor(doctor);
        appointments.setAppointmentDate(appointmentDate);
        appointments.setNotes(notes);
        appointments.setStatus(status);

        return appointmentRepository.save(appointments);
    }
}
