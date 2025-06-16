package com.example.academic_system.services;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.repositories.DosenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DosenService {
    private final DosenRepository dosenRepository;

    @Autowired
    public DosenService(DosenRepository dosenRepository) {
        this.dosenRepository = dosenRepository;
    }

    // Basic CRUD operations
    public List<Dosen> findAll() {
        return dosenRepository.findAll();
    }

    public Optional<Dosen> findById(Long id) {
        return dosenRepository.findById(id);
    }

    public Dosen getDosenById(Long id) {
        return dosenRepository.findById(id).orElse(null);
    }

    public Dosen createDosen(Dosen dosen) {
        return dosenRepository.save(dosen);
    }

    public Dosen save(Dosen dosen) {
        return dosenRepository.save(dosen);
    }

    public Dosen updateDosen(Long id, Dosen dosenDetails) {
        Optional<Dosen> existingDosen = dosenRepository.findById(id);

        if (existingDosen.isPresent()) {
            Dosen dosen = existingDosen.get();

            // Explicit field updates
            if (dosenDetails.getNama() != null) {
                dosen.setNama(dosenDetails.getNama());
            }
            if (dosenDetails.getEmail() != null) {
                dosen.setEmail(dosenDetails.getEmail());
            }
            if (dosenDetails.getFakultas() != null) {
                dosen.setFakultas(dosenDetails.getFakultas());
            }
            if (dosenDetails.getNip() != null) {
                dosen.setNip(dosenDetails.getNip());
            }

            return dosenRepository.save(dosen);
        }
        return null;
    }

    public void deleteById(Long id) {
        dosenRepository.deleteById(id);
    }

    public long count() {
        return dosenRepository.count();
    }

    // Search operations
    public Optional<Dosen> findByNip(String nip) {
        return dosenRepository.findByNip(nip);
    }

    public Dosen getDosenByNip(String nip) {
        return dosenRepository.findByNip(nip).orElse(null);
    }

    public List<Dosen> findByNamaContainingIgnoreCase(String nama) {
        return dosenRepository.findByNamaContainingIgnoreCase(nama);
    }

    public List<Dosen> findByEmailContainingIgnoreCase(String email) {
        return dosenRepository.findByEmailContainingIgnoreCase(email);
    }

    // Validation methods
    public boolean existsByNip(String nip) {
        return dosenRepository.existsByNip(nip);
    }

    public boolean existsByEmail(String email) {
        return dosenRepository.existsByEmail(email);
    }

    public boolean isNipAvailable(String nip) {
        return !dosenRepository.existsByNip(nip);
    }

    public boolean isEmailAvailable(String email) {
        return !dosenRepository.existsByEmail(email);
    }

    public boolean isNipAvailableForUpdate(String nip, Long dosenId) {
        Optional<Dosen> existingDosen = dosenRepository.findByNip(nip);
        return existingDosen.isEmpty() || existingDosen.get().getId().equals(dosenId);
    }

    public boolean isEmailAvailableForUpdate(String email, Long dosenId) {
        Optional<Dosen> existingDosen = dosenRepository.findByEmail(email);
        return existingDosen.isEmpty() || existingDosen.get().getId().equals(dosenId);
    }

    public Dosen getDosenByEmail(String email) {
        return dosenRepository.findByEmail(email).orElse(null);
    }
}