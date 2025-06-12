package com.example.academic_system.controllers;

import com.example.academic_system.models.MataKuliah;
import com.example.academic_system.repositories.MataKuliahRepository;
import com.example.academic_system.services.ActivityLogService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminMataKuliahController {

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/admin/manajemen_matakuliah")
    public String listMatakuliah(Model model) {
        List<MataKuliah> mataKuliahList = mataKuliahRepository.findAll();
        for (MataKuliah mk : mataKuliahList) {
            if (mk.getKelasList() != null) {
                mk.setKelasList(mk.getKelasList());
            }
        }
        model.addAttribute("mataKuliahList", mataKuliahList);
        model.addAttribute("tipeMataKuliah", List.of("Wajib", "MKWU", "MKWK", "Peminatan"));
        model.addAttribute("mataKuliah", new MataKuliah());
        return "admin/manajemen_matakuliah";
    }

    @PostMapping("/admin/tambah_matakuliah")
    public String tambahMatakuliah(@ModelAttribute MataKuliah mataKuliahForm, RedirectAttributes redirectAttributes, Principal principal) {
        mataKuliahRepository.save(mataKuliahForm);

        String detail = String.format("NamaMK: %s, SKS: %d, Tipe: %s",
                mataKuliahForm.getNamaMK(), mataKuliahForm.getSks(), mataKuliahForm.getTipeMatakuliah());
        activityLogService.log("MataKuliah", mataKuliahForm.getKodeMK(), "CREATE", detail, principal.getName());

        redirectAttributes.addFlashAttribute("sukses", "Mata kuliah berhasil ditambahkan.");
        redirectAttributes.addFlashAttribute("namaMK", mataKuliahForm.getNamaMK());
        redirectAttributes.addFlashAttribute("sks", mataKuliahForm.getSks());
        redirectAttributes.addFlashAttribute("kodeMK", mataKuliahForm.getKodeMK());
        redirectAttributes.addFlashAttribute("tipeMataKuliah", mataKuliahForm.getTipeMatakuliah());
        return "redirect:/admin/manajemen_matakuliah";
    }

    @GetMapping("/admin/manajemen_matakuliah/{kodeMK}")
    @Transactional
    public String hapusMatakuliah(@PathVariable("kodeMK") String kodeMK, RedirectAttributes redirectAttributes, Principal principal) {
        mataKuliahRepository.findById(kodeMK).ifPresent(mk -> {
            mataKuliahRepository.delete(mk);
            String detail = String.format("NamaMK: %s, SKS: %d, Tipe: %s", mk.getNamaMK(), mk.getSks(), mk.getTipeMatakuliah());
            activityLogService.log("MataKuliah", kodeMK, "DELETE", detail, principal.getName());
        });

        redirectAttributes.addFlashAttribute("sukses", "Mata kuliah berhasil dihapus.");
        return "redirect:/admin/manajemen_matakuliah";
    }

    @GetMapping("/admin/manajemen_matakuliah/edit/{kodeMK}")
    @Transactional
    public String editMatakuliahForm(@PathVariable("kodeMK") String kodeMK, Model model) {
        MataKuliah mataKuliah = mataKuliahRepository.findById(kodeMK).orElse(null);
        if (mataKuliah == null) return "redirect:/admin/manajemen_matakuliah";

        model.addAttribute("mataKuliah", mataKuliah);
        model.addAttribute("editMode", true);
        model.addAttribute("mataKuliahList", mataKuliahRepository.findAll());
        model.addAttribute("tipeMataKuliah", List.of("Wajib", "MKWU", "MKWK", "Peminatan"));
        return "admin/manajemen_matakuliah";
    }

    @PostMapping("/admin/manajemen_matakuliah/edit/{kodeMK}")
    @Transactional
    public String editMatakuliah(@ModelAttribute MataKuliah mataKuliahForm, RedirectAttributes redirectAttributes, Principal principal) {
        MataKuliah existing = mataKuliahRepository.findById(mataKuliahForm.getKodeMK()).orElse(null);

        if (existing != null) {
            String oldDetail = String.format("Sebelum: NamaMK=%s, SKS=%d, Tipe=%s",
                    existing.getNamaMK(), existing.getSks(), existing.getTipeMatakuliah());

            existing.setNamaMK(mataKuliahForm.getNamaMK());
            existing.setSks(mataKuliahForm.getSks());
            existing.setKodeMK(mataKuliahForm.getKodeMK()); // optional, redundant
            existing.setTipeMatakuliah(mataKuliahForm.getTipeMatakuliah());

            mataKuliahRepository.save(existing);

            String newDetail = String.format("Sesudah: NamaMK=%s, SKS=%d, Tipe=%s",
                    existing.getNamaMK(), existing.getSks(), existing.getTipeMatakuliah());

            activityLogService.log("MataKuliah", existing.getKodeMK(), "UPDATE", oldDetail + " -> " + newDetail, principal.getName());

            redirectAttributes.addFlashAttribute("sukses", "Data mata kuliah berhasil diperbarui.");
        }

        return "redirect:/admin/manajemen_matakuliah";
    }
}
