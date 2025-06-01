package com.example.academic_system.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard_mahasiswa")
    public String mahasiswaDashboard() {
        return "dashboard_mahasiswa";
    }

    @GetMapping("/dashboard_dosen")
    public String dosenDashboard() {
        return "dashboard_dosen";
    }

    @GetMapping("/dashboard_admin")
    public String adminDashboard() {
        return "dashboard_admin";
    }
}
