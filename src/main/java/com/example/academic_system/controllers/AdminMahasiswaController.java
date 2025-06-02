package com.example.academic_system.controllers;

import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.Mahasiswa.MahasiswaRepository;
import com.example.academic_system.services.GeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        model.addAttribute("mahasiswa", new Mahasiswa()); // Untuk form tambah mahasiswa
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

}
