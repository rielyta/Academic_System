package com.example.academic_system.models;

import jakarta.persistence.*;

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

    @OneToMany(mappedBy = "mataKuliah", cascade = CascadeType.ALL)
    private List<Kelas> kelasList;

    @Column(name = "tipe_matakuliah", nullable = false)
    private String tipeMatakuliah;

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

    public int getJumlahKelas() {
        return kelasList != null ? kelasList.size() : 0;
    }

    public List<Kelas> getKelasList() {
        return kelasList;
    }

    public void setKelasList(List<Kelas> kelasList) {
        this.kelasList = kelasList;
    }

    public String getTipeMatakuliah() {
        return tipeMatakuliah;
    }

    public void setTipeMatakuliah(String tipeMatakuliah) {
        this.tipeMatakuliah = tipeMatakuliah;
    }
}