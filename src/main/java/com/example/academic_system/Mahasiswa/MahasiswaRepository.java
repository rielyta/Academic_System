package com.example.academic_system.Mahasiswa;

import com.example.academic_system.models.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MahasiswaRepository extends JpaRepository<Mahasiswa, String> {
    Optional<Mahasiswa> findByNim(String nim);
    Optional<Mahasiswa> findByEmail(String email);

    void deleteByNim(String nim);

}
