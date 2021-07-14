package com.akarinti.preapproved.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class LogoutResponseDTO {

    @JsonProperty(value="status")
    private Boolean status;

}
