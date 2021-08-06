package com.akarinti.preapproved.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "PLATFORM_GENERATOR")
@DynamicUpdate
@Data
public class PlatformGenerator extends Base {

    @Column(name = "PLATFORM_TERPILIH")
    private long platformTerpilih;

    @Column(name = "FLAG")
    private String flag;

    @Column(name = "TENOR")
    private long tenor;

    @Column(name = "MASA_BERLAKU")
    private long masaBerlaku;

    @Column(name = "PLAFON_KEMAMPUAN")
    private long plafonKemampuan;

    @Column(name = "PLAFON_MATRIKS")
    private long platformMatriks;

    @Column(name = "ANGSURAN")
    private long angsuran;

    @Column(name = "KODE_PENGAJUAN")
    private long kode_pengajuan;

    @Column(name = "APP_DATA_ID")
    private long appDataId;


    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "APLIKASI_ID")
    private Aplikasi aplikasi;
}
