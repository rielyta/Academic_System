package com.example.academic_system.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

//    @GetMapping("mahasiswa/dashboard_mahasiswa")
//    public String mahasiswaDashboard() {return "mahasiswa/dashboard_mahasiswa";
//    }

    @GetMapping("dosen/dashboard_dosen")
    public String dosenDashboard() {
        return "dosen/dashboard_dosen";
    }
}
