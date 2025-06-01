package com.example.academic_system.models;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Kelas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaKelas;

    @ManyToOne
    @JoinColumn(name = "mata_kuliah_kode")
    private MataKuliah mataKuliah;

    @ManyToOne
    @JoinColumn(name = "dosen_id")
    private Dosen dosenPengampu;

    @ManyToMany(mappedBy = "kelasDiikuti")
    private List<Mahasiswa> mahasiswaTerdaftar;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
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

    public List<Mahasiswa> getMahasiswaTerdaftar() {
        return mahasiswaTerdaftar;
    }

    public void setMahasiswaTerdaftar(List<Mahasiswa> mahasiswaTerdaftar) {
        this.mahasiswaTerdaftar = mahasiswaTerdaftar;
    }
}

