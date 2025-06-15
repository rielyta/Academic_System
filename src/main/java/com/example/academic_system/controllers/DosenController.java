package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
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
@RequestMapping("/api/dosen")
public class DosenController {

    private final DosenService dosenService;
    private final KelasService kelasService;
    private final MahasiswaService mahasiswaService;
    private final MataKuliahService mataKuliahService;

    @Autowired
    public DosenController(DosenService dosenService,
                           KelasService kelasService,
                           MahasiswaService mahasiswaService,
                           MataKuliahService mataKuliahService) {
        this.dosenService = dosenService;
        this.kelasService = kelasService;
        this.mahasiswaService = mahasiswaService;
        this.mataKuliahService = mataKuliahService;
    }

    @GetMapping
    public ResponseEntity<List<Dosen>> getAllDosen() {
        List<Dosen> dosenList = dosenService.findAll();
        return ResponseEntity.ok(dosenList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dosen> getDosenById(@PathVariable Long id) {
        Optional<Dosen> dosen = dosenService.findById(id);
        return dosen.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<Map<String, Object>> getDosenStatistics(@PathVariable Long id) {
        Optional<Dosen> dosen = dosenService.findById(id);

        if (dosen.isPresent()) {
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("dosen", dosen.get());
            statistics.put("totalKelas", kelasService.countByDosenId(id));
            statistics.put("totalMahasiswa", mahasiswaService.countByDosenId(id));
            statistics.put("totalMataKuliah", mataKuliahService.countByDosenId(id));

            return ResponseEntity.ok(statistics);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Dosen> createDosen(@RequestBody Dosen dosen) {
        Dosen createdDosen = dosenService.createDosen(dosen);
        return ResponseEntity.ok(createdDosen);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dosen> updateDosen(@PathVariable Long id, @RequestBody Dosen dosenDetails) {
        Optional<Dosen> existingDosen = dosenService.findById(id);

        if (existingDosen.isPresent()) {
            Dosen dosen = existingDosen.get();
            // Update fields - adjust based on your Dosen model
            if (dosenDetails.getNama() != null) {
                dosen.setNama(dosenDetails.getNama());
            }
            if (dosenDetails.getNip() != null) {
                dosen.setNip(dosenDetails.getNip());
            }
            if (dosenDetails.getEmail() != null) {
                dosen.setEmail(dosenDetails.getEmail());
            }
            // Add other fields as needed

            Dosen updatedDosen = dosenService.save(dosen);
            return ResponseEntity.ok(updatedDosen);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDosen(@PathVariable Long id) {
        Optional<Dosen> dosen = dosenService.findById(id);

        if (dosen.isPresent()) {
            dosenService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}