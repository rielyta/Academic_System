package com.example.academic_system.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mata_kuliah")
public class MataKuliah {
    @Id
    @Column(name = "kode_mk", unique = true, nullable = false)
    private String kodeMK;

    @Column(name ="namamk", unique = true, nullable = false)
    private String namaMK;

    @Column(nullable = false)
    private int sks;

    @OneToMany(mappedBy = "mataKuliah", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Kelas> daftarKelas = new ArrayList<>();

    public MataKuliah() {}

    public MataKuliah(String kodeMK, String namaMK, int sks) {
        this.kodeMK = kodeMK;
        this.namaMK = namaMK;
        this.sks = sks;
    }

    public String getKodeMK() {
        return kodeMK;
    }

    public void setKodeMK(String kodeMk) {
        this.kodeMK = kodeMk;
    }

    public String getNamaMK() {
        return namaMK;
    }

    public void setNamaMK(String namaMk) {
        this.namaMK = namaMk;
    }

    public int getSks() {
        return sks;
    }

    public void setSks(int sks) {
        this.sks = sks;
    }

    public List<Kelas> getDaftarKelas() {
        return daftarKelas;
    }

    public void setDaftarKelas(List<Kelas> daftarKelas) {
        this.daftarKelas = daftarKelas;
    }

}