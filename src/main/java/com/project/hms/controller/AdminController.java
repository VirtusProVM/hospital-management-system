package com.project.hms.controller;

import com.project.hms.dto.ActiveUserDTO;
import com.project.hms.entity.*;
import com.project.hms.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admins")
public class AdminController {

    public AdminService adminService;

    private SessionRegistry sessionRegistry;

    public AdminController(AdminService adminService, SessionRegistry sessionRegistry) {
        this.adminService = adminService;
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/")
    public String adminPage(Model model) {

        return "admin";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        return "admin-list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("admin") Admin admin,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin-form";
        }
        adminService.saveAdmin(admin);
        redirectAttributes.addFlashAttribute("successMessage", "Admin saved successfully!");
        return "redirect:/admins";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("admin", adminService.getAdminById(id));
        return "admin-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.deleteAdmin(id);
        redirectAttributes.addFlashAttribute("successMessage", "Admin deleted successfully!");
        return "redirect:/admins";
    }

    @GetMapping("/doctors")
    public String doctorList(Model model) {

        model.addAttribute("doctors", adminService.getDoctors());

        return "doctor-list";
    }

    @GetMapping("/doctors/new")
    public String doctorForm(Model model) {

        List<Departments> departments = adminService.getDepartments();
        model.addAttribute("department", departments);
        model.addAttribute("doctor", new Doctors());

        return "doctor-form";
    }

    @PostMapping("/doctors/create")
    public String adminCreateDoctor(@Valid @ModelAttribute("doctor") Doctors doctor, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            return "doctor-form";
        }

        Long deptId = doctor.getDepartment().getDepartmentId();
        Departments dept = adminService.getDepartment(deptId);
        doctor.setDepartment(dept);

