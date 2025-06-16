package com.example.academic_system.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "mahasiswa")
public class Mahasiswa extends Pengguna {

    @Column(name = "nim", unique = true, nullable = false)
    private String nim;

    @Column(name = "prodi")
    private String prodi;

    @Column(name = "fakultas")
    private String fakultas;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "mahasiswa_kelas_diikuti",
            joinColumns = @JoinColumn(name = "mahasiswa_terdaftar_id"),
            inverseJoinColumns = @JoinColumn(name = "kelas_diikuti_id")
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

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
