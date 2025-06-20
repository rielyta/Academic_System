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

    // Method untuk mendapatkan semua kelas dengan mahasiswa terdaftar
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

            // Gunakan JPQL query yang lebih reliable
            List<Kelas> kelasList = kelasRepository.findByMahasiswaId(mahasiswa.getId());
            System.out.println("Found " + kelasList.size() + " kelas for mahasiswa");

            return kelasList;

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

    public Kelas saveKelas(Kelas kelas) {
        return kelasRepository.save(kelas);
    }

    public void deleteKelas(Long id) {
        kelasRepository.deleteById(id);
    }

    public long count() {
        return kelasRepository.count();
    }

    public int countByDosenId(Long dosenId) {
        return kelasRepository.countByDosenId(dosenId);
    }

    // PERBAIKAN: Handle semester parameter dengan benar
    public List<Kelas> findKelasWithFilters(String fakultas, String tahunAjar,
                                            String kode, String namaKelas, String semester) {
        try {
            // Convert semester string to integer jika tidak null/empty
            Integer semesterInt = null;
            if (semester != null && !semester.trim().isEmpty()) {
                try {
                    semesterInt = Integer.parseInt(semester.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid semester format: " + semester);
                    // Jika format salah, set ke null agar tidak memfilter berdasarkan semester
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

            // Refresh mahasiswa dengan eager loading
            Optional<Mahasiswa> mhsOpt = mahasiswaRepository.findByIdWithKelas(mahasiswa.getId());
            if (!mhsOpt.isPresent()) {
                System.out.println("ERROR: Mahasiswa not found");
                return false;
            }

            // PERBAIKAN: Load kelas dengan mahasiswa terdaftar
            Optional<Kelas> kelasOpt = kelasRepository.findByIdWithMahasiswa(kelas.getId());
            if (!kelasOpt.isPresent()) {
                System.out.println("ERROR: Kelas not found");
                return false;
            }

            // PERBAIKAN: Gunakan variabel baru untuk menghindari lambda issue
            Mahasiswa mahasiswaFromDb = mhsOpt.get();
            Kelas kelasFromDb = kelasOpt.get();

            // Initialize kelasDiikuti if null
            if (mahasiswaFromDb.getKelasDiikuti() == null) {
                mahasiswaFromDb.setKelasDiikuti(new ArrayList<>());
                System.out.println("INFO: Initialized kelasDiikuti collection");
            }

            // Check if already enrolled - gunakan variabel final
            final Long kelasId = kelasFromDb.getId();
            boolean alreadyExists = mahasiswaFromDb.getKelasDiikuti().stream()
                    .anyMatch(k -> k.getId().equals(kelasId));

            if (alreadyExists) {
                System.out.println("WARNING: Already enrolled");
                return false;
            }

            // Add kelas to mahasiswa's kelasDiikuti
            mahasiswaFromDb.getKelasDiikuti().add(kelasFromDb);
            System.out.println("INFO: Added kelas to mahasiswa collection. Size: " + mahasiswaFromDb.getKelasDiikuti().size());

            // Add mahasiswa to kelas's mahasiswaTerdaftar
            kelasFromDb.getMahasiswaTerdaftar().add(mahasiswaFromDb);
            System.out.println("INFO: Added mahasiswa to kelas collection. Size: " + kelasFromDb.getMahasiswaTerdaftar().size());

            // Save both entities
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
}
