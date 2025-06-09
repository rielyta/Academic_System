package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.MataKuliah;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.MataKuliahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DropdownController {

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @GetMapping("/dosen")
    public List<Dosen> getAllDosen() {
        return dosenRepository.findAll();
    }

    @GetMapping("/matakuliah")
    public List<MataKuliah> getAllMataKuliah() {
        return mataKuliahRepository.findAll();
    }
}
