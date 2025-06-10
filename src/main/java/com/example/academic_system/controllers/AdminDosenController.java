package com.example.academic_system.controllers;

import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.models.Dosen;
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
public class AdminDosenController {

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/manajemen_dosen")
    public String listDosen(Model model) {
        model.addAttribute("dosen", new Dosen());

        List<String> daftarFakultas = List.of(
                "Fasilkom-TI",
                "FEB",
                "FH",
                "FK"
        );

        List<Dosen> dosenList = dosenRepository.findAll();

        model.addAttribute("daftarFakultas", daftarFakultas);
        model.addAttribute("dosenList", dosenList);
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
        redirectAttributes.addFlashAttribute("NIP", generatedNip);
        redirectAttributes.addFlashAttribute("nama", dosenForm.getNama());
        redirectAttributes.addFlashAttribute("email", dosenForm.getEmail());
        redirectAttributes.addFlashAttribute("fakultas", dosenForm.getFakultas());
        redirectAttributes.addFlashAttribute("rawPassword", rawPassword);

        return "redirect:/admin/manajemen_dosen";
    }

    @GetMapping("/admin/manajemen_dosen/{nip}")
    @Transactional
    public String hapusDosen(@PathVariable("nip") String nip, RedirectAttributes redirectAttributes) {
        dosenRepository.findByNip(nip).ifPresent(dosenRepository::delete);
        redirectAttributes.addFlashAttribute("sukses", "Dosen berhasil dihapus.");
        return "redirect:/admin/manajemen_dosen";
    }


    @GetMapping("/admin/manajemen_dosen/edit/{nip}")
    @Transactional
    public String editDosenForm(@PathVariable("nip") String nip, Model model) {
        Dosen dosen = dosenRepository.findByNip(nip).orElse(null);
        if (dosen == null) return "redirect:/admin/manajemen_mahasiswa";

        model.addAttribute("daftarFakultas", List.of("Fasilkom-TI", "FEB", "FH", "FK"));


        model.addAttribute("dosen", dosen);
        model.addAttribute("editMode", true);
        model.addAttribute("dosenList", dosenRepository.findAll());
        return "admin/manajemen_dosen";
    }



    @PostMapping("/admin/manajemen_dosen/edit/{nip}")
    @Transactional
    public String editDosen(@ModelAttribute Dosen dosenForm, RedirectAttributes redirectAttributes) {
        Dosen existing = dosenRepository.findByNip(dosenForm.getNip()).orElse(null);
        // Mode edit
        existing.setNama(dosenForm.getNama());
        existing.setEmail(dosenForm.getEmail());
        existing.setFakultas(dosenForm.getFakultas());
        dosenRepository.save(existing);
        redirectAttributes.addFlashAttribute("sukses", "Data dosen berhasil diperbarui.");
        return "redirect:/admin/manajemen_dosen";
    }



}