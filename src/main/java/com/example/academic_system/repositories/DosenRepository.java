package com.example.academic_system.repositories;

import com.example.academic_system.models.Dosen;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Optional;

public interface DosenRepository extends JpaRepository<Dosen, Long> {
    Optional<Dosen> findByNip(String nip);
}