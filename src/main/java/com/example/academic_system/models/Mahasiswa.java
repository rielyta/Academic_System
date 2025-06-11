package com.example.academic_system.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Mahasiswa extends Pengguna {

    @Column(unique = true, nullable = false)
    private String nim;

    private String prodi;

    private String fakultas;

    @ManyToMany
    @JoinTable(
            name = "mahasiswa_kelas",
            joinColumns = @JoinColumn(name = "mahasiswa_id"),
            inverseJoinColumns = @JoinColumn(name = "kelas_id")
    )
    private List<Kelas> kelasDiikuti;



    public Mahasiswa() {}

    public Mahasiswa(String nama, String email, String password) {
        super(nama, email, password);
    }


    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }



    public List<Kelas> getKelasDiikuti() {
        return kelasDiikuti;
    }

    public void setKelasDiikuti(List<Kelas> kelasDiikuti) {
        this.kelasDiikuti = kelasDiikuti;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

}

