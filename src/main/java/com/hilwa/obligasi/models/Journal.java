package com.hilwa.obligasi.models;

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
    private Double dPenerbitan;
    private Double kPenerbitanUtangObligasi;
    private Double kPenerbitan;
    private Double dBayarBungaAwal;
    private Double kBayarBungaAwal;
    private Double dBayarBungaAkhir;
    private Double kBayarBungaAkhir;
    private Double dAkhirTahun;
    private Double kAkhirTahun;
    private Double dPelunasan;
    private Double kPelunasan;

    @ManyToOne
    @JoinColumn(name = "transaction", referencedColumnName = "id")
    private Transaction transaction;
}
