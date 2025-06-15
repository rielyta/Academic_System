package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.services.DosenService;
import com.example.academic_system.services.KelasService;
import com.example.academic_system.services.MahasiswaService;
import com.example.academic_system.services.MataKuliahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/mahasiswa")
public class MahasiswaController {

    private final DosenService dosenService;
    private final KelasService kelasService;
    private final MahasiswaService mahasiswaService;

    @Autowired
    public MahasiswaController(DosenService dosenService,
                           KelasService kelasService,
                           MahasiswaService mahasiswaService) {
        this.dosenService = dosenService;
        this.kelasService = kelasService;
        this.mahasiswaService = mahasiswaService;
    }

    @GetMapping
    public ResponseEntity<List<Mahasiswa>> getAllMahasiswa() {
        List<Mahasiswa> mahasiswaList = mahasiswaService.findAll();
        return ResponseEntity.ok(mahasiswaList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mahasiswa> getMahasiswaById(@PathVariable Long id) {
        Optional<Mahasiswa> mahasiswa = mahasiswaService.findById(id);
        return mahasiswa.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<Map<String, Object>> getMahasiswaStatistics(@PathVariable Long id) {
        Optional<Mahasiswa> mahasiswa = mahasiswaService.findById(id);

        if (mahasiswa.isPresent()) {
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("mahasiswa", mahasiswa.get());

            return ResponseEntity.ok(statistics);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Mahasiswa> createMahasiswa(@RequestBody Mahasiswa mahasiswa) {
        Mahasiswa createdMahasiswa = mahasiswaService.createMahasiswa(mahasiswa);
        return ResponseEntity.ok(createdMahasiswa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mahasiswa> updateMahasiswa(@PathVariable Long id, @RequestBody Mahasiswa mahasiswaDetails) {
        Optional<Mahasiswa> existingMahasiswa = mahasiswaService.findById(id);

        if (existingMahasiswa.isPresent()) {
            Mahasiswa mahasiswa = existingMahasiswa.get();
            if (mahasiswaDetails.getNama() != null) {
                mahasiswa.setNama(mahasiswaDetails.getNama());
            }
            if (mahasiswaDetails.getNim() != null) {
                mahasiswa.setNim(mahasiswaDetails.getNim());
            }
            if (mahasiswaDetails.getEmail() != null) {
                mahasiswa.setEmail(mahasiswaDetails.getEmail());
            }
            Mahasiswa updatedMahasiswa = mahasiswaService.save(mahasiswa);
            return ResponseEntity.ok(updatedMahasiswa);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMahasiswa(@PathVariable Long id) {
        Optional<Mahasiswa> mahasiswa = mahasiswaService.findById(id);

        if (mahasiswa.isPresent()) {
            mahasiswaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}