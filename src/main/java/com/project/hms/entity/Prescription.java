package com.project.hms.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    /*
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctors doctor;
*/
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    /*
    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointments appointments;
*/

    private String drugName;

    private String dosage;

    private String details;
    private Date date;


    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
/*
    public Doctors getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctors doctor) {
        this.doctor = doctor;
    }
*/
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
/*
    public Appointments getAppointment() {
        return appointments;
    }

    public void setAppointment(Appointments appointments) {
        this.appointments = appointments;
    }
*/
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", drugName='" + drugName + '\'' +
                ", dosage='" + dosage + '\'' +
                ", details='" + details + '\'' +
                ", date=" + date +
                '}';
    }
}
