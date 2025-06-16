package com.example.academic_system.controllers;

import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.repositories.MahasiswaRepository;
import com.example.academic_system.services.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/mahasiswa")
public class MhsProfileController {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private ActivityLogService activityLogService;

    @GetMapping("/profil_mahasiswa")
    public String showProfile(Model model, Principal principal) {
        try {
            String email = principal.getName();

            Mahasiswa mahasiswa = mahasiswaRepository.findByEmail(email).orElse(null);
            if (mahasiswa == null) {
                return "redirect:/login?error=notfound";
            }
            model.addAttribute("mahasiswa", mahasiswa);
            return "mahasiswa/profil_mahasiswa";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @PostMapping("/update_profile/{id}")
    public String updateProfile(@PathVariable Long id,
                                @ModelAttribute Mahasiswa formMahasiswa,
                                RedirectAttributes redirectAttributes, Principal principal) {
        try {
            Mahasiswa existing = mahasiswaRepository.findById(id).orElse(null);
            if (existing == null) {
                redirectAttributes.addFlashAttribute("error", "Mahasiswa tidak ditemukan.");
                return "redirect:/mahasiswa/profil_mahasiswa";
            }

            // Validasi input
            if (formMahasiswa.getNama() == null || formMahasiswa.getNama().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Nama tidak boleh kosong.");
                return "redirect:/mahasiswa/profil_mahasiswa";
            }

            if (formMahasiswa.getKataSandi() == null || formMahasiswa.getKataSandi().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Kata sandi tidak boleh kosong.");
                return "redirect:/mahasiswa/profil_mahasiswa";
            }

            String oldDetails = String.format("Sebelum: Nama = %s, NIM = %s, Kata Sandi = [PROTECTED]",
                    existing.getNama(), existing.getNim());

            existing.setNama(formMahasiswa.getNama().trim());

            existing.setKataSandi(passwordEncoder.encode(formMahasiswa.getKataSandi()));

            mahasiswaRepository.save(existing);

            String newDetails = String.format("Sesudah: Nama = %s, NIM = %s, Kata Sandi = [PROTECTED]",
                    existing.getNama(), existing.getNim());

            // Log activity
            if (activityLogService != null) {
                activityLogService.log("Mahasiswa", existing.getId().toString(), "UPDATE",
                        oldDetails + " -> " + newDetails, principal.getName());
            }

            redirectAttributes.addFlashAttribute("sukses", "Profil berhasil diperbarui.");

            return "redirect:/mahasiswa/profil_mahasiswa";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat memperbarui profil: " + e.getMessage());
            return "redirect:/mahasiswa/profil_mahasiswa";
        }
    }

    @GetMapping("/change_password")
    public String showChangePasswordForm(Model model, Principal principal) {
        try {
            String email = principal.getName();

            Mahasiswa mahasiswa = mahasiswaRepository.findByEmail(email).orElse(null);
            if (mahasiswa == null) {
                return "redirect:/login?error=notfound";
            }
            model.addAttribute("mahasiswa", mahasiswa);
            return "mahasiswa/change_password";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @PostMapping("/change_password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            String email = principal.getName();
            Mahasiswa mahasiswa = mahasiswaRepository.findByEmail(email).orElse(null);

            if (mahasiswa == null) {
                redirectAttributes.addFlashAttribute("error", "Sesi login tidak valid.");
                return "redirect:/login";
            }

            // Validasi password lama
            if (!passwordEncoder.matches(currentPassword, mahasiswa.getKataSandi())) {
                redirectAttributes.addFlashAttribute("error", "Kata sandi lama tidak benar.");
                return "redirect:/mahasiswa/change_password";
            }

            // Validasi password baru
            if (newPassword == null || newPassword.trim().length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Kata sandi baru minimal 6 karakter.");
                return "redirect:/mahasiswa/change_password";
            }

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Konfirmasi kata sandi tidak cocok.");
                return "redirect:/mahasiswa/change_password";
            }

            String oldDetails = "Kata sandi lama = [PROTECTED]";

            // Update password
            String encodedPassword = passwordEncoder.encode(newPassword);
            mahasiswa.setKataSandi(encodedPassword);
            mahasiswaRepository.save(mahasiswa);

            String newDetails = "Kata sandi baru = [PROTECTED]";

            // Log activity
            if (activityLogService != null) {
                activityLogService.log("Mahasiswa", mahasiswa.getId().toString(), "CHANGE_PASSWORD",
                        oldDetails + " -> " + newDetails, principal.getName());
            }

            redirectAttributes.addFlashAttribute("sukses", "Kata sandi berhasil diubah.");
            return "redirect:/mahasiswa/profil_mahasiswa";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat mengubah kata sandi: " + e.getMessage());
            return "redirect:/mahasiswa/change_password";
        }
    }
}
