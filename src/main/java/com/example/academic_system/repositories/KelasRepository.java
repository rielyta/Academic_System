package com.example.academic_system.repositories;

import com.example.academic_system.models.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KelasRepository extends JpaRepository<Kelas, Long> {

    // Query untuk mencari kelas berdasarkan dosen ID
    @Query("SELECT k FROM Kelas k WHERE k.dosen.id = :dosenId")
    List<Kelas> findByDosenId(@Param("dosenId") Long dosenId);

    // Cari semua kelas berdasarkan kode mata kuliah
    List<Kelas> findByMataKuliah_KodeMK(String kodeMK);

    // Cari semua kelas berdasarkan NIP dosen
    List<Kelas> findByDosen_Nip(String nip);

    // Query untuk mencari kelas berdasarkan nama kelas (case insensitive)
    List<Kelas> findByNamaKelasContainingIgnoreCase(String namaKelas);


    // Cari berdasarkan kombinasi mata kuliah dan dosen
    List<Kelas> findByMataKuliah_KodeMKAndDosen_Nip(String kodeMK, String nip);

    // Cek apakah kelas dengan nama dan mata kuliah sudah ada
    boolean existsByNamaKelasAndMataKuliah_KodeMK(String namaKelas, String kodeMK);

    // Query untuk mendapatkan statistik mata kuliah yang diajar dosen
    @Query("SELECT mk.namaMK, COUNT(k), SUM(SIZE(k.mahasiswaTerdaftar)) " +
            "FROM Kelas k JOIN k.mataKuliah mk " +
            "WHERE k.dosen.id = :dosenId " +
            "GROUP BY mk.namaMK")
    List<Object[]> findStatistikMataKuliahByDosenId(@Param("dosenId") Long dosenId);

    // Query untuk menghitung total mahasiswa semua kelas dosen
    @Query("SELECT COALESCE(SUM(SIZE(k.mahasiswaTerdaftar)), 0) " +
            "FROM Kelas k WHERE k.dosen.id = :dosenId")
    Long countTotalMahasiswaByDosenId(@Param("dosenId") Long dosenId);

    // Query untuk menghitung jumlah mata kuliah unik yang diajar dosen
    @Query("SELECT COUNT(DISTINCT k.mataKuliah.kodeMK) " +
            "FROM Kelas k WHERE k.dosen.id = :dosenId")
    Long countDistinctMataKuliahByDosenId(@Param("dosenId") Long dosenId);

    // Count method untuk statistik
    int countByDosenId(Long dosenId);

    List<Kelas> findByMahasiswaTerdaftarContaining(com.example.academic_system.models.Mahasiswa mahasiswa);

}