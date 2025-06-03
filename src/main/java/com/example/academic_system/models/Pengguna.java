package com.example.academic_system.models;

import jakarta.persistence.*;

@Entity
    @Inheritance(strategy = InheritanceType.JOINED)
    @Table(name = "pengguna")
    public class Pengguna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;

    @Column(unique = true, nullable = false)
    private String email;

    private String kataSandi;

    @Transient
    private String kataSandiAsli;

    @Column(nullable = false)
    private String peran;


    public Pengguna() {}

    public Pengguna(String nama, String email, String kataSandi) {
        this.nama = nama;
        this.email = email;
        this.kataSandi = kataSandi;
        this.peran = peran;
    }









    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKataSandi() {
        return kataSandi;
    }

    public void setKataSandi(String password) {
        this.kataSandi = password;
    }

    public String getPeran() {
        return peran;
    }

    public void setPeran(String peran) {
        this.peran = peran;
    }


    public String getKataSandiAsli() {
        return kataSandiAsli;
    }

    public void setKataSandiAsli(String kataSandiAsli) {
        this.kataSandiAsli = kataSandiAsli;
    }
}


