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

    public List<Dosen> findAll() {
        return dosenRepository.findAll();
    }

    public Optional<Dosen> findById(Long id) {
        return dosenRepository.findById(id);
    }


    public Dosen createDosen(Dosen dosen) {
        return dosenRepository.save(dosen);
    }

    public Dosen save(Dosen dosen) {
        return dosenRepository.save(dosen);
    }

    public void deleteById(Long id) {
        dosenRepository.deleteById(id);
    }

    public long count() {
        return dosenRepository.count();
    }

    public Dosen getDosenByEmail(String email) {
        return dosenRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Dosen dengan email " + email + " tidak ditemukan"));
    }
}