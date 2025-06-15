package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.MataKuliah;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.KelasRepository;
import com.example.academic_system.repositories.MataKuliahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kelas")
public class KelasRestController {

    @Autowired
    private KelasRepository kelasRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @PostMapping
    public Kelas tambahKelas(@RequestBody KelasRequest request) {
        Dosen dosen = dosenRepository.findById(request.getDosenId()).orElse(null);
        MataKuliah mk = mataKuliahRepository.findByKodeMK(request.getMataKuliahId());

        if (dosen == null || mk == null) {
            throw new RuntimeException("Dosen atau Mata Kuliah tidak ditemukan");
        }

        Kelas kelas = new Kelas();
        kelas.setNamaKelas(request.getNamaKelas());
        kelas.setDosen(dosen);
        kelas.setMataKuliah(mk);

        return kelasRepository.save(kelas);
    }

    public static class KelasRequest {
        private String namaKelas;
        private String mataKuliahId;
        private Long dosenId;

        public String getNamaKelas() { return namaKelas; }
        public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }

        public String getMataKuliahId() { return mataKuliahId; }
        public void setMataKuliahId(String mataKuliahId) { this.mataKuliahId = mataKuliahId; }

        public Long getDosenId() { return dosenId; }
        public void setDosenId(Long dosenId) { this.dosenId = dosenId; }
    }

}
