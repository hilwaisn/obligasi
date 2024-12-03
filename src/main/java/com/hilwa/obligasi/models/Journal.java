package com.hilwa.obligasi.models;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double dPenerbitanKas;
    private Double dPenerbitanDiskon;
    private Double kPenerbitanUtangObligasi;
    private Double kPenerbitanPremi;
    private Double dBayarBungaAwal;
    private Double kBayarBungaAwal;
    private Double dBayarBungaAkhir;
    private Double kBayarBungaAkhir;
    private Double dAkhirTahun;
    private Double kAkhirTahun;
    private Date tanggalPelunasan;
    private Double dPelunasan;
    private Double dPelunasanPremi;
    private Double kPelunasan;
    private Double kPelunasanDiskon;

    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

    public void setTanggalPelunasan(Date tanggalPelunasan) {
        this.tanggalPelunasan = tanggalPelunasan;
    }
}
