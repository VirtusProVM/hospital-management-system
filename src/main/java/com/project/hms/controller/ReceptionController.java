package com.project.hms.controller;

import com.project.hms.entity.Appointments;
import com.project.hms.entity.Doctors;
import com.project.hms.entity.Patient;
import com.project.hms.service.MedicalHistoryService;
import com.project.hms.service.ReceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/reception")
public class ReceptionController {

    private final ReceptionService receptionService;
    private final MedicalHistoryService medicalHistoryService;

    @Autowired
    public ReceptionController(ReceptionService receptionService, MedicalHistoryService medicalHistoryService) {
        this.receptionService = receptionService;
        this.medicalHistoryService = medicalHistoryService;
    }

    @GetMapping("/")
    public String receptionHomePage(Model model) {

        List<Patient> patients = receptionService.getPatients();
        model.addAttribute("patients", patients);
        List<Appointments> appointments = receptionService.getTodaysAppointments();
        model.addAttribute("appointments", appointments);

        return "reception";
    }

    @GetMapping("/patients/new")
    public String showRegisterPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "reception-patient-new";
    }

    @PostMapping("/patients")
    public String registerPatient(@ModelAttribute Patient patient, RedirectAttributes redirectAttributes) {
        Patient saved = receptionService.registerPatient(patient);
        redirectAttributes.addFlashAttribute("successMessage", "Patient registered successfully");
        return "redirect:/reception/patients/" + saved.getPatientId();
    }

    @GetMapping("/patients/{patientId}")
    public String viewPatientDetails(@PathVariable Long patientId, Model model, RedirectAttributes redirectAttributes) throws IOException {
        Patient patient = receptionService.getPatientById(patientId);
        if (patient == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Patient not found");
            return "redirect:/reception/patients/search";
        }
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", receptionService.getAppointmentsByPatientId(patientId));
        model.addAttribute("medicalHistory", medicalHistoryService.readMedicalHistory(patientId));
        return "reception-patient-details";
    }

    @GetMapping("/patients/edit/{patientId}")
    public String showEditPatientForm(@PathVariable Long patientId, Model model, RedirectAttributes redirectAttributes) {
        Patient patient = receptionService.getPatientById(patientId);
        if (patient == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Patient not found");
            return "redirect:/reception/patients/search";
        }
        model.addAttribute("patient", patient);
        return "reception-patient-edit";
    }

    @PostMapping("/patients/{patientId}")
    public String updatePatient(@PathVariable Long patientId,
                                @ModelAttribute Patient patient,
                                RedirectAttributes redirectAttributes) {
        try {
            receptionService.updatePatient(patientId, patient);
            redirectAttributes.addFlashAttribute("successMessage", "Patient updated successfully");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/reception/patients/" + patientId;
    }

    @GetMapping("/patients/{patientId}/appointments/new")
    public String showScheduleForm(@PathVariable Long patientId,
                                   @RequestParam(value = "doctorId", required = false) Long doctorId,
                                   Model model, RedirectAttributes redirectAttributes) {
        Patient patient = receptionService.getPatientById(patientId);
        if (patient == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Patient not found");
            return "redirect:/reception/patients/search";
        }

        Appointments appointments = new Appointments();
        appointments.setDoctor(new Doctors()); // Initialize nested doctor object
        model.addAttribute("appointment", appointments);

        model.addAttribute("patientId", patientId);
        model.addAttribute("doctors", receptionService.getAllDoctors());
        model.addAttribute("preselectedDoctorId", doctorId);
        return "reception-schedule-appointment";
    }


    @GetMapping("/patients/{patientId}/appointments")
    public String viewPatientAppointments(@PathVariable Long patientId, Model model) {
        model.addAttribute("appointments", receptionService.getAppointmentsByPatientId(patientId));
        model.addAttribute("patientId", patientId);
        return "reception-patient-appointments";
    }

    @PostMapping("/patients/{patientId}/create")
    public String createAppointment(@PathVariable Long patientId,
                                    @ModelAttribute("appointment") Appointments appointments,
                                    RedirectAttributes redirectAttributes) {
        receptionService.createAppointment(patientId, appointments.getDoctor().getDoctorId(),
                appointments.getAppointmentDate(), appointments.getStatus(), appointments.getNotes());
        redirectAttributes.addFlashAttribute("successMessage", "Appointment created successfully!");
        return "redirect:/reception/patients/" + patientId;
    }

    @PostMapping("/appointments/{appointmentId}/cancel")
    public String cancelAppointment(@PathVariable Long appointmentId, RedirectAttributes redirectAttributes) {
        try {
            receptionService.cancelAppointment(appointmentId);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment canceled");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/reception/";
    }

    @GetMapping("/patients/{patientId}/medical-history-file")
    public String editMedicalHistoryFile(@PathVariable Long patientId, Model model) throws IOException {
        model.addAttribute("patientId", patientId);
        model.addAttribute("medicalHistory", medicalHistoryService.readMedicalHistory(patientId));
        return "reception-edit-medical-history";
    }

    @PostMapping("/patients/{patientId}/medical-history-file")
    public String saveMedicalHistoryFile(@PathVariable Long patientId,
                                         @RequestParam("medicalHistory") String medicalHistory,
                                         RedirectAttributes redirectAttributes) throws IOException {
        medicalHistoryService.saveMedicalHistory(patientId, medicalHistory);
        redirectAttributes.addFlashAttribute("successMessage", "Medical history saved");
        return "redirect:/reception/patients/" + patientId;
    }

    @GetMapping("/patients/search")
    public String searchPatients(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("patients", receptionService.searchPatients(keyword));
        return "reception-patient-search";
    }

    @GetMapping("/patients/delete/{id}")
    public String deletePatients(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        receptionService.deletePatients(id);

        redirectAttributes.addFlashAttribute("successMessage", "Patient deleted successfully");
        return "redirect:/reception/";
    }
}
