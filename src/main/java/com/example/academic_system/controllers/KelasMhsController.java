package com.example.academic_system.controllers;

import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.services.KelasService;
import com.example.academic_system.services.MahasiswaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String daftarKelasMahasiswa(Model model,
                                       Principal principal,
                                       @RequestParam(required = false) String fakultas,
                                       @RequestParam(required = false) String tahunAjar,
                                       @RequestParam(required = false) String kode,
                                       @RequestParam(required = false) String namaKelas,
                                       @RequestParam(required = false) String semester) {
        try {
            String email = principal.getName();
            Optional<Mahasiswa> mahasiswaOpt = mahasiswaService.findByEmail(email);

            if (mahasiswaOpt.isPresent()) {
                Mahasiswa mahasiswa = mahasiswaOpt.get();

                List<Kelas> semuaKelas;
                if (hasAnyFilter(fakultas, tahunAjar, kode, namaKelas, semester)) {
                    semuaKelas = kelasService.findKelasWithFilters(fakultas, tahunAjar, kode, namaKelas, semester);
                } else {
                    semuaKelas = kelasService.getAllKelas();
                }

                List<Kelas> kelasSaya = kelasService.findByMahasiswa(mahasiswa);

                List<String> daftarFakultas = kelasService.findDistinctFakultas();
                List<String> daftarTahunAjaran = kelasService.findDistinctTahunAjar();

                model.addAttribute("semuaKelas", semuaKelas);
                model.addAttribute("kelasSaya", kelasSaya);
                model.addAttribute("mhs", mahasiswa);
                model.addAttribute("daftarFakultas", daftarFakultas);
                model.addAttribute("daftarTahunAjaran", daftarTahunAjaran);

                model.addAttribute("inputFakultas", fakultas);
                model.addAttribute("inputTahunAjar", tahunAjar);
                model.addAttribute("inputKode", kode);
                model.addAttribute("inputNamaKelas", namaKelas);
                model.addAttribute("inputSemester", semester);

                return "mahasiswa/daftarKelas";
            } else {
                return "redirect:/login?error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    private boolean hasAnyFilter(String... filters) {
        for (String filter : filters) {
            if (filter != null && !filter.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/mahasiswa/daftarKelas/{kelasId}")
    public String daftarKeKelas(@PathVariable Long kelasId,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        try {
            String email = principal.getName();
            Optional<Mahasiswa> mahasiswaOpt = mahasiswaService.findByEmail(email);
            Optional<Kelas> kelasOpt = kelasService.getKelasById(kelasId);

            if (mahasiswaOpt.isPresent() && kelasOpt.isPresent()) {
                Mahasiswa mahasiswa = mahasiswaOpt.get();
                Kelas kelas = kelasOpt.get();

                List<Kelas> kelasSaya = kelasService.findByMahasiswa(mahasiswa);
                if (kelasSaya.contains(kelas)) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Anda sudah terdaftar di kelas " + kelas.getNamaKelas());
                    return "redirect:/mahasiswa/daftarKelas";
                }
                boolean success = kelasService.daftarMahasiswaKeKelas(mahasiswa, kelas);

                if (success) {
                    redirectAttributes.addFlashAttribute("successMessage",
                            "Berhasil mendaftar ke kelas " + kelas.getNamaKelas());
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Gagal mendaftar ke kelas " + kelas.getNamaKelas());
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Kelas tidak ditemukan atau mahasiswa tidak valid");
            }

            return "redirect:/mahasiswa/daftarKelas";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Terjadi kesalahan saat mendaftar kelas");
            return "redirect:/mahasiswa/daftarKelas";
        }
    }

    @GetMapping("/mahasiswa/KelasSaya")
    public String kelasSayaMahasiswa(Model model, Principal principal) {
        try {
            String email = principal.getName();
            Optional<Mahasiswa> mahasiswaOpt = mahasiswaService.findByEmail(email);

            if (mahasiswaOpt.isPresent()) {
                Mahasiswa mahasiswa = mahasiswaOpt.get();

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
