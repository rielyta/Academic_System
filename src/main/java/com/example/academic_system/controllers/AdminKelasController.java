package com.example.academic_system.controllers;

import com.example.academic_system.models.Kelas;
import com.example.academic_system.repositories.KelasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminKelasController {

    @Autowired
    private KelasRepository kelasRepository;

    @GetMapping("/admin/manajemen_kelas")
    public String listKelas(Model model) {
        List<Kelas> kelasList = kelasRepository.findAll();
        model.addAttribute("kelasList", kelasList);
        return "admin/manajemen_kelas";
    }
}
