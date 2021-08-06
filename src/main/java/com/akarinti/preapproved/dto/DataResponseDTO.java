package com.akarinti.preapproved.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataResponseDTO {
    @JsonProperty(value = "success")
    private boolean success;

    public DataResponseDTO(boolean success) {
        this.success = success;
    }

    public DataResponseDTO() {

    }
}
