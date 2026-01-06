package com.project.hms.service;

import com.project.hms.entity.*;
import com.project.hms.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private DoctorRepository doctorRepository;

    private PatientRepository patientRepository;

    private AppointmentsRepository appointmentsRepository;
    private PrescriptionRepository prescriptionRepository;

    private DrugRepository drugRepository;

    public DoctorService(DoctorRepository doctorRepository, PatientRepository patientRepository,
                         AppointmentsRepository appointmentsRepository, PrescriptionRepository prescriptionRepository,
                         DrugRepository drugRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentsRepository = appointmentsRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.drugRepository = drugRepository;
    }

    public List<Doctors> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctors findByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }

    public void savePatient(Patient patient) {

        patientRepository.save(patient);
    }

    public List<Patient> searchPatients(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword);
        } else {
            return List.of();
        }
    }

    public Optional<Patient> findPatientByFirstAndLastName(String firstName, String lastName) {
        return patientRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    public Appointments createAppointment(Long patientId, LocalDateTime dateTime, String status, String notes) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Appointments appointments = new Appointments();
        appointments.setPatient(patient);
        appointments.setAppointmentDate(dateTime);
        appointments.setStatus(status);
        appointments.setNotes(notes);

        return appointmentsRepository.save(appointments);
    }

    public Appointments createAppointment(Long patientId, Long doctorId, LocalDateTime dateTime, String status, String notes) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        Doctors doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        Appointments appointments = new Appointments();
        appointments.setPatient(patient);
        appointments.setDoctor(doctor);
        appointments.setAppointmentDate(dateTime);
        appointments.setNotes(notes);
        appointments.setStatus(status);

        return appointmentsRepository.save(appointments);
    }

    public void createPrescription(Long patientId, Prescription prescription) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        prescription.setPatient(patient);
        prescription.setDate(new Date());

        prescriptionRepository.save(prescription);
    }

    public List<Appointments> getAppointmentsByPatientId(Long patientId) {
        return appointmentsRepository.findByPatientId(patientId);
    }

    public Prescription getPrescriptionById(Long prescriptionId) {
        return prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found"));

    }

    public void updatePrescription(Long prescriptionId, Prescription updatedPrescription) {
        Prescription prescription = getPrescriptionById(prescriptionId);
        prescription.setDosage(updatedPrescription.getDosage());
        prescription.setDrugName(updatedPrescription.getDrugName());
        prescription.setDetails(updatedPrescription.getDetails());
        prescription.setDate(new Date());
        prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

}
