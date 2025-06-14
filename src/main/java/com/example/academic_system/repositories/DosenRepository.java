package com.example.academic_system.repositories;

import com.example.academic_system.models.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DosenRepository extends JpaRepository<Dosen, Long> {

    // Find dosen by NIP
    Optional<Dosen> findByNip(String nip);

    // Find dosen by email
    Optional<Dosen> findByEmail(String email);

    // Search dosen by nama (case insensitive)
    List<Dosen> findByNamaContainingIgnoreCase(String nama);

    // Search dosen by email (case insensitive)
    List<Dosen> findByEmailContainingIgnoreCase(String email);

    // Check if NIP exists
    boolean existsByNip(String nip);

    // Check if email exists
    boolean existsByEmail(String email);
}