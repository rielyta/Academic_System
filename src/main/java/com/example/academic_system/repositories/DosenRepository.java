package com.example.academic_system.repositories;

import com.example.academic_system.models.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DosenRepository extends JpaRepository<Dosen, Long> {

    Optional<Dosen> findByNip(String nip);

    Optional<Dosen> findByEmail(String email);
}