package com.akarinti.preapproved.dto.rumahsaya;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApplicationDataRequestDTO {

    @JsonProperty(value = "secure_id")
    private String secureId;
}
