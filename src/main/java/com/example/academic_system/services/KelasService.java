package com.example.academic_system.services;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.repositories.KelasRepository;
import com.example.academic_system.repositories.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class KelasService {

    private final KelasRepository kelasRepository;
    private final MahasiswaRepository mahasiswaRepository;

    @Autowired
    public KelasService(KelasRepository kelasRepository, MahasiswaRepository mahasiswaRepository) {
        this.kelasRepository = kelasRepository;
        this.mahasiswaRepository = mahasiswaRepository;
    }

    public List<Kelas> getAllKelas() {
        return kelasRepository.findAll();
    }

    public List<Kelas> getAllKelasWithMahasiswa() {
        try {
            return kelasRepository.findAllWithMahasiswa();
        } catch (Exception e) {
            System.out.println("Error loading kelas with mahasiswa: " + e.getMessage());
            return kelasRepository.findAll();
        }
    }

    public List<Kelas> findByMahasiswa(Mahasiswa mahasiswa) {
        try {
            System.out.println("=== Finding kelas for mahasiswa ID: " + mahasiswa.getId() + " ===");

            List<Kelas> kelasList = kelasRepository.findByMahasiswaId(mahasiswa.getId());

            // mastiin gaada data duplikat
            List<Kelas> distinctKelas = kelasList.stream()
                    .collect(Collectors.toMap(
                            Kelas::getId,
                            kelas -> kelas,
                            (existing, replacement) -> existing
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toList());

            System.out.println("Found " + kelasList.size() + " total records, " + distinctKelas.size() + " distinct kelas for mahasiswa");

            return distinctKelas;

        } catch (Exception e) {
            System.out.println("Error in findByMahasiswa: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<String> findDistinctFakultas() {
        return kelasRepository.findDistinctFakultas();
    }

    public List<String> findDistinctTahunAjar() {
        return kelasRepository.findDistinctTahunAjar();
    }

    public Optional<Kelas> getKelasById(Long id) {
        return kelasRepository.findById(id);
    }

    public long count() {
        return kelasRepository.count();
    }

    public Long countByDosenId(Long dosenId) {
        try {
            Long count = kelasRepository.countByDosenId(dosenId);
            return count != null ? count : 0L;
        } catch (Exception e) {
            System.out.println("Error counting kelas by dosen: " + e.getMessage());
            return 0L;
        }
    }

    public List<Kelas> findKelasWithFilters(String fakultas, String tahunAjar,
                                            String kode, String namaKelas, String semester) {
        try {
            Integer semesterInt = null;
            if (semester != null && !semester.trim().isEmpty()) {
                try {
                    semesterInt = Integer.parseInt(semester.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid semester format: " + semester);
                    semesterInt = null;
                }
            }

            return kelasRepository.findKelasWithFilters(fakultas, tahunAjar, kode, namaKelas, semester, semesterInt);
        } catch (Exception e) {
            System.out.println("Error in findKelasWithFilters: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Transactional
    public boolean daftarMahasiswaKeKelas(Mahasiswa mahasiswa, Kelas kelas) {
        try {
            System.out.println("=== KelasService: Starting registration ===");
            System.out.println("Mahasiswa ID: " + mahasiswa.getId() + ", Kelas ID: " + kelas.getId());

            Optional<Mahasiswa> mhsOpt = mahasiswaRepository.findByIdWithKelas(mahasiswa.getId());
            if (!mhsOpt.isPresent()) {
                System.out.println("ERROR: Mahasiswa not found");
                return false;
            }

            Optional<Kelas> kelasOpt = kelasRepository.findByIdWithMahasiswa(kelas.getId());
            if (!kelasOpt.isPresent()) {
                System.out.println("ERROR: Kelas not found");
                return false;
            }

            Mahasiswa mahasiswaFromDb = mhsOpt.get();
            Kelas kelasFromDb = kelasOpt.get();

            if (mahasiswaFromDb.getKelasDiikuti() == null) {
                mahasiswaFromDb.setKelasDiikuti(new ArrayList<>());
                System.out.println("INFO: Initialized kelasDiikuti collection");
            }

            final Long kelasId = kelasFromDb.getId();
            boolean alreadyExists = mahasiswaFromDb.getKelasDiikuti().stream()
                    .anyMatch(k -> k.getId().equals(kelasId));

            if (alreadyExists) {
                System.out.println("WARNING: Already enrolled");
                return false;
            }

            mahasiswaFromDb.getKelasDiikuti().add(kelasFromDb);
            System.out.println("INFO: Added kelas to mahasiswa collection. Size: " + mahasiswaFromDb.getKelasDiikuti().size());

            kelasFromDb.getMahasiswaTerdaftar().add(mahasiswaFromDb);
            System.out.println("INFO: Added mahasiswa to kelas collection. Size: " + kelasFromDb.getMahasiswaTerdaftar().size());

            mahasiswaRepository.save(mahasiswaFromDb);
            kelasRepository.save(kelasFromDb);
            System.out.println("INFO: Saved both mahasiswa and kelas successfully");

            System.out.println("=== KelasService: Registration successful ===");
            return true;

        } catch (Exception e) {
            System.out.println("=== KelasService: Exception occurred ===");
            e.printStackTrace();
            return false;
        }
    }

    public List<Kelas> findByDosenId(Long dosenId) {
        return kelasRepository.findByDosen_Id(dosenId);
    }

    public List<Kelas> findByDosen(Dosen dosen) {
        return kelasRepository.findByDosen(dosen);
    }

    // NEW: Find kelas yang tersedia untuk dosen (belum memiliki dosen atau dapat diambil)
    public List<Kelas> findAvailableKelasForDosen(Dosen dosen) {
        try {
            System.out.println("=== Finding available kelas for dosen: " + dosen.getNama() + " (NIP: " + dosen.getNip() + ") ===");

            List<Kelas> availableKelas = kelasRepository.findAvailableKelasForDosen(dosen.getId());

            for (Kelas k : availableKelas) {
                System.out.println(">> " + k.getKodeKelas() + " | " + k.getNamaKelas() +
                        " | Dosen: " + (k.getDosen() == null ? "Belum ada dosen" : k.getDosen().getNama()));
            }

            return availableKelas;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public List<Kelas> findAvailableKelasWithFilters(Dosen dosen, String fakultas,
                                                     String tahunAjar, String semester, String namaKelas) {
        try {
            Integer semesterInt = null;
            if (semester != null && !semester.trim().isEmpty()) {
                try {
                    semesterInt = Integer.parseInt(semester.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid semester format: " + semester);
                }
            }

            return kelasRepository.findAvailableKelasWithFilters(
                    dosen.getNip(), fakultas, tahunAjar, semester, namaKelas, semesterInt);

        } catch (Exception e) {
            System.out.println("Error in findAvailableKelasWithFilters: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Transactional
    public boolean enrollDosenToKelas(Dosen dosen, Long kelasId) {
        try {
            System.out.println("=== KelasService: Starting dosen enrollment ===");
            System.out.println("Dosen NIP: " + dosen.getNip() + ", Kelas ID: " + kelasId);

            Optional<Kelas> kelasOpt = kelasRepository.findById(kelasId);
            if (!kelasOpt.isPresent()) {
                System.out.println("ERROR: Kelas not found");
                return false;
            }

            Kelas kelas = kelasOpt.get();

            if (kelas.getDosen() != null && !kelas.getDosen().getNip().equals(dosen.getNip())) {
                System.out.println("ERROR: Kelas already has a different dosen");
                return false;
            }

            kelas.setDosen(dosen);
            kelasRepository.save(kelas);

            System.out.println("INFO: Successfully enrolled dosen to kelas");
            System.out.println("=== KelasService: Dosen enrollment successful ===");
            return true;

        } catch (Exception e) {
            System.out.println("=== KelasService: Exception occurred during dosen enrollment ===");
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean removeDosenFromKelas(Dosen dosen, Long kelasId) {
        try {
            System.out.println("=== KelasService: Removing dosen from kelas ===");
            System.out.println("Dosen NIP: " + dosen.getNip() + ", Kelas ID: " + kelasId);

            Optional<Kelas> kelasOpt = kelasRepository.findById(kelasId);
            if (!kelasOpt.isPresent()) {
                System.out.println("ERROR: Kelas not found");
                return false;
            }

            Kelas kelas = kelasOpt.get();

            if (kelas.getDosen() == null || !kelas.getDosen().getNip().equals(dosen.getNip())) {
                System.out.println("ERROR: Dosen is not assigned to this kelas");
                return false;
            }

            kelas.setDosen(null);
            kelasRepository.save(kelas);

            System.out.println("INFO: Successfully removed dosen from kelas");
            System.out.println("=== KelasService: Dosen removal successful ===");
            return true;

        } catch (Exception e) {
            System.out.println("=== KelasService: Exception occurred during dosen removal ===");
            e.printStackTrace();
            return false;
        }
    }
}