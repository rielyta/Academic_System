package com.example.academic_system.controllers;

import com.example.academic_system.models.Kelas;
import com.example.academic_system.repositories.KelasRepository;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.MataKuliahRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminKelasController {

    @Autowired
    private KelasRepository kelasRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    private void injectDropdowns(Model model) {
        model.addAttribute("daftarSemester", List.of("Ganjil", "Genap"));
        model.addAttribute("daftarTahunAjar", List.of("2023/2024", "2024/2025", "2025/2026"));
        model.addAttribute("daftarRuangan", List.of("A101", "A102", "B201", "B202", "C301", "C302"));
        model.addAttribute("daftarDosen", dosenRepository.findAll());
        model.addAttribute("daftarMataKuliah", mataKuliahRepository.findAll());
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
    public String tambahKelas(@ModelAttribute Kelas kelasForm, RedirectAttributes redirectAttributes) {
        kelasRepository.save(kelasForm);
        redirectAttributes.addFlashAttribute("sukses", "Kelas berhasil ditambahkan.");
        redirectAttributes.addFlashAttribute("namaKelas", kelasForm.getNamaKelas());
        redirectAttributes.addFlashAttribute("mataKuliah", kelasForm.getMataKuliah());
        redirectAttributes.addFlashAttribute("dosen", kelasForm.getDosen());
        redirectAttributes.addFlashAttribute("jamMulai", kelasForm.getJamMulai());
        redirectAttributes.addFlashAttribute("jamSelesai", kelasForm.getJamKeluar());
        redirectAttributes.addFlashAttribute("semester", kelasForm.getSemester());
        redirectAttributes.addFlashAttribute("tahunAjar", kelasForm.getTahunAjar());
        redirectAttributes.addFlashAttribute("ruangan", kelasForm.getRuangan());
        redirectAttributes.addFlashAttribute("jumlahMahasiswa", 0);
        return "redirect:/admin/manajemen_kelas";
    }

    @GetMapping("/admin/manajemen_kelas/{id}")
    @Transactional
    public String hapusKelas(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        kelasRepository.findById(id).ifPresent(kelasRepository::delete);
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
    public String editKelas(@ModelAttribute Kelas kelasForm, RedirectAttributes redirectAttributes) {
        Kelas existing = kelasRepository.findById(kelasForm.getId()).orElse(null);
        if (existing != null) {
            existing.setNamaKelas(kelasForm.getNamaKelas());
            existing.setJamMulai(kelasForm.getJamMulai());
            existing.setJamKeluar(kelasForm.getJamKeluar());
            existing.setSemester(kelasForm.getSemester());
            existing.setTahunAjar(kelasForm.getTahunAjar());
            existing.setRuangan(kelasForm.getRuangan());

            kelasRepository.save(existing);
            redirectAttributes.addFlashAttribute("sukses", "Data kelas berhasil diperbarui.");
        }
        return "redirect:/admin/manajemen_kelas";
    }
}
