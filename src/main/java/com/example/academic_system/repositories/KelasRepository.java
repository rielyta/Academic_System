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

    @Query("SELECT COUNT(k) FROM Kelas k WHERE k.dosen.id = :dosenId")
    Long countByDosenId(@Param("dosenId") Long dosenId);

    @Query("SELECT DISTINCT k.fakultas FROM Kelas k WHERE k.fakultas IS NOT NULL")
    List<String> findDistinctFakultas();

    @Query("SELECT DISTINCT k.tahunAjar FROM Kelas k WHERE k.tahunAjar IS NOT NULL ORDER BY k.tahunAjar DESC")
    List<String> findDistinctTahunAjar();

    @Query(value = "SELECT k.* FROM kelas k " +
            "INNER JOIN mahasiswa_kelas_diikuti mkd ON k.id = mkd.kelas_diikuti_id " +
            "WHERE mkd.mahasiswa_terdaftar_id = :mahasiswaId",
            nativeQuery = true)
    List<Kelas> findByMahasiswaId(@Param("mahasiswaId") Long mahasiswaId);

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

    List<Kelas> findByDosen_Id(Long dosenId);

    List<Kelas> findByDosen(Dosen dosen);

    @Query("SELECT DISTINCT k FROM Kelas k LEFT JOIN FETCH k.mahasiswaTerdaftar")
    List<Kelas> findAllWithMahasiswa();

    @Query("SELECT DISTINCT k FROM Kelas k LEFT JOIN FETCH k.mahasiswaTerdaftar WHERE k.id = :id")
    Optional<Kelas> findByIdWithMahasiswa(@Param("id") Long id);

    @Query("SELECT k FROM Kelas k WHERE " +
            "(k.dosen IS NULL OR k.dosen.id = :dosenId) " +
            "ORDER BY k.tahunAjar DESC, k.semester ASC, k.namaKelas ASC")
    List<Kelas> findAvailableKelasForDosen(@Param("dosenId") Long dosenId);

    @Query("SELECT k FROM Kelas k WHERE " +
            "(k.dosen IS NULL OR k.dosen.nip = :dosenNip) AND " +
            "(:fakultas IS NULL OR :fakultas = '' OR k.fakultas = :fakultas) AND " +
            "(:tahunAjar IS NULL OR :tahunAjar = '' OR k.tahunAjar = :tahunAjar) AND " +
            "(:semester IS NULL OR :semester = '' OR k.semester = :semesterInt) AND " +
            "(:namaKelas IS NULL OR :namaKelas = '' OR LOWER(k.namaKelas) LIKE LOWER(CONCAT('%', :namaKelas, '%'))) " +
            "ORDER BY k.tahunAjar DESC, k.semester ASC, k.namaKelas ASC")
    List<Kelas> findAvailableKelasWithFilters(@Param("dosenNip") String dosenNip,
                                              @Param("fakultas") String fakultas,
                                              @Param("tahunAjar") String tahunAjar,
                                              @Param("semester") String semester,
                                              @Param("namaKelas") String namaKelas,
                                              @Param("semesterInt") Integer semesterInt);

    @Query("SELECT k FROM Kelas k WHERE k.dosen IS NULL " +
            "ORDER BY k.tahunAjar DESC, k.semester ASC, k.namaKelas ASC")
    List<Kelas> findKelasWithoutDosen();

    @Query("SELECT COUNT(k) > 0 FROM Kelas k WHERE k.id = :kelasId AND " +
            "(k.dosen IS NULL OR k.dosen.nip = :dosenNip)")
    boolean isKelasAvailableForDosen(@Param("kelasId") Long kelasId, @Param("dosenNip") String dosenNip);

    @Query("SELECT k FROM Kelas k WHERE " +
            "(k.dosen IS NULL OR k.dosen.nip = :dosenNip) AND " +
            "k.mataKuliah.kodeMK = :mataKuliahKode " +
            "ORDER BY k.tahunAjar DESC, k.semester ASC")
    List<Kelas> findAvailableKelasByMataKuliah(@Param("dosenNip") String dosenNip,
                                               @Param("mataKuliahKode") String mataKuliahKode);
}