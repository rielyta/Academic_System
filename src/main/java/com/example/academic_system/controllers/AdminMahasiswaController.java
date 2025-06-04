package com.example.academic_system.controllers;

import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.Mahasiswa.MahasiswaRepository;
import com.example.academic_system.services.GeneratorUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminMahasiswaController {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/manajemen_mahasiswa")
    public String listMahasiswa(Model model) {
        List<Mahasiswa> mahasiswaList = mahasiswaRepository.findAll();
        model.addAttribute("mahasiswaList", mahasiswaList);
        model.addAttribute("mahasiswa", new Mahasiswa());
        model.addAttribute("editMode", false);
        return "admin/manajemen_mahasiswa";
    }

    @PostMapping("/admin/tambah_mahasiswa")
    public String tambahMahasiswa(@ModelAttribute Mahasiswa mahasiswaForm, RedirectAttributes redirectAttributes) {
        String generatedNim = GeneratorUtil.generateNim();
        String rawPassword = mahasiswaForm.getKataSandi();

        mahasiswaForm.setNim(generatedNim);
        mahasiswaForm.setKataSandi(passwordEncoder.encode(rawPassword));
        mahasiswaForm.setKataSandiAsli(rawPassword);
        mahasiswaForm.setPeran("ROLE_MAHASISWA");

        mahasiswaRepository.save(mahasiswaForm);

        redirectAttributes.addFlashAttribute("sukses", "Mahasiswa berhasil ditambahkan.");
        redirectAttributes.addFlashAttribute("nim", generatedNim);
        redirectAttributes.addFlashAttribute("nama", mahasiswaForm.getNama());
        redirectAttributes.addFlashAttribute("email", mahasiswaForm.getEmail());
        redirectAttributes.addFlashAttribute("fakultas", mahasiswaForm.getFakultas());
        redirectAttributes.addFlashAttribute("prodi", mahasiswaForm.getProdi());
        redirectAttributes.addFlashAttribute("rawPassword", rawPassword);

        return "redirect:/admin/manajemen_mahasiswa";
    }

    @PostMapping("/admin/manajemen_mahasiswa/edit/{nim}")
    @Transactional
    public String hapusMahasiswa(@PathVariable("nim") String nim, RedirectAttributes redirectAttributes) {
        mahasiswaRepository.deleteByNim(nim);
        redirectAttributes.addFlashAttribute("sukses", "Mahasiswa berhasil dihapus.");
        return "redirect:/admin/manajemen_mahasiswa";
    }

    @GetMapping("/admin/manajemen_mahasiswa/edit/{nim}")
    @Transactional
    public String editMahasiswaForm(@PathVariable("nim") String nim, Model model) {
        Mahasiswa mahasiswa = mahasiswaRepository.findByNim(nim).orElse(null);
        if (mahasiswa == null) return "redirect:/admin/manajemen_mahasiswa";

        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("editMode", true);
        model.addAttribute("mahasiswaList", mahasiswaRepository.findAll());
        return "admin/manajemen_mahasiswa";
    }





    @PostMapping("/admin/manajemen_mahasiswaedit/{nim}")
    @Transactional
    public String editMahasiswa(@ModelAttribute Mahasiswa mahasiswaForm, RedirectAttributes redirectAttributes) {
        Mahasiswa existing = mahasiswaRepository.findByNim(mahasiswaForm.getNim()).orElse(null);

        if (existing != null) {
            // Mode edit
            existing.setNama(mahasiswaForm.getNama());
            existing.setEmail(mahasiswaForm.getEmail());
            existing.setFakultas(mahasiswaForm.getFakultas());
            existing.setProdi(mahasiswaForm.getProdi());
            mahasiswaRepository.save(existing);
            redirectAttributes.addFlashAttribute("sukses", "Data mahasiswa berhasil diperbarui.");
        } else {
            // Mode tambah
            String generatedNim = GeneratorUtil.generateNim();
            mahasiswaForm.setNim(generatedNim);
            mahasiswaForm.setKataSandi(passwordEncoder.encode(mahasiswaForm.getKataSandi()));
            mahasiswaForm.setKataSandiAsli(mahasiswaForm.getKataSandi());
            mahasiswaForm.setPeran("ROLE_MAHASISWA");
            mahasiswaRepository.save(mahasiswaForm);
            redirectAttributes.addFlashAttribute("sukses", "Mahasiswa berhasil ditambahkan.");
        }

        return "redirect:/admin/manajemen_mahasiswa";
    }








}
