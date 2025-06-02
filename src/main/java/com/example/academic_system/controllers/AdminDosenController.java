package com.example.academic_system.controllers;

import com.example.academic_system.Dosen.DosenRepository;
import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Mahasiswa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminDosenController {

    @Autowired
    private DosenRepository dosenRepository;

    @GetMapping("admin/manajemen_dosen")
    public String listDosen(Model model) {
        List<Dosen> dosenList = dosenRepository.findAll();
        model.addAttribute("dosenList", dosenList);
        return "admin/manajemen_dosen";
    }
}