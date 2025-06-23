package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.services.DosenService;
import com.example.academic_system.services.MahasiswaService;
import com.example.academic_system.services.KelasService;
import com.example.academic_system.services.MataKuliahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final MahasiswaService mahasiswaService;
    private final KelasService kelasService;
    private final DosenService dosenService;
    private final MataKuliahService mataKuliahService;

    @Autowired
    public DashboardController(MahasiswaService mahasiswaService, KelasService kelasService,
                               DosenService dosenService, MataKuliahService mataKuliahService) {
        this.mahasiswaService = mahasiswaService;
        this.kelasService = kelasService;
        this.dosenService = dosenService;
        this.mataKuliahService = mataKuliahService;
    }

    @GetMapping("/mahasiswa/dashboard_mahasiswa")
    public String mahasiswaDashboard(Model model, Principal principal) {
        String email = principal.getName();
        Optional<Mahasiswa> mahasiswaOpt = mahasiswaService.findByEmail(email);

        if (mahasiswaOpt.isPresent()) {
            Mahasiswa mahasiswa = mahasiswaOpt.get();
            model.addAttribute("mhs", mahasiswa);

            List<Kelas> kelasSaya = kelasService.findByMahasiswa(mahasiswa);

            Map<String, List<Kelas>> kelasPerHari = new HashMap<>();
            kelasPerHari.put("MONDAY", filterAndSortByDay(kelasSaya, DayOfWeek.MONDAY));
            kelasPerHari.put("TUESDAY", filterAndSortByDay(kelasSaya, DayOfWeek.TUESDAY));
            kelasPerHari.put("WEDNESDAY", filterAndSortByDay(kelasSaya, DayOfWeek.WEDNESDAY));
            kelasPerHari.put("THURSDAY", filterAndSortByDay(kelasSaya, DayOfWeek.THURSDAY));
            kelasPerHari.put("FRIDAY", filterAndSortByDay(kelasSaya, DayOfWeek.FRIDAY));
            kelasPerHari.put("SATURDAY", filterAndSortByDay(kelasSaya, DayOfWeek.SATURDAY));

            model.addAttribute("kelasPerHari", kelasPerHari);
            model.addAttribute("kelasSaya", kelasSaya);

            model.addAttribute("kelasSenin", kelasPerHari.get("MONDAY"));
            model.addAttribute("kelasSelasa", kelasPerHari.get("TUESDAY"));
            model.addAttribute("kelasRabu", kelasPerHari.get("WEDNESDAY"));
            model.addAttribute("kelasKamis", kelasPerHari.get("THURSDAY"));
            model.addAttribute("kelasJumat", kelasPerHari.get("FRIDAY"));
            model.addAttribute("kelasSabtu", kelasPerHari.get("SATURDAY"));

        } else {
            return "redirect:/login?error";
        }

        return "mahasiswa/dashboard_mahasiswa";
    }

    private List<Kelas> filterAndSortByDay(List<Kelas> kelasList, DayOfWeek day) {
        return kelasList.stream()
                .filter(k -> k.getHariKelas() == day)
                .sorted((a, b) -> a.getJamMulai().compareTo(b.getJamMulai()))
                .collect(Collectors.toList());
    }

    @GetMapping("/dosen/dashboard_dosen")
    public String dosenDashboard(Model model, Principal principal) {
        String email = principal.getName();
        Dosen dosen = dosenService.getDosenByEmail(email);

        if (dosen == null) {
            return "redirect:/login?error";
        }

        model.addAttribute("dosen", dosen);
        List<Kelas> kelasList = kelasService.findByDosen(dosen);
        model.addAttribute("kelasList", kelasList);
        model.addAttribute("totalKelas", kelasService.countByDosenId(dosen.getId()));
        model.addAttribute("totalMahasiswa", mahasiswaService.countByDosenId(dosen.getId()));
        model.addAttribute("totalMataKuliah", mataKuliahService.countByDosenId(dosen.getId()));

        return "dosen/dashboard_dosen";
    }
}