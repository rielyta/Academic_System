package com.example.academic_system.services;

import com.example.academic_system.models.MataKuliah;
import com.example.academic_system.repositories.MataKuliahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MataKuliahService {

    private final MataKuliahRepository mataKuliahRepository;

    @Autowired
    public MataKuliahService(MataKuliahRepository mataKuliahRepository) {
        this.mataKuliahRepository = mataKuliahRepository;
    }

    public long count() {
        return mataKuliahRepository.count();
    }

    public int countByDosenId(Long dosenId) {
        return mataKuliahRepository.countByDosenId(dosenId);
    }
}