        adminService.createDoctor(doctor);
        redirectAttributes.addFlashAttribute("successMessage", "Doctor created successfully!");
        return "redirect:/admins/doctors";
    }

    @GetMapping("/doctors/edit/{id}")
    public String editDoctor(@PathVariable Long id, Model model) {
        Doctors doctor = adminService.getDoctor(id);
        List<Departments> departments = adminService.getDepartments();

        model.addAttribute("doctor", doctor);
        model.addAttribute("department", departments);

        return "doctor-form";
    }

    @PostMapping("/doctors/update/{id}")
    public String updateDoctor(@PathVariable Long id, @ModelAttribute("doctor") Doctors updatedDoctor, RedirectAttributes redirectAttributes) {
        Doctors existingDoctor = adminService.findById(id);

        existingDoctor.setFirstName(updatedDoctor.getFirstName());
        existingDoctor.setLastName(updatedDoctor.getLastName());
        existingDoctor.setPhone(updatedDoctor.getPhone());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        existingDoctor.setUsername(updatedDoctor.getUsername());
        existingDoctor.setPassword(updatedDoctor.getPassword());
        existingDoctor.setSpecialty(updatedDoctor.getSpecialty());
        existingDoctor.setIsActive(updatedDoctor.getIsActive());

        Long deptId = updatedDoctor.getDepartment().getDepartmentId();
        Departments department = adminService.getDepartment(deptId);
        existingDoctor.setDepartment(department);

        adminService.updateDoctor(id, existingDoctor);

        redirectAttributes.addFlashAttribute("successMessage", "Doctor updated successfully!");
        return "redirect:/admins/doctors";
    }

    @GetMapping("/doctors/delete/{id}")
    public String adminDeleteDoctor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.deleteDoctor(id);
        redirectAttributes.addFlashAttribute("successMessage", "Doctor deleted successfully!");
        return "redirect:/admins/doctors";
    }

    @GetMapping("/departments")
    public String departmentsList(Model model) {
        model.addAttribute("departments", adminService.getDepartments());
        return "department-list";
    }

    @GetMapping("/departments/addNewDepartment")
    public String departmentForm(Model model) {

        model.addAttribute("department", new Departments());

        return "department-form";
    }

    @PostMapping("/departments/create")
    public String createDepartment(@Valid @ModelAttribute("department") Departments department, BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            return "department-form";
        }

        adminService.createDepartment(department);
        redirectAttributes.addFlashAttribute("successMessage", "Department created successfully!");
        return "redirect:/admins/departments";
    }

    @GetMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.deleteDepartment(id);
        redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully!");

        return "redirect:/admins/departments";
    }

    @GetMapping("/departments/edit/{id}")
    public String editDepartemntForm(@PathVariable Long id, Model model) {

        Departments departments = adminService.getDepartment(id);
        model.addAttribute("department", departments);
        return "department-form";
    }

    @PutMapping("/departments/update")
    public String updateDepartment(@PathVariable Long id, @ModelAttribute  Departments department, RedirectAttributes redirectAttributes) {

        adminService.updateDepartment(id, department);
        redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully!");
        return "redirect:/admins/departments";
    }

    @GetMapping("/cashiers")
    public String cashierList(Model model) {

        model.addAttribute("cashiers", adminService.getAllCashier());
        return "cashier-list";
    }

    @GetMapping("/cashiers/new")
    public String cashierForm(Model model) {

        model.addAttribute("cashier", new Cashier());

        return "cashier-form";
    }

    @PostMapping("/cashiers/save")
    public String adminCreateCashier(@Valid @ModelAttribute("cashier") Cashier cashier, BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            return "cashier-form";
        }

        adminService.createCashier(cashier);
        redirectAttributes.addFlashAttribute("successMessage", "Cashier created successfully!");
        return "redirect:/admins/cashiers";
    }

    @GetMapping("/cashiers/edit/{id}")
    public String editCashier(@PathVariable Long id, Model model) {
        Cashier cashier = adminService.getCashierByID(id);

        model.addAttribute("cashier", cashier);

        return "cashier-form";
    }

    @PostMapping("/cashiers/update/{id}")
    public String updateCashier(@PathVariable("id") Long id, @ModelAttribute("cashier") Cashier updatedCashier, RedirectAttributes redirectAttributes) {
        Cashier existingCashier = adminService.getCashierByID(id);


        existingCashier.setEmail(updatedCashier.getEmail());
        existingCashier.setUsername(updatedCashier.getUsername());
        existingCashier.setPassword(updatedCashier.getPassword());
        existingCashier.setIsActive(updatedCashier.getIsActive());

        adminService.updateCashier(id, existingCashier);

        redirectAttributes.addFlashAttribute("successMessage", "Cashier updated successfully!");
        return "redirect:/admins/cashiers";
    }

    @GetMapping("/cashiers/delete/{id}")
    public String deleteCashier(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        adminService.deleteCashier(id);
        redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully!");

        return "redirect:/admins/cashiers";
    }

    @GetMapping("/receptions")
    public String receptionList(Model model) {
        model.addAttribute("receptions", adminService.receptionList());

        return "reception-list";
    }

    @GetMapping("/receptions/new")
    public String receptionForm(Model model) {

        model.addAttribute("reception", new Reception());

        return "reception-form";
    }

    @PostMapping("/receptions/save")
    public String createReceptionist(@Valid @ModelAttribute("reception") Reception reception, BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            return "reception-form";
        }

        adminService.createReception(reception);
        redirectAttributes.addFlashAttribute("successMessage", "Reception created successfully!");
        return "redirect:/admins/receptions";
    }

    @GetMapping("/receptions/edit/{id}")
    public String editReception(@PathVariable Long id, Model model) {
        Reception reception = adminService.getReceptionByID(id);

        model.addAttribute("reception", reception);

        return "reception-form";
    }

    @PostMapping("/receptions/update/{id}")
    public String updateReceptions(@PathVariable Long id, @ModelAttribute("reception") Reception reception, RedirectAttributes redirectAttributes) {
        Reception existingReception = adminService.getReceptionByID(id);


        existingReception.setEmail(reception.getEmail());
        existingReception.setUsername(reception.getUsername());
        existingReception.setPassword(reception.getPassword());
        existingReception.setIsActive(reception.getIsActive());

        adminService.updateReception(id, existingReception);

        redirectAttributes.addFlashAttribute("successMessage", "Reception updated successfully!");
        return "redirect:/admins/receptions";
    }

    @GetMapping("/receptions/delete/{id}")
    public String deleteReception(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        adminService.deleteReception(id);
        redirectAttributes.addFlashAttribute("successMessage", "Reception deleted successfully!");

        return "redirect:/admins/receptions";
    }

    @GetMapping("/activeUsers")
    public String listActiveUsers(Model model) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        List<ActiveUserDTO> activeUsers = principals.stream()
                .filter(principal -> principal instanceof UserDetails)
                .map(principal -> {
                    UserDetails user = (UserDetails) principal;
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    return new ActiveUserDTO(
                            user.getUsername(),
                            user.getAuthorities().toString(),
                            sessions.size()
                    );
                })
                .collect(Collectors.toList());

        model.addAttribute("activeUsers", activeUsers);
        return "active-users";
    }

}
