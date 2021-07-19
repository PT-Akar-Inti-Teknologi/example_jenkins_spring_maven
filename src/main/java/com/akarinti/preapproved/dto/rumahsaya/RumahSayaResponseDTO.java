package com.akarinti.preapproved.dto.rumahsaya;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RumahSayaResponseDTO {
    @JsonProperty(value = "success")
    private Boolean success = true;

}
