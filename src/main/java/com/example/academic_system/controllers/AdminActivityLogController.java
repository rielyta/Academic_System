package com.example.academic_system.controllers;

import com.example.academic_system.models.Pengguna;
import com.example.academic_system.repositories.ActivityLogRepository;
import com.example.academic_system.models.ActivityLog;
import com.example.academic_system.repositories.UserRepository;
import com.example.academic_system.services.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class AdminActivityLogController {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/admin/activity_log")
    public String viewActivityLog(Model model, @RequestParam(required = false) String search,
                                  @RequestParam(required = false) String filter) {
        List<ActivityLog> logs;

        if (search != null && !search.trim().isEmpty()) {
            logs = activityLogRepository.searchLogs(search.trim());
            model.addAttribute("searchKeyword", search);
        } else if (filter != null && !filter.trim().isEmpty()) {
            switch (filter.toUpperCase()) {
                case "CREATE":
                    logs = activityLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc("CREATE");
                    break;
                case "UPDATE":
                    logs = activityLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc("UPDATE");
                    break;
                case "DELETE":
                    logs = activityLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc("DELETE");
                    break;
                case "LOGIN":
                    logs = activityLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc("LOGIN");
                    break;
                case "LOGOUT":
                    logs = activityLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc("LOGOUT");
                    break;
                default:
                    logs = activityLogRepository.findAllByOrderByTimestampDesc();
            }
            model.addAttribute("activeFilter", filter);
        } else {
            logs = activityLogRepository.findAllByOrderByTimestampDesc();
        }

        model.addAttribute("logList", logs);
        model.addAttribute("totalLogs", logs.size());

        // Add statistics
        long createCount = logs.stream().filter(log -> "CREATE".equals(log.getAction())).count();
        long updateCount = logs.stream().filter(log -> "UPDATE".equals(log.getAction())).count();
        long deleteCount = logs.stream().filter(log -> "DELETE".equals(log.getAction())).count();
        long loginCount = logs.stream().filter(log -> "LOGIN".equals(log.getAction())).count();
        long logoutCount = logs.stream().filter(log -> "LOGOUT".equals(log.getAction())).count();

        model.addAttribute("createCount", createCount);
        model.addAttribute("updateCount", updateCount);
        model.addAttribute("deleteCount", deleteCount);
        model.addAttribute("loginCount", loginCount);
        model.addAttribute("logoutCount", logoutCount);

        return "admin/activity_log";
    }

    @PostMapping("/admin/activity_log/search")
    public String searchActivityLog(@RequestParam String searchKeyword, Model model) {
        return "redirect:/admin/activity_log?search=" + searchKeyword;
    }

    @GetMapping("/admin/activity_log/filter")
    public String filterActivityLog(@RequestParam String action, Model model) {
        return "redirect:/admin/activity_log?filter=" + action;
    }

    @PostMapping("/update_profile/{id}")
    public String updateProfile(@PathVariable Long id,
                                @ModelAttribute Pengguna formPengguna,
                                RedirectAttributes redirectAttributes,
                                Principal principal) {

        Pengguna existing = userRepository.findById(id).orElse(null);
        if (existing == null) {
            redirectAttributes.addFlashAttribute("error", "Pengguna tidak ditemukan.");
            return "redirect:/admin/profile";
        }
        // Simpan detail sebelum dan sesudah
        String oldDetails = String.format("Sebelum: Nama = %s, Kata Sandi = [PROTECTED]",
                existing.getNama());

        existing.setNama(formPengguna.getNama());

        // Hash password sebelum menyimpan
        existing.setKataSandi(passwordEncoder.encode(formPengguna.getKataSandi()) );

        userRepository.save(existing);
        String newDetails = String.format("Sesudah: Nama = %s, Kata Sandi = [PROTECTED]",
                existing.getNama());

        // Simpan ke activity log
        activityLogService.log("Pengguna", existing.getId().toString(), "UPDATE",
                oldDetails + " -> " + newDetails, principal.getName());

        redirectAttributes.addFlashAttribute("sukses", "Profil berhasil diperbarui.");

        return "redirect:/admin/profile";
    }
}