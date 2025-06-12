package com.example.academic_system.controllers;

import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.repositories.MahasiswaRepository;
import com.example.academic_system.services.ActivityLogService;
import com.example.academic_system.services.GeneratorUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminMahasiswaController {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/admin/manajemen_mahasiswa")
    public String listMahasiswa(Model model) {
        List<Mahasiswa> mahasiswaList = mahasiswaRepository.findAll();

        List<String> daftarFakultas = List.of(
                "Fasilkom-TI",
                "FEB",
                "FH",
                "FK"
        );

        model.addAttribute("daftarFakultas", daftarFakultas);
        model.addAttribute("mahasiswaList", mahasiswaList);
        model.addAttribute("mahasiswa", new Mahasiswa());
        return "admin/manajemen_mahasiswa";
    }

    @PostMapping("/admin/tambah_mahasiswa")
    public String tambahMahasiswa(@ModelAttribute Mahasiswa mahasiswaForm, RedirectAttributes redirectAttributes, Principal principal) {
        String generatedNim = GeneratorUtil.generateNim();
        String rawPassword = mahasiswaForm.getKataSandi();

        mahasiswaForm.setNim(generatedNim);
        mahasiswaForm.setKataSandi(passwordEncoder.encode(rawPassword));
        mahasiswaForm.setKataSandiAsli(rawPassword);
        mahasiswaForm.setPeran("ROLE_MAHASISWA");

        mahasiswaRepository.save(mahasiswaForm);

        String detail = String.format("Nama: %s, Email: %s, Fakultas: %s, Prodi: %s",
                mahasiswaForm.getNama(), mahasiswaForm.getEmail(), mahasiswaForm.getFakultas(), mahasiswaForm.getProdi());
        activityLogService.log("Mahasiswa", generatedNim, "CREATE", detail, principal.getName());

        redirectAttributes.addFlashAttribute("sukses", "Mahasiswa berhasil ditambahkan.");
        redirectAttributes.addFlashAttribute("nim", generatedNim);
        redirectAttributes.addFlashAttribute("nama", mahasiswaForm.getNama());
        redirectAttributes.addFlashAttribute("email", mahasiswaForm.getEmail());
        redirectAttributes.addFlashAttribute("fakultas", mahasiswaForm.getFakultas());
        redirectAttributes.addFlashAttribute("prodi", mahasiswaForm.getProdi());
        redirectAttributes.addFlashAttribute("rawPassword", rawPassword);

        return "redirect:/admin/manajemen_mahasiswa";
    }

    @GetMapping("/admin/manajemen_mahasiswa/{nim}")
    @Transactional
    public String hapusMahasiswa(@PathVariable("nim") String nim, RedirectAttributes redirectAttributes, Principal principal) {
        mahasiswaRepository.findByNim(nim).ifPresent(mahasiswa -> {
            mahasiswaRepository.delete(mahasiswa);
            String detail = String.format("Hapus Mahasiswa: %s, Email: %s", mahasiswa.getNama(), mahasiswa.getEmail());
            activityLogService.log("Mahasiswa", nim, "DELETE", detail, principal.getName());
        });

        redirectAttributes.addFlashAttribute("sukses", "Mahasiswa berhasil dihapus.");
        return "redirect:/admin/manajemen_mahasiswa";
    }

    @GetMapping("/admin/manajemen_mahasiswa/edit/{nim}")
    @Transactional
    public String editMahasiswaForm(@PathVariable("nim") String nim, Model model) {
        Mahasiswa mahasiswa = mahasiswaRepository.findByNim(nim).orElse(null);
        if (mahasiswa == null) return "redirect:/admin/manajemen_mahasiswa";

        model.addAttribute("daftarFakultas", List.of("Fasilkom-TI", "FEB", "FH", "FK"));
        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("editMode", true);
        model.addAttribute("mahasiswaList", mahasiswaRepository.findAll());
        return "admin/manajemen_mahasiswa";
    }

    @PostMapping("/admin/manajemen_mahasiswa/edit/{nim}")
    @Transactional
    public String editMahasiswa(@ModelAttribute Mahasiswa mahasiswaForm, RedirectAttributes redirectAttributes, Principal principal) {
        Mahasiswa existing = mahasiswaRepository.findByNim(mahasiswaForm.getNim()).orElse(null);

        if (existing != null) {
            String oldDetail = String.format("Sebelum: Nama=%s, Email=%s, Fakultas=%s, Prodi=%s",
                    existing.getNama(), existing.getEmail(), existing.getFakultas(), existing.getProdi());

            existing.setNama(mahasiswaForm.getNama());
            existing.setEmail(mahasiswaForm.getEmail());
            existing.setFakultas(mahasiswaForm.getFakultas());
            existing.setProdi(mahasiswaForm.getProdi());
            mahasiswaRepository.save(existing);

            String newDetail = String.format("Sesudah: Nama=%s, Email=%s, Fakultas=%s, Prodi=%s",
                    existing.getNama(), existing.getEmail(), existing.getFakultas(), existing.getProdi());

            activityLogService.log("Mahasiswa", mahasiswaForm.getNim(), "UPDATE", oldDetail + " -> " + newDetail, principal.getName());

            redirectAttributes.addFlashAttribute("sukses", "Data mahasiswa berhasil diperbarui.");
        }

        return "redirect:/admin/manajemen_mahasiswa";
    }
}
