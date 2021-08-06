package com.akarinti.preapproved.dto.rumahsaya;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TenorAngsuranDTO {
    @JsonProperty("tenor")
    private String tenor;

    @JsonProperty("angsuran")
    private String angsuran;
}
