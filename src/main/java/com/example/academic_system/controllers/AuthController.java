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






    @GetMapping("/")
    public String redirectToLogin(Principal principal) {
        if (principal == null) return "redirect:/login";

        String identitas = principal.getName();
        Optional<Pengguna> user = userRepository.findByEmailOrNipOrNim(identitas, identitas, identitas);

        if (user.isPresent()) {
            return switch (user.get().getPeran()) {
                case "ROLE_ADMIN" -> "redirect:/admin/dashboard_admin";
                case "ROLE_DOSEN" -> "redirect:/dosen/dashboard_dosen";
                case "ROLE_MAHASISWA" -> "redirect:/mahasiswa/dashboard_mahasiswa";
                default -> "redirect:/login?error";
            };
        }

        return "redirect:/login";
    }





    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }



    @PostMapping("/encrypt")
    @ResponseBody
    public String encryptPassword(@RequestParam String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }


}
