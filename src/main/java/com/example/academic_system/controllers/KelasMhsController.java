package com.example.academic_system.controllers;

import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.services.KelasService;
import com.example.academic_system.services.MahasiswaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class KelasMhsController {

    @Autowired
    private KelasService kelasService;

    @Autowired
    private MahasiswaService mahasiswaService;

    @GetMapping("/mahasiswa/daftarKelas")
    public String daftarKelas(Model model, Principal principal) {
        try {
            String email = principal.getName();
            Optional<Mahasiswa> mahasiswaOpt = mahasiswaService.findByEmail(email);

            if (mahasiswaOpt.isPresent()) {
                Mahasiswa mahasiswa = mahasiswaOpt.get();

                List<Kelas> semuaKelas = kelasService.getAllKelas();

                // Ambil kelas yang sudah diikuti mahasiswa (untuk pengecekan)
                List<Kelas> kelasSaya = kelasService.findByMahasiswa(mahasiswa);

                model.addAttribute("semuaKelas", semuaKelas);
                model.addAttribute("kelasSaya", kelasSaya);
                model.addAttribute("mhs", mahasiswa);

                return "mahasiswa/daftarKelas";
            } else {
                return "redirect:/login?error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @GetMapping("/mahasiswa/KelasSaya")
    public String KelasSaya(Model model, Principal principal) {
        try {
            String email = principal.getName();
            Optional<Mahasiswa> mahasiswaOpt = mahasiswaService.findByEmail(email);

            if (mahasiswaOpt.isPresent()) {
                Mahasiswa mahasiswa = mahasiswaOpt.get();

                // Ambil kelas yang diikuti mahasiswa
                List<Kelas> kelasSaya = kelasService.findByMahasiswa(mahasiswa);

                model.addAttribute("kelasSaya", kelasSaya);
                model.addAttribute("mhs", mahasiswa);

                return "mahasiswa/KelasSaya";
            } else {
                return "redirect:/login?error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
}
