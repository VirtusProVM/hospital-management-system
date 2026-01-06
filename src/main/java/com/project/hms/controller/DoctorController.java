package com.project.firestorm.controller;

import com.project.firestorm.entity.Appointments;
import com.project.firestorm.entity.Patient;
import com.project.firestorm.entity.Prescription;
import com.project.firestorm.service.DoctorService;
import com.project.firestorm.service.MedicalHistoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private DoctorService doctorService;

    private MedicalHistoryService medicalHistoryService;

    public DoctorController(DoctorService doctorService, MedicalHistoryService medicalHistoryService) {
        this.doctorService = doctorService;
        this.medicalHistoryService = medicalHistoryService;
    }

    @GetMapping("/")
    public String doctorHomePage(Model model) {
        return "doctor";
    }

    @GetMapping("/patients/new")
    public String showCreatePatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "doctor-create-patient";
    }

    @PostMapping("/patients/create")
    public String createPatient(@Valid @ModelAttribute("patient") Patient patient,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "doctor-create-patient";
        }
        doctorService.savePatient(patient);
        redirectAttributes.addFlashAttribute("successMessage", "Patient created successfully!");
        return "redirect:/doctor/";
    }

    @GetMapping("/patients/search")
    public String searchPatients(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
        List<Patient> patients = doctorService.searchPatients(keyword);
        model.addAttribute("patients", patients);
        model.addAttribute("keyword", keyword);
        return "doctor-patient-search";
    }

    @GetMapping("/patients/{firstName}/{lastName}")
    public String getPatientDetailsByName(@PathVariable String firstName,
                                          @PathVariable String lastName,
                                          Model model) throws IOException {
        Optional<Patient> patientOpt = doctorService.findPatientByFirstAndLastName(firstName, lastName);
        String existingHistory = medicalHistoryService.readMedicalHistory(patientOpt.get().getPatientId());
        List<Prescription> prescriptions = doctorService.getPrescriptions();
        if (patientOpt.isPresent()) {
            model.addAttribute("patient", patientOpt.get());
            model.addAttribute("medicalHistory", existingHistory);
            model.addAttribute("prescriptions", prescriptions);
            return "doctor-patient-details";
        } else {
            model.addAttribute("errorMessage", "Patient not found.");
            return "redirect:/doctor/patients/search";
        }
    }

    @GetMapping("/patients/{patientId}/appointments/new")
    public String showCreateAppointmentForm(@PathVariable Long patientId,
                                            Model model) {
        model.addAttribute("patientId", patientId);
        model.addAttribute("appointment", new Appointments());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "doctor-create-appointment";
    }

    @PostMapping("/patients/{patientId}/appointments")
    public String createAppointment(@PathVariable Long patientId,
                                    @ModelAttribute("appointment") Appointments appointments,
                                    RedirectAttributes redirectAttributes) {
        doctorService.createAppointment(patientId, appointments.getDoctor().getDoctorId(), appointments.getAppointmentDate(), appointments.getStatus(), appointments.getNotes());
        redirectAttributes.addFlashAttribute("successMessage", "Appointment created successfully!");
        return "redirect:/doctor/";
    }

    @GetMapping("/patients/{patientId}/medical-history-file")
    public String showMedicalHistoryForm(@PathVariable Long patientId, Model model) throws IOException {
        String existingHistory = medicalHistoryService.readMedicalHistory(patientId);
        model.addAttribute("patientId", patientId);
        model.addAttribute("medicalHistory", existingHistory);
        return "doctor-create-medical-history-file";
    }

    @PostMapping("/patients/{patientId}/medical-history-file")
    public String saveMedicalHistory(@PathVariable Long patientId,
                                     @RequestParam("medicalHistory") String medicalHistory,
                                     RedirectAttributes redirectAttributes) {
        try {
            medicalHistoryService.saveMedicalHistory(patientId, medicalHistory);
            redirectAttributes.addFlashAttribute("successMessage", "Medical history saved!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving medical history!");
        }
        return "redirect:/doctor/";
    }

    @GetMapping("/patients/{patientId}")
    public String showPatientPage(@PathVariable Long patientId, Model model) throws IOException {
        Patient patient = doctorService.getPatientById(patientId);
        String medicalHistory = medicalHistoryService.readMedicalHistory(patientId);
        List<Prescription> prescriptions = doctorService.getPrescriptions();
        model.addAttribute("medicalHistory", medicalHistory);
        model.addAttribute("patient", patient);
        model.addAttribute("prescriptions", prescriptions);
        // ...add patient and other data
        return "doctor-patient-details";
    }

    @GetMapping("/patients/{patientId}/appointments")
    public String viewAppointments(@PathVariable Long patientId, Model model) {
        model.addAttribute("appointments", doctorService.getAppointmentsByPatientId(patientId));
        model.addAttribute("patientId", patientId);
        return "doctor-patient-appointments";
    }

    @GetMapping("/patients/{patientId}/prescriptions/new")
    public String showCreatePrescriptionForm(@PathVariable Long patientId, Model model) {
        model.addAttribute("patientId", patientId);
        model.addAttribute("prescription", new Prescription());
        return "doctor-create-prescription";
    }

    @PostMapping("/patients/{patientId}/prescriptions")
    public String createPrescription(@PathVariable Long patientId,
                                     @ModelAttribute Prescription prescription,
                                     RedirectAttributes redirectAttributes) {
        doctorService.createPrescription(patientId, prescription);
        redirectAttributes.addFlashAttribute("successMessage", "Prescription created successfully!");
        return "redirect:/doctor/patients/" + patientId;
    }

    @GetMapping("/patients/{patientId}/prescriptions/{prescriptionId}/edit")
    public String showEditPrescriptionForm(@PathVariable Long patientId,
                                           @PathVariable Long prescriptionId,
                                           Model model) {
        Prescription prescription = doctorService.getPrescriptionById(prescriptionId);
        model.addAttribute("patientId", patientId);
        model.addAttribute("prescription", prescription);
        return "doctor-edit-prescription";
    }

    @PostMapping("/patients/{patientId}/prescriptions/{prescriptionId}")
    public String updatePrescription(@PathVariable Long patientId,
                                     @PathVariable Long prescriptionId,
                                     @ModelAttribute Prescription prescription,
                                     RedirectAttributes redirectAttributes) {
        doctorService.updatePrescription(prescriptionId, prescription);
        redirectAttributes.addFlashAttribute("successMessage", "Prescription updated successfully!");
        return "redirect:/doctor/patients/" + patientId;
    }

}

