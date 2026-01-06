package com.project.hms.controller;

import com.project.hms.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
