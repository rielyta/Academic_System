package com.example.academic_system.controllers;

import com.example.academic_system.repositories.*;
import com.example.academic_system.models.ActivityLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private KelasRepository kelasRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @GetMapping("/admin/dashboard_admin")
    public String adminDashboard(Model model) {
        // Basic counts
        model.addAttribute("totalDosen", dosenRepository.count());
        model.addAttribute("totalMahasiswa", mahasiswaRepository.count());
        model.addAttribute("totalKelas", kelasRepository.count());
        model.addAttribute("totalMatkul", mataKuliahRepository.count());

        // Recent activity logs
        List<ActivityLog> recentLogs = activityLogRepository.findTop10ByOrderByTimestampDesc();
        model.addAttribute("recentLogs", recentLogs);

        // Activity statistics
        List<ActivityLog> allLogs = activityLogRepository.findAll();
        long totalActivities = allLogs.size();
        long todayActivities = allLogs.stream()
                .filter(log -> {
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
                    cal.set(java.util.Calendar.MINUTE, 0);
                    cal.set(java.util.Calendar.SECOND, 0);
                    cal.set(java.util.Calendar.MILLISECOND, 0);
                    return log.getTimestamp().after(cal.getTime());
                })
                .count();

        model.addAttribute("totalActivities", totalActivities);
        model.addAttribute("todayActivities", todayActivities);

        return "admin/dashboard_admin";
    }
}