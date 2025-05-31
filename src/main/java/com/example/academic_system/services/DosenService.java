package com.example.academic_system.services;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.repository.DosenRepository;
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

    public List<Dosen> getAllDosen() {
        return dosenRepository.findAll();
    }

    public Dosen getDosenById(Long id) {
        return dosenRepository.findById(id).orElse(null);
    }

    public Dosen createDosen(Dosen dosen) {
        return dosenRepository.save(dosen);
    }

    public Dosen updateDosen(Long id, Dosen dosenDetails) {
        Dosen dosen = dosenRepository.findById(id).orElse(null);
        if (dosen != null) {
            dosen.setNama(dosenDetails.getNama());
            dosen.setEmail(dosenDetails.getEmail());
            dosen.setNip(dosenDetails.getNip());
            dosen.setFakultas(dosenDetails.getFakultas());
            return dosenRepository.save(dosen);
        }
        return null;
    }

    public void deleteDosen(Long id) {
        dosenRepository.deleteById(id);
    }
}