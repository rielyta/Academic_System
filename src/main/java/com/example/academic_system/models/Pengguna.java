package com.example.academic_system.models;

import jakarta.persistence.*;

@Entity
    @Inheritance(strategy = InheritanceType.JOINED)
    @Table(name = "pengguna")
    public class Pengguna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String kataSandi;

    @Column(nullable = false)
    private String peran;

    //Constructor
    public Pengguna() {}

    public Pengguna(String nama, String email, String kataSandi, String peran) {
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
}


