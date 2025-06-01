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


    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("signupRequest", new SignUpRequest());
        return "signup";
    }

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





    @PostMapping("/signup")
    public String processSignUp(@ModelAttribute SignUpRequest request,
                                Model model,
                                HttpServletRequest servletRequest) {
        if (userRepository.existsByEmail(request.getEmail())) {
            model.addAttribute("error", "Email sudah digunakan.");
            return "signup";
        }

        Pengguna user;

        if ("ROLE_MAHASISWA".equals(request.getRole())) {
            Mahasiswa mhs = new Mahasiswa(
                    request.getNama(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword())
            );
            mhs.setNim(GeneratorUtil.generateNim());
            user = mhs;
        } else if ("ROLE_DOSEN".equals(request.getRole())) {
            Dosen dsn = new Dosen(
                    request.getNama(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword())
            );
            dsn.setNip(GeneratorUtil.generateNip());
            user = dsn;
        } else {
            model.addAttribute("error", "Peran tidak valid.");
            return "signup";
        }

        user.setPeran(request.getRole());

        try {
            userRepository.save(user);


            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(auth);


            servletRequest.getSession().setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );


            servletRequest.getSession().setAttribute("FROM_SIGNUP", true);


            return "redirect:/profil";


        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("signupRequest", request);
            model.addAttribute("error", "Gagal menyimpan data: " + e.getMessage());
            return "signup";
        }
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
