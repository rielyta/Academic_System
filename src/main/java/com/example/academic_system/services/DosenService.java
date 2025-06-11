package com.example.academic_system.services;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.repositories.DosenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DosenService {
    private final DosenRepository dosenRepository;

    @Autowired
    public DosenService(DosenRepository dosenRepository) {
        this.dosenRepository = dosenRepository;
    }

    public long count() {
        return dosenRepository.count();
    }

    public List<Dosen> getAllDosen() {
        return dosenRepository.findAll();
    }

    // Ambil dosen berdasarkan ID (Long)
    public Dosen getDosenById(Long id) {
        return dosenRepository.findById(id).orElse(null);
    }

    // Ambil dosen berdasarkan NIP (kalau kamu tambahkan method-nya)
    public Dosen getDosenByNip(String nip) {
        return dosenRepository.findByNip(nip).orElse(null);
    }

    public Dosen createDosen(Dosen dosen) {
        return dosenRepository.save(dosen);
    }

    public Dosen updateDosen(Long id, Dosen dosenDetails) {
        Dosen dosen = dosenRepository.findById(id).orElse(null);
        if (dosen != null) {
            dosen.setNama(dosenDetails.getNama());
            dosen.setEmail(dosenDetails.getEmail());
            dosen.setFakultas(dosenDetails.getFakultas());
            dosen.setNip(dosenDetails.getNip());
            return dosenRepository.save(dosen);
        }
        return null;
    }

    public void deleteDosen(Long id) {
        dosenRepository.deleteById(id);
    }
}
