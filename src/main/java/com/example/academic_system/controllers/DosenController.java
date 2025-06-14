package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.services.DosenService;
import com.example.academic_system.services.KelasService;
import com.example.academic_system.services.MahasiswaService;
import com.example.academic_system.services.MataKuliahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dosen")
public class DosenController {

    @Autowired
    private DosenService dosenService;

    @Autowired
    private KelasService kelasService;

    @Autowired
    private MahasiswaService mahasiswaService;

    @Autowired
    private MataKuliahService mataKuliahService;

    @GetMapping("/list")
    public String listDosen(Model model) {
        List<Dosen> dosenList = dosenService.findAll();
        model.addAttribute("dosenList", dosenList);
        return "dosen/list";
    }

    @GetMapping("/{id}")
    public String detailDosen(@PathVariable Long id, Model model) {
        Optional<Dosen> dosen = dosenService.findById(id);
        if (dosen.isPresent()) {
            model.addAttribute("dosen", dosen.get());

            // Calculate statistics
            int totalKelas = kelasService.countByDosenId(id);
            int totalMahasiswa = mahasiswaService.countByDosenId(id);
            int totalMataKuliah = mataKuliahService.countByDosenId(id);

            model.addAttribute("totalKelas", totalKelas);
            model.addAttribute("totalMahasiswa", totalMahasiswa);
            model.addAttribute("totalMataKuliah", totalMataKuliah);

            return "dosen/detail";
        }
        return "redirect:/dosen";
    }

    // Add other controller methods as needed
}