package com.example.academic_system.repository;

import com.example.academic_system.models.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DosenRepository extends JpaRepository<Dosen, Long> {
    Optional<Dosen> findByNip(String nip);
    Optional<Dosen> findByEmail(String email);
}
