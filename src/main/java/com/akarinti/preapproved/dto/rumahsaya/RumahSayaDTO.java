package com.akarinti.preapproved.dto.rumahsaya;

import com.akarinti.preapproved.jpa.entity.Aplikasi;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.*;
import java.util.UUID;

@Data
public class RumahSayaDTO {

    @JsonProperty(value = "secure_id")
    private String secureId;

    @JsonProperty(value = "norek_bca")
    @Size(max= 10)
    private String norekBca;

    @JsonProperty(value = "no_telpdi_bca")
    @Size(max= 15)
    private String noTelpdiBca;

    @JsonProperty(value = "id_data")
    @NotNull
    private UUID idData;

    @JsonProperty(value = "id_member")
    @NotNull
    private UUID idMember;

    @Pattern(regexp = "^[MSW]$")
    @JsonProperty(value = "status_pernikahan")
    private String statusPernikahan;

    @Pattern(regexp = "^[YN]$")
    @JsonProperty(value = "status_pisah_harta")
    private String statusPisahHarta;

    @JsonProperty(value = "joint_income")
    private String jointIncome;

    @Size(max = 100)
    @JsonProperty(value = "nama_lengkap")
    private String namaLengkap;

    @Pattern(regexp = "^[MF]$")
    @JsonProperty(value = "jenis_kelamin")
    private String jenisKelamin;

    @Pattern(regexp = "^[A-Za-z0-9\\s]*$")
    @Size(max = 40)
    @JsonProperty(value = "tempat_lahir")
    private String tempatLahir;

    @JsonProperty(value = "tanggal_lahir")
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tanggalLahir;

    @Size(max = 16)
    @JsonProperty(value = "nik")
    private String nik;

    @Pattern(regexp = "^[A-Za-z0-9\\s]*$")
    @Size(max = 40)
    @JsonProperty(value = "nama_gadis_ibu_kandung")
    private String namaGadisIbuKandung;

    @Pattern(regexp = "^[1-3]$")
    @JsonProperty(value = "jenis_pekerjaan")
    private String jenisPekerjaan;

    @Pattern(regexp = "^[A-Za-z0-9\\s]*$")
    @Size(max = 100)
    @JsonProperty(value = "nama_lengkap_pasangan")
    private String namaLengkapPasangan;

    @Pattern(regexp = "^[MF]$")
    @JsonProperty(value = "jenis_kelamin_pasangan")
    private String jenisKelaminPasangan;

    @Pattern(regexp = "^[A-Za-z0-9\\s]*$")
    @Size(max = 40)
    @JsonProperty(value = "tempat_lahir_pasangan")
    private String tempatLahirPasangan;

    @JsonProperty(value = "tanggal_lahir_pasangan")
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tanggalLahirPasangan;

    @Size(max = 16)
    @JsonProperty(value = "nik_pasangan")
    private String nikPasangan;

    @Pattern(regexp = "^[A-Za-z0-9\\s]*$")
    @Size(max = 40)
    @JsonProperty(value = "nama_gadis_ibu_kandung_pasangan")
    private String namaGadisIbuKandungPasangan;

    @JsonProperty(value = "penghasilan_pemohon")
    private Long penghasilanPemohon;

    @JsonProperty(value = "penghasilan_pasangan")
    private Long penghasilanPasangan;

    @JsonProperty(value = "biaya_rumah_tangga")
    private Long biayaRumahTangga;

    @Pattern(regexp = "^C[1-4]$")
    @JsonProperty(value = "tujuan_kredit")
    private String tujuanKredit;

    @JsonProperty(value = "tujuan_kredit_description")
    private String tujuanKreditDescription;

    @JsonProperty(value = "plafon_pengajuan_kpr")
    private Long plafonPengajuanKpr;

    @JsonProperty(value = "suku_bunga")
    private Float sukuBunga;

    @Size(max = 30)
    @JsonProperty(value = "provinsi")
    private String provinsi;

    @JsonProperty(value = "id_provinsi")
    private String idProvinsi;

    @JsonProperty(value = "kota_atau_kabupaten")
    private String kotaAtauKabupaten;

    @Size(max = 6)
    @JsonProperty(value = "id_kota_atau_kabupaten")
    private String idKotaAtauKabupaten;

    @JsonProperty(value = "nama_file_ktp")
    private String namaFileKtp;

    @JsonProperty(value = "nama_file_ktp_pasangan")
    private String namaFileKtpPasangan;

