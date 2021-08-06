package com.akarinti.preapproved.dto.rumahsaya;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RumahSayaCreateResponseDTO {
    @JsonProperty(value = "success")
    private Boolean success = true;

    public RumahSayaCreateResponseDTO(boolean status) {
        this.success = true;
    }
    public RumahSayaCreateResponseDTO() {
    }
}
