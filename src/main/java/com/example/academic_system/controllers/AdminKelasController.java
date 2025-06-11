package com.example.academic_system.controllers;

import com.example.academic_system.models.Kelas;
import com.example.academic_system.repositories.KelasRepository;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.MataKuliahRepository;
import com.example.academic_system.services.ActivityLogService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.security.Principal;

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

    private void injectDropdowns(Model model) {
        model.addAttribute("daftarSemester", List.of("1", "2", "3", "4", "5", "6", "7", "8"));
        model.addAttribute("daftarTahunAjar", List.of("2023/2024", "2024/2025", "2025/2026"));
        model.addAttribute("daftarRuangan", List.of("A101", "A102", "B201", "B202", "C301", "C302"));
        model.addAttribute("daftarDosen", dosenRepository.findAll());
        model.addAttribute("daftarMataKuliah", mataKuliahRepository.findAll());

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
        List<Kelas> kelasList = kelasRepository.findAll();
        model.addAttribute("kelasList", kelasList);
        model.addAttribute("kelas", new Kelas());
        injectDropdowns(model);
        return "admin/manajemen_kelas";
    }

    @PostMapping("/admin/tambah_kelas")
    public String tambahKelas(@ModelAttribute Kelas kelasForm, RedirectAttributes redirectAttributes, Principal principal) {
        kelasRepository.save(kelasForm);

        String detail = String.format("Kelas %s - %s (%s) oleh %s, Semester %s, TA %s, Ruangan %s, Jam %s-%s",
                kelasForm.getNamaKelas(),
                kelasForm.getMataKuliah().getNamaMK(),
                kelasForm.getMataKuliah().getKodeMK(),
                kelasForm.getDosen().getNama(),
                kelasForm.getSemester(),
                kelasForm.getTahunAjar(),
                kelasForm.getRuangan(),
                kelasForm.getJamMulai(),
                kelasForm.getJamKeluar());

        activityLogService.log("Kelas", String.valueOf(kelasForm.getId()), "CREATE", detail, principal.getName());

        redirectAttributes.addFlashAttribute("sukses", "Kelas berhasil ditambahkan.");
        return "redirect:/admin/manajemen_kelas";
    }

    @GetMapping("/admin/manajemen_kelas/{id}")
    @Transactional
    public String hapusKelas(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, Principal principal) {
        kelasRepository.findById(id).ifPresent(kelas -> {
            String detail = String.format("Menghapus kelas %s - %s (%s), oleh %s, Semester %s, TA %s",
                    kelas.getNamaKelas(),
                    kelas.getMataKuliah().getNamaMK(),
                    kelas.getMataKuliah().getKodeMK(),
                    kelas.getDosen().getNama(),
                    kelas.getSemester(),
                    kelas.getTahunAjar());

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
            String oldDetail = String.format("Sebelum: Kelas %s, MK %s, Dosen %s, Ruangan %s, Hari %s, Jam %s-%s",
                    existing.getNamaKelas(),
                    existing.getMataKuliah().getNamaMK(),
                    existing.getDosen().getNama(),
                    existing.getRuangan(),
                    existing.getHariKelas(),
                    existing.getJamMulai(),
                    existing.getJamKeluar());

            existing.setMataKuliah(kelasForm.getMataKuliah());
            existing.setDosen(kelasForm.getDosen());
            existing.setRuangan(kelasForm.getRuangan());
            existing.setSemester(kelasForm.getSemester());
            existing.setTahunAjar(kelasForm.getTahunAjar());
            existing.setHariKelas(kelasForm.getHariKelas());
            existing.setJamMulai(kelasForm.getJamMulai());
            existing.setJamKeluar(kelasForm.getJamKeluar());

            kelasRepository.save(existing);

            String newDetail = String.format("Sesudah: Kelas %s, MK %s, Dosen %s, Ruangan %s, Hari %s, Jam %s-%s",
                    existing.getNamaKelas(),
                    existing.getMataKuliah().getNamaMK(),
                    existing.getDosen().getNama(),
                    existing.getRuangan(),
                    existing.getHariKelas(),
                    existing.getJamMulai(),
                    existing.getJamKeluar());

            activityLogService.log("Kelas", String.valueOf(existing.getId()), "UPDATE", oldDetail + " -> " + newDetail, principal.getName());

            redirectAttributes.addFlashAttribute("sukses", "Data kelas berhasil diperbarui.");
        }
        return "redirect:/admin/manajemen_kelas";
    }
}
