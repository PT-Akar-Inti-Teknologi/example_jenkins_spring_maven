package com.akarinti.preapproved.dto.authentication.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GenerateTokenResponseDTO {
    @JsonProperty(value="access_token")
    private String accessToken;
}
