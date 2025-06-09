package com.example.academic_system.controllers;

import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminDashboardController {

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @GetMapping("/admin/dashboard_admin")
    public String dashboard(Model model) {
        long totalDosen = dosenRepository.count();
        long totalMahasiswa = mahasiswaRepository.count();

        long totalKelas = 8;
        long totalMatkul = 20;
        long totalAdmin = 2;

        model.addAttribute("totalDosen", totalDosen);
        model.addAttribute("totalMahasiswa", totalMahasiswa);
        model.addAttribute("totalKelas", totalKelas);
        model.addAttribute("totalMatkul", totalMatkul);
        model.addAttribute("totalAdmin", totalAdmin);

        return "admin/dashboard_admin";
    }
}
