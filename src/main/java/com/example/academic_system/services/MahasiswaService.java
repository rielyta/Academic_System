package com.example.academic_system.services;

import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.repositories.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MahasiswaService {

    private final MahasiswaRepository mahasiswaRepository;

    @Autowired
    public MahasiswaService(MahasiswaRepository mahasiswaRepository) {
        this.mahasiswaRepository = mahasiswaRepository;
    }

    public long count() {
        return mahasiswaRepository.count();
    }

    public List<Mahasiswa> getAllMahasiswa() {
        return mahasiswaRepository.findAll();
    }

    public Mahasiswa getMahasiswaById(Long id) {
        return mahasiswaRepository.findById(id).orElse(null);
    }

    public Mahasiswa getMahasiswaByNim(String nim) {
        return mahasiswaRepository.findByNim(nim).orElse(null);
    }

    public Mahasiswa createMahasiswa(Mahasiswa mahasiswa) {
        return mahasiswaRepository.save(mahasiswa);
    }

    public Mahasiswa updateMahasiswa(Long id, Mahasiswa mahasiswaDetails) {
        Mahasiswa mahasiswa = mahasiswaRepository.findById(id).orElse(null);
        if (mahasiswa != null) {
            mahasiswa.setNama(mahasiswaDetails.getNama());
            mahasiswa.setEmail(mahasiswaDetails.getEmail());
            mahasiswa.setFakultas(mahasiswaDetails.getFakultas());
            mahasiswa.setNim(mahasiswaDetails.getNim());
            return mahasiswaRepository.save(mahasiswa);
        }
        return null;
    }

    public void deleteMahasiswa(Long id) {
        mahasiswaRepository.deleteById(id);
    }

    public int countByDosenId(Long dosenId) {
        return mahasiswaRepository.countByKelasDosenId(dosenId);
    }
}
