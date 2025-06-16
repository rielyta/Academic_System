package com.example.academic_system.services;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MahasiswaService {

    private final MahasiswaRepository mahasiswaRepository;

    @Autowired
    public MahasiswaService(MahasiswaRepository mahasiswaRepository) {
        this.mahasiswaRepository = mahasiswaRepository;
    }

    public List<Mahasiswa> findAll() {
        return mahasiswaRepository.findAll();
    }

    public Optional<Mahasiswa> findById(Long id) {
        return mahasiswaRepository.findById(id);
    }

    public Mahasiswa getMahasiswaById(Long id) {
        return mahasiswaRepository.findById(id).orElse(null);
    }

    public Optional<Mahasiswa> findByEmail(String email) {
        return mahasiswaRepository.findByEmail(email);
    }

    public Optional<Mahasiswa> findByEmailWithKelas(String email) {
        return mahasiswaRepository.findByEmailWithKelas(email);
    }

    public Mahasiswa createMahasiswa(Mahasiswa mahasiswa) {
        return mahasiswaRepository.save(mahasiswa);
    }

    public Mahasiswa save(Mahasiswa mahasiswa) {
        return mahasiswaRepository.save(mahasiswa);
    }

    public Mahasiswa updatemahasiswa(Long id, Mahasiswa mahasiswaDetails) {
        Optional<Mahasiswa> existingMahasiswa = mahasiswaRepository.findById(id);

        if (existingMahasiswa.isPresent()) {
            Mahasiswa mahasiswa = existingMahasiswa.get();

            if (mahasiswaDetails.getNama() != null) {
                mahasiswa.setNama(mahasiswaDetails.getNama());
            }
            if (mahasiswaDetails.getEmail() != null) {
                mahasiswa.setEmail(mahasiswaDetails.getEmail());
            }
            if (mahasiswaDetails.getFakultas() != null) {
                mahasiswa.setFakultas(mahasiswaDetails.getFakultas());
            }
            if (mahasiswaDetails.getNim() != null) {
                mahasiswa.setNim(mahasiswaDetails.getNim());
            }

            return mahasiswaRepository.save(mahasiswa);
        }
        return null;
    }

    public void deleteById(Long id) {
        mahasiswaRepository.deleteById(id);
    }

    public long count() {
        return mahasiswaRepository.count();
    }

    public long countByDosenId(Long dosenId) {
        // Implementasi untuk menghitung jumlah mahasiswa berdasarkan dosen ID
        return mahasiswaRepository.countByDosenId(dosenId);
    }

    public Optional<Mahasiswa> findByNip(String nim) {
        return mahasiswaRepository.findByNim(nim);
    }

    public Mahasiswa getMahasiswaByNip(String nim) {
        return mahasiswaRepository.findByNim(nim).orElse(null);
    }

    public List<Mahasiswa> findByNamaContainingIgnoreCase(String nama) {
        return mahasiswaRepository.findByNamaContainingIgnoreCase(nama);
    }

    public List<Mahasiswa> findByEmailContainingIgnoreCase(String email) {
        return mahasiswaRepository.findByEmailContainingIgnoreCase(email);
    }

    // Validation methods
    public boolean existsByNip(String nim) {
        return mahasiswaRepository.existsByNim(nim);
    }

    public boolean existsByEmail(String email) {
        return mahasiswaRepository.existsByEmail(email);
    }

    public boolean isNipAvailable(String nim) {
        return !mahasiswaRepository.existsByNim(nim);
    }

    public boolean isEmailAvailable(String email) {
        return !mahasiswaRepository.existsByEmail(email);
    }

    public boolean isNipAvailableForUpdate(String nim, Long mahasiswaId) {
        Optional<Mahasiswa> existingMahasiswa = mahasiswaRepository.findByNim(nim);
        return existingMahasiswa.isEmpty() || existingMahasiswa.get().getId().equals(mahasiswaId);
    }

    public boolean isEmailAvailableForUpdate(String email, Long mahasiswaId) {
        Optional<Mahasiswa> existingMahasiswa = mahasiswaRepository.findByEmail(email);
        return existingMahasiswa.isEmpty() || existingMahasiswa.get().getId().equals(mahasiswaId);
    }
}
