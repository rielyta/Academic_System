package com.example.academic_system.models;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Kelas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kode_kelas", unique = true)
    private String kodeKelas;

    @Column(name = "nama_kelas", nullable = false, unique = true)
    private String namaKelas;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kode_mk", referencedColumnName = "kode_mk", nullable = false)
    private MataKuliah mataKuliah;

    @Column(name = "fakultas")
    private String fakultas;

    @ManyToOne(optional = true)
    @JoinColumn(name = "dosen_id", nullable = true)
    private Dosen dosen;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mahasiswa_kelas_diikuti",
            joinColumns = @JoinColumn(name = "kelas_diikuti_id"),
            inverseJoinColumns = @JoinColumn(name = "mahasiswa_terdaftar_id")
    )
    private List<Mahasiswa> mahasiswaTerdaftar = new ArrayList<>();

    @Transient
    private int jumlahMahasiswa;

    @Column(name = "jam_mulai", nullable = true)
    private LocalTime jamMulai;

    @Column(name = "jam_keluar", nullable = true)
    private LocalTime jamKeluar;

    @Column(nullable = false)
    private Integer semester;

    @Column(name = "tahun_ajar", nullable = false)
    private String tahunAjar;

    @Column(name = "ruangan_kelas", nullable = true)
    private String ruangan;

    @Column(name = "hari_kelas", nullable = true)
    @Enumerated(EnumType.STRING)
    private DayOfWeek hariKelas;

    public Kelas() {}

    public Kelas(String kodeKelas, String namaKelas, MataKuliah mataKuliah,
                 String fakultas, Integer semester, String tahunAjar) {
        this.kodeKelas = kodeKelas;
        this.namaKelas = namaKelas;
        this.mataKuliah = mataKuliah;
        this.fakultas = fakultas;
        this.semester = semester;
        this.tahunAjar = tahunAjar;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKodeKelas() { return kodeKelas; }
    public void setKodeKelas(String kodeKelas) { this.kodeKelas = kodeKelas; }

    public String getNamaKelas() { return namaKelas; }
    public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }

    public MataKuliah getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(MataKuliah mataKuliah) { this.mataKuliah = mataKuliah; }

    public String getFakultas() { return fakultas; }
    public void setFakultas(String fakultas) { this.fakultas = fakultas; }

    public Dosen getDosen() { return dosen; }
    public void setDosen(Dosen dosen) { this.dosen = dosen; }

    public List<Mahasiswa> getMahasiswaTerdaftar() { return mahasiswaTerdaftar; }

    public int getJumlahMahasiswa() {
        return mahasiswaTerdaftar != null ? mahasiswaTerdaftar.size() : 0;
    }

    public LocalTime getJamMulai() { return jamMulai; }
    public void setJamMulai(LocalTime jamMulai) { this.jamMulai = jamMulai; }

    public LocalTime getJamKeluar() { return jamKeluar; }
    public void setJamKeluar(LocalTime jamKeluar) { this.jamKeluar = jamKeluar; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public String getTahunAjar() { return tahunAjar; }
    public void setTahunAjar(String tahunAjar) { this.tahunAjar = tahunAjar; }

    public String getRuangan() { return ruangan; }
    public void setRuangan(String ruangan) { this.ruangan = ruangan; }

    public DayOfWeek getHariKelas() { return hariKelas; }
    public void setHariKelas(DayOfWeek hariKelas) { this.hariKelas = hariKelas; }

    @PostLoad
    public void initJumlahMahasiswa() {
        this.jumlahMahasiswa = this.mahasiswaTerdaftar != null ? this.mahasiswaTerdaftar.size() : 0;
    }

    @Override
    public String toString() {
        return "Kelas{" +
                "id=" + id +
                ", kodeKelas='" + kodeKelas + '\'' +
                ", namaKelas='" + namaKelas + '\'' +
                ", mataKuliah=" + (mataKuliah != null ? mataKuliah.getNamaMK() : "null") +
                ", dosen=" + (dosen != null ? dosen.getNama() : "Belum ada dosen") +
                ", fakultas='" + fakultas + '\'' +
                ", jumlahMahasiswa=" + jumlahMahasiswa +
                ", semester=" + semester +
                ", tahunAjar='" + tahunAjar + '\'' +
                ", ruangan='" + ruangan + '\'' +
                ", hariKelas=" + hariKelas +
                ", jamMulai=" + jamMulai +
                ", jamKeluar=" + jamKeluar +
                '}';
    }
}