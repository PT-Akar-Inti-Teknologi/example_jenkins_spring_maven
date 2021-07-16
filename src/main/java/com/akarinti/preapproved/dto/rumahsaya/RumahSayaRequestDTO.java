package com.akarinti.preapproved.dto.rumahsaya;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RumahSayaRequestDTO {
    @JsonProperty(value = "norek_bca")
    private String norekBca;

    @JsonProperty(value = "no_telpdi_bca")
    private String noTelpdiBca;

    @JsonProperty(value = "id_data")
    private String idData;

    @JsonProperty(value = "id_member")
    private String idMember;

    @JsonProperty(value = "status_pernikahan")
    private String statusPernikahan;

    @JsonProperty(value = "status_pisah_harta")
    private String statusPisahHarta;

    @JsonProperty(value = "joint_income")
    private String jointIncome;

    @JsonProperty(value = "nama_lengkap")
    private String namaLengkap;

    @JsonProperty(value = "jenis_kelamin")
    private String jenisKelamin;

    @JsonProperty(value = "tempat_lahir")
    private String tempatLahir;

    @JsonProperty(value = "tanggal_lahir")
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tanggalLahir;

    @JsonProperty(value = "nik")
    private String nik;

    @JsonProperty(value = "nama_gadis_ibu_kandung")
    private String namaGadisIbuKandung;

    @JsonProperty(value = "jenis_pekerjaan")
    private String jenisPekerjaan;

    @JsonProperty(value = "nama_lengkap_pasangan")
    private String namaLengkapPasangan;

    @JsonProperty(value = "jenis_kelamin_pasangan")
    private String jenisKelaminPasangan;

    @JsonProperty(value = "tempat_lahir_pasangan")
    private String tempatLahirPasangan;

    @JsonProperty(value = "tanggal_lahir_pasangan")
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tanggalLahirPasangan;

    @JsonProperty(value = "nik_pasangan")
    private String nikPasangan;

    @JsonProperty(value = "nama_gadis_ibu_kandung_pasangan")
    private String namaGadisIbuKandungPasangan;

    @JsonProperty(value = "penghasilan_pemohon")
    private Long penghasilanPemohon;

    @JsonProperty(value = "penghasilan_pasangan")
    private Long penghasilanPasangan;

    @JsonProperty(value = "biaya_rumah_tangga")
    private Long biayaRumahTangga;

    @JsonProperty(value = "tujuan_kredit")
    private String tujuanKredit;

    @JsonProperty(value = "tujuan_kredit_description")
    private String tujuanKreditDescription;

    @JsonProperty(value = "plafon_pengajuan_kpr")
    private Long plafonPengajuanKpr;

    @JsonProperty(value = "suku_bunga")
    private Float sukuBunga;

    @JsonProperty(value = "provinsi")
    private String provinsi;

    @JsonProperty(value = "id_provinsi")
    private String idProvinsi;

    @JsonProperty(value = "kota_atau_kabupaten")
    private String kotaAtauKabupaten;

    @JsonProperty(value = "id_kota_atau_kabupaten")
    private String idKotaAtauKabupaten;

    @JsonProperty(value = "nama_file_ktp")
    private String namaFileKtp;

    @JsonProperty(value = "nama_file_ktp_pasangan")
    private String namaFileKtpPasangan;

}
