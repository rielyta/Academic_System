package com.example.academic_system.repositories;

import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Long> {
    Optional<Mahasiswa> findByNim(String nim);
    Optional<Mahasiswa> findByEmail(String email);

    List<Mahasiswa> findByNamaContainingIgnoreCase(String nama);
    List<Mahasiswa> findByEmailContainingIgnoreCase(String email);

    boolean existsByNim(String nim);
    boolean existsByEmail(String email);

    void deleteByNim(String nim);

    @Query("SELECT COUNT(DISTINCT m) FROM Kelas k " +
            "JOIN k.mahasiswaTerdaftar m WHERE k.dosen.id = :dosenId")
    int countByDosenId(@Param("dosenId") Long dosenId);

    // Query untuk mendapatkan kelas yang diikuti mahasiswa
    @Query("SELECT m.kelasDiikuti FROM Mahasiswa m WHERE m.id = :mahasiswaId")
    List<Kelas> findKelasByMahasiswaId(@Param("mahasiswaId") Long mahasiswaId);

    // Query untuk mendapatkan mahasiswa dengan kelas yang diikuti (eager loading)
    @Query("SELECT m FROM Mahasiswa m LEFT JOIN FETCH m.kelasDiikuti WHERE m.id = :mahasiswaId")
    Optional<Mahasiswa> findByIdWithKelas(@Param("mahasiswaId") Long mahasiswaId);

    // Query untuk mendapatkan mahasiswa dengan kelas yang diikuti berdasarkan email
    @Query("SELECT m FROM Mahasiswa m LEFT JOIN FETCH m.kelasDiikuti WHERE m.email = :email")
    Optional<Mahasiswa> findByEmailWithKelas(@Param("email") String email);
}