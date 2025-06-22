package com.example.academic_system.services;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.KelasRepository;
import com.example.academic_system.repositories.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MahasiswaService {

    private final KelasRepository kelasRepository;

    private final MahasiswaRepository mahasiswaRepository;

    @Autowired
    public MahasiswaService(MahasiswaRepository mahasiswaRepository, KelasRepository kelasRepository) {
        this.mahasiswaRepository = mahasiswaRepository;
        this.kelasRepository = kelasRepository;
    }

    public List<Mahasiswa> findAll() {
        return mahasiswaRepository.findAll();
    }

    public Optional<Mahasiswa> findById(Long id) {
        return mahasiswaRepository.findById(id);
    }

    public Optional<Mahasiswa> findByEmail(String email) {
        return mahasiswaRepository.findByEmail(email);
    }

    public Mahasiswa createMahasiswa(Mahasiswa mahasiswa) {
        return mahasiswaRepository.save(mahasiswa);
    }

    public Mahasiswa save(Mahasiswa mahasiswa) {
        return mahasiswaRepository.save(mahasiswa);
    }

    public void deleteById(Long id) {
        mahasiswaRepository.deleteById(id);
    }

    public long count() {
        return mahasiswaRepository.count();
    }

    public long countByDosenId(Long dosenId) {
        return mahasiswaRepository.countByDosenId(dosenId);
    }

    public List<Mahasiswa> findAllByDosen(Dosen dosen) {
        List<Kelas> kelasList = kelasRepository.findByDosen(dosen);
        System.out.println("Jumlah kelas ditemukan: " + kelasList.size());

        return kelasList.stream()
                .flatMap(k -> k.getMahasiswaTerdaftar().stream())
                .distinct()
                .peek(m -> System.out.println("Mahasiswa: " + m.getNama()))
                .collect(Collectors.toList());
    }

}
