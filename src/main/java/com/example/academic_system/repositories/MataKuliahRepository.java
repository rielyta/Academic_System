package com.example.academic_system.repositories;

import com.example.academic_system.models.MataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MataKuliahRepository extends JpaRepository<MataKuliah, String> {
    MataKuliah findByKodeMK(String kodeMK);
}
