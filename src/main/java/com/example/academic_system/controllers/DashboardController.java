package com.example.academic_system.controllers;

import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.services.MahasiswaService;
import com.example.academic_system.services.KelasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.academic_system.models.Dosen;
import com.example.academic_system.services.DosenService;
import com.example.academic_system.services.MataKuliahService;
import com.example.academic_system.models.Dosen;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    private final MahasiswaService mahasiswaService;
    private KelasService kelasService;
    private final DosenService dosenService;
    private final MataKuliahService mataKuliahService;

    @Autowired
    public DashboardController(MahasiswaService mahasiswaService,
                               KelasService kelasService,
                               DosenService dosenService,
                               MataKuliahService mataKuliahService) {
        this.mahasiswaService = mahasiswaService;
        this.kelasService = kelasService;
        this.dosenService = dosenService;
        this.mataKuliahService = mataKuliahService;
    }


    @GetMapping("/mahasiswa/dashboard_mahasiswa")
    public String mahasiswaDashboard(Model model, Principal principal) {
        String email = principal.getName(); // dapatkan email login
        Optional<Mahasiswa> mahasiswaOpt = mahasiswaService.findByEmail(email);

        if (mahasiswaOpt.isPresent()) {
            Mahasiswa mahasiswa = mahasiswaOpt.get();
            model.addAttribute("mhs", mahasiswa); // lempar ke Thymeleaf
        } else {
            return "redirect:/login?error";
        }

        return "mahasiswa/dashboard_mahasiswa";
    }

    @GetMapping("/dosen/dashboard_dosen")
    public String dosenDashboard(Model model, Principal principal) {
        String email = principal.getName();
        Dosen dosen = dosenService.getDosenByEmail(email);

        if (dosen == null) {
            return "redirect:/login?error"; // atau tampilkan pesan error
        }

        model.addAttribute("dosen", dosen);
        model.addAttribute("totalKelas", kelasService.countByDosenId(dosen.getId()));
        model.addAttribute("totalMahasiswa", mahasiswaService.countByDosenId(dosen.getId()));
        model.addAttribute("totalMataKuliah", mataKuliahService.countByDosenId(dosen.getId()));

        List<Kelas> kelasList = kelasService.findByDosenId(dosen.getId()); // atau .findByDosen(dosen)
        model.addAttribute("dosen", dosen);
        model.addAttribute("kelasList", kelasList);

        return "dosen/dashboard_dosen";
    }
}
