package com.example.academic_system.controllers;

import com.example.academic_system.models.Pengguna;
import com.example.academic_system.repositories.UserRepository;
import com.example.academic_system.services.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        String email = principal.getName();

        Pengguna pengguna = userRepository.findByEmail(email).orElse(null);
        if (pengguna == null) {
            return "redirect:/login?error=notfound";
        }
        model.addAttribute("admin", pengguna);
        return "admin/profile_admin";
    }

    @PostMapping("/update_profile/{id}")
    public String updateProfile(@PathVariable Long id,
                                @ModelAttribute Pengguna formPengguna,
                                RedirectAttributes redirectAttributes, Principal principal) {

        Pengguna existing = userRepository.findById(id).orElse(null);
        if (existing == null) {
            redirectAttributes.addFlashAttribute("error", "Pengguna tidak ditemukan.");
            return "redirect:/admin/profile";
        }

        String oldDetails = String.format("Sebelum: Nama = %s, Kata Sandi = [PROTECTED]",existing.getNama());

        existing.setNama(formPengguna.getNama());

        // Hash password sebelum menyimpan
        existing.setKataSandi(passwordEncoder.encode(formPengguna.getKataSandi()) );

        userRepository.save(existing);

        String newDetails = String.format("Sesudah: Nama = %s, Kata Sandi = [PROTECTED]",existing.getNama());
        activityLogService.log("Pengguna", existing.getId().toString(), "UPDATE",
                oldDetails + " -> " + newDetails, principal.getName());

        redirectAttributes.addFlashAttribute("sukses", "Profil berhasil diperbarui.");

        return "redirect:/admin/profile";
    }

}
