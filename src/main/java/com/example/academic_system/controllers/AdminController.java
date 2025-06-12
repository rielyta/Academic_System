package com.example.academic_system.controllers;

import com.example.academic_system.repositories.ActivityLogRepository;
import com.example.academic_system.services.DosenService;
import com.example.academic_system.services.KelasService;
import com.example.academic_system.services.MahasiswaService;
import com.example.academic_system.services.MataKuliahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AdminController {

    private final DosenService dosenService;
    private final MahasiswaService mahasiswaService;
    private final KelasService kelasService;
    private final MataKuliahService matkulService;
    private final ActivityLogRepository activityLogRepository;

    @Autowired
    public AdminController(
            DosenService dosenService,
            MahasiswaService mahasiswaService,
            KelasService kelasService,
            MataKuliahService matkulService,
            ActivityLogRepository activityLogRepository
    ) {
        this.dosenService = dosenService;
        this.mahasiswaService = mahasiswaService;
        this.kelasService = kelasService;
        this.matkulService = matkulService;
        this.activityLogRepository = activityLogRepository;
    }

    @GetMapping("/admin/dashboard_admin")
    public String showDashboard(Model model, Principal principal) {
        model.addAttribute("totalDosen", dosenService.count());
        model.addAttribute("totalMahasiswa", mahasiswaService.count());
        model.addAttribute("totalKelas", kelasService.count());
        model.addAttribute("totalMatkul", matkulService.count());

        // Ambil 10 log aktivitas terbaru
        model.addAttribute("recentLogs", activityLogRepository.findTop10ByOrderByTimestampDesc());

        return "admin/dashboard_admin";
    }
}
