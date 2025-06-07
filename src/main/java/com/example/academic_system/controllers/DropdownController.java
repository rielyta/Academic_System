package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.MataKuliah;
import com.example.academic_system.services.DosenService;
import com.example.academic_system.services.MataKuliahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DropdownController {

    private final DosenService dosenService;
    private final MataKuliahService mataKuliahService;

    @Autowired
    public DropdownController(DosenService dosenService, MataKuliahService mataKuliahService) {
        this.dosenService = dosenService;
        this.mataKuliahService = mataKuliahService;
    }

    // Endpoint untuk ambil semua dosen
    @GetMapping("/dosen")
    public List<Dosen> getAllDosens() {
        return dosenService.getAllDosen();
    }

    // Endpoint untuk ambil semua mata kuliah
    @GetMapping("/matakuliah")
    public List<MataKuliah> getAllMataKuliah() {
        return mataKuliahService.getAllMataKuliah();
    }
}
