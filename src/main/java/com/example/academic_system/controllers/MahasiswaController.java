package com.example.academic_system.controllers;

import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.services.MahasiswaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MahasiswaController {

    private final MahasiswaService mahasiswaService;

    @Autowired
    public MahasiswaController(MahasiswaService mahasiswaService) {
        this.mahasiswaService = mahasiswaService;
    }

    @GetMapping("/mahasiswa/dashboard_mahasiswa")
    public String dashboardMahasiswa(HttpSession session, Model model) {
        Mahasiswa mhs = (Mahasiswa) session.getAttribute("mahasiswa");
        if (mhs == null) {
            return "redirect:/login";
        }
        model.addAttribute("Mahasiswa", mhs);
        return "mahasiswa/dashboard_mahasiswa";
    }



}