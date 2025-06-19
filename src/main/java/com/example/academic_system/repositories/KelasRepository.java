package com.example.academic_system.repositories;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    // Count method untuk statistik
    int countByDosenId(Long dosenId);

    @Query("SELECT DISTINCT k.fakultas FROM Kelas k WHERE k.fakultas IS NOT NULL")
    List<String> findDistinctFakultas();

    @Query("SELECT DISTINCT k.tahunAjar FROM Kelas k WHERE k.tahunAjar IS NOT NULL ORDER BY k.tahunAjar DESC")
    List<String> findDistinctTahunAjar();

    // PERBAIKAN: Query dengan nama tabel dan kolom yang benar
    @Query(value = "SELECT k.* FROM kelas k " +
            "INNER JOIN mahasiswa_kelas_diikuti mkd ON k.id = mkd.kelas_diikuti_id " +
            "WHERE mkd.mahasiswa_terdaftar_id = :mahasiswaId",
            nativeQuery = true)
    List<Kelas> findByMahasiswaId(@Param("mahasiswaId") Long mahasiswaId);

    // Alternative: Query JPQL yang mengakses dari sisi Mahasiswa
    @Query("SELECT k FROM Mahasiswa m JOIN m.kelasDiikuti k WHERE m.id = :mahasiswaId")
    List<Kelas> findByMahasiswaIdJPQL(@Param("mahasiswaId") Long mahasiswaId);

    // Query untuk filter dan search yang kompleks
    @Query("SELECT k FROM Kelas k WHERE " +
            "(:fakultas IS NULL OR :fakultas = '' OR k.fakultas = :fakultas) AND " +
            "(:tahunAjar IS NULL OR :tahunAjar = '' OR k.tahunAjar = :tahunAjar) AND " +
            "(:kode IS NULL OR :kode = '' OR k.mataKuliah.kodeMK LIKE %:kode%) AND " +
            "(:namaKelas IS NULL OR :namaKelas = '' OR LOWER(k.namaKelas) LIKE LOWER(CONCAT('%', :namaKelas, '%'))) AND " +
            "(:semester IS NULL OR :semester = '' OR k.semester = :semesterInt)")
    List<Kelas> findKelasWithFilters(@Param("fakultas") String fakultas,
                                     @Param("tahunAjar") String tahunAjar,
                                     @Param("kode") String kode,
                                     @Param("namaKelas") String namaKelas,
                                     @Param("semester") String semester,
                                     @Param("semesterInt") Integer semesterInt);

    // Query statistik yang diperbaiki dengan nama tabel yang benar
    @Query(value = "SELECT mk.nama_mk, COUNT(k.id), COUNT(mkd.mahasiswa_terdaftar_id) " +
            "FROM kelas k " +
            "JOIN mata_kuliah mk ON k.mata_kuliah_id = mk.id " +
            "LEFT JOIN mahasiswa_kelas_diikuti mkd ON k.id = mkd.kelas_diikuti_id " +
            "WHERE k.dosen_id = :dosenId " +
            "GROUP BY mk.nama_mk",
            nativeQuery = true)
    List<Object[]> findStatistikMataKuliahByDosenId(@Param("dosenId") Long dosenId);

    // Query untuk menghitung total mahasiswa semua kelas dosen
    @Query(value = "SELECT COALESCE(COUNT(mkd.mahasiswa_terdaftar_id), 0) " +
            "FROM kelas k " +
            "LEFT JOIN mahasiswa_kelas_diikuti mkd ON k.id = mkd.kelas_diikuti_id " +
            "WHERE k.dosen_id = :dosenId",
            nativeQuery = true)
    Long countTotalMahasiswaByDosenId(@Param("dosenId") Long dosenId);

    // Query untuk menghitung jumlah mata kuliah unik yang diajar dosen
    @Query("SELECT COUNT(DISTINCT k.mataKuliah.kodeMK) " +
            "FROM Kelas k WHERE k.dosen.id = :dosenId")
    Long countDistinctMataKuliahByDosenId(@Param("dosenId") Long dosenId);

    List<Kelas> findByDosen_Id(Long dosenId);

    List<Kelas> findByDosen(Dosen dosen);

    // Query untuk load semua kelas dengan mahasiswa terdaftar
    @Query("SELECT DISTINCT k FROM Kelas k LEFT JOIN FETCH k.mahasiswaTerdaftar")
    List<Kelas> findAllWithMahasiswa();

    // Query untuk load kelas by ID dengan mahasiswa terdaftar
    @Query("SELECT DISTINCT k FROM Kelas k LEFT JOIN FETCH k.mahasiswaTerdaftar WHERE k.id = :id")
    Optional<Kelas> findByIdWithMahasiswa(@Param("id") Long id);
}
