package com.hilwa.obligasi.models;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String namaPerusahaan;
    private Date tanggalTerbit;
    private Double nilaiNominal;
    private Integer lembarUtang;
    private Integer bunga;
    private Date tanggalBayar;
    private Date tanggalBayarr;
    private Integer jangkaWaktu;
    private Integer kurs;
    private Integer sukuBungaPasar;
    private Double hargaPenerbitan;
}
