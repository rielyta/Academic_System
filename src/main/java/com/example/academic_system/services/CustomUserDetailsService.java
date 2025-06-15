package com.example.academic_system.services;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.models.Pengguna;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.MahasiswaRepository;
import com.example.academic_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    @Lazy
    private DosenRepository dosenRepository;
    @Autowired
    @Lazy
    private MahasiswaRepository mahasiswaRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {

        Pengguna user = userRepository.findByEmail(input).orElse(null);

        if (user == null) {
            Dosen dosen = dosenRepository.findByNip(input).orElse(null);
            if (dosen != null) user = dosen;
        }

        if (user == null) {
            Mahasiswa mhs = mahasiswaRepository.findByNim(input).orElse(null);
            if (mhs != null) user = mhs;
        }

        if (user == null) {
            throw new UsernameNotFoundException("Identitas tidak ditemukan: " + input);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),  // email tetap jadi principal
                user.getKataSandi(),
                List.of(new SimpleGrantedAuthority(user.getPeran()))
        );
    }
}
