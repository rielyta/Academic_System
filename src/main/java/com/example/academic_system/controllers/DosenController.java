package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.services.DosenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class DosenController {

    private final DosenService dosenService;

    @Autowired
    public DosenController(DosenService dosenService) {
        this.dosenService = dosenService;
    }

    @GetMapping
    public List<Dosen> getAllDosen() {
        return dosenService.getAllDosen();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dosen> getDosenById(@PathVariable Long id) {
        Dosen dosen = dosenService.getDosenById(id);
        return dosen != null ? ResponseEntity.ok(dosen) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Dosen createDosen(@RequestBody Dosen dosen) {
        return dosenService.createDosen(dosen);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dosen> updateDosen(@PathVariable Long id, @RequestBody Dosen dosenDetails) {
        Dosen updatedDosen = dosenService.updateDosen(id, dosenDetails);
        return updatedDosen != null ? ResponseEntity.ok(updatedDosen) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDosen(@PathVariable Long id) {
        dosenService.deleteDosen(id);
        return ResponseEntity.noContent().build();
    }
}