    @JsonProperty(value = "created_time")
    private Long createdTime;

    @JsonProperty(value = "sisa_sl")
    private Long sisaSL;

    public static RumahSayaDTO fromEntity(Aplikasi aplikasi) {
        return new RumahSayaDTO(
                aplikasi.getSecureId(),
                aplikasi.getNorekBca(),
                aplikasi.getNoTelpdiBca(),
                aplikasi.getAppDataID(),
                aplikasi.getIdMember(),
                aplikasi.getStatusPernikahan(),
                aplikasi.getStatusPisahHarta(),
                aplikasi.getJointIncome(),
                aplikasi.getNamaLengkap(),
                aplikasi.getJenisKelamin(),
                aplikasi.getTempatLahir(),
                aplikasi.getTanggalLahir(),
                aplikasi.getNik(),
                aplikasi.getNamaGadisIbuKandung(),
                aplikasi.getJenisPekerjaan(),
                aplikasi.getNamaLengkapPasangan(),
                aplikasi.getJenisKelaminPasangan(),
                aplikasi.getTempatLahirPasangan(),
                aplikasi.getTanggalLahirPasangan(),
                aplikasi.getNikPasangan(),
                aplikasi.getNamaGadisIbuKandungPasangan(),
                aplikasi.getPenghasilanPemohon(),
                aplikasi.getPenghasilanPasangan(),
                aplikasi.getBiayaRumahTangga(),
                aplikasi.getTujuanKredit(),
                aplikasi.getTujuanKreditDescription(),
                aplikasi.getPlafonPengajuanKpr(),
                aplikasi.getSukuBunga(),
                aplikasi.getProvinsi(),
                aplikasi.getIdProvinsi(),
                aplikasi.getKotaAtauKabupaten(),
                aplikasi.getIdKotaAtauKabupaten(),
                aplikasi.getNamaFileKtp(),
                aplikasi.getNamaFileKtpPasangan(),
                aplikasi.getCreationDate()
            );
    }

    public RumahSayaDTO(String secureId,
                        @Size(max = 10) String norekBca,
                        @Size(max = 15) String noTelpdiBca,
                        @NotNull UUID idData,
                        @NotNull UUID idMember,
                        @Pattern(regexp = "^[MSW]$") String statusPernikahan,
                        @Pattern(regexp = "^[YN]$") String statusPisahHarta,
                        String jointIncome, @Size(max = 100) String namaLengkap,
                        @Pattern(regexp = "^[MF]$") String jenisKelamin,
                        @Pattern(regexp = "^[A-Za-z0-9\\s]*$") @Size(max = 40) String tempatLahir,
                        LocalDate tanggalLahir,
                        @Size(max = 16) String nik,
                        @Pattern(regexp = "^[A-Za-z0-9\\s]*$") @Size(max = 40) String namaGadisIbuKandung,
                        @Pattern(regexp = "^[1-3]$") String jenisPekerjaan,
                        @Pattern(regexp = "^[A-Za-z0-9\\s]*$") @Size(max = 100) String namaLengkapPasangan,
                        @Pattern(regexp = "^[MF]$") String jenisKelaminPasangan,
                        @Pattern(regexp = "^[A-Za-z0-9\\s]*$") @Size(max = 40) String tempatLahirPasangan,
                        LocalDate tanggalLahirPasangan, @Size(max = 16) String nikPasangan,
                        @Pattern(regexp = "^[A-Za-z0-9\\s]*$") @Size(max = 40) String namaGadisIbuKandungPasangan,
                        Long penghasilanPemohon,
                        Long penghasilanPasangan,
                        Long biayaRumahTangga,
                        @Pattern(regexp = "^C[1-4]$") String tujuanKredit,
                        String tujuanKreditDescription,
                        Long plafonPengajuanKpr,
                        Float sukuBunga,
                        @Size(max = 30) String provinsi,
                        String idProvinsi, String kotaAtauKabupaten,
                        @Size(max = 6) String idKotaAtauKabupaten,
                        String namaFileKtp,
                        String namaFileKtpPasangan,
                        LocalDateTime createdTime) {
        this.secureId = secureId;
        this.norekBca = norekBca;
        this.noTelpdiBca = noTelpdiBca;
        this.idData = idData;
        this.idMember = idMember;
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
        this.createdTime = createdTime.toInstant(ZoneOffset.ofHours(7)).getEpochSecond();
    }

    public RumahSayaDTO() {
    }
}
