package com.example.academic_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController // ← tambahkan ini!
public class EncryptPasswordController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/encrypt") // gunakan GET agar bisa diuji lewat browser
    public String encryptPassword(@RequestParam String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
}
