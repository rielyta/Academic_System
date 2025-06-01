package com.example.academic_system.controllers;

import com.example.academic_system.models.Pengguna;
import com.example.academic_system.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfilController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profil/profil_mahasiswa")
    public String profilMahasiswa(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails, Model model) {
        String email = userDetails.getUsername();
        Pengguna pengguna = userRepository.findByEmail(email).orElse(null);

        if (pengguna instanceof com.example.academic_system.models.Mahasiswa mahasiswa) {
            model.addAttribute("mahasiswa", mahasiswa);
            return "profil/profil_mahasiswa";
        }

        return "redirect:/login?error=unauthorized";
    }


    @GetMapping("/profil/profil_dosen")
    public String profilDosen(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails, Model model) {
        String email = userDetails.getUsername();
        Pengguna user = userRepository.findByEmail(email).orElse(null);
        model.addAttribute("dosen", user);
        return "profil/profil_dosen";
    }
}
