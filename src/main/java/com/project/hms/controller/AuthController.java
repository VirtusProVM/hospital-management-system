package com.project.firestorm.controller;

import com.project.firestorm.entity.Admin;
import com.project.firestorm.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class AuthController {
    private final AdminService adminService;

    @Autowired
    public AuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/doctor/login")
    public String doctorLogin() {
        return "doctor-login";
    }

    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin-login";
    }

    @GetMapping("/reception/login")
    public String receptionLogin() {
        return "reception-login";
    }

    @GetMapping("/cashier/login")
    public String cashierLogin() {
        return "cashier-login";
    }

}
