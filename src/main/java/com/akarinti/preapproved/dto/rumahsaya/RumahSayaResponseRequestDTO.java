package com.akarinti.preapproved.dto.rumahsaya;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RumahSayaResponseRequestDTO {
    @JsonProperty(value = "plafon_terpilih")
    private Double plafonTerpilih;

    @JsonProperty(value = "flag")
    private String flag;

    @JsonProperty(value = "masa_berlaku")
    private String masaBerlaku;

    @JsonProperty(value = "plafon_kemampuan")
    private Double plafonKemampuan;

    @JsonProperty(value = "plafon_matriks")
    private Double plafonMatriks;

    @JsonProperty(value = "kode_pengajuan")
    private String kodePengajuan;

    @JsonProperty(value = "app_data_id")
    private String appDataId;

    @JsonProperty(value = "tenor_text")
    private String tenorText;

    @JsonProperty(value = "angsuran_text")
    private String angsuranText;

    @JsonProperty(value = "tenor_angsuran")
    private List<TenorAngsuranDTO> tenorAngsuranList;

}
