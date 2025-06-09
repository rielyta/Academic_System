package com.example.academic_system.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Kelas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String namaKelas;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kode_mk", nullable = false)
    private MataKuliah mataKuliah;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dosen_id", nullable = false)
    private Dosen dosen;

    @ManyToMany
    @JoinTable(
            name = "kelas_mahasiswa",
            joinColumns = @JoinColumn(name = "kelas_id"),
            inverseJoinColumns = @JoinColumn(name = "mahasiswa_id")
    )
    private List<Mahasiswa> mahasiswaTerdaftar;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNamaKelas() { return namaKelas; }
    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public MataKuliah getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(MataKuliah mataKuliah) {
        this.mataKuliah = mataKuliah;
    }

    public Dosen getDosen() { return dosen; }
    public void setDosen(Dosen dosen) { this.dosen = dosen; }

    public List<Mahasiswa> getMahasiswaTerdaftar() {
        return mahasiswaTerdaftar;
    }
    public void setMahasiswaTerdaftar(List<Mahasiswa> mahasiswaTerdaftar) {
        this.mahasiswaTerdaftar = mahasiswaTerdaftar;
    }
}