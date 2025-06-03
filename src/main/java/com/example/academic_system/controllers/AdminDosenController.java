package com.example.academic_system.controllers;

import com.example.academic_system.Dosen.DosenRepository;
import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Mahasiswa;
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
public class AdminDosenController {

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("admin/manajemen_dosen")
    public String listDosen(Model model) {
        List<Dosen> dosenList = dosenRepository.findAll();
        model.addAttribute("dosenList", dosenList);
        model.addAttribute("dosen", new Dosen());
        return "admin/manajemen_dosen";
    }

    @PostMapping("/admin/tambah_dosen")
    public String tambahDosen(@ModelAttribute Dosen dosenForm, RedirectAttributes redirectAttributes) {
        String generatedNip = GeneratorUtil.generateNip();
        String rawPassword = dosenForm.getKataSandi();

        dosenForm.setNip(generatedNip);
        dosenForm.setKataSandi(passwordEncoder.encode(rawPassword));
        dosenForm.setKataSandiAsli(rawPassword);
        dosenForm.setPeran("ROLE_DOSEN");

        dosenRepository.save(dosenForm);

        redirectAttributes.addFlashAttribute("sukses", "Dosen berhasil ditambahkan.");
        redirectAttributes.addFlashAttribute("NIM", generatedNip);
        redirectAttributes.addFlashAttribute("nama", dosenForm.getNama());
        redirectAttributes.addFlashAttribute("email", dosenForm.getEmail());
        redirectAttributes.addFlashAttribute("fakultas", dosenForm.getFakultas());
        redirectAttributes.addFlashAttribute("rawPassword", rawPassword);

        return "redirect:/admin/manajemen_dosen";
    }
}