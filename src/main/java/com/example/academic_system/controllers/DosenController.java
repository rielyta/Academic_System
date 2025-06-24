package com.example.academic_system.controllers;

import com.example.academic_system.models.Dosen;
import com.example.academic_system.models.Kelas;
import com.example.academic_system.models.Mahasiswa;
import com.example.academic_system.services.DosenService;
import com.example.academic_system.services.KelasService;
import com.example.academic_system.services.MahasiswaService;
import com.example.academic_system.services.MataKuliahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/dosen")
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

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Dosen>> getAllDosen() {
        List<Dosen> dosenList = dosenService.findAll();
        return ResponseEntity.ok(dosenList);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Dosen> getDosenById(@PathVariable Long id) {
        Optional<Dosen> dosen = dosenService.findById(id);
        return dosen.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/{id}/statistics")
    @ResponseBody
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

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Dosen> createDosen(@RequestBody Dosen dosen) {
        Dosen createdDosen = dosenService.createDosen(dosen);
        return ResponseEntity.ok(createdDosen);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Dosen> updateDosen(@PathVariable Long id, @RequestBody Dosen dosenDetails) {
        Optional<Dosen> existingDosen = dosenService.findById(id);

        if (existingDosen.isPresent()) {
            Dosen dosen = existingDosen.get();
            if (dosenDetails.getNama() != null) {
                dosen.setNama(dosenDetails.getNama());
            }
            if (dosenDetails.getNip() != null) {
                dosen.setNip(dosenDetails.getNip());
            }
            if (dosenDetails.getEmail() != null) {
                dosen.setEmail(dosenDetails.getEmail());
            }

            Dosen updatedDosen = dosenService.save(dosen);
            return ResponseEntity.ok(updatedDosen);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteDosen(@PathVariable Long id) {
        Optional<Dosen> dosen = dosenService.findById(id);

        if (dosen.isPresent()) {
            dosenService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/profil")
    public String profilDosen(Model model, Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);
            model.addAttribute("dosen", dosen);
        }
        return "profil-dosen"; // nama file HTML template
    }

    @GetMapping("/kelas_dosen")
    public String lihatKelasDosen(Model model, Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);

            model.addAttribute("dosen", dosen);
            model.addAttribute("kelasList", kelasService.findByDosenId(dosen.getId()));
        }

        return "dosen/kelas_dosen";
    }

    @GetMapping("/jadwal_mengajar_dosen")
    public String lihatJadwal(Model model, Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);

            List<Kelas> kelasList = kelasService.findByDosenId(dosen.getId());
            model.addAttribute("kelasList", kelasList);
        }
        return "dosen/jadwal_mengajar_dosen";
    }

    @GetMapping("/mahasiswa_dosen")
    public String lihatMahasiswaDosen(Model model, Authentication auth) {
        String email = auth.getName();
        Dosen dosen = dosenService.getDosenByEmail(email);

        List<Mahasiswa> mahasiswaList = mahasiswaService.findAllByDosen(dosen);
        model.addAttribute("mahasiswaList", mahasiswaList);

        return "dosen/mahasiswa_dosen";
    }

    @GetMapping("/cari_kelas")
    public String cariKelas(Model model, Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);

            // Ambil kelas yang belum memiliki dosen atau bisa diambil
            List<Kelas> kelasAvailable = kelasService.findAvailableKelasForDosen(dosen);

            model.addAttribute("dosen", dosen);
            model.addAttribute("kelasAvailable", kelasAvailable);
            model.addAttribute("daftarFakultas", kelasService.findDistinctFakultas());
            model.addAttribute("daftarTahunAjar", kelasService.findDistinctTahunAjar());
        }
        return "dosen/cari_kelas";
    }

    @GetMapping("/cari_kelas/api")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getKelasAvailableApi(
            @RequestParam(required = false) String fakultas,
            @RequestParam(required = false) String tahunAjar,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String namaKelas,
            @RequestParam(required = false) String _t, // timestamp parameter untuk prevent caching
            Authentication authentication) {

        try {
            if (authentication == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", "Authentication required");
                return ResponseEntity.status(401).body(errorResponse);
            }

            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);

            List<Kelas> kelasAvailable;
            if (fakultas != null || tahunAjar != null || semester != null || namaKelas != null) {
                kelasAvailable = kelasService.findAvailableKelasWithFilters(
                        dosen, fakultas, tahunAjar, semester, namaKelas);
            } else {
                kelasAvailable = kelasService.findAvailableKelasForDosen(dosen);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("kelasAvailable", kelasAvailable);
            response.put("timestamp", System.currentTimeMillis());
            response.put("totalKelas", kelasAvailable.size());

            response.put("dosenId", dosen.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Gagal memuat data: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/enroll_kelas/{kelasId}")
    public String enrollKeKelas(@PathVariable Long kelasId,
                                RedirectAttributes redirectAttributes,
                                Authentication authentication) {
        try {
            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);

            boolean success = kelasService.enrollDosenToKelas(dosen, kelasId);

            if (success) {
                redirectAttributes.addFlashAttribute("sukses", "Berhasil mengambil kelas!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Gagal mengambil kelas. Kelas mungkin sudah memiliki dosen.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan: " + e.getMessage());
        }

        return "redirect:/dosen/cari_kelas";
    }

    @PostMapping("/enroll_kelas/{kelasId}/api")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> enrollKeKelasApi(@PathVariable Long kelasId,
                                                                Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("error", "Authentication required");
                return ResponseEntity.status(401).body(response);
            }

            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);

            boolean success = kelasService.enrollDosenToKelas(dosen, kelasId);

            if (success) {
                response.put("success", true);
                response.put("message", "Berhasil mengambil kelas!");
            } else {
                response.put("success", false);
                response.put("error", "Gagal mengambil kelas. Kelas mungkin sudah memiliki dosen.");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Terjadi kesalahan: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/keluar_kelas/{kelasId}")
    public String keluarDariKelas(@PathVariable Long kelasId,
                                  RedirectAttributes redirectAttributes,
                                  Authentication authentication) {
        try {
            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);

            boolean success = kelasService.removeDosenFromKelas(dosen, kelasId);

            if (success) {
                redirectAttributes.addFlashAttribute("sukses", "Berhasil keluar dari kelas!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Gagal keluar dari kelas.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan: " + e.getMessage());
        }

        return "redirect:/dosen/kelas_dosen";
    }

    @PostMapping("/keluar_kelas/{kelasId}/api")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> keluarDariKelasApi(@PathVariable Long kelasId,
                                                                  Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("error", "Authentication required");
                return ResponseEntity.status(401).body(response);
            }

            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);

            boolean success = kelasService.removeDosenFromKelas(dosen, kelasId);

            if (success) {
                response.put("success", true);
                response.put("message", "Berhasil keluar dari kelas!");
            } else {
                response.put("success", false);
                response.put("error", "Gagal keluar dari kelas.");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Terjadi kesalahan: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cari_kelas/filter")
    public String filterKelasAvailable(@RequestParam(required = false) String fakultas,
                                       @RequestParam(required = false) String tahunAjar,
                                       @RequestParam(required = false) String semester,
                                       @RequestParam(required = false) String namaKelas,
                                       Model model,
                                       Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);

            List<Kelas> kelasAvailable = kelasService.findAvailableKelasWithFilters(
                    dosen, fakultas, tahunAjar, semester, namaKelas);

            model.addAttribute("dosen", dosen);
            model.addAttribute("kelasAvailable", kelasAvailable);
            model.addAttribute("daftarFakultas", kelasService.findDistinctFakultas());
            model.addAttribute("daftarTahunAjar", kelasService.findDistinctTahunAjar());

            // Preserve filter values
            model.addAttribute("selectedFakultas", fakultas);
            model.addAttribute("selectedTahunAjar", tahunAjar);
            model.addAttribute("selectedSemester", semester);
            model.addAttribute("selectedNamaKelas", namaKelas);
        }
        return "dosen/cari_kelas";
    }

    @GetMapping("/dashboard/stats/api")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDashboardStats(Authentication authentication) {
        Map<String, Object> stats = new HashMap<>();

        try {
            if (authentication == null) {
                stats.put("success", false);
                stats.put("error", "Authentication required");
                return ResponseEntity.status(401).body(stats);
            }

            String email = authentication.getName();
            Dosen dosen = dosenService.getDosenByEmail(email);

            List<Kelas> myKelas = kelasService.findByDosenId(dosen.getId());
            List<Kelas> availableKelas = kelasService.findAvailableKelasForDosen(dosen);

            stats.put("success", true);
            stats.put("totalKelasAmpu", myKelas.size());
            stats.put("totalKelasAvailable", availableKelas.size());
            stats.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            stats.put("success", false);
            stats.put("error", e.getMessage());
            return ResponseEntity.status(500).body(stats);
        }
    }
}