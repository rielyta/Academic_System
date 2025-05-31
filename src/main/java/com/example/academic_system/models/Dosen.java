package com.example.academic_system.models;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Dosen extends Pengguna {

    @Column(unique = true, nullable = false)
    private String nip;

    @Column(nullable = false)
    private String fakultas;

    @OneToMany(mappedBy = "dosenPengampu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Kelas> kelasDiajar;

    //Constructor
    public Dosen() {
        super();
        this.setPeran("DOSEN");
    }

    public Dosen(String nama, String email, String kataSandi, String nip, String fakultas) {
        super(nama, email, kataSandi, "DOSEN");
        this.nip = nip;
        this.fakultas = fakultas;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public List<Kelas> getKelasDiajar() {
        return kelasDiajar;
    }

    public void setKelasDiajar(List<Kelas> kelasDiajar) {
        this.kelasDiajar = kelasDiajar;
    }
}
