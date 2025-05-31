package com.example.academic_system.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "kelas")
public class Kelas {
    @Id
    @Column(name = "kode_kelas", unique = true, nullable = false)
    private String kodeKelas;

    @ManyToOne
    @JoinColumn(name = "kode_mk", nullable = false)
    private MataKuliah mataKuliah;

    @ManyToOne
    @JoinColumn(name = "dosen_id", nullable = false)
    private Dosen dosenPengampu;

    @ManyToMany
    @JoinTable(
            name = "kelas_mahasiswa",
            joinColumns = @JoinColumn(name = "kode_kelas"),
            inverseJoinColumns = @JoinColumn(name = "mahasiswa_id")
    )
    private List<Mahasiswa> daftarMahasiswa;

    public Kelas() {}

    public Kelas(String kodeKelas, MataKuliah mataKuliah, Dosen dosenPengampu) {
        this.kodeKelas = kodeKelas;
        this.mataKuliah = mataKuliah;
        this.dosenPengampu = dosenPengampu;
    }

    public String getKodeKelas() {
        return kodeKelas;
    }

    public void setKodeKelas(String kodeKelas) {
        this.kodeKelas = kodeKelas;
    }

    public MataKuliah getMataKuliah() {
        return mataKuliah;
    }

    public void setMataKuliah(MataKuliah mataKuliah) {
        this.mataKuliah = mataKuliah;
    }

    public Dosen getDosenPengampu() {
        return dosenPengampu;
    }

    public void setDosenPengampu(Dosen dosenPengampu) {
        this.dosenPengampu = dosenPengampu;
    }

    public List<Mahasiswa> getDaftarMahasiswa() {
        return daftarMahasiswa;
    }

    public void setDaftarMahasiswa(List<Mahasiswa> daftarMahasiswa) {
        this.daftarMahasiswa = daftarMahasiswa;
    }
}