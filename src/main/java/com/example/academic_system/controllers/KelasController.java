package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.MataKuliah;
import com.example.academic_system.payload.KelasRequest;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.KelasRepository;
import com.example.academic_system.repositories.MataKuliahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/api/admin/kelas")
public class KelasController {

    @Autowired
    private KelasRepository kelasRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @PostMapping
    public ResponseEntity<?> createKelas(@RequestBody KelasRequest request) {
        try {
            if (kelasRepository.existsByNamaKelasAndMataKuliah_KodeMK(
                    request.getNamaKelas(), request.getMataKuliahId())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Kelas dengan nama tersebut sudah ada untuk mata kuliah ini"));
            }

            MataKuliah mataKuliah = mataKuliahRepository.findById(request.getMataKuliahId())
                    .orElseThrow(() -> new RuntimeException("Mata kuliah tidak ditemukan"));
            Dosen dosen = dosenRepository.findById(request.getDosenId())
                    .orElseThrow(() -> new RuntimeException("Dosen tidak ditemukan"));

            Kelas kelas = new Kelas();
            kelas.setNamaKelas(request.getNamaKelas());
            kelas.setMataKuliah(mataKuliah);
            kelas.setDosen(dosen);

            Kelas saved = kelasRepository.save(kelas);
            return ResponseEntity.ok(Map.of("message", "Kelas berhasil ditambahkan", "kelas", saved));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error: " + e.getMessage()));
        }
    }

}