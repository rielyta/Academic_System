package com.example.academic_system.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard_mahasiswa")
    public String dashboard() {
        return "dashboard_mahasiswa";
    }
}

