package com.example.academic_system.services;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.models.Pengguna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Pengguna, Long> {
    Optional<Pengguna> findByEmail(String email);
    boolean existsByEmail(String email);
}

