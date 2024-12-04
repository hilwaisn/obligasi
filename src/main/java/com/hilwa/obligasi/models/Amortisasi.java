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
public class Amortisasi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date tanggalBayarBunga;
    private Date tanggalBayarrBunga;
    private Double bungaDibayarkan;
    private Double bebanBunga;
    private Double amortisasiDiskonto;
    private Double yangBelumAmortisasi;
    private Double nilaiBukuObligasi;
    
    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "journal", referencedColumnName = "id")
    private Journal journal;
}
