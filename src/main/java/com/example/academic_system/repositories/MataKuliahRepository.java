package com.example.academic_system.repositories;

import com.example.academic_system.models.MataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MataKuliahRepository extends JpaRepository<MataKuliah, String> {
    MataKuliah findByKodeMK(String kodeMK);


    @Query("SELECT COUNT(DISTINCT mk) FROM MataKuliah mk " +
            "JOIN mk.kelasList k WHERE k.dosen.id = :dosenId")
    int countByDosenId(@Param("dosenId") Long dosenId);
}
