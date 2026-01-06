package com.project.hms.service;

import com.project.hms.entity.*;
import com.project.hms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private AdminRepository adminRepository;
    private DoctorRepository doctorRepository;
    private DepartmentRepository departmentRepository;
    private CashierRepository cashierRepository;
    private ReceptionRepository receptionRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, DoctorRepository doctorRepository, DepartmentRepository departmentRepository,
                        CashierRepository cashierRepository, ReceptionRepository receptionRepository,PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.cashierRepository = cashierRepository;
        this.receptionRepository = receptionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    public void saveAdmin(Admin admin) {
        if (admin.getAdminId() == null || admin.getPassword().length() < 20) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        adminRepository.save(admin);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public List<Doctors> getDoctors() {
        return doctorRepository.findAll();
    }
    public Doctors createDoctor(Doctors doctor) {
        String encodedPassword = passwordEncoder.encode(doctor.getPassword());
        doctor.setPassword(encodedPassword);
        return doctorRepository.save(doctor);
    }

    public Doctors updateDoctor(Long doctorId, Doctors doctorDetails) {
        Doctors doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        if (!doctor.getPassword().equals(doctorDetails.getPassword())) {
            doctor.setPassword(passwordEncoder.encode(doctorDetails.getPassword()));
        }
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }

    public List<Departments> getDepartments() {
        return departmentRepository.findAll();
    }

    public Departments createDepartment(Departments department) {
        return departmentRepository.save(department);
    }

    public Departments updateDepartment(Long departmentId, Departments departmentDetails) {
        Departments department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        department.setName(departmentDetails.getName());
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    public Cashier createCashier(Cashier cashier) {
        String encodedPassword = passwordEncoder.encode(cashier.getPassword());
        cashier.setPassword(encodedPassword);
        return cashierRepository.save(cashier);
    }

    public Cashier updateCashier(Long id, Cashier cashier) {
        Cashier c = cashierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found"));
        if (!c.getPassword().equals(cashier.getPassword())) {
            c.setPassword(passwordEncoder.encode(cashier.getPassword()));
        }
        return cashierRepository.save(c);
    }

    public void deleteCashier(Long cashierId) {
        cashierRepository.deleteById(cashierId);
    }

    public Reception createReception(Reception reception) {
        String encodedPassword = passwordEncoder.encode(reception.getPassword());
        reception.setPassword(encodedPassword);
        return receptionRepository.save(reception);
    }

    public void deleteReception(Long receptionId) {
        receptionRepository.deleteById(receptionId);
    }

    public Departments getDepartment(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
    }

    public Doctors getDoctor(Long id) {
        Doctors doctor = doctorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No doctor found"));

        return doctor;
    }

    public Doctors findById(Long id) {
        Doctors existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor Id:" + id));

        return existingDoctor;
    }

    public List<Cashier> getAllCashier() {
        return cashierRepository.findAll();
    }

    public Cashier getCashierByID(Long id) {
        Cashier cashier = cashierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cashier ID: " + id));

        return cashier;
    }

    public List<Reception> receptionList() {
        return receptionRepository.findAll();
    }

    public Reception getReceptionByID(Long id) {
        Reception reception = receptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reception ID: " + id));

        return reception;
    }

    public Reception updateReception(Long id, Reception reception) {
        Reception c = receptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reception not found"));
        if (!c.getPassword().equals(reception.getPassword())) {
            c.setPassword(passwordEncoder.encode(reception.getPassword()));
        }
        return receptionRepository.save(c);
    }


}
