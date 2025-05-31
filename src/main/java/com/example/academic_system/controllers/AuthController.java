package com.example.academic_system.controllers;

import com.example.academic_system.models.*;
import com.example.academic_system.services.GeneratorUtil;
import com.example.academic_system.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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



    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("signupRequest", new SignUpRequest());
        return "signup";
    }

    @GetMapping("/profil")
    public String showProfilForm(Principal principal, Model model) {
        Optional<Pengguna> current = userRepository.findByEmail(principal.getName());

        if (current.isPresent()) {
            model.addAttribute("user", current.get());

            if (current.get() instanceof Mahasiswa mhs) {
                model.addAttribute("jurusan", mhs.getJurusan());
            } else if (current.get() instanceof Dosen dsn) {
                model.addAttribute("fakultas", dsn.getFakultas());
            }
        }
        return "profil";
    }




    @PostMapping("/signup")
    public String processSignUp(@ModelAttribute SignUpRequest request, Model model) {
        System.out.println("Memproses signup untuk: " + request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            model.addAttribute("error", "Email sudah digunakan.");
            return "signup";
        }

        Pengguna pengguna;

        if ("ROLE_MAHASISWA".equals(request.getRole())) {
            Mahasiswa mhs = new Mahasiswa(
                    request.getNama(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword())
            );
            mhs.setNim(GeneratorUtil.generateNim());
            pengguna = mhs;

        } else if ("ROLE_DOSEN".equals(request.getRole())) {
            Dosen dsn = new Dosen(
                    request.getNama(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword())
            );
            dsn.setNip(GeneratorUtil.generateNip());
            pengguna = dsn;

        } else {
            model.addAttribute("error", "Peran tidak valid.");
            return "signup";
        }

        pengguna.setPeran(request.getRole());

        try {
            userRepository.save(pengguna);

            Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
            if (currentAuth == null || !currentAuth.isAuthenticated() || currentAuth instanceof AnonymousAuthenticationToken) {
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            return "redirect:/profil";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("signupRequest", request);
            model.addAttribute("error", "Gagal menyimpan data: " + e.getMessage());
            return "signup";
        }
    }


    @PostMapping("/profil")
    public String updateProfil(@ModelAttribute("user") Pengguna pengguna, Principal principal) {
        Optional<Pengguna> optional = userRepository.findByEmail(principal.getName());

        if (optional.isPresent()) {
            Pengguna current = optional.get();

            if (current instanceof Mahasiswa mhs && pengguna instanceof Mahasiswa input) {
                mhs.setJurusan(input.getJurusan());
                userRepository.save(mhs);

            } else if (current instanceof Dosen dsn && pengguna instanceof Dosen input) {
                dsn.setFakultas(input.getFakultas());
                userRepository.save(dsn);
            }
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/profil";
        }
        return "login";
    }







}
