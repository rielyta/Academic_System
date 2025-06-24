package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.MataKuliah;
import com.example.academic_system.repositories.DosenRepository;
import com.example.academic_system.repositories.KelasRepository;
import com.example.academic_system.repositories.MataKuliahRepository;
import com.example.academic_system.services.ActivityLogService;
import com.example.academic_system.services.KelasService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminKelasController {

    @Autowired
    private KelasRepository kelasRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private KelasService kelasService;

    private void injectDropdowns(Model model) {
        model.addAttribute("daftarSemester", List.of("1", "2", "3", "4", "5", "6", "7", "8"));
        model.addAttribute("daftarTahunAjar", List.of("2023/2024", "2024/2025", "2025/2026"));
        model.addAttribute("daftarRuangan", List.of("A101", "A102", "B201", "B202", "C301", "C302"));
        model.addAttribute("daftarDosen", dosenRepository.findAll());
        model.addAttribute("daftarMataKuliah", mataKuliahRepository.findAll());

        model.addAttribute("daftarFakultas", List.of(
                "Fasilkom-TI",
                "FH",
                "FEB",
                "FK"
        ));

        Map<String, String> hariMap = new LinkedHashMap<>();
        hariMap.put("MONDAY", "Senin");
        hariMap.put("TUESDAY", "Selasa");
        hariMap.put("WEDNESDAY", "Rabu");
        hariMap.put("THURSDAY", "Kamis");
        hariMap.put("FRIDAY", "Jumat");
        model.addAttribute("hariMap", hariMap);
    }

    @GetMapping("/admin/manajemen_kelas")
    public String listKelas(Model model) {
        try {
            List<Kelas> kelasList = kelasService.getAllKelasWithMahasiswa();

            System.out.println("=== Admin: Loading manajemen kelas ===");
            for (Kelas kelas : kelasList) {
                System.out.println("Kelas: " + kelas.getNamaKelas() +
                        " - Jumlah Mahasiswa: " + kelas.getJumlahMahasiswa() +
                        " - Dosen: " + (kelas.getDosen() != null ? kelas.getDosen().getNama() : "Belum ada dosen") +
                        " - MataKuliah: " + (kelas.getMataKuliah() != null ? kelas.getMataKuliah().getNamaMK() : "Tidak ada"));
            }

            model.addAttribute("kelasList", kelasList);
            model.addAttribute("kelas", new Kelas());
            injectDropdowns(model);
            return "admin/manajemen_kelas";
        } catch (Exception e) {
            System.out.println("Error in listKelas: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Terjadi kesalahan saat memuat data kelas: " + e.getMessage());
            model.addAttribute("kelasList", List.of());
            model.addAttribute("kelas", new Kelas());
            injectDropdowns(model);
            return "admin/manajemen_kelas";
        }
    }

    @PostMapping("/admin/tambah_kelas")
    public String tambahKelas(@ModelAttribute Kelas kelasForm,
                              @RequestParam(name = "mataKuliah", required = false) String kodeMK,
                              @RequestParam(name = "dosen.id", required = false) Long dosenId,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        try {
            System.out.println("=== Processing tambah kelas ===");
            System.out.println("Received kodeMK: " + kodeMK);
            System.out.println("KelasForm mataKuliah: " + kelasForm.getMataKuliah());

            if (kodeMK == null || kodeMK.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Mata kuliah harus dipilih.");
                return "redirect:/admin/manajemen_kelas";
            }

            MataKuliah mataKuliah = mataKuliahRepository.findById(kodeMK).orElse(null);
            if (mataKuliah == null) {
                redirectAttributes.addFlashAttribute("error", "Mata kuliah dengan kode " + kodeMK + " tidak ditemukan.");
                return "redirect:/admin/manajemen_kelas";
            }

            kelasForm.setMataKuliah(mataKuliah);

            if (dosenId != null) {
                Dosen dosen = dosenRepository.findById(dosenId)
                        .orElseThrow(() -> new IllegalArgumentException("Dosen tidak ditemukan"));
                kelasForm.setDosen(dosen);
            } else {
                kelasForm.setDosen(null);
            }


            if (kelasForm.getMataKuliah() == null) {
                redirectAttributes.addFlashAttribute("error", "Mata kuliah harus dipilih.");
                return "redirect:/admin/manajemen_kelas";
            }

            if (kelasForm.getKodeKelas() == null || kelasForm.getKodeKelas().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Kode kelas harus diisi.");
                return "redirect:/admin/manajemen_kelas";
            }

            if (kelasForm.getNamaKelas() == null || kelasForm.getNamaKelas().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Nama kelas harus diisi.");
                return "redirect:/admin/manajemen_kelas";
            }

            if (kelasForm.getFakultas() == null || kelasForm.getFakultas().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Fakultas harus dipilih.");
                return "redirect:/admin/manajemen_kelas";
            }

            if (kelasForm.getDosen() != null && kelasForm.getDosen().getId() != null) {
                Dosen dosen = dosenRepository.findById(kelasForm.getDosen().getId()).orElse(null);
                kelasForm.setDosen(dosen);
            }


            Kelas savedKelas = kelasRepository.save(kelasForm);

            String dosenNama = kelasForm.getDosen() != null ? kelasForm.getDosen().getNama() : "Belum ada dosen";
            String detail = String.format("Tambah: %s (%s) - %s oleh %s, Sem %s, TA %s",
                    kelasForm.getNamaKelas(),
                    kelasForm.getKodeKelas(),
                    kelasForm.getMataKuliah().getNamaMK(),
                    dosenNama,
                    kelasForm.getSemester() != null ? kelasForm.getSemester() : "N/A",
                    kelasForm.getTahunAjar() != null ? kelasForm.getTahunAjar() : "N/A");

            activityLogService.log("Kelas", String.valueOf(savedKelas.getId()), "CREATE", detail, principal.getName());

            redirectAttributes.addFlashAttribute("sukses", "Kelas berhasil ditambahkan.");

        } catch (Exception e) {
            System.out.println("Error adding kelas: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Gagal menambah kelas: " + e.getMessage());
        }

        return "redirect:/admin/manajemen_kelas";
    }

    @GetMapping("/admin/manajemen_kelas/{id}")
    @Transactional
    public String hapusKelas(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            kelasRepository.findById(id).ifPresent(kelas -> {
                String dosenNama = kelas.getDosen() != null ? kelas.getDosen().getNama() : "Belum ada dosen";
                String mataKuliahNama = kelas.getMataKuliah() != null ? kelas.getMataKuliah().getNamaMK() : "Mata kuliah tidak ada";
                String detail = String.format("Hapus: %s (%s) - %s oleh %s",
                        kelas.getNamaKelas(),
                        kelas.getKodeKelas(),
                        mataKuliahNama,
                        dosenNama);

                kelasRepository.delete(kelas);
                activityLogService.log("Kelas", String.valueOf(kelas.getId()), "DELETE", detail, principal.getName());
            });

            redirectAttributes.addFlashAttribute("sukses", "Kelas berhasil dihapus.");
        } catch (Exception e) {
            System.out.println("Error deleting kelas: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus kelas: " + e.getMessage());
        }
        return "redirect:/admin/manajemen_kelas";
    }

    @GetMapping("/admin/manajemen_kelas/edit/{id}")
    @Transactional
    public String editKelasForm(@PathVariable("id") Long id, Model model) {
        try {
            Kelas kelas = kelasRepository.findById(id).orElse(null);
            if (kelas == null) {
                model.addAttribute("error", "Kelas tidak ditemukan.");
                return "redirect:/admin/manajemen_kelas";
            }

            model.addAttribute("kelas", kelas);
            model.addAttribute("editMode", true);
            model.addAttribute("kelasList", kelasRepository.findAll());
            injectDropdowns(model);
            return "admin/manajemen_kelas";
        } catch (Exception e) {
            System.out.println("Error in editKelasForm: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Terjadi kesalahan: " + e.getMessage());
            return "redirect:/admin/manajemen_kelas";
        }
    }

    @PostMapping("/admin/manajemen_kelas/edit/{id}")
    @Transactional
    public String editKelas(@ModelAttribute Kelas kelasForm,
                            @RequestParam(name = "mataKuliah", required = false) String kodeMK,
                            @RequestParam(name = "dosen.id", required = false) Long dosenId,
                            RedirectAttributes redirectAttributes,
                            Principal principal) {
        try {
            Kelas existing = kelasRepository.findById(kelasForm.getId()).orElse(null);
            if (existing != null) {
                if (kodeMK != null && !kodeMK.trim().isEmpty()) {
                    MataKuliah mataKuliah = mataKuliahRepository.findById(kodeMK).orElse(null);
                    if (mataKuliah == null) {
                        redirectAttributes.addFlashAttribute("error", "Mata kuliah dengan kode " + kodeMK + " tidak ditemukan.");
                        return "redirect:/admin/manajemen_kelas";
                    }
                    kelasForm.setMataKuliah(mataKuliah);
                }

                String dosenNama = kelasForm.getDosen() != null ? kelasForm.getDosen().getNama() : "Belum ada dosen";
                String mataKuliahNama = kelasForm.getMataKuliah() != null ? kelasForm.getMataKuliah().getNamaMK() : "Mata kuliah tidak ada";
                String detail = String.format("Edit: %s (%s) → MK: %s, Dosen: %s, Ruang: %s",
                        existing.getNamaKelas(),
                        existing.getKodeKelas(),
                        mataKuliahNama,
                        dosenNama,
                        kelasForm.getRuangan());

                if (dosenId != null) {
                    Dosen dosen = dosenRepository.findById(dosenId)
                            .orElseThrow(() -> new IllegalArgumentException("Dosen tidak ditemukan"));
                    existing.setDosen(dosen);
                } else {
                    existing.setDosen(null);
                }

                existing.setKodeKelas(kelasForm.getKodeKelas());
                existing.setNamaKelas(kelasForm.getNamaKelas());
                existing.setFakultas(kelasForm.getFakultas());
                existing.setMataKuliah(kelasForm.getMataKuliah());
                existing.setDosen(kelasForm.getDosen());
                existing.setRuangan(kelasForm.getRuangan());
                existing.setSemester(kelasForm.getSemester());
                existing.setTahunAjar(kelasForm.getTahunAjar());
                existing.setHariKelas(kelasForm.getHariKelas());
                existing.setJamMulai(kelasForm.getJamMulai());
                existing.setJamKeluar(kelasForm.getJamKeluar());

                if (kelasForm.getDosen() != null && kelasForm.getDosen().getId() != null) {
                    Dosen dosen = dosenRepository.findById(kelasForm.getDosen().getId()).orElse(null);
                    existing.setDosen(dosen);
                } else {
                    existing.setDosen(null);
                }


                kelasRepository.save(existing);
                activityLogService.log("Kelas", String.valueOf(existing.getId()), "UPDATE", detail, principal.getName());

                redirectAttributes.addFlashAttribute("sukses", "Data kelas berhasil diperbarui.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Kelas tidak ditemukan.");
            }
        } catch (Exception e) {
            System.out.println("Error updating kelas: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Gagal memperbarui kelas: " + e.getMessage());
        }
        return "redirect:/admin/manajemen_kelas";
    }
}