package com.example.academic_system.services;

import com.example.academic_system.models.Pengguna;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        Pengguna pengguna = userRepository
                .findByEmailOrNipOrNim(input, input, input)
                .orElseThrow(() -> new UsernameNotFoundException("Identitas tidak ditemukan: " + input));

        return new org.springframework.security.core.userdetails.User(
                pengguna.getEmail(), // tetap pakai email sebagai principal
                pengguna.getKataSandi(),
                List.of(new SimpleGrantedAuthority(pengguna.getPeran()))
        );
    }

}


