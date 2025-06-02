package com.example.academic_system.controllers;

import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.models.Dosen;
import com.example.academic_system.Dosen.*;
import com.example.academic_system.Mahasiswa.MahasiswaRepository;
import com.example.academic_system.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfilController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @GetMapping("/profil/profil_mahasiswa")
    public String profilMahasiswa(@AuthenticationPrincipal User userDetails, Model model) {
        String email = userDetails.getUsername();

        Mahasiswa mahasiswa = mahasiswaRepository.findByEmail(email).orElse(null);
        if (mahasiswa != null) {
            model.addAttribute("mahasiswa", mahasiswa);
            return "profil/profil_mahasiswa";
        }

        return "redirect:/login?error=unauthorized";
    }

    @GetMapping("/profil/profil_dosen")
    public String profilDosen(@AuthenticationPrincipal User userDetails, Model model) {
        String email = userDetails.getUsername();

        Dosen dosen = dosenRepository.findByEmail(email).orElse(null);
        if (dosen != null) {
            model.addAttribute("dosen", dosen);
            return "profil/profil_dosen";
        }

        return "redirect:/login?error=unauthorized";
    }
}
