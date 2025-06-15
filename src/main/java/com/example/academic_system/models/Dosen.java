package com.example.academic_system.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Dosen extends Pengguna {
    @Column(unique = true, nullable = false)
    private String nip;

    private String fakultas;

    private String nidn;
    private String noTelepon;

    @JsonIgnore
    @OneToMany(mappedBy = "dosen")
    private List<Kelas> kelasDiajar;

    public Dosen() {}

    public Dosen(String nama, String email, String password) {
        super(nama, email, password);
    }

    // Getters and Setters
    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }

    public String getFakultas() { return fakultas; }
    public void setFakultas(String fakultas) { this.fakultas = fakultas; }

    public List<Kelas> getKelasDiajar() { return kelasDiajar; }
    public void setKelasDiajar(List<Kelas> kelasDiajar) {
        this.kelasDiajar = kelasDiajar;
    }

    public String getNidn() {
        return nidn;
    }

    public void setNidn(String nidn) {
        this.nidn = nidn;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }
}