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

    public Dosen() {}

    public Dosen(String nama, String email, String password) {
        super(nama, email, password);
    }

    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }

    public String getFakultas() { return fakultas; }
    public void setFakultas(String fakultas) { this.fakultas = fakultas; }


    @Override
    public String toString() {
        return "Dosen{" +
                "id=" + getId() +
                ", nama='" + getNama() + '\'' +
                ", nip='" + nip + '\'' +
                ", email='" + getEmail() + '\'' +
                ", fakultas='" + fakultas + '\'' +
                '}';
    }
}