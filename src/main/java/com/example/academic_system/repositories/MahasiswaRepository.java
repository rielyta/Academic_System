package com.example.academic_system.repositories;

import com.example.academic_system.models.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Long> {
    Optional<Mahasiswa> findByNim(String nim);

    void deleteByNim(String nim);



    @Query("SELECT COUNT(DISTINCT m) FROM Kelas k " +
            "JOIN k.mahasiswaTerdaftar m WHERE k.dosen.id = :dosenId")
    int countByDosenId(@Param("dosenId") Long dosenId);
}