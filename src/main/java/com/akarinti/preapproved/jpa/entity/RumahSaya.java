package com.akarinti.preapproved.jpa.entity;

import com.akarinti.preapproved.dto.rumahsaya.RumahSayaRequestDTO;
import lombok.Data;
import lombok.SneakyThrows;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "RUMAHSAYA")
@DynamicUpdate
@Data
public class RumahSaya extends Base {

    @OneToOne(mappedBy = "rumahSaya")
    private Aplikasi aplikasi;

    @Column(name = "ID_APP_DATA")
    private String appDataID;

    @Column(name = "NO_REK_BCA")
    private String norekBca;

    @Column(name = "NO_TELP_BCA")
    private String noTelpdiBca;

    @Column(name = "ID_MEMBER")
    private String idMember;

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date tanggalLahir;

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date tanggalLahirPasangan;

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

    public RumahSaya() {

    }

    @SneakyThrows
    public RumahSaya(
            String norekBca,
            String noTelpdiBca,
            String idData,
            String idMember,
            String statusPernikahan,
            String statusPisahHarta,
            String jointIncome,
            String namaLengkap,
            String jenisKelamin,
            String tempatLahir,
            String tanggalLahir,
            String nik,
            String namaGadisIbuKandung,
            String jenisPekerjaan,
            String namaLengkapPasangan,
            String jenisKelaminPasangan,
            String tempatLahirPasangan,
            String tanggalLahirPasangan,
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
        DateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd");

        this.norekBca = norekBca;
        this.noTelpdiBca = noTelpdiBca;
        this.appDataID = idData;
        this.idMember = idMember;
        this.statusPernikahan = statusPernikahan;
        this.statusPisahHarta = statusPisahHarta;
        this.jointIncome = jointIncome;
        this.namaLengkap = namaLengkap;
        this.jenisKelamin = jenisKelamin;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = formatter.parse(tanggalLahir);
        this.nik = nik;
        this.namaGadisIbuKandung = namaGadisIbuKandung;
        this.jenisPekerjaan = jenisPekerjaan;
        this.namaLengkapPasangan = namaLengkapPasangan;
        this.jenisKelaminPasangan = jenisKelaminPasangan;
        this.tempatLahirPasangan = tempatLahirPasangan;
        this.tanggalLahirPasangan = formatter.parse(tanggalLahirPasangan);
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
    }

    public static RumahSaya fromDTO(RumahSayaRequestDTO rumahSayaRequestDTO) {
        return new RumahSaya(
                rumahSayaRequestDTO.getNorekBca(),
                rumahSayaRequestDTO.getNoTelpdiBca(),
                rumahSayaRequestDTO.getIdData(),
                rumahSayaRequestDTO.getIdMember(),
                rumahSayaRequestDTO.getStatusPernikahan(),
                rumahSayaRequestDTO.getStatusPisahHarta(),
                rumahSayaRequestDTO.getJointIncome(),
                rumahSayaRequestDTO.getNamaLengkap(),
                rumahSayaRequestDTO.getJenisKelamin(),
                rumahSayaRequestDTO.getTempatLahir(),
                rumahSayaRequestDTO.getTanggalLahir(),
                rumahSayaRequestDTO.getNik(),
                rumahSayaRequestDTO.getNamaGadisIbuKandung(),
                rumahSayaRequestDTO.getJenisPekerjaan(),
                rumahSayaRequestDTO.getNamaLengkapPasangan(),
                rumahSayaRequestDTO.getJenisKelaminPasangan(),
                rumahSayaRequestDTO.getTempatLahirPasangan(),
                rumahSayaRequestDTO.getTanggalLahirPasangan(),
                rumahSayaRequestDTO.getNikPasangan(),
                rumahSayaRequestDTO.getNamaGadisIbuKandungPasangan(),
                rumahSayaRequestDTO.getPenghasilanPemohon(),
                rumahSayaRequestDTO.getPenghasilanPasangan(),
                rumahSayaRequestDTO.getBiayaRumahTangga(),
                rumahSayaRequestDTO.getTujuanKredit(),
                rumahSayaRequestDTO.getTujuanKreditDescription(),
                rumahSayaRequestDTO.getPlafonPengajuanKpr(),
                rumahSayaRequestDTO.getSukuBunga(),
                rumahSayaRequestDTO.getProvinsi(),
                rumahSayaRequestDTO.getIdProvinsi(),
                rumahSayaRequestDTO.getKotaAtauKabupaten(),
                rumahSayaRequestDTO.getIdKotaAtauKabupaten(),
                rumahSayaRequestDTO.getNamaFileKtp(),
                rumahSayaRequestDTO.getNamaFileKtpPasangan()
        );
    }
}
