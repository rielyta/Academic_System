package com.example.academic_system.controllers;

import com.example.academic_system.models.*;
import com.example.academic_system.services.GeneratorUtil;
import com.example.academic_system.services.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Optional;

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
    private PasswordEncoder passwordEncoder;


    @GetMapping("/profil")
    public String showProfilForm(Principal principal) {
        Optional<Pengguna> current = userRepository.findByEmail(principal.getName());

        if (current.isPresent()) {
            String role = current.get().getPeran();

            if ("ROLE_MAHASISWA".equals(role)) {
                return "redirect:/profil/profil_mahasiswa";
            } else if ("ROLE_DOSEN".equals(role)) {
                return "redirect:/profil/profil_dosen";
            }
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/profil")
    public String updateProfil(@ModelAttribute("user") Pengguna user, Principal principal) {
        Optional<Pengguna> optional = userRepository.findByEmail(principal.getName());

        if (optional.isPresent()) {
            Pengguna current = optional.get();

            if (current instanceof Mahasiswa mhs && user instanceof Mahasiswa input) {
                mhs.setProdi(input.getProdi());
                userRepository.save(mhs);

            } else if (current instanceof Dosen dsn && user instanceof Dosen input) {
                dsn.setFakultas(input.getFakultas());
                userRepository.save(dsn);
            }
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // biarkan form login selalu tampil
    }








}
