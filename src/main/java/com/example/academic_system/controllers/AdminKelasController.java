package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.MataKuliah;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.KelasRepository;
import com.example.academic_system.repositories.MataKuliahRepository;
import com.example.academic_system.services.ActivityLogService;
import com.example.academic_system.services.KelasService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminKelasController {

    @Autowired
    private KelasRepository kelasRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private KelasService kelasService;


    private void injectDropdowns(Model model) {
        model.addAttribute("daftarSemester", List.of("1", "2", "3", "4", "5", "6", "7", "8"));
        model.addAttribute("daftarTahunAjar", List.of("2023/2024", "2024/2025", "2025/2026"));
        model.addAttribute("daftarRuangan", List.of("A101", "A102", "B201", "B202", "C301", "C302"));
        model.addAttribute("daftarDosen", dosenRepository.findAll());
        model.addAttribute("daftarMataKuliah", mataKuliahRepository.findAll());

        model.addAttribute("daftarFakultas", List.of(
                "Fasilkom-TI",
                "FH",
                "FEB",
                "FK"
        ));

        Map<String, String> hariMap = new LinkedHashMap<>();
        hariMap.put("MONDAY", "Senin");
        hariMap.put("TUESDAY", "Selasa");
        hariMap.put("WEDNESDAY", "Rabu");
        hariMap.put("THURSDAY", "Kamis");
        hariMap.put("FRIDAY", "Jumat");
        model.addAttribute("hariMap", hariMap);
    }

    @GetMapping("/admin/manajemen_kelas")
    public String listKelas(Model model) {
        List<Kelas> kelasList = kelasService.getAllKelasWithMahasiswa();

        System.out.println("=== Admin: Loading manajemen kelas ===");
        for (Kelas kelas : kelasList) {
            System.out.println("Kelas: " + kelas.getNamaKelas() +
                    " - Jumlah Mahasiswa: " + kelas.getJumlahMahasiswa());
        }

        model.addAttribute("kelasList", kelasList);
        model.addAttribute("kelas", new Kelas());
        injectDropdowns(model);
        return "admin/manajemen_kelas";
    }

    @PostMapping("/admin/tambah_kelas")
    public String tambahKelas(@ModelAttribute Kelas kelasForm, RedirectAttributes redirectAttributes, Principal principal) {
        kelasRepository.save(kelasForm);

        String detail = String.format("Tambah: %s (%s) - %s oleh %s, Sem %s, TA %s",
                kelasForm.getNamaKelas(),
                kelasForm.getKodeKelas(),
                kelasForm.getMataKuliah().getNamaMK(),
                kelasForm.getDosen().getNama(),
                kelasForm.getSemester(),
                kelasForm.getTahunAjar());

        activityLogService.log("Kelas", String.valueOf(kelasForm.getId()), "CREATE", detail, principal.getName());

        redirectAttributes.addFlashAttribute("sukses", "Kelas berhasil ditambahkan.");
        return "redirect:/admin/manajemen_kelas";
    }

    @GetMapping("/admin/manajemen_kelas/{id}")
    @Transactional
    public String hapusKelas(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, Principal principal) {
        kelasRepository.findById(id).ifPresent(kelas -> {
            String detail = String.format("Hapus: %s (%s) - %s oleh %s",
                    kelas.getNamaKelas(),
                    kelas.getKodeKelas(),
                    kelas.getMataKuliah().getNamaMK(),
                    kelas.getDosen().getNama());

            kelasRepository.delete(kelas);
            activityLogService.log("Kelas", String.valueOf(kelas.getId()), "DELETE", detail, principal.getName());
        });

        redirectAttributes.addFlashAttribute("sukses", "Kelas berhasil dihapus.");
        return "redirect:/admin/manajemen_kelas";
    }

    @GetMapping("/admin/manajemen_kelas/edit/{id}")
    @Transactional
    public String editKelasForm(@PathVariable("id") Long id, Model model) {
        Kelas kelas = kelasRepository.findById(id).orElse(null);
        if (kelas == null) return "redirect:/admin/manajemen_kelas";

        model.addAttribute("kelas", kelas);
        model.addAttribute("editMode", true);
        model.addAttribute("kelasList", kelasRepository.findAll());
        injectDropdowns(model);
        return "admin/manajemen_kelas";
    }

    @PostMapping("/admin/manajemen_kelas/edit/{id}")
    @Transactional
    public String editKelas(@ModelAttribute Kelas kelasForm, RedirectAttributes redirectAttributes, Principal principal) {
        Kelas existing = kelasRepository.findById(kelasForm.getId()).orElse(null);
        if (existing != null) {
            String detail = String.format("Edit: %s (%s) → MK: %s, Dosen: %s, Ruang: %s",
                    existing.getNamaKelas(),
                    existing.getKodeKelas(),
                    kelasForm.getMataKuliah().getNamaMK(),
                    kelasForm.getDosen().getNama(),
                    kelasForm.getRuangan());

            existing.setKodeKelas(kelasForm.getKodeKelas());
            existing.setNamaKelas(kelasForm.getNamaKelas());
            existing.setFakultas(kelasForm.getFakultas());
            existing.setMataKuliah(kelasForm.getMataKuliah());
            existing.setDosen(kelasForm.getDosen());
            existing.setRuangan(kelasForm.getRuangan());
            existing.setSemester(kelasForm.getSemester());
            existing.setTahunAjar(kelasForm.getTahunAjar());
            existing.setHariKelas(kelasForm.getHariKelas());
            existing.setJamMulai(kelasForm.getJamMulai());
            existing.setJamKeluar(kelasForm.getJamKeluar());

            kelasRepository.save(existing);
            activityLogService.log("Kelas", String.valueOf(existing.getId()), "UPDATE", detail, principal.getName());

            redirectAttributes.addFlashAttribute("sukses", "Data kelas berhasil diperbarui.");
        }
        return "redirect:/admin/manajemen_kelas";
    }

}