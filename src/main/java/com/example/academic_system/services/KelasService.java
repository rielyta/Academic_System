package com.example.academic_system.services;

import com.example.academic_system.models.Kelas;
import com.example.academic_system.repositories.KelasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KelasService {

    private final KelasRepository kelasRepository;

    @Autowired
    public KelasService(KelasRepository kelasRepository) {
        this.kelasRepository = kelasRepository;
    }

    public List<Kelas> getAllKelas() {
        return kelasRepository.findAll();
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
}
