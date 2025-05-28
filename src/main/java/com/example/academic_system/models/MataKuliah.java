package com.example.academic_system.models;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class MataKuliah {

    @Id
    private String kode;
    private String nama;
    private int sks;
    @OneToMany(mappedBy = "mataKuliah")
    private List<Kelas> daftarKelas;


    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

