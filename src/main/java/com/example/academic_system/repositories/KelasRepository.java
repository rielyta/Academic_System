package com.example.academic_system.repositories;

import com.example.academic_system.models.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KelasRepository extends JpaRepository<Kelas, Long> {

    // Cari semua kelas berdasarkan kode mata kuliah
    List<Kelas> findByMataKuliah_KodeMK(String kodeMK);

    // Cari semua kelas berdasarkan NIP dosen
    List<Kelas> findByDosen_Nip(String nip);

    // Cari kelas berdasarkan nama kelas (case insensitive)
    List<Kelas> findByNamaKelasContainingIgnoreCase(String namaKelas);

    // Cari berdasarkan kombinasi mata kuliah dan dosen
    List<Kelas> findByMataKuliah_KodeMKAndDosen_Nip(String kodeMK, String nip);

    // Cek apakah kelas dengan nama dan mata kuliah sudah ada
    boolean existsByNamaKelasAndMataKuliah_KodeMK(String namaKelas, String kodeMK);
}