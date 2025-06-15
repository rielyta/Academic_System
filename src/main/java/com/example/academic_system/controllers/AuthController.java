package com.example.academic_system.controllers;

import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.models.Pengguna;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.MahasiswaRepository;
import com.example.academic_system.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @GetMapping("/")
    public String redirectToDashboard(Principal principal, HttpSession session) {
        if (principal == null) return "redirect:/login";

        String identitas = principal.getName();

        Pengguna user = userRepository.findByEmail(identitas).orElse(null);
        if (user == null) user = dosenRepository.findByNip(identitas).orElse(null);
        if (user == null) user = mahasiswaRepository.findByNim(identitas).orElse(null);

        if (user == null) return "redirect:/login?error=notfound";

        return switch (user.getPeran()) {
            case "ROLE_ADMIN" -> "redirect:/admin/dashboard_admin";
            case "ROLE_DOSEN" -> "redirect:/dosen/dashboard_dosen";
            case "ROLE_MAHASISWA" -> {
                Mahasiswa mhs = mahasiswaRepository.findByEmail(identitas).orElse(null);
                if (mhs == null) {
                    mhs = mahasiswaRepository.findByNim(identitas).orElse(null);
                }
                if (mhs != null) {
                    session.setAttribute("mahasiswa", mhs);
                    yield "redirect:/mahasiswa/dashboard_mahasiswa";
                } else {
                    yield "redirect:/login?error=mahasiswaNotFound";
                }
            }
            default -> "redirect:/login?error=invalidrole";
        };
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
