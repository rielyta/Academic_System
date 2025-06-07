package com.example.academic_system.payload;

public class KelasRequest {
    private String namaKelas;
    private String mataKuliahId;
    private Long dosenId; // GANTI dari String dosenNip

    // Getter Setter
    public String getNamaKelas() { return namaKelas; }
    public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }

    public String getMataKuliahId() { return mataKuliahId; }
    public void setMataKuliahId(String mataKuliahId) { this.mataKuliahId = mataKuliahId; }

    public Long getDosenId() { return dosenId; }
    public void setDosenId(Long dosenId) { this.dosenId = dosenId; }
}
