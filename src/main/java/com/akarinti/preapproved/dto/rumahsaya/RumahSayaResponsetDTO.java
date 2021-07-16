package com.akarinti.preapproved.dto.rumahsaya;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RumahSayaResponsetDTO {
    @JsonProperty(value = "success")
    private boolean success;

}
