package com.example.academic_system.controllers;

import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.services.MahasiswaService;
import com.example.academic_system.services.KelasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    private final MahasiswaService mahasiswaService;
    private KelasService kelasService;

    @Autowired
    public DashboardController(MahasiswaService mahasiswaService, KelasService kelasService) {
        this.kelasService = kelasService;
        this.mahasiswaService = mahasiswaService;
    }

    @GetMapping("/mahasiswa/dashboard_mahasiswa")
    public String mahasiswaDashboard(Model model, Principal principal) {
        String email = principal.getName(); // dapatkan email login
        Optional<Mahasiswa> mahasiswaOpt = mahasiswaService.findByEmail(email);

        if (mahasiswaOpt.isPresent()) {
            Mahasiswa mahasiswa = mahasiswaOpt.get();
            model.addAttribute("mhs", mahasiswa); // lempar ke Thymeleaf
        } else {
            return "redirect:/login?error";
        }

        return "mahasiswa/dashboard_mahasiswa";
    }

    @GetMapping("/dosen/dashboard_dosen")
    public String dosenDashboard() {
        return "dosen/dashboard_dosen";
    }
}
