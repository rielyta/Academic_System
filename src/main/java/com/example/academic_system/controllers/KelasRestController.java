package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.MataKuliah;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.KelasRepository;
import com.example.academic_system.repositories.MataKuliahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

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
        kelas.setKodeKelas(request.getKodeKelas());
        kelas.setNamaKelas(request.getNamaKelas());
        kelas.setFakultas(request.getFakultas()); // FIXED: Added fakultas
        kelas.setDosen(dosen);
        kelas.setMataKuliah(mk);
        kelas.setRuangan(request.getRuangan());
        kelas.setSemester(request.getSemester());
        kelas.setTahunAjar(request.getTahunAjar());
        kelas.setHariKelas(request.getHariKelas());
        kelas.setJamMulai(request.getJamMulai());
        kelas.setJamKeluar(request.getJamKeluar());

        return kelasRepository.save(kelas);
    }

    public static class KelasRequest {
        private String kodeKelas;
        private String namaKelas;
        private String fakultas; // FIXED: Added fakultas field
        private String mataKuliahId;
        private Long dosenId;
        private String ruangan;
        private Integer semester;
        private String tahunAjar;
        private DayOfWeek hariKelas;
        private LocalTime jamMulai;
        private LocalTime jamKeluar;


        public String getKodeKelas() { return kodeKelas; }
        public void setKodeKelas(String kodeKelas) { this.kodeKelas = kodeKelas; }

        public String getNamaKelas() { return namaKelas; }
        public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }

        public String getFakultas() { return fakultas; }
        public void setFakultas(String fakultas) { this.fakultas = fakultas; }

        public String getMataKuliahId() { return mataKuliahId; }
        public void setMataKuliahId(String mataKuliahId) { this.mataKuliahId = mataKuliahId; }

        public Long getDosenId() { return dosenId; }
        public void setDosenId(Long dosenId) { this.dosenId = dosenId; }

        public String getRuangan() { return ruangan; }
        public void setRuangan(String ruangan) { this.ruangan = ruangan; }

        public Integer getSemester() { return semester; }
        public void setSemester(Integer semester) { this.semester = semester; }

        public String getTahunAjar() { return tahunAjar; }
        public void setTahunAjar(String tahunAjar) { this.tahunAjar = tahunAjar; }

        public DayOfWeek getHariKelas() { return hariKelas; }
        public void setHariKelas(DayOfWeek hariKelas) { this.hariKelas = hariKelas; }

        public LocalTime getJamMulai() { return jamMulai; }
        public void setJamMulai(LocalTime jamMulai) { this.jamMulai = jamMulai; }

        public LocalTime getJamKeluar() { return jamKeluar; }
        public void setJamKeluar(LocalTime jamKeluar) { this.jamKeluar = jamKeluar; }
    }
}