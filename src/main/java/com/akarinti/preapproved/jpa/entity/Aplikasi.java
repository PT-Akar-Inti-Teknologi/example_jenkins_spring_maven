package com.akarinti.preapproved.jpa.entity;

import com.akarinti.preapproved.dto.rumahsaya.RumahSayaDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "APLIKASI")
@DynamicUpdate
@Data
public class Aplikasi extends Base {

    @Column(name = "ID_APP_DATA")
    private String appDataID;

    @Column(name = "NO_REK_BCA")
    private String norekBca;

    @Column(name = "NO_TELP_BCA")
    private String noTelpdiBca;

    @Column(name = "ID_MEMBER")
    private UUID idMember;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "STATUS_PERNIKAHAN")
    private String statusPernikahan;

    @Column(name = "STATUS_PISAH_HARTA")
    private String statusPisahHarta;

    @Column(name = "JOINT_INCOME")
    private String jointIncome;

    @Column(name = "NAMA_LENGKAP")
    private String namaLengkap;

    @Column(name = "JENIS_KELAMIN")
    private String jenisKelamin;

    @Column(name = "TEMPAT_LAHIR")
    private String tempatLahir;

    @Column(name = "TANGGAL_LAHIR")
    private LocalDate tanggalLahir;

    @Column(name = "NIK")
    private String nik;

    @Column(name = "NAMA_GADIS_IBU_KANDUNG")
    private String namaGadisIbuKandung;

    @Column(name = "JENIS_PEKERJAAN")
    private String jenisPekerjaan;

    @Column(name = "NAMA_LENGKAP_PASANGAN")
    private String namaLengkapPasangan;

    @Column(name = "JENIS_KELAMIN_PASANGAN")
    private String jenisKelaminPasangan;

    @Column(name = "TEMPAT_LAHIR_PASANGAN")
    private String tempatLahirPasangan;

    @Column(name = "TANGGAL_LAHIR_PASANGAN")
    private LocalDate tanggalLahirPasangan;

    @Column(name = "NIK_PASANGAN")
    private String nikPasangan;

    @Column(name = "NAMA_GADIS_IBU_KANDUNG_PASANGAN")
    private String namaGadisIbuKandungPasangan;

    @Column(name = "PENGHASILAN_PEMOHON")
    private Long penghasilanPemohon;

    @Column(name = "PENGHASILAN_PASANGAN")
    private Long penghasilanPasangan;

    @Column(name = "BIAYA_RUMAH_TANGGA")
    private Long biayaRumahTangga;

    @Column(name = "TUJUAN_KREDIT")
    private String tujuanKredit;

    @Column(name = "TUJUAN_KREDIT_DESCRIPTION")
    private String tujuanKreditDescription;

    @Column(name = "PLAFON_PENGAJUAN_KPR")
    private Long plafonPengajuanKpr;

    @Column(name = "SUKU_BUNGA")
    private Float sukuBunga;

    @Column(name = "PROVINSI")
    private String provinsi;

    @Column(name = "ID_PROVINSI")
    private String idProvinsi;

    @Column(name = "KOTA_ATAU_KABUPATEN")
    private String kotaAtauKabupaten;

    @Column(name = "ID_KOTA_ATAU_KABUPATEN")
    private String idKotaAtauKabupaten;

    @Column(name = "NAMA_FILE_KTP")
    private String namaFileKtp;

    @Column(name = "NAMA_FILE_KTP_PASANGAN")
    private String namaFileKtpPasangan;

    @Column(name = "SERVICE_LEVEL_OVERDUE")
    @ColumnDefault("false")
    private boolean serviceLevelOverdue;

    @CreatedDate
    @Column(name = "TGL_MULAI_SL")
    private LocalDateTime tglMulaiSL;
    
    @Column(name = "STATUS", columnDefinition = "varchar(255) default 'NEW'")
    private String status;

    public Aplikasi() {

    }

    @SneakyThrows
    public Aplikasi(
            String norekBca,
            String noTelpdiBca,
            String idData,
            UUID idMember,
            String email,
            String statusPernikahan,
            String statusPisahHarta,
            String jointIncome,
            String namaLengkap,
            String jenisKelamin,
            String tempatLahir,
            LocalDate tanggalLahir,
            String nik,
            String namaGadisIbuKandung,
            String jenisPekerjaan,
            String namaLengkapPasangan,
            String jenisKelaminPasangan,
            String tempatLahirPasangan,
            LocalDate tanggalLahirPasangan,
            String nikPasangan,
            String namaGadisIbuKandungPasangan,
            Long penghasilanPemohon,
            Long penghasilanPasangan,
            Long biayaRumahTangga,
            String tujuanKredit,
            String tujuanKreditDescription,
            Long plafonPengajuanKpr,
            Float sukuBunga,
            String provinsi,
            String idProvinsi,
            String kotaAtauKabupaten,
            String idKotaAtauKabupaten,
            String namaFileKtp,
            String namaFileKtpPasangan
    ) {
//        DateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd");

        this.norekBca = norekBca;
        this.noTelpdiBca = noTelpdiBca;
        this.appDataID = idData;
        this.idMember = idMember;
        this.email = email;
        this.statusPernikahan = statusPernikahan;
        this.statusPisahHarta = statusPisahHarta;
        this.jointIncome = jointIncome;
        this.namaLengkap = namaLengkap;
        this.jenisKelamin = jenisKelamin;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.nik = nik;
        this.namaGadisIbuKandung = namaGadisIbuKandung;
        this.jenisPekerjaan = jenisPekerjaan;
        this.namaLengkapPasangan = namaLengkapPasangan;
        this.jenisKelaminPasangan = jenisKelaminPasangan;
        this.tempatLahirPasangan = tempatLahirPasangan;
        this.tanggalLahirPasangan = tanggalLahirPasangan;
        this.nikPasangan = nikPasangan;
        this.namaGadisIbuKandungPasangan = namaGadisIbuKandungPasangan;
        this.penghasilanPemohon = penghasilanPemohon;
        this.penghasilanPasangan = penghasilanPasangan;
        this.biayaRumahTangga = biayaRumahTangga;
        this.tujuanKredit = tujuanKredit;
        this.tujuanKreditDescription = tujuanKreditDescription;
        this.plafonPengajuanKpr = plafonPengajuanKpr;
        this.sukuBunga = sukuBunga;
        this.provinsi = provinsi;
        this.idProvinsi = idProvinsi;
        this.kotaAtauKabupaten = kotaAtauKabupaten;
        this.idKotaAtauKabupaten = idKotaAtauKabupaten;
        this.namaFileKtp = namaFileKtp;
        this.namaFileKtpPasangan = namaFileKtpPasangan;
        this.tglMulaiSL = LocalDateTime.now();
    }

    public static Aplikasi fromDTO(RumahSayaDTO rumahSayaDTO) {
        return new Aplikasi(
                rumahSayaDTO.getNorekBca(),
                rumahSayaDTO.getNoTelpdiBca(),
                rumahSayaDTO.getIdData(),
                rumahSayaDTO.getIdMember(),
                rumahSayaDTO.getEmail(),
                rumahSayaDTO.getStatusPernikahan(),
                rumahSayaDTO.getStatusPisahHarta(),
                rumahSayaDTO.getJointIncome(),
                rumahSayaDTO.getNamaLengkap(),
                rumahSayaDTO.getJenisKelamin(),
                rumahSayaDTO.getTempatLahir(),
                rumahSayaDTO.getTanggalLahir(),
                rumahSayaDTO.getNik(),
                rumahSayaDTO.getNamaGadisIbuKandung(),
                rumahSayaDTO.getJenisPekerjaan(),
                rumahSayaDTO.getNamaLengkapPasangan(),
                rumahSayaDTO.getJenisKelaminPasangan(),
                rumahSayaDTO.getTempatLahirPasangan(),
                rumahSayaDTO.getTanggalLahirPasangan(),
                rumahSayaDTO.getNikPasangan(),
                rumahSayaDTO.getNamaGadisIbuKandungPasangan(),
                rumahSayaDTO.getPenghasilanPemohon(),
                rumahSayaDTO.getPenghasilanPasangan(),
                rumahSayaDTO.getBiayaRumahTangga(),
                rumahSayaDTO.getTujuanKredit(),
                rumahSayaDTO.getTujuanKreditDescription(),
                rumahSayaDTO.getPlafonPengajuanKpr(),
                rumahSayaDTO.getSukuBunga(),
                rumahSayaDTO.getProvinsi(),
                rumahSayaDTO.getIdProvinsi(),
                rumahSayaDTO.getKotaAtauKabupaten(),
                rumahSayaDTO.getIdKotaAtauKabupaten(),
                rumahSayaDTO.getNamaFileKtp(),
                rumahSayaDTO.getNamaFileKtpPasangan()
        );
    }
}
