package com.project.hms.service;

import java.io.*;
import org.springframework.stereotype.Service;

@Service
public class MedicalHistoryService {

    public void saveMedicalHistory(Long patientId, String medicalHistoryText) throws IOException {
        String fileName = "medical_history_patient_" + patientId + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(medicalHistoryText);
        }
    }

    public void saveMedicalHistory(String firstname, String lastname, String medicalHistoryText) throws IOException {
        String fileName = "medical_history_patient_" + firstname + "_" + lastname + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(medicalHistoryText);
        }
    }

    public String readMedicalHistory(Long patientId) throws IOException {
        String fileName = "medical_history_patient_" + patientId + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
            return "No medical history found for this patient.";
        }
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    public String readMedicalHistory(String firstname, String lastname) throws IOException {
        String fileName = "medical_history_patient_" + firstname + "_" + lastname + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
            return "No medical history found for this patient.";
        }
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
